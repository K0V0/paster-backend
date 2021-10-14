package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

  public List<Item> getAllOfUser(long userId) {
    List<Item> items = itemRepo.findAllByUser(userId);
    for (int i = 0; i < items.size(); i++) {
      Item item = items.get(i);
      if (item.getText().length() >= previewLength) {
        item.setPreview(item.getText().substring(0, previewLength) + "...");
        item.setLarge(true);
      } else {
        item.setPreview(items.get(i).getText());
      }
      item.setText("");
    }
    return items;
  }

  @Override
  public Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException {
    Optional<Item> item = itemRepo.findFirstByUserAndId(userId, itemId);
    if (item.isPresent()) {
      return item.get();
    }
    throw new ItemNotFoundException();
  }

  public void addItem(long userId, String text) {
    User user = userRepo.getById(userId);
    // TODO exception ak nenajdeny user
    Item item = new Item();
    item.setUser(user);
    item.setText(text);
    itemRepo.save(item);
  }

}
