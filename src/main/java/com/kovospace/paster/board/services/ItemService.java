package com.kovospace.paster.board.services;

import com.kovospace.paster.board.exceptions.ItemNotFoundException;
import com.kovospace.paster.board.models.Item;
import java.util.List;

public interface ItemService {

  List<Item> getAllOfUser(long userId);

  Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException;

  void addItem(long userId, String text);

}
