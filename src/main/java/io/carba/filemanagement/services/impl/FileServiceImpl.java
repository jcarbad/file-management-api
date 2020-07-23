package io.carba.filemanagement.services.impl;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.repositories.FileRepository;
import io.carba.filemanagement.services.FileService;
import io.carba.filemanagement.services.FileVersionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

   private final FileRepository fileRepository;
   private final FileVersionService fileVersionService;

   public FileServiceImpl(FileRepository fileRepository, FileVersionService fileVersionService) {
      this.fileRepository = fileRepository;
      this.fileVersionService = fileVersionService;
   }

   @Override
   public Optional<File> getAllById(Long fileId) {
      Optional<File> file = fileRepository.findByFileId(fileId);

      if (file.isPresent()) {
         Optional<List<FileVersion>> fileVersions = fileVersionService.getAllVersionsByParentId(fileId);
         file.get().setVersions(fileVersions.orElse(Collections.emptyList()));
      }

      return file;
   }

   @Override
   public Optional<File> updateFile(FileDto update, byte[] file) throws Exception {
      if (file.length == 0) {
         throw new Exception("Invalid file");
      }

      List<FileVersion> fileVersions = fileVersionService.getAllVersionsByParentId(update.getFileId()).orElse(null);
      if (fileVersions == null) {
         throw new Exception("invalid");
      }

      File existing = fileRepository.findByFileId(update.getFileId()).orElse(null);
      if (existing == null) {
         throw new Exception("Non existing");
      }

      existing.setFilename(update.getName());
      existing.setDescription(update.getDescription());
      existing.setMediaType(update.getMimeType());

      long lastSequence = fileVersions.stream()
            .mapToLong(FileVersion::getSequenceNumber)
            .sorted()
            .findFirst()
            .orElse(0);

      FileVersion newVersion = FileVersion.builder()
            .parentFile(existing)
            .sequenceNumber(lastSequence == 0L ? 0L : lastSequence + 1L)
            .file(file)
            .build();

      existing.getVersions().add(newVersion);

      return Optional.of(fileRepository.save(existing));
   }

   @Override
   public void deleteAllById(Long fileId) {
      fileRepository.delete(File.builder().fileId(fileId).build());
   }
}
