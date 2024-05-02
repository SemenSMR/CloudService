package com.example.cloudservice.controller;

import com.example.cloudservice.auth.AuthenticationRequest;
import com.example.cloudservice.auth.AuthenticationResponse;
import com.example.cloudservice.auth.RegisterRequest;
import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.service.AuthenticationService;
import com.example.cloudservice.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FileController {

    private FileService fileService;
    private AuthenticationService service;


    // Метод для получения списка файлов
    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> getFileList() {
        List<FileEntity> fileList = fileService.getFileList();
        return ResponseEntity.ok(fileList);
    }

    // Метод для скачивания файла
    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) throws IOException {
        byte[] file = fileService.downloadFile(fileName);
        return ResponseEntity.ok().body(file);
    }


    // Метод для добавления файла
    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {

        fileService.uploadFile(file.getOriginalFilename(), file);
        return ResponseEntity.ok().build();
    }

    // Метод для удаления файла
    @DeleteMapping("/file/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) throws FileNotFoundException {
        fileService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    // Добавление метода редактирования содержимого файла
    @PutMapping("/file/{fileName}")
    public ResponseEntity<Void> editFileContent(@PathVariable String fileName, @RequestParam("file") MultipartFile file) {
        fileService.editFileContent(fileName, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/file/add")
    public ResponseEntity<Void> addUser(@RequestBody MyUser user) {
        fileService.addUser(user);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, @RequestParam String password) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
