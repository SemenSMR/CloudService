package com.example.cloudservice;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.exception.FileNotFoundException;
import com.example.cloudservice.repository.FileRepository;
import com.example.cloudservice.repository.UserRepository;
import com.example.cloudservice.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FileServiceTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetFileList() {
        Long userId = 1L;
        MyUser user = new MyUser();
        user.setId(userId);
        List<FileEntity> fileList = Arrays.asList(new FileEntity(), new FileEntity());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileRepository.findDistinctByUser(user)).thenReturn(fileList);

        List<FileEntity> result = fileService.getFileList(userId);

        assertEquals(fileList, result);
    }
    @Test
    void testDownloadFile() throws FileNotFoundException {
        String fileName = "test.txt";
        Long userId = 1L;
        FileEntity fileEntity = new FileEntity();
        byte[] fileData = "test content".getBytes();
        fileEntity.setFileName(fileName);
        fileEntity.setFileData(fileData);
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.of(fileEntity));

        byte[] result = fileService.downloadFile(fileName, userId);

        assertArrayEquals(fileData, result);
    }
    @Test
    void testDownloadFile_FileNotFound() {
        String fileName = "test.txt";
        Long userId = 1L;
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> fileService.downloadFile(fileName, userId));
    }
    @Test
    void testUploadFile() throws IOException {
        Long userId = 1L;
        MyUser user = new MyUser();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "test content".getBytes());

        fileService.uploadFile(userId, multipartFile);

        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }
    @Test
    void testDeleteFile() throws FileNotFoundException {
        String fileName = "test.txt";
        Long userId = 1L;
        FileEntity fileEntity = new FileEntity();
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.of(fileEntity));

        fileService.deleteFile(fileName, userId);

        verify(fileRepository, times(1)).delete(fileEntity);
    }
    @Test
    void testDeleteFile_FileNotFound() {
        String fileName = "test.txt";
        Long userId = 1L;
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(fileName, userId));
    }
    @Test
    void testEditFileContent() throws IOException {
        String fileName = "test.txt";
        Long userId = 1L;
        FileEntity fileEntity = new FileEntity();
        byte[] newFileData = "new test content".getBytes();
        MultipartFile newFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", newFileData);
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.of(fileEntity));

        fileService.editFileContent(fileName, newFile, userId);

        assertArrayEquals(newFileData, fileEntity.getFileData());
        verify(fileRepository, times(1)).save(fileEntity);
    }
    @Test
    void testEditFileContent_FileNotFound() {
        String fileName = "test.txt";
        Long userId = 1L;
        MultipartFile newFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "new test content".getBytes());
        when(fileRepository.findByFileNameAndUserId(fileName, userId)).thenReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> fileService.editFileContent(fileName, newFile, userId));
    }
}
