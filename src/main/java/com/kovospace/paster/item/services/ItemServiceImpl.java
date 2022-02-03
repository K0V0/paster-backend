package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public List<Item> getAllOfUser(long userId) {
    // TODO exception ak predsalen user z jwt tokenu neexistuje
    return userRepo.findById(userId)
        .map(User::getItems)
        .map(itemsList -> itemsList
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
            .collect(Collectors.toList()))
        //.orElseThrow(() -> new Exception("trorlolo"));
        .orElse(new ArrayList<>());
  }

  // TODO unit/integracny test
  @Override
  public Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException {
    // TODO test situacie ak pozadovana items id pre usera neexistuje
    // TODO exception in that case
    return userRepo
        .findById(userId)
        .flatMap(user -> itemRepo
            .findFirstByUserAndId(user, itemId))
        .orElseThrow(ItemNotFoundException::new);
  }

  public void addItem(long userId, String text) {
    User user = userRepo.getById(userId);
    // TODO exception ak nenajdeny user
    Item item = new Item();
    item.setUser(user);
    item.setText(text);
    itemRepo.save(item);
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
