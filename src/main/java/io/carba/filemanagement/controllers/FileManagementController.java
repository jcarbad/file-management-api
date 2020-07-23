package io.carba.filemanagement.controllers;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.dtos.FileVersionDto;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.services.FileService;
import io.carba.filemanagement.services.FileVersionService;
import io.carba.filemanagement.services.impl.FileVersionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("files")
public class FileManagementController {

   private final FileService fileService;
   private final FileVersionService fileVersionService;

   public FileManagementController(FileService fileService, FileVersionService fileVersionService) {
      this.fileService = fileService;
      this.fileVersionService = fileVersionService;
   }

   @GetMapping(value = "/{fileId}")
   private ResponseEntity<byte[]> fetchFile(@PathVariable Long fileId, @RequestParam(required = false) Long version) {
      FileVersion fileVersion = fileVersionService.getFileVersionByParentIdAndVersion(fileId, version).orElse(null);
      File file = fileService.getAllById(fileId).orElse(null);

      if (fileVersion == null || file == null) {
         return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok()
            .header("Content-Type", file.getMediaType())
            .body(fileVersion.getFile());
   }


   @GetMapping(value = "/{fileId}/all")
   @ResponseStatus(HttpStatus.OK)
   private FileDto fetchAll(@PathVariable Long fileId) throws Exception
   {
      File file = fileService.getAllById(fileId).orElse(null);

      if (file == null) {
         throw new Exception("Not found");
      }

      List<FileVersionDto> fileVersions = fileVersionService.getAllVersionsByParentId(fileId).orElse(Collections.emptyList()).stream()
            .map(fv -> FileVersionDto.builder()
               .uri("/files/" + fileId + "?version=" + fv.getSequenceNumber())
               .version(fv.getSequenceNumber())
               .build())
            .collect(Collectors.toList());

      return FileDto.builder()
            .fileId(fileId)
            .name(file.getFilename())
            .mimeType(file.getMediaType())
            .description(file.getDescription())
            .versions(fileVersions)
            .build();
   }

/**
   @PostMapping
   private FileDto saveFile() {
      return FileDto.builder().fileId("File #1").build();
   }

   @PutMapping("/{fileId}")
   private String updateFile(@PathVariable String fileId) {
      return "GOT file{id: " + fileId + ", version: latest + 1}";
   }

   @DeleteMapping("/{fileId}")
   private String deleteFile(@PathVariable String fileId, @RequestParam String version) {
      return "Deleted {file: " + fileId + ", version: " + version + "}";
   }

   @DeleteMapping("/{fileId}/all")
   private String deleteAll(@PathVariable String fileId) {
      return "Deleted all versions of " + fileId;
   }

   */
}
