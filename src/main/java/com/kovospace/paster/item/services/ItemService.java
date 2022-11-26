package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.exceptions.v2.FileCreationException;
import com.kovospace.paster.item.exceptions.v2.FileException;
import com.kovospace.paster.item.models.Item;

import java.util.List;

public interface ItemService {

  List<Item> getAllOfUser(long userId) throws UserNotFoundException;

  Item getItemOfUser(long userId, long itemId) throws ItemNotFoundException;

  void addTextItem(long userId, String text, String platform, String deviceName) throws UserNotFoundException;

  Item initiateFile(long userId, String platform, String deviceName,
                    String originalFileName, String mimeType, Long chunksCount, Long chunkSize) throws UserNotFoundException, FileCreationException, FileException;

  Item addFileChunk(long userId, long itemId, long fileId, byte[] data, long part) throws ItemNotFoundException;

  boolean deleteItem(long userId, long itemId) throws ItemException;

}
