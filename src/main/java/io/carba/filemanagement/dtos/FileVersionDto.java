package io.carba.filemanagement.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileVersionDto {
   private final String uri;
   private final Long version;
}
