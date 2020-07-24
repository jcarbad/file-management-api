package io.carba.filemanagement.dtos;

import io.carba.filemanagement.model.File;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileDto
{
   @Data
   @Builder
   @ToString
   @NotNull
   public static class Request
   {
      private final String filename;
      private final String mediaType;
      private final String description;
   }

   @Data
   @Builder
   @ToString
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
         return fromFileModels(Collections.singletonList(fileModel));
      }
   }
}
