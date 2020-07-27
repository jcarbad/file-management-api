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

/**
 *
 *   Main controller and entry point to our File Management REST API. Supports operations for clients to store/create a
 *   new file with additional details, update details of an existing file, fetch an existing file or its details, and to
 *   delete and existing file or its different versions.
 *
 *   This controller is advices by @ControllerExceptionHandler.
 *
 *    @see io.carba.filemanagement.controllers.advice.ControllerExceptionHandler
 *    @author Armando Carballo <jcarbad@gmail.com>
 * */
@RestController
@RequestMapping("files")
public class FileManagementController {

   private final FileService fileService;

   public FileManagementController(FileService fileService) {
      this.fileService = fileService;
   }

   /**
    * Create/store new files with additional details via an HTTP POST using a FileDto.Request as multipart/form-data values.
    *
    * @param fileData input representation of a File resource in our system
    * @throws InvalidFileArgException if the input data provided is not valid
    * */
   @ResponseStatus(CREATED)
   @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
   public FileDto.Response saveFile(@ModelAttribute FileDto.Request fileData) throws InvalidFileArgException, IOException {
      if (fileData == null || fileData.getFile() == null) {
         throw new InvalidFileArgException("A file must be provided");
      }

      File created = fileService.createFile(fileData, fileData.getFile().getBytes());
      return FileDto.Response.fromFileModel(created);
   }


   /**
    *  Edit an existing file's contents or details using a FileDto.Request representation
    *
    * @param fileId File ID of the resource to be updated
    * @param fileData input representation of a File resource in our system
    * @throws InvalidFileArgException if the input data provided is not valid
    * @throws FileNotFoundException if file ID provided doesn't any existing file resource
    * */
   @ResponseStatus(NO_CONTENT)
   @PutMapping(value = "/{fileId}")
   public void editFile(@PathVariable Long fileId, @ModelAttribute FileDto.Request fileData)
         throws InvalidFileArgException, IOException, FileNotFoundException {

      fileService.editFile(fileId, fileData, fileData.getFile() != null ? fileData.getFile().getBytes() : new byte[0]);
   }


   /**
    * Fetch the actual binary file by providing its file resource ID.
    *
    * @param fileId File ID of the resource to be retrieved
    * @param version Version number of the file to be retrieved
    * */
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


   /**
    * Fetch all details of a file resource by its file ID.
    *
    * @param fileId File ID of the resource to be retrieved the details for
    * @throws InvalidFileArgException if the input data provided is not valid
    * @throws FileNotFoundException if file ID provided doesn't any existing file resource
    * */
   @ResponseStatus(OK)
   @GetMapping(value = "/{fileId}/details")
   public FileDto.Response fetchAll(@PathVariable Long fileId) throws FileNotFoundException, InvalidFileArgException {
      List<File> files = fileService.getAllFiles(fileId);

      if (CollectionUtils.isEmpty(files)) {
         throw new FileNotFoundException("File with ID: " + fileId + " was not found.");
      }

      return FileDto.Response.fromFileModels(files);
   }


   /**
    * Delete a specific version of a file including its contents and meta-data. Has an empty response with 204 No-Content
    * to ensure the idempotent behavior
    *
    * @param fileId File ID of the resource to be deleted
    * @param version Specific version of a file resource to be deleted
    * */
   @ResponseStatus(NO_CONTENT)
   @DeleteMapping("/{fileId}")
   public void deleteFileVersion(@PathVariable Long fileId, @RequestParam Long version) throws InvalidFileArgException {
      fileService.deleteFileVersion(fileId, version);
   }


   /**
    * Deletes all file versions and meta-data under the same file ID. Has an empty response with 204 No-Content
    * to ensure the idempotent behavior
    *
    * @param fileId File ID of the resource to be deleted
    * */
   @ResponseStatus(NO_CONTENT)
   @DeleteMapping("/{fileId}/all")
   public void deleteAll(@PathVariable Long fileId) throws InvalidFileArgException {
      fileService.deleteAllByFileId(fileId);
   }
}
