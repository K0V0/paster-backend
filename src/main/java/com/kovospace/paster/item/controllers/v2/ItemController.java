package com.kovospace.paster.item.controllers.v2;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import com.kovospace.paster.base.controllers.BaseController;
import com.kovospace.paster.item.controllerHelpers.v2.ItemControllerResponder;
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
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RestController("v2Controller")
@RequestMapping("/api/v2/board")
public class ItemController extends BaseController {

  @Qualifier("v2Responder")
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

  @PublicEndpoint
  @PostMapping("/item")
  public ResponseEntity<Void> add(
      @RequestHeader(value = "Authorization") String token,
      @Valid @RequestBody TextItemRequestDTO dto
  ) throws JwtException, UserNotFoundException {
    responder.addTextItem(token, dto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /** get full file in case of only preview sent to client */
  @GetMapping("/file/{id}")
  public ResponseEntity<FileResponseDTO> getFile(
          @RequestHeader(value = "Authorization") String token,
          @PathVariable long id
  ) throws FileException, ItemNotFoundException {
    return responder.getFile(token, id);
  }

  /** just initiate request - provide some details/defaults about file and upcoming transfer */
  @PostMapping("/file")
  public ResponseEntity<FileItemUploadResponseDTO> addFile(
          @RequestHeader(value = "Authorization") String token,
          @Valid @RequestBody FileItemInitiateRequestDTO dto
  ) throws JwtException, UserNotFoundException, FileException, ItemNotFoundException {
    return responder.initiateFileItem(token, dto);
  }

  /** file transfer by chunks */
  @PutMapping("/file")
  public ResponseEntity<FileItemUploadResponseDTO> uploadChunk(
          @RequestHeader(value = "Authorization") String token,
          @Valid @RequestBody FileItemUploadChunkRequestDTO dto
  ) throws JwtException, ItemNotFoundException {
    return responder.uploadFileChunk(token, dto);
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
  ) throws ItemException {
    return responder.deleteItem(token, id);
  }

}
