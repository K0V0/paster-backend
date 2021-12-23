package com.kovospace.paster.item.controllerHelpers;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.WebsocketService;
import com.kovospace.paster.item.converters.itemResponseDTOConverter.ItemResponseDTOPropertyMap;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.ItemResponseDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.services.ItemService;
import io.jsonwebtoken.JwtException;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemControllerResponderImpl implements ItemControllerResponder {

  private ItemService itemService;
  private JwtService jwtService;
  private ModelMapper modelMapper;
  private WebsocketService websocketService;

  @Autowired
  public ItemControllerResponderImpl(
      ItemService itemService,
      JwtService jwtService,
      ModelMapper modelMapper,
      WebsocketService websocketService
  ) {
    this.itemService = itemService;
    this.jwtService = jwtService;
    this.modelMapper = modelMapper;
    this.websocketService = websocketService;
    this.initMappings();
  }

  private void initMappings() {
    // TODO WARNING: An illegal reflective access operation has occurred
    this.modelMapper.addMappings(new ItemResponseDTOPropertyMap());
  }

  @Override
  public ResponseEntity<ItemsResponseDTO> getItems(String token)
      throws JwtException {
    long userId = jwtService.parse(token);
    return new ResponseEntity<ItemsResponseDTO>(
        new ItemsResponseDTO(
            itemService.getAllOfUser(userId)
                .stream()
                .map(i -> modelMapper.map(i, ItemResponseDTO.class))
                .collect(Collectors.toList())
        ),
        HttpStatus.OK
    );

  }

  @Override
  public ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
      throws JwtException, ItemNotFoundException {
    long userId = jwtService.parse(token);
    return new ResponseEntity<ItemResponseDTO>(
      modelMapper.map(
          itemService.getItemOfUser(userId, itemId),
          ItemResponseDTO.class
      ),
      HttpStatus.OK
    );
  }

  @Override
  public void addItem(String token, ItemRequestDTO dto) throws JwtException {
    long userId = jwtService.parse(token);
    itemService.addItem(userId, dto.getText());
    websocketService.notifyForChanges(userId);
  }

  @Override
  public void deleteItem(String token, long itemId) throws JwtException {
    long userId = jwtService.parse(token);
    itemService.deleteItem(userId, itemId);
  }
}
