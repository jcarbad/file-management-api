package io.carba.filemanagement.exceptions;

/**
 * Exception thrown when the arguments provided to a File operation do not allow to identify a File. Should result in no-op.
 * */
public class FileNotFoundException extends Exception {
   public FileNotFoundException(String message) {
      super(message);
   }

   public FileNotFoundException(Long fileId) {
      this(String.format("File with fileId: %s was not found", fileId));
   }

   public FileNotFoundException(Long fileId, Long version) {
      this(String.format("File with fileId: %s and version: %d was not found", fileId, version));
   }
}
