package io.carba.filemanagement.controllers;

import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.services.FileService;
import io.carba.filemanagement.services.FileVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
/**
   @GetMapping("/{fileId}/all")
   private byte[][] fetchAll(@PathVariable String fileId) {
      return new byte[][]{"A".getBytes(), "B".getBytes()};
   }

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
