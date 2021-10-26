package com.kovospace.paster.item.controllers;

import com.kovospace.paster.item.controllerHelpers.ItemControllerResponder;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.ItemResponseDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import io.jsonwebtoken.JwtException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class ItemController {

  private ItemControllerResponder responder;

  @Autowired
  public ItemController(ItemControllerResponder responder) {
    this.responder = responder;
  }

  @GetMapping("/items")
  public ResponseEntity<ItemsResponseDTO> getAll(
      @RequestHeader(value = "Authorization") String token
  ) throws JwtException {
    return responder.getItems(token);
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<ItemsResponseDTO> get(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable long id
  ) throws JwtException {
    //return responder.getItemsList(token);
    return null;
  }

  @PostMapping("/item")
  public ResponseEntity<Void> add(
      @RequestHeader(value = "Authorization") String token,
      @Valid @RequestBody ItemRequestDTO dto
  ) throws JwtException {
    responder.addItem(token, dto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/item")
  public ResponseEntity<ItemResponseDTO> update() {
    return null;
  }

  @DeleteMapping("/item")
  public ResponseEntity<ItemResponseDTO> delete() {
    return null;
  }

}
