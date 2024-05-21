package com.example.cloudservice.controller;

import com.example.cloudservice.auth.AuthenticationRequest;
import com.example.cloudservice.auth.AuthenticationResponse;
import com.example.cloudservice.auth.RegisterRequest;
import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.service.AuthenticationService;
import com.example.cloudservice.service.FileService;
import com.example.cloudservice.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
    public ResponseEntity<List<FileEntity>> getFileList(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.trim());
        List<FileEntity> fileList = fileService.getFileList(userId);
        return ResponseEntity.ok(fileList);
    }

    // Метод для скачивания файла+++
    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, @RequestHeader("Authorization") String token) throws IOException {
        Long userId = jwtService.extractUserId(token.trim());
        byte[] file = fileService.downloadFile(fileName, userId);
        return ResponseEntity.ok().body(file);
    }


    // Метод для добавления файла  ++
    @PostMapping("/file/{filename}")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Void> uploadFile(@PathVariable("filename") String filename, @RequestPart("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token);
        fileService.uploadFile(userId, filename, file);
        return ResponseEntity.ok().build();
    }

    // Метод для удаления файла+++
    @DeleteMapping("/file/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName, @RequestHeader("Authorization") String token) throws FileNotFoundException {
        Long userId = jwtService.extractUserId(token);
        fileService.deleteFile(fileName, userId);
        return ResponseEntity.ok().build();
    }

    // Добавление метода редактирования содержимого файла
    @PutMapping("/file/{fileName}")
    public ResponseEntity<Void> editFileContent(@PathVariable String fileName, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token);
        fileService.editFileContent(fileName, file, userId);
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
