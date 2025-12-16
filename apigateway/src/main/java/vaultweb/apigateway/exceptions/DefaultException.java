package vaultweb.apigateway.exceptions;

import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;

import lombok.Getter;

@SuppressWarnings("unused")
@Getter
public class DefaultException extends Exception {

  private final DefaultExceptionLevels level;

  /**
   * Constructs a new Default exception with the specified detail message.
   *
   * @param message the detail message
   */
  public DefaultException(String message) {
    super(message);
    this.level = DefaultExceptionLevels.DEFAULT_EXCEPTION; // default level
  }

  /**
   * Constructs a new Default exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public DefaultException(String message, Throwable cause) {
    super(message, cause);
    this.level = DefaultExceptionLevels.DEFAULT_EXCEPTION; // default level
  }

  /**
   * Constructs a new Default exception with the specified cause.
   *
   * @param cause the cause of the exception
   */
  public DefaultException(Throwable cause) {
    super(cause);
    this.level = DefaultExceptionLevels.DEFAULT_EXCEPTION; // default level
  }

  /**
   * Constructs a new Default exception with the specified message and level.
   *
   * @param message the detail message
   * @param level the severity level of the exception
   */
  public DefaultException(String message, DefaultExceptionLevels level) {
    super(message);
    this.level = level;
  }

  public DefaultException(String message, DefaultExceptionLevels level, Throwable cause) {
    super(message, cause);
    this.level = level;
  }
}
