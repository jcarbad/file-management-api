package io.carba.filemanagement.controllers;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("files")
public class FileManagementController {

   private final FileService fileService;

   public FileManagementController(FileService fileService) {
      this.fileService = fileService;
   }

   @GetMapping(value = "/{fileId}")
   @ResponseStatus(HttpStatus.OK)
   private ResponseEntity<byte[]> fetchFile(@PathVariable Long fileId, @RequestParam(required = false) Long version) throws Exception {
      File file = fileService.getFileVersion(fileId, version);

      if (file == null) {
         throw new Exception("File not found");
      }

      return ResponseEntity.ok()
            .header("Content-Type", file.getMediaType())
            .body(file.getContents());
   }


   @GetMapping(value = "/{fileId}/details")
   @ResponseStatus(HttpStatus.OK)
   private FileDto.Response fetchAll(@PathVariable Long fileId) throws Exception {
      List<File> files = fileService.getAllFiles(fileId);

      if (CollectionUtils.isEmpty(files)) {
         throw new Exception("File not Found");
      }

      return FileDto.Response.fromFileModels(files);
   }

   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   private FileDto.Response saveFile(@ModelAttribute FileDto.Request fileData) throws Exception {
      File created = fileService.createFile(fileData, fileData.getFile().getBytes());
      return FileDto.Response.fromFileModel(created);
   }


   @PutMapping("/{fileId}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   private void editFile(@PathVariable Long fileId, @ModelAttribute FileDto.Request fileData) throws Exception {
      if (fileId == null) {
         throw new Exception("File ID must be provided");
      }

      fileService.editFile(fileId, fileData, fileData.getFile().getBytes());
   }

   @DeleteMapping("/{fileId}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   private void deleteFileVersion(@PathVariable Long fileId, @Valid @RequestParam @Min(1) Long version) throws Exception {
      if (version == null) {
         throw new Exception("File version must be provided");
      }

      fileService.deleteFileVersion(fileId, version);
   }


   @DeleteMapping("/{fileId}/all")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   private void deleteAll(@PathVariable Long fileId) throws Exception {
      fileService.deleteAllByFileId(fileId);
   }
}
