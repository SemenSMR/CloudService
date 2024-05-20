package com.example.cloudservice.service;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.exception.FileNotFoundException;
import com.example.cloudservice.exception.UsernameNotFoundException;
import com.example.cloudservice.repository.FileRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
    public List<FileEntity> getFileList(Long userId) {
        Optional<MyUser> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            MyUser user = userOptional.get();
            return fileRepository.findDistinctByUser(user);
        }else {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

    }

    // Метод скачивания файлов
    public byte[] downloadFile(String fileName, Long userId) throws FileNotFoundException {
        Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName, userId);
        if (optionalFile.isPresent()) {
            FileEntity fileEntity = optionalFile.get();
            return fileEntity.getFileData();
        } else {
            throw new FileNotFoundException("File not found or you don't have access to it: " + fileName);
        }
    }


    // Метод для добавления файла
    public void uploadFile(Long userId ,String filename, MultipartFile file) {
        try {
            MyUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(filename);
            fileEntity.setFileData(file.getBytes());
            fileEntity.setUser(user);
            fileRepository.save(fileEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления файла
    public void deleteFile(String fileName,Long userId) throws FileNotFoundException {
        Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName,userId);
        if (optionalFile.isPresent()) {
            fileRepository.delete(optionalFile.get());
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    // Метод для Изменение файла
    public void editFileContent(String fileName, MultipartFile file,Long userId) {
        try {
            Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName,userId);
            if (optionalFile.isPresent()) {
                FileEntity fileEntity = optionalFile.get();
                fileEntity.setFileData(file.getBytes());
                fileRepository.save(fileEntity);
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public void addUser(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
}

//   public boolean authenticate(String username , String password){
//        Optional<MyUser> userOptional = userRepository.findByUsername(username);
//        if(userOptional.isPresent()){
//            MyUser user = userOptional.get();
//            return passwordEncoder.matches(password,user.getPassword());
//        }
//        return false;
//   }
}

