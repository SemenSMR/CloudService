package com.example.cloudservice.controller;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FileController {

    private FileService fileService;

    // Метод для получения списка файлов
    @GetMapping("/files")
    public ResponseEntity<List<FileEntity>> getFileList() {
        List<FileEntity> fileList = fileService.getFileList();
        return ResponseEntity.ok(fileList);
    }

    // Метод для добавления файла
    @PostMapping("/files")
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {

        fileService.uploadFile(file.getOriginalFilename(), file);
        return ResponseEntity.ok().build();
    }

    // Метод для удаления файла
    @DeleteMapping("/files/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) throws FileNotFoundException {
        fileService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }
    // Добавление метода редактирования содержимого файла
    @PutMapping("/files/{fileName}")
    public ResponseEntity<Void> editFileContent(@PathVariable String fileName, @RequestParam("file") MultipartFile file) {
        fileService.editFileContent(fileName, file);
        return ResponseEntity.ok().build();
    }


    // Метод для аутентификации пользователя
//    @PostMapping("/login")
//    public ResponseEntity<Void> authenticate(@RequestParam String username, @RequestParam String password) {
//        if (fileService.authenticate(username, password)) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
}
