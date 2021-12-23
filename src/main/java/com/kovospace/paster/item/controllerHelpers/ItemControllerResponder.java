package com.kovospace.paster.item.controllerHelpers;

import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.ItemResponseDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;

public interface ItemControllerResponder {

  ResponseEntity<ItemsResponseDTO> getItems(String token) throws JwtException;

  ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
      throws JwtException, ItemNotFoundException;

  void addItem(String token, ItemRequestDTO dto) throws JwtException;

  void deleteItem(String token, long itemId) throws JwtException;

}
