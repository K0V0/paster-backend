package com.kovospace.paster.item.controllerHelpers.v1;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.WebsocketService;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.dtos.v1.ItemRequestDTO;
import com.kovospace.paster.item.dtos.v1.ItemResponseDTO;
import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import com.kovospace.paster.item.mappings.dtoConversions.v1.ItemToItemResponseDTOConversion;
import com.kovospace.paster.item.services.ItemService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component("v1Responder")
public class ItemControllerResponderImpl implements ItemControllerResponder {

  private final ItemService itemService;
  private final JwtService jwtService;
  private final WebsocketService websocketService;

  @Qualifier("v1ItemToItemResponseDTOConversion")
  private final ItemToItemResponseDTOConversion conversion;

  @Autowired
  public ItemControllerResponderImpl(
      ItemService itemService,
      JwtService jwtService,
      WebsocketService websocketService,
      ItemToItemResponseDTOConversion conversion) {
    this.itemService = itemService;
    this.jwtService = jwtService;
    this.websocketService = websocketService;
    this.conversion = conversion;
  }

  @Override
  public ResponseEntity<ItemsResponseDTO> getItems(String token)
          throws JwtException, UserNotFoundException
  {
    long userId = jwtService.parse(token);
    return new ResponseEntity<>(
            new ItemsResponseDTO<>(itemService.getAllOfUser(userId)
                    .stream()
                    .filter(item -> item.getData() != null) // V2+ compatibility
                    .map(conversion)
                    .collect(Collectors.toList())),
            HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
          throws JwtException, ItemNotFoundException
  {
    long userId = jwtService.parse(token);
    return Optional.ofNullable(conversion.apply(itemService.getItemOfUser(userId, itemId)))
            .map(itemResponseDTO -> new ResponseEntity<>(itemResponseDTO, HttpStatus.OK))
            .orElseThrow(ItemNotFoundException::new);
  }

  @Override
  public void addItem(String token, ItemRequestDTO dto) throws JwtException, UserNotFoundException {
    long userId = jwtService.parse(token);
    itemService.addTextItem(userId, dto.getText(), dto.getPlatform(), dto.getDeviceName());
    websocketService.notifyForChanges(userId);
  }

  @Override
  public ResponseEntity<Void> deleteItem(String token, long itemId) throws JwtException, ItemException {
    long userId = jwtService.parse(token);
    boolean needToDelete = itemService.deleteItem(userId, itemId);
    websocketService.notifyForChanges(userId);
    return new ResponseEntity<Void>(needToDelete ? HttpStatus.OK : HttpStatus.NO_CONTENT);
  }
}
