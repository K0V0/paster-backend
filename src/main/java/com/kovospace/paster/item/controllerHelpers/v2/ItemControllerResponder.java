package com.kovospace.paster.item.controllerHelpers.v2;

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
import org.springframework.http.ResponseEntity;

public interface ItemControllerResponder {

  ResponseEntity<ItemsResponseDTO> getItems(String token) throws JwtException, UserNotFoundException;

  ResponseEntity<ItemResponseDTO> getItem(String token, long itemId)
      throws JwtException, ItemNotFoundException;

  ResponseEntity<FileResponseDTO> getFile(String token, long itemId)
          throws JwtException, ItemNotFoundException, FileException;

  void addTextItem(String token, TextItemRequestDTO dto) throws JwtException, UserNotFoundException;

  ResponseEntity<FileItemUploadResponseDTO> initiateFileItem(String token, FileItemInitiateRequestDTO dto)
          throws JwtException, UserNotFoundException, FileException, ItemNotFoundException;

  ResponseEntity<FileItemUploadResponseDTO> uploadFileChunk(String token, FileItemUploadChunkRequestDTO dto)
          throws JwtException, ItemNotFoundException;

  ResponseEntity<Void> deleteItem(String token, long itemId) throws JwtException, ItemException;

}
