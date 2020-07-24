package io.carba.filemanagement.repositories;

import io.carba.filemanagement.model.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

   List<File> findAllByFileId(Long fileId);

   File findFirstByFileIdOrderByVersionDesc(Long fileId);

   File findByFileIdAndVersionEquals(Long fileId, Long version);

   @Query("SELECT f.fileId FROM File f ORDER BY f.fileId DESC")
   Long findLastFileId();

   @Transactional
   void deleteAllByFileId(Long fileId);

   @Transactional
   void deleteFileByFileIdAndVersionEquals(Long fileId, Long version);
}
