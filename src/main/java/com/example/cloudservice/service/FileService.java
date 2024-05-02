package com.example.cloudservice.service;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.repository.FileRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    // Метод для вывода списка файлов
    public List<FileEntity> getFileList() {
        return fileRepository.findAll();

    }

    // Метод скачивания файлов
    public byte[] downloadFile(String fileName) throws FileNotFoundException {
        Optional<FileEntity> optionalFile = fileRepository.findByFileName(fileName);
        if (optionalFile.isPresent()) {
            return optionalFile.get().getFileData();
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
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

    // Метод для Изменение файла
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

public void addUser(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
}

   public boolean authenticate(String username , String password){
        Optional<MyUser> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            MyUser user = userOptional.get();
            return passwordEncoder.matches(password,user.getPassword());
        }
        return false;
   }
}

