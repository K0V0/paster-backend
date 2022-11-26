package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.ItemNotOwnedByUserException;
import com.kovospace.paster.item.exceptions.NoItemToDeleteException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.exceptions.v2.FileException;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kovospace.paster.item.services.ItemServiceUtils.cleanOldItems;
import static com.kovospace.paster.item.services.ItemServiceUtils.prepareNewFile;
import static com.kovospace.paster.item.services.ItemServiceUtils.prepareNewItem;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepo;
  private final UserRepository userRepo;
  private final FilesystemOperationsService filesystem;

  @Autowired
  public ItemServiceImpl(
          ItemRepository itemRepo,
          UserRepository userRepo,
          FilesystemOperationsService filesystem
  ) {
    this.userRepo = userRepo;
    this.itemRepo = itemRepo;
    this.filesystem = filesystem;
  }

  // TODO unit/integracny test
  public List<Item> getAllOfUser(long userId) throws UserNotFoundException {
    User user = userRepo.findById(userId)
            .orElseThrow(UserNotFoundException::new);
    return itemRepo.findAllByUserOrderByCreatedAtDesc(user);
  }

  // TODO unit/integracny test
  @Override
  public Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException {
    // TODO test situacie ak pozadovana items id pre usera neexistuje
    return userRepo
        .findById(userId)
        .flatMap(user -> itemRepo
            .findFirstByUserAndId(user, itemId))
        .orElseThrow(ItemNotFoundException::new);
  }

  @Transactional
  public void addTextItem(long userId, String text, String platform, String deviceName) throws UserNotFoundException {
    Item item = prepareNewItem(userId, platform, deviceName, userRepo);
    item.setData(text);
    itemRepo.save(item);
    cleanOldItems(item.getUser(), itemRepo);
  }

  @Override
  public Item initiateFile(long userId, String platform, String deviceName,
                           String originalFileName, String mimeType, Long chunksCount, Long chunkSize)
          throws UserNotFoundException, FileException
  {
    Item item = prepareNewItem(userId, platform, deviceName, userRepo);
    File file = prepareNewFile(originalFileName, mimeType, chunksCount, chunkSize);
    item.setFile(file);
    file.setItem(item);
    itemRepo.save(item);
    file = filesystem.createEmptyFile(file);
    item.setFile(file);
    itemRepo.save(item);
    return item;
  }

  @Override
  public Item addFileChunk(long userId, long itemId, long fileId, byte[] data, long part)
          throws ItemNotFoundException
  {
    Item item = itemRepo.findById(itemId)
            .orElseThrow(ItemNotFoundException::new);
    File file = item.getFile();
    file = filesystem.addChunkToFile(file, data, part);
    if (part == file.getChunksCount()-1) {
      file = filesystem.moveFromTempToFinal(file);
      //TODO prerobit na uplnu url bud tu alebo pri konverzii na DTO
      item.setData(file.getFilePath());
    }
    item.setFile(file);
    itemRepo.save(item);
    return item;
  }

  @Override
  @Transactional
  public boolean deleteItem(long userId, long itemId) throws ItemException {
    User user = userRepo.findById(userId)
            .orElseThrow(UserNotFoundException::new);
    Item item = itemRepo.findById(itemId)
            .orElseThrow(NoItemToDeleteException::new);
    if (!item.getUser().equals(user)) {
      throw new ItemNotOwnedByUserException();
    }
    itemRepo.deleteByUserAndId(user, itemId);
    filesystem.delete(item.getFile());
    return true;
  }

}
