package io.carba.filemanagement.services;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.exceptions.InvalidFileArgException;
import io.carba.filemanagement.model.File;
import java.util.List;

public interface FileService {
   File createFile(FileDto.Request create, byte[] contents) throws InvalidFileArgException;

   File editFile(Long fileId, FileDto.Request update, byte[] contents) throws InvalidFileArgException;

   File getFileVersion(Long fileId, Long version) throws InvalidFileArgException;

   List<File> getAllFiles(Long fileId) throws InvalidFileArgException;

   void deleteFileVersion(Long fileId, Long version) throws InvalidFileArgException;

   void deleteAllByFileId(Long fileId) throws InvalidFileArgException;
}
