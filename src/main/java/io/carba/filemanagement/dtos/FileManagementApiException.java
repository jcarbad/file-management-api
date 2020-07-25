package io.carba.filemanagement.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileManagementApiException {
   private String status;
   private String message;
   private String path;
}
