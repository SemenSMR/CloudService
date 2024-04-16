package com.example.cloudservice.service;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    // Метод для вывода списка файлов
    public List<FileEntity> getFileList() {
        return fileRepository.findAll();

    }

    // Метод для добавления файла
    public void uploadFile(String fileName, MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFileData(file.getBytes());
            fileRepository.save(fileEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления файла
    public void deleteFile(String fileName) throws FileNotFoundException {

        Optional<FileEntity> optionalFile = fileRepository.findByFileName(fileName);
        if (optionalFile.isPresent()) {
            fileRepository.delete(optionalFile.get());
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    public void editFileContent(String fileName, MultipartFile file) {
        try {
            Optional<FileEntity> optionalFile = fileRepository.findByFileName(fileName);
            if (optionalFile.isPresent()) {
                FileEntity fileEntity = optionalFile.get();
                fileEntity.setFileData(file.getBytes());
                fileRepository.save(fileEntity);
            } else {
                System.out.println("Файл не найден");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для аутентификации пользователя
//    public Object authenticate(String username, String password) {
//
//    }
}

