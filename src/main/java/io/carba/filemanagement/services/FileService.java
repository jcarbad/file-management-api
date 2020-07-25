package io.carba.filemanagement.services;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;

import java.util.List;

public interface FileService {
   File createFile(FileDto.Request create, byte[] contents) throws Exception;

   File editFile(Long fileId, FileDto.Request update, byte[] contents) throws Exception;

   File getFileVersion(Long fileId, Long version) throws Exception;

   List<File> getAllFiles(Long fileId) throws Exception;

   void deleteFileVersion(Long fileId, Long version) throws Exception;

   void deleteAllByFileId(Long fileId) throws Exception;
}
