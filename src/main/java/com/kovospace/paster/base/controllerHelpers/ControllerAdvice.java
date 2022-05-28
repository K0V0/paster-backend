package com.kovospace.paster.base.controllerHelpers;

import com.kovospace.paster.base.configurations.strings.Strings;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.base.dtos.ErrorsResponseDTO;
import com.kovospace.paster.base.exceptions.ApiKeyMissingException;
import com.kovospace.paster.base.exceptions.FeatureNotImplementedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorResponseDTO wrongMethod() {
    return new ErrorResponseDTO("general.endpoint.httpMethod.wrong");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(NoHandlerFoundException.class)
  public ErrorResponseDTO notFoundMethod() {
    return new ErrorResponseDTO("general.endpoint.missing");
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
  public ErrorResponseDTO notSupportedMedia() {
    return new ErrorResponseDTO("general.endpoint.mediaType.wrong");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponseDTO requestBodyMissing() {
    return new ErrorResponseDTO("general.endpoint.request.wrong");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 403
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ErrorResponseDTO authorizationHeaderMissing() {
    return new ErrorResponseDTO("general.endpoint.authentication.missing");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorsResponseDTO someFieldMissing(MethodArgumentNotValidException ex) {
    Map<String, Map<String, String>> errorMessages = ex.getFieldErrors()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.toMap(
                            DefaultMessageSourceResolvable::getDefaultMessage,
                            val -> Strings.s(Optional.ofNullable(val.getDefaultMessage()).orElse(""))
                    )));
    return new ErrorsResponseDTO(errorMessages);
  }

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED) // 501
  @ExceptionHandler(FeatureNotImplementedException.class)
  public ErrorResponseDTO implementationMissing() {
    return new ErrorResponseDTO("general.implementation.missing");
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
  @ExceptionHandler(ApiKeyMissingException.class)
  public ErrorResponseDTO apiKeyMissing() {
    return new ErrorResponseDTO("general.endpoint.authentication.apikey.missing");
  }

}
