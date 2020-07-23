package io.carba.filemanagement.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class FileDto
{
   private final Long fileId;
   private final String name;
   private final String mimeType;
   private final String description;
   private final String createdAt;
   private List<FileVersionDto> versions;
}
