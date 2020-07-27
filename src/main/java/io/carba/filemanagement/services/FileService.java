package io.carba.filemanagement.services;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.exceptions.FileNotFoundException;
import io.carba.filemanagement.exceptions.InvalidFileArgException;
import io.carba.filemanagement.model.File;
import java.util.List;

/**
 * Definition of File service interface to enable the file operations.
 *
 * @author Armando Carballo <jcarbad@gmail.com>
 * */
public interface FileService {

   /**
    * Persists a new file provided its details and binary contents.
    *
    * @param create Object representation containing the details of the File to be created
    * @param contents Binary contents of the file
    * @throws InvalidFileArgException When the provided input data is invalid
    * @return File entity created
    * */
   File createFile(FileDto.Request create, byte[] contents) throws InvalidFileArgException;


   /**
    * Persists modifications done on an existing File entity
    *
    * @param fileId File ID of File to be modified
    * @param update input details to modify
    * @param contents Binary contents of the file
    * @throws InvalidFileArgException When the provided input data is invalid
    * @throws FileNotFoundException When file ID provided doesn't any existing file resource
    * @return File entity updated
    * */
   File editFile(Long fileId, FileDto.Request update, byte[] contents) throws InvalidFileArgException, FileNotFoundException;


   /**
    * Retrieves a specific version of a File entity provided its file ID and file version.
    *
    * @param fileId File ID of File to be modified
    * @param version Specific version of the file
    * @throws InvalidFileArgException  When the provided file ID or version are invalid
    * @return Specific version of File, null if not matched
    * */
   File getFileVersion(Long fileId, Long version) throws InvalidFileArgException;


   /**
    * Retrieves a list of File entities under a same file ID.
    *
    * @param fileId File ID of File to be modified
    * @throws InvalidFileArgException  When the provided file ID is invalid
    * @return List for File entities under the given file ID
    * */
   List<File> getAllFiles(Long fileId) throws InvalidFileArgException;


   /**
    * Removes a specific version of a File provided its file ID.
    *
    * @param fileId File ID of File to be modified
    * @param version Specific version of the file
    * @throws InvalidFileArgException  When the provided file ID or version are invalid
    * */
   void deleteFileVersion(Long fileId, Long version) throws InvalidFileArgException;


   /**
    * Removes a specific version of a File provided its file ID.
    *
    * @param fileId File ID of File to be modified
    * @throws InvalidFileArgException  When the provided file ID or version are invalid
    * */
   void deleteAllByFileId(Long fileId) throws InvalidFileArgException;
}
