package com.master.data.management.datamodel.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class PropertyUserCreationException extends RuntimeException {

  private final HttpStatus status;
  public PropertyUserCreationException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
