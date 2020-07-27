package io.carba.filemanagement.dtos;

import io.carba.filemanagement.model.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import static java.util.Collections.singletonList;

/**
 * Base class for the input/output representation of our REST API.
 *
 * @author Armando Carballo <jcarbad@gmail.com>
 * */

public class FileDto {

   /**
    * Input representation of a file resource.
    * */
   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class Request
   {
      private String filename;
      private String mediaType;
      private String description;
      private MultipartFile file;
   }


   /**
    * Output representation of a file resource.
    * */
   @Data
   @Builder
   public static class Response {
      private final Long fileId;
      private final String createdAt;
      private List<FileVersionDto> versions;

      public static Response fromFileModels(List<File> fileModels) {
         return Response.builder()
               .fileId(fileModels.get(0).getFileId())
               .createdAt(fileModels.get(0).getCreatedAt().toString())
               .versions(FileVersionDto.fromFileModels(fileModels))
               .build();
      }

      public static Response fromFileModel(File fileModel) {
         return fromFileModels(singletonList(fileModel));
      }
   }
}
