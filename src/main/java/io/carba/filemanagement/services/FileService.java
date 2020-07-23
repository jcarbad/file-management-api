package io.carba.filemanagement.services;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.model.File;

import java.util.Optional;

public interface FileService {
   Optional<File> getAllById(Long fileId);
   Optional<File> updateFile(FileDto update, byte[] file) throws Exception;
   void deleteAllById(Long fileId);
}
