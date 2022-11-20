package com.kovospace.paster.item.controllerHelpers.v2;

import com.kovospace.paster.item.dtos.v2.ItemResponseDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.dtos.v2.TextItemRequestDTO;
import com.kovospace.paster.item.exceptions.ItemException;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;

public interface ItemControllerResponder {

  ResponseEntity<ItemsResponseDTO> getItems(String token) throws JwtException, UserNotFoundException;

  ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
      throws JwtException, ItemNotFoundException;

  void addTextItem(String token, TextItemRequestDTO dto) throws JwtException, UserNotFoundException;

  ResponseEntity<Void> deleteItem(String token, long itemId) throws JwtException, ItemException;

}
