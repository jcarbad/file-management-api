package io.carba.filemanagement.exceptions;

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
