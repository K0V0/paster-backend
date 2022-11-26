package com.kovospace.paster.item.controllerHelpers.v2;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.WebsocketService;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.dtos.v2.FileItemInitiateRequestDTO;
import com.kovospace.paster.item.dtos.v2.FileItemUploadChunkRequestDTO;
import com.kovospace.paster.item.dtos.v2.FileItemUploadResponseDTO;
import com.kovospace.paster.item.dtos.v2.FileResponseDTO;
import com.kovospace.paster.item.dtos.v2.ItemResponseDTO;
import com.kovospace.paster.item.dtos.v2.TextItemRequestDTO;
import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.exceptions.v2.FileException;
import com.kovospace.paster.item.mappings.dtoConversions.v2.FileToFileResponseDTOConversion;
import com.kovospace.paster.item.mappings.dtoConversions.v2.ItemToFileItemUploadResponseDTOConversion;
import com.kovospace.paster.item.mappings.dtoConversions.v2.ItemToItemResponseDTOConversion;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.services.FilesystemOperationsService;
import com.kovospace.paster.item.services.ItemService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.kovospace.paster.base.utils.Utils.isFilled;

@Component("v2Responder")
public class ItemControllerResponderImpl implements ItemControllerResponder {

  private final ItemService itemService;
  private final JwtService jwtService;
  private final WebsocketService websocketService;
  private final FilesystemOperationsService filesystemOperationsService;

  @Qualifier("v2ItemToItemResponseDTOConversion")
  private final ItemToItemResponseDTOConversion itemToItemResponseDTOConversion;

  private final FileToFileResponseDTOConversion fileToFileResponseDTOConversion;
  private final ItemToFileItemUploadResponseDTOConversion itemToFileItemUploadResponseDTOConversion;

  @Autowired
  public ItemControllerResponderImpl(
          ItemService itemService,
          JwtService jwtService,
          WebsocketService websocketService,
          FilesystemOperationsService filesystemOperationsService, ItemToItemResponseDTOConversion itemToItemResponseDTOConversion,
          FileToFileResponseDTOConversion fileToFileResponseDTOConversion, ItemToFileItemUploadResponseDTOConversion itemToFileItemUploadResponseDTOConversion
  ) {
    this.itemService = itemService;
    this.jwtService = jwtService;
    this.filesystemOperationsService = filesystemOperationsService;
    this.itemToItemResponseDTOConversion = itemToItemResponseDTOConversion;
    this.websocketService = websocketService;
    this.fileToFileResponseDTOConversion = fileToFileResponseDTOConversion;
    this.itemToFileItemUploadResponseDTOConversion = itemToFileItemUploadResponseDTOConversion;
  }

  @Override
  public ResponseEntity<ItemsResponseDTO> getItems(String token)
          throws JwtException, UserNotFoundException
  {
    long userId = jwtService.parse(token);
    return new ResponseEntity<>(
        new ItemsResponseDTO<>(itemService.getAllOfUser(userId)
                .stream()
                .map(itemToItemResponseDTOConversion)
                .collect(Collectors.toList())),
            HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
          throws JwtException, ItemNotFoundException
  {
    long userId = jwtService.parse(token);
    return new ResponseEntity<ItemResponseDTO>(
            itemToItemResponseDTOConversion.apply(itemService.getItemOfUser(userId, itemId)),
            HttpStatus.OK);
  }

  @Override
  public ResponseEntity<FileResponseDTO> getFile(String token, long itemId)
          throws JwtException, ItemNotFoundException, FileException {
    long userId = jwtService.parse(token);
    Item item = itemService.getItemOfUser(userId, itemId);
    byte[] data = filesystemOperationsService.getFileData(item.getFile());
    return new ResponseEntity<>(fileToFileResponseDTOConversion.apply(item.getFile(), data), HttpStatus.OK);
  }

  @Override
  public void addTextItem(String token, TextItemRequestDTO dto)
          throws JwtException, UserNotFoundException
  {
    long userId = jwtService.parse(token);
    itemService.addTextItem(userId, dto.getText(), dto.getPlatform(), dto.getDeviceName());
    websocketService.notifyForChanges(userId);
  }

  @Override
  public ResponseEntity<FileItemUploadResponseDTO> initiateFileItem(String token, FileItemInitiateRequestDTO dto)
          throws JwtException, UserNotFoundException, FileException, ItemNotFoundException
  {
    long userId = jwtService.parse(token);
    Item item;
    if (isFilled(dto.getFileId()) && isFilled(dto.getItemId())) {
      /** request to continue interrupted upload */
      item = itemService.getItemOfUser(userId, dto.getItemId());
    } else {
      /** request to create new file */
      item = itemService.initiateFile(userId, dto.getPlatform(), dto.getDeviceName(),
              dto.getOriginalFileName(), dto.getMimeType(), dto.getChunksCount(), dto.getChunkSize());
    }
    return new ResponseEntity<FileItemUploadResponseDTO>(
            itemToFileItemUploadResponseDTOConversion.apply(item),
            HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<FileItemUploadResponseDTO> uploadFileChunk(String token, FileItemUploadChunkRequestDTO dto)
          throws JwtException, ItemNotFoundException
  {
    long userId = jwtService.parse(token);
    Item item = itemService.addFileChunk(userId, dto.getItemId(), dto.getFileId(), dto.getFileContent(), dto.getChunkNumber());
    FileItemUploadResponseDTO response = itemToFileItemUploadResponseDTOConversion.apply(item);
    HttpStatus responseHttpStatus;
    if (item.getData() != null) {
      responseHttpStatus = HttpStatus.CREATED;
      websocketService.notifyForChanges(userId);
    } else {
      responseHttpStatus = HttpStatus.ACCEPTED;
    }
    return new ResponseEntity<FileItemUploadResponseDTO>(response, responseHttpStatus);
  }

  @Override
  public ResponseEntity<Void> deleteItem(String token, long itemId)
          throws JwtException, ItemException
  {
    long userId = jwtService.parse(token);
    boolean needToDelete = itemService.deleteItem(userId, itemId);
    websocketService.notifyForChanges(userId);
    return new ResponseEntity<Void>(needToDelete ? HttpStatus.OK : HttpStatus.NO_CONTENT);
  }

}
