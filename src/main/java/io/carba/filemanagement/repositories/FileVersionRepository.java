package io.carba.filemanagement.repositories;

import io.carba.filemanagement.model.File;
import io.carba.filemanagement.model.FileVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FileVersionRepository extends CrudRepository<FileVersion, Long> {
   Optional<List<FileVersion>> findAllByParentFile(File parentFile);

   Optional<FileVersion> findFirstByParentFileOrderBySequenceNumberAsc(File parentFile);

   @Query("SELECT fv from FileVersion fv WHERE fv.parentFile = :parent AND fv.sequenceNumber = :sequenceNum")
   Optional<FileVersion> findExactVersion(File parent, Long sequenceNum);
}
