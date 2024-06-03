package com.example.cloudservice.controller;

import com.example.cloudservice.auth.AuthenticationRequest;
import com.example.cloudservice.auth.AuthenticationResponse;
import com.example.cloudservice.auth.RegisterRequest;
import com.example.cloudservice.dto.EditFileNameRequest;
import com.example.cloudservice.dto.FileListResponse;
import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.service.AuthenticationService;
import com.example.cloudservice.service.FileService;
import com.example.cloudservice.service.JwtService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
//@RequestMapping("/api")
public class FileController {

    private FileService fileService;
    private AuthenticationService service;
    private JwtService jwtService;


    // Метод для получения списка файлов ++
    @GetMapping("/list")
    public ResponseEntity<List<FileListResponse>> getFileList(@RequestHeader("Auth-token") String token, @RequestParam("limit") Integer limit) {
        Long userId = jwtService.extractUserId(token.trim());
        List<FileListResponse> fileListResponses = fileService.getFileList(userId, limit);
        return ResponseEntity.ok(fileListResponses);
    }

    // Метод для скачивания файла+++
    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("Auth-token") String token, @RequestParam String filename) throws IOException {
        Long userId = jwtService.extractUserId(token.trim());
        byte[] file = fileService.downloadFile(filename, userId);
        return ResponseEntity.ok(file);
    }


    // Метод для добавления файла  ++
    @PostMapping("/file")
    public ResponseEntity<FileListResponse> uploadFile(
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Auth-Token") String token) throws IOException {
        Long userId = jwtService.extractUserId(token);
       FileListResponse fileListResponses = fileService.uploadFile(userId, filename, file);
        return ResponseEntity.ok(fileListResponses);
    }

    // Метод для удаления файла+++
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam String filename, @RequestHeader("Auth-Token") String token) throws FileNotFoundException {
        Long userId = jwtService.extractUserId(token);
        fileService.deleteFile(filename, userId);
        return ResponseEntity.ok().build();
    }

    // Добавление метода редактирования содержимого файла
    @PutMapping(value = "/file")
    public ResponseEntity<Void> editFileContent(@RequestHeader("Auth-Token") String token , @RequestParam String filename, @RequestBody EditFileNameRequest editFileNameRequest) {
        Long userId = jwtService.extractUserId(token);
        fileService.editFileContent(filename, editFileNameRequest, userId);
        return ResponseEntity.ok().build();
    }

    // Добавление в базу польщователя , он не нужен
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
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
        return service.login(loginRequest);
    }
}
