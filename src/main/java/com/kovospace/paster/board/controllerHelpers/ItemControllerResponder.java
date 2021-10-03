package com.kovospace.paster.board.controllerHelpers;

import com.kovospace.paster.base.exceptions.JwtTokenException;
import com.kovospace.paster.base.exceptions.jwtTokenException.InvalidJwtTokenException;
import com.kovospace.paster.board.dtos.ItemRequestDTO;
import com.kovospace.paster.board.dtos.ItemResponseDTO;
import com.kovospace.paster.board.dtos.ItemsResponseDTO;
import com.kovospace.paster.board.exceptions.ItemNotFoundException;
import org.springframework.http.ResponseEntity;

public interface ItemControllerResponder {

  ResponseEntity<ItemsResponseDTO> getItems(String token) throws JwtTokenException;

  ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
      throws JwtTokenException, ItemNotFoundException;

  void addItem(String token, ItemRequestDTO dto) throws JwtTokenException;

}
