package io.carba.filemanagement.dtos;

import lombok.Builder;
import lombok.Data;

/**
 * Base representation of an API error to hint the client of any mishappenings that occur.
 * */
@Data
@Builder
public class FileManagementApiException {
   private String status;
   private String message;
   private String path;
}
