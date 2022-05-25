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
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

  @Value("${board.preview-length}")
  private int previewLength;

  private ItemRepository itemRepo;
  private UserRepository userRepo;

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
  public void addItem(long userId, String text, String platform) throws UserNotFoundException {
    User user = userRepo.findById(userId)
            .orElseThrow(UserNotFoundException::new);
    Item item = new Item();
    item.setUser(user);
    item.setText(text);
    item.setPlatform(PlatformEnum.valueOf(platform.toUpperCase()));
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
  public void deleteItem(long userId, long itemId) {
    User user = userRepo.getById(userId);
    // TODO exception throw and catch if not found
    itemRepo.deleteByUserAndId(user, itemId);
  }

}
