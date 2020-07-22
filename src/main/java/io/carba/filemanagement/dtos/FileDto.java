package io.carba.filemanagement.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class FileDto
{
   private final String fileId;
   private final String version;
   private final String name;
   private final String mimeType;
   private final String description;
   private final String createdAt;
}
