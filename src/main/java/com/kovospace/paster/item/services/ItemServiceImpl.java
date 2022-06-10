package com.kovospace.paster.item.services;

import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

  @Value("${board.preview-length}")
  private int previewLength;

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
    return itemRepo.findAllByUserOrderByCreatedAtDesc(user)
            .stream()
            .map(item -> {
              if (item.getText().length() >= previewLength) {
                item.setPreview(item.getText().substring(0, previewLength) + "...");
                item.setLarge(true);
              } else {
                item.setPreview(item.getText());
              }
              //item.setText(""); // povodna logika WTF ??
              return item; })
            .collect(Collectors.toList());
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
    item.setText(text);
    item.setPlatform(convertToPlatformEnum(platform));
    item.setDeviceName(deviceName);
    itemRepo.save(item);
    // TODO v buducnosti neobmedzene polozky pre premium useraň
    // TODO SQL OFFET nefunguje ako ocakavane, ak ma user menej ako 20 itemov tak vracia tie
    if (itemRepo.findAllByUserOrderByCreatedAtDesc(user).size() > 20) {
      itemRepo.deleteAll(itemRepo.findUserItemsAbove20(userId));
    }
  }

  // TODO unit/integracny test
  @Override
  @Transactional
  public boolean deleteItem(long userId, long itemId) {
    User user = userRepo.getById(userId);
    // TODO test unauthorized user handling
    // TODO exception throw and catch if user not found/not authorized for given item
    boolean needToDelete = itemRepo.existsById(itemId);
    if (needToDelete) {
      itemRepo.deleteByUserAndId(user, itemId);
    }
    return needToDelete;
  }

  private PlatformEnum convertToPlatformEnum(String platform) {
    return Optional.ofNullable(platform)
            .map(pl -> PlatformEnum.valueOf(pl.toUpperCase()))
            .orElse(PlatformEnum.UNKNOWN);
  }

}
