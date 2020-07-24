package io.carba.filemanagement.services.impl;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.repositories.FileRepository;
import io.carba.filemanagement.services.FileService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.*;

@Service
public class FileServiceImpl implements FileService {

   private final FileRepository fileRepository;

   public FileServiceImpl(FileRepository fileRepository) {
      this.fileRepository = fileRepository;
   }

   @Override
   public File createFile(FileDto.Request create, byte[] contents) throws Exception {
      if (contents.length == 0) {
         throw new Exception("File contents can't be empty");
      }

      String filename = create.getFilename();
      String description = create.getDescription();
      String mediaType = create.getMediaType();

      if (Stream.of(filename, description, mediaType).anyMatch(StringUtils::isEmpty)) {
         throw new Exception("File name, description and media type must be provided");
      }

      Long last = fileRepository.findLastFileId();

      File newFile = File.builder()
            .fileId(last == null ? 1L : last + 1L)
            .filename(filename)
            .description(description)
            .mediaType(mediaType)
            .version(1L)
            .contents(contents)
            .build();

      return fileRepository.save(newFile);
   }

   @Override
   public File getFileVersion(Long fileId, Long version) throws Exception {
      if (fileId == null) {
         throw new Exception("File ID must be provided");
      }

      return version == null
            ? fileRepository.findFirstByFileIdOrderByVersionDesc(fileId)
            : fileRepository.findByFileIdAndVersionEquals(fileId, version);
   }

   @Override
   public List<File> getAllFiles(Long fileId) throws Exception {
      if (fileId == null) {
         throw new Exception("File ID must be provided");
      }

      return fileRepository.findAllByFileId(fileId);
   }

   @Override
   public File editFile(Long fileId, FileDto.Request update, byte[] contents) throws Exception {
      if (fileId == null) {
         throw new Exception("File ID must be provided");
      }

      String filename = update.getFilename();
      String description = update.getDescription();
      String mediaType = update.getMediaType();
      boolean emptyContents = contents.length == 0;

      File oldFile = fileRepository.findFirstByFileIdOrderByVersionDesc(fileId);

      File updatedFile = File.builder()
            .fileId(oldFile.getFileId())
            .filename(isEmpty(filename) ? oldFile.getFilename() : filename)
            .description(isEmpty(description) ? oldFile.getDescription() : description)
            .mediaType(isEmpty(mediaType) ? oldFile.getMediaType() : mediaType)
            .contents(emptyContents ? oldFile.getContents() : contents)
            .version(emptyContents ? null : oldFile.getVersion() + 1L)
            .build();

      return fileRepository.save(updatedFile);
   }

   @Override
   public void deleteFileVersion(Long fileId, Long version) throws Exception {
      if (fileId == null || version == null) {
         throw new Exception("File ID and version must be provided");
      }

      fileRepository.deleteFileByFileIdAndVersionEquals(fileId, version);
   }

   @Override
   public void deleteAllByFileId(Long fileId) throws Exception {
      if (fileId == null) {
         throw new Exception("File ID must be provided");
      }

      fileRepository.deleteAllByFileId(fileId);
   }
}
