package io.carba.filemanagement.controllers.advice;

import io.carba.filemanagement.dtos.FileManagementApiException;
import io.carba.filemanagement.exceptions.FileNotFoundException;
import io.carba.filemanagement.exceptions.InvalidFileArgException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {

   @ResponseStatus(NOT_FOUND)
   @ExceptionHandler(FileNotFoundException.class)
   public FileManagementApiException handleNotFoundException(Exception ex, HttpServletRequest req) {
      return handleApiException(ex, NOT_FOUND, req);
   }

   @ResponseStatus(BAD_REQUEST)
   @ExceptionHandler(InvalidFileArgException.class)
   public FileManagementApiException handleBadRequestException(Exception ex, HttpServletRequest req) {
      return handleApiException(ex, BAD_REQUEST, req);
   }

   private FileManagementApiException handleApiException(Exception ex, HttpStatus status, HttpServletRequest req) {
      return FileManagementApiException.builder()
            .status(status.toString())
            .message(ex.getMessage())
            .path(req.getRequestURI())
            .build();
   }
}