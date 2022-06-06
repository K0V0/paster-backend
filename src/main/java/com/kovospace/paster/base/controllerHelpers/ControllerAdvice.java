package com.kovospace.paster.base.controllerHelpers;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.dtos.ErrorsResponseDTO;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.exceptions.FeatureNotImplementedException;
import com.kovospace.paster.base.services.StringsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {

  protected StringsService stringsService;
  protected ControllerAdvice(StringsService stringsService) {
    this.stringsService = stringsService;
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorResponseDTO wrongMethod() {
    return stringsService.getErrorResponseDTO("general.endpoint.httpMethod.wrong");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(NoHandlerFoundException.class)
  public ErrorResponseDTO notFoundMethod() {
    return stringsService.getErrorResponseDTO("general.endpoint.missing");
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE) // 415
  @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
  public ErrorResponseDTO notSupportedMedia() {
    return stringsService.getErrorResponseDTO("general.endpoint.mediaType.wrong");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponseDTO requestBodyMissing() {
    return stringsService.getErrorResponseDTO("general.endpoint.request.wrong");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 403
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ErrorResponseDTO authorizationHeaderMissing() {
    return stringsService.getErrorResponseDTO("general.endpoint.authentication.missing");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorsResponseDTO someFieldMissing(MethodArgumentNotValidException ex) {
    stringsService.getLocale();
    Map<String, List<ErrorResponseDTO>> errorMessages = new HashMap<>();
    ex.getFieldErrors()
            .stream()
            .filter(Objects::nonNull)
            .forEach(fieldError -> {
              String field = fieldError.getField();
              errorMessages.computeIfAbsent(field, k -> new ArrayList<>());
              errorMessages.get(field).add(stringsService.getErrorResponseDTO(fieldError.getDefaultMessage()));
            });
    return new ErrorsResponseDTO(errorMessages);
  }

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED) // 501
  @ExceptionHandler(FeatureNotImplementedException.class)
  public ErrorResponseDTO implementationMissing() {
    return stringsService.getErrorResponseDTO("general.implementation.missing");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
  @ExceptionHandler(ApiKeyMissingException.class)
  public ErrorResponseDTO apiKeyMissing() {
    return stringsService.getErrorResponseDTO("general.endpoint.authentication.apikey.missing");
  }

}
