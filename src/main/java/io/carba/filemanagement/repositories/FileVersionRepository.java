package io.carba.filemanagement.repositories;

import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FileVersionRepository extends CrudRepository<FileVersion, Long> {
   Optional<Set<FileVersion>> findAllByParentFile(File parentFile);
   Optional<FileVersion> findBySequenceNumber(Long sequenceNum);
   Optional<FileVersion> findFirstByParentFileOrderBySequenceNumberAsc(File parentFile);
}
