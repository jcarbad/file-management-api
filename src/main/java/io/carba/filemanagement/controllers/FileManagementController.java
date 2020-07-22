package io.carba.filemanagement.controllers;

import com.sun.tools.javac.util.ArrayUtils;
import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.repositories.FileVersionRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("files")
public class FileManagementController {

   private final FileVersionRepository fileVersionRepository;

   public FileManagementController(FileVersionRepository fileVersionRepository) {
      this.fileVersionRepository = fileVersionRepository;
   }

   @GetMapping(value = "/{fileId}")
   private ResponseEntity<byte[]> fetchFile(@PathVariable String fileId, @RequestParam(required = false) String version) {
      Optional<FileVersion> fileVersion = fileVersionRepository.findFirstByParentFileOrderBySequenceNumberAsc(new File(){{setFileId(Long.parseLong(fileId));}});
      byte[] result = fileVersion.isPresent() ? fileVersion.get().getFile() : new byte[]{0,0,1,0,0,1,0,1};

      return ResponseEntity.ok().header("Content-Type", MediaType.IMAGE_JPEG_VALUE).body(result);
   }

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
}
