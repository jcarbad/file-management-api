package io.carba.filemanagement.controllers;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.exceptions.FileNotFoundException;
import io.carba.filemanagement.exceptions.InvalidFileArgException;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("files")
public class FileManagementController {

   private final FileService fileService;

   public FileManagementController(FileService fileService) {
      this.fileService = fileService;
   }

   @ResponseStatus(CREATED)
   @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
   public FileDto.Response saveFile(@ModelAttribute FileDto.Request fileData) throws InvalidFileArgException, IOException {
      if (fileData == null || fileData.getFile() == null) {
         throw new InvalidFileArgException("A file must be provided");
      }

      File created = fileService.createFile(fileData, fileData.getFile().getBytes());
      return FileDto.Response.fromFileModel(created);
   }

   @ResponseStatus(NO_CONTENT)
   @PutMapping(value = "/{fileId}")
   public void editFile(@PathVariable Long fileId, @ModelAttribute FileDto.Request fileData)
         throws InvalidFileArgException, IOException, FileNotFoundException {

      fileService.editFile(fileId, fileData, fileData.getFile() != null ? fileData.getFile().getBytes() : new byte[0]);
   }

   @ResponseStatus(OK)
   @GetMapping(value = "/{fileId}")
   public ResponseEntity<byte[]> fetchFile(@PathVariable Long fileId, @RequestParam(required = false) Long version)
         throws FileNotFoundException, InvalidFileArgException {
      File file = fileService.getFileVersion(fileId, version);

      if (file == null) {
         throw version == null ? new FileNotFoundException(fileId) : new FileNotFoundException(fileId, version);
      }

      return ResponseEntity.ok()
            .header("Content-Type", file.getMediaType())
            .body(file.getContents());
   }

   @ResponseStatus(OK)
   @GetMapping(value = "/{fileId}/details")
   public FileDto.Response fetchAll(@PathVariable Long fileId) throws FileNotFoundException, InvalidFileArgException {
      List<File> files = fileService.getAllFiles(fileId);

      if (CollectionUtils.isEmpty(files)) {
         throw new FileNotFoundException("File with ID: " + fileId + " was not found.");
      }

      return FileDto.Response.fromFileModels(files);
   }

   @ResponseStatus(NO_CONTENT)
   @DeleteMapping("/{fileId}")
   public void deleteFileVersion(@PathVariable Long fileId, @RequestParam Long version) throws InvalidFileArgException {
      fileService.deleteFileVersion(fileId, version);
   }

   @ResponseStatus(NO_CONTENT)
   @DeleteMapping("/{fileId}/all")
   public void deleteAll(@PathVariable Long fileId) throws InvalidFileArgException {
      fileService.deleteAllByFileId(fileId);
   }
}
