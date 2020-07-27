package io.carba.filemanagement.exceptions;

/**
 * Exception thrown when the arguments provided to a File operation are invalid. Should result in no-op.
 * */
public class InvalidFileArgException extends Exception {
   public InvalidFileArgException(String message) {
      super(message);
   }
}
