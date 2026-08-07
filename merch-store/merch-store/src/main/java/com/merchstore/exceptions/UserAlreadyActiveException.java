package com.merchstore.exceptions;

public class UserAlreadyActiveException extends RuntimeException {

  public UserAlreadyActiveException(String message) {
    super(message);
  }
}