package io.carba.filemanagement.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class FileDto
{
   private final Long fileId;
   private final Long version;
   private final String name;
   private final String mimeType;
   private final String description;
   private final String createdAt;
}
