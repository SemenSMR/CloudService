package com.example.cloudservice;

import com.example.cloudservice.service.JwtService;
import com.example.cloudservice.controller.FileController;
import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FileControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFileList() {

        String token = "mockToken";
        Long userId = 1L;


        when(jwtService.extractUserId(token)).thenReturn(userId);


        List<FileEntity> fileList = new ArrayList<>();
        fileList.add(new FileEntity(1L, "file1.txt", new byte[]{}, new MyUser())); // Адаптированный код
        fileList.add(new FileEntity(2L, "file2.txt", new byte[]{}, new MyUser())); // Адаптированный код
        when(fileService.getFileList(userId)).thenReturn(fileList);

    }
}