package com.merchstore.exceptions;

public class InvalidProductException extends RuntimeException {

  public InvalidProductException(String message) {
    super(message);
  }
}