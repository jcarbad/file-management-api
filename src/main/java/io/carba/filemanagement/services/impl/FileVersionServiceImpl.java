package io.carba.filemanagement.services.impl;

import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.repositories.FileVersionRepository;
import io.carba.filemanagement.services.FileVersionService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log
@Service
public class FileVersionServiceImpl implements FileVersionService {
   private final FileVersionRepository fileVersionRepository;

   public FileVersionServiceImpl(FileVersionRepository fileVersionRepository) {
      this.fileVersionRepository = fileVersionRepository;
   }

   @Override
   public Optional<FileVersion> createFileVersion(FileVersion toCreate) {
      return Optional.of(fileVersionRepository.save(toCreate));
   }

   @Override
   public Optional<FileVersion> getFileVersionByParentIdAndVersion(Long parentId, Long version) {
      File parent = File.builder()
            .fileId(parentId)
            .build();

      return version == null ?
         fileVersionRepository.findFirstByParentFileOrderBySequenceNumberAsc(parent)
         : fileVersionRepository.findExactVersion(parent, version);
   }

   @Override
   public Optional<List<FileVersion>> getAllVersionsByParentId(Long parentId) {
      File parent = File.builder()
            .fileId(parentId)
            .build();

      return fileVersionRepository.findAllByParentFile(parent);
   }

   @Override
   public void deleteFileVersion(Long parentId, Long version) {
      File parent = File.builder()
            .fileId(parentId)
            .build();

      log.info("\n\n\n\n" + parent.toString());

      fileVersionRepository.deleteFileVersionByParentFileAndSequenceNumberEquals(parent, version);
   }
}
