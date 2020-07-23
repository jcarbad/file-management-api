package io.carba.filemanagement.services;

import io.carba.filemanagement.model.FileVersion;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FileVersionService {
   Optional<FileVersion> createFileVersion(FileVersion toCreate);
   Optional<FileVersion> getFileVersionByParentIdAndVersion(Long parentId, Long version);
   Optional<List<FileVersion>> getAllVersionsByParentId(Long parentId);
   void deleteFileVersion(Long parentId, Long version);
}
