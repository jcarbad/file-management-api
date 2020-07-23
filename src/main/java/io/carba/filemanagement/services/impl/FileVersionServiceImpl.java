package io.carba.filemanagement.services.impl;

import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import io.carba.filemanagement.repositories.FileVersionRepository;
import io.carba.filemanagement.services.FileVersionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

      FileVersion fileVersion = FileVersion.builder()
            .parentFile(parent)
            .sequenceNumber(version)
            .build();

      fileVersionRepository.delete(fileVersion);
   }
}
