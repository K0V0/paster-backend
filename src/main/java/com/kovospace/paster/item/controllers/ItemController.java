package com.kovospace.paster.item.controllers;

import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.item.controllerHelpers.ItemControllerResponder;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.ItemResponseDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.exceptions.ItemNotFoundException;
import com.kovospace.paster.item.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/board")
public class ItemController extends BaseController {

  private final ItemControllerResponder responder;

  @Autowired
  public ItemController(ItemControllerResponder responder) {
    this.responder = responder;
  }

  @GetMapping("/items")
  public ResponseEntity<ItemsResponseDTO> getAll(
      @RequestHeader(value = "Authorization") String token
  ) throws JwtException, UserNotFoundException {
    return responder.getItems(token);
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<ItemResponseDTO> get(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable long id
  ) throws JwtException, ItemNotFoundException {
    return responder.getItem(token, id);
  }

  @PostMapping("/item")
  public ResponseEntity<Void> add(
      @RequestHeader(value = "Authorization") String token,
      @Valid @RequestBody ItemRequestDTO dto
  ) throws JwtException, UserNotFoundException {
    responder.addItem(token, dto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/item/{id}")
  public ResponseEntity<Void> update(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable long id
  ) {
    return null;
  }

  @DeleteMapping("/item/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader(value = "Authorization") String token,
      @PathVariable long id
  ) {
    return responder.deleteItem(token, id);
  }

}
