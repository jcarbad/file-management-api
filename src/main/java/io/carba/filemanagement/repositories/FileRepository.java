package io.carba.filemanagement.repositories;

import io.carba.filemanagement.model.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
   @Transactional
   Optional<File> findByFileId(Long id);
}
