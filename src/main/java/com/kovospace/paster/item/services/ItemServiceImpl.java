package com.kovospace.paster.item.services;

import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.ItemNotOwnedByUserException;
import com.kovospace.paster.item.exceptions.NoItemToDeleteException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepo;
  private final UserRepository userRepo;

  public ItemServiceImpl(ItemRepository itemRepo, UserRepository userRepo) {
    this.userRepo = userRepo;
    this.itemRepo = itemRepo;
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

  @Override
  @Transactional
  public void addItem(long userId, String text, String platform, String deviceName) throws UserNotFoundException {
    User user = userRepo.findById(userId)
            .orElseThrow(UserNotFoundException::new);
    Item item = new Item();
    item.setUser(user);
    item.setData(text);
    item.setPlatform(convertToPlatformEnum(platform));
    item.setDeviceName(deviceName);
    itemRepo.save(item);
    // TODO v buducnosti neobmedzene polozky pre premium useraň
    List<Item> items = itemRepo.findAllByUserOrderByCreatedAtDesc(user);
    if (items.size() > 20) {
      itemRepo.deleteAll(items.subList(20, items.size()));
    }
  }

  // TODO unit/integracny test
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
    return true;
  }

  private PlatformEnum convertToPlatformEnum(String platform) {
    return Optional.ofNullable(platform)
            .map(pl -> PlatformEnum.valueOf(pl.toUpperCase()))
            .orElse(PlatformEnum.UNKNOWN);
  }

}
