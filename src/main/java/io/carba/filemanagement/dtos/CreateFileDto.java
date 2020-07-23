package io.carba.filemanagement.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateFileDto {
   @NotEmpty
   private final String name;

   @NotEmpty
   private final String mimeType;

   @NotEmpty
   private final String description;
}
