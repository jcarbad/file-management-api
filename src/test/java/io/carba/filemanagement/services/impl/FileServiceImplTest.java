package io.carba.filemanagement.services.impl;

import io.carba.filemanagement.dtos.FileDto;
import io.carba.filemanagement.exceptions.FileNotFoundException;
import io.carba.filemanagement.exceptions.InvalidFileArgException;
import io.carba.filemanagement.model.File;
import io.carba.filemanagement.repositories.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

   @Mock
   FileRepository fileRepository;

   @InjectMocks
   FileServiceImpl fileService;

   @Mock
   MultipartFile fauxFile = Mockito.mock(MultipartFile.class);

   FileDto.Request mockRequest;

   @BeforeEach
   void setUp() throws IOException {
      MockitoAnnotations.initMocks(this);
      Long lastFileId = 35L;
      when(fauxFile.getBytes()).thenReturn("{\"message\": \"Hello Spring Boot!\"}".getBytes());
      when(fileRepository.countExistingByFileId()).thenReturn(lastFileId);
      when(fileRepository.save(any(File.class))).thenAnswer(f -> f.getArguments()[0]);
   }

   @Test
   void createFileFromValidInputData() throws IOException, InvalidFileArgException {
      Long expectedId = 36L;
      String filename = "my_file.json";
      String description = "My greeting JSON file";
      String mediaType = "application/json";

      mockRequest = FileDto.Request.builder()
            .filename(filename)
            .description(description)
            .mediaType(mediaType)
            .file(fauxFile)
            .build();

      File created = fileService.createFile(mockRequest, fauxFile.getBytes());

      assertEquals(expectedId, created.getFileId());
      assertEquals(filename, created.getFilename(), filename);
      assertEquals(description, created.getDescription(), description);
      assertEquals(mediaType, created.getMediaType(), mediaType);
      assertEquals(1L, created.getVersion(), 1L);
      verify(fileRepository, times(1)).countExistingByFileId();
      verify(fileRepository, times(1)).save(any(File.class));
   }

   @Test
   void createFileWithEmptyFileContents() throws IOException {
      when(fauxFile.getBytes()).thenReturn(new byte[0]);
      mockRequest = FileDto.Request.builder().file(fauxFile).build();

      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.createFile(mockRequest, fauxFile.getBytes()));
      assertEquals("File contents can't be empty", ex.getMessage());
   }

   @Test
   void createFileWithMissingRequiredFields() {
      mockRequest = FileDto.Request.builder()
            .filename("my_file.json")
            .description("My greeting JSON file")
            .mediaType("")
            .file(fauxFile)
            .build();

      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.createFile(mockRequest, fauxFile.getBytes()));
      assertEquals("File name, description and media type must be provided", ex.getMessage());
   }

   @Test
   void editFileFromValidInputData() throws FileNotFoundException, InvalidFileArgException {
      Long fileId = 666L;
      String filename = "my_file_v2.json";
      String description = "My greeting JSON file V2";

      mockRequest = FileDto.Request.builder()
            .filename(filename)
            .description(description)
            .file(fauxFile)
            .build();

      File old = File.builder()
            .fileId(fileId)
            .filename("my_file_original.json")
            .description("My existing description")
            .version(33L)
            .mediaType("test/existing")
            .build();

      when(fileRepository.findFirstByFileIdOrderByVersionDesc(fileId)).thenReturn(old);

      File edited = fileService.editFile(fileId, mockRequest, new byte[0]);
      assertEquals(filename, edited.getFilename());
      assertEquals(description, edited.getDescription());
      assertEquals("test/existing", edited.getMediaType());
      assertEquals(34L, edited.getVersion());
   }

   @Test
   void editFileWithMissingRequiredFields() {
      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.editFile(null, mockRequest, new byte[0]));
      assertEquals("File ID must be provided", ex.getMessage());
   }

   @Test
   void editFileWithValidInputDataButNonExistentFileId() {
      Long nonExistingFileId = 666L;

      when(fileRepository.findFirstByFileIdOrderByVersionDesc(nonExistingFileId)).thenReturn(null);

      Exception ex = assertThrows(FileNotFoundException.class, () -> fileService.editFile(nonExistingFileId, new FileDto.Request(), new byte[0]));
      assertEquals("File with fileId: 666 was not found", ex.getMessage());
   }

   @Test
   void getFileVersionWithoutProvidingVersionNumber() throws InvalidFileArgException {
      Long fileId = 999L;
      when(fileRepository.findFirstByFileIdOrderByVersionDesc(fileId)).thenReturn(new File());

      File fetched = fileService.getFileVersion(fileId, null);

      assertNotEquals(null, fetched);
      verify(fileRepository, times(1)).findFirstByFileIdOrderByVersionDesc(fileId);
   }

   @Test
   void getFileVersionProvidingVersionNumber() throws InvalidFileArgException {
      Long fileId = 999L;
      Long version = 5L;

      when(fileRepository.findByFileIdAndVersionEquals(fileId, version)).thenReturn(File.builder().version(version).build());

      File fetched = fileService.getFileVersion(fileId, version);

      assertEquals(version, fetched.getVersion());
      verify(fileRepository, times(1)).findByFileIdAndVersionEquals(fileId, version);
   }

   @Test
   void getFileVersionForNonExistentFileId() throws InvalidFileArgException {
      Long nonExFileId = 999L;
      when(fileRepository.findFirstByFileIdOrderByVersionDesc(nonExFileId)).thenReturn(null);

      File fetched = fileService.getFileVersion(nonExFileId, null);

      assertNull(fetched);
   }

   @Test
   void getFileVersionPassingNullFileId() {
      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.getFileVersion(null, null));
      assertEquals("File ID must be provided", ex.getMessage());
   }

   @Test
   void getAllFiles() throws InvalidFileArgException {
      Long fileId = 506L;

      List<File> expected = Arrays.asList(new File(), new File(), new File());
      when(fileRepository.findAllByFileId(fileId)).thenReturn(expected);

      List<File> results = fileService.getAllFiles(fileId);
      assertEquals(expected.size(), results.size());
   }

   @Test
   void getAllFilesForFileIdWithNoMatches() throws InvalidFileArgException {
      Long fileId = 506L;
      when(fileRepository.findAllByFileId(fileId)).thenReturn(Collections.emptyList());

      List<File> results = fileService.getAllFiles(fileId);
      assertEquals(0, results.size());
   }

   @Test
   void getAllFilesPassingNullFileId() throws InvalidFileArgException {
      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.getAllFiles(null));
      assertEquals("File ID must be provided", ex.getMessage());
   }

   @Test
   void deleteFileVersion() throws InvalidFileArgException{
      Long fileId = 1L;
      Long version = 1L;

      fileService.deleteFileVersion(fileId, version);

      verify(fileRepository, times(1)).deleteFileByFileIdAndVersionEquals(fileId, version);
   }

   @Test
   void deleteFileVersionWithoutProvidingRequiredFields() {
      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.deleteFileVersion(null, null));
      Exception ex2 = assertThrows(InvalidFileArgException.class, () -> fileService.deleteFileVersion(100L, null));
      assertEquals("File ID and version must be provided", ex.getMessage());
      assertEquals("File ID and version must be provided", ex2.getMessage());
   }

   @Test
   void deleteAllByFileId() throws InvalidFileArgException{
      Long fileId = 1L;

      fileService.deleteAllByFileId(fileId);

      verify(fileRepository, times(1)).deleteAllByFileId(fileId);
   }

   @Test
   void deleteAllByFileIdPassingNullFileId() {
      Exception ex = assertThrows(InvalidFileArgException.class, () -> fileService.deleteAllByFileId(null));
      assertEquals("File ID must be provided", ex.getMessage());
   }
}