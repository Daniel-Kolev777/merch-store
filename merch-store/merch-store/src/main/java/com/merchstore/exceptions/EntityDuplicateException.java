package com.merchstore.exceptions;

public class EntityDuplicateException extends RuntimeException {

  private final String fieldName;

  public EntityDuplicateException(String type, String fieldName, String value) {
    super(String.format("%s with %s %s already exists.", type, fieldName, value));
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
