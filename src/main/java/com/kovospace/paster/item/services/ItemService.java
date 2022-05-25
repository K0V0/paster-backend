package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.models.Item;

import java.util.List;

public interface ItemService {

  List<Item> getAllOfUser(long userId) throws UserNotFoundException;

  Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException;

  void addItem(long userId, String text, String platform) throws UserNotFoundException;

  void deleteItem(long userId, long itemId);

}
