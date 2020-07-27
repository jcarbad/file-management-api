package io.carba.filemanagement.dtos;

import io.carba.filemanagement.model.File;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of a file version sub-resource that will depend on a FileDto resource.
 **/
@Data
@Builder
public class FileVersionDto {
   private final String filename;
   private final String mediaType;
   private final String description;
   private final String uri;
   private final String createdAt;
   private final String updatedAt;

   public static List<FileVersionDto> fromFileModels(List<File> fileModels) {
      return fileModels.stream()
            .map(fm -> FileVersionDto.builder()
                  .filename(fm.getFilename())
                  .description(fm.getDescription())
                  .mediaType(fm.getMediaType())
                  .uri("/files/" + fm.getFileId() + "?version=" + fm.getVersion())
                  .createdAt(fm.getCreatedAt().toString())
                  .updatedAt(fm.getUpdatedAt().toString())
                  .build())
            .collect(Collectors.toList());
   }
}
