package com.example.cloudservice.service;

import com.example.cloudservice.dto.EditFileNameRequest;
import com.example.cloudservice.dto.FileListResponse;
import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import com.example.cloudservice.exception.FileNotFoundException;
import com.example.cloudservice.exception.UsernameNotFoundException;
import com.example.cloudservice.repository.FileRepository;
import com.example.cloudservice.repository.UserRepository;
import lombok.AllArgsConstructor;



import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    // Метод для вывода списка файлов
    public List<FileListResponse> getFileList(Long userId, Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Optional<MyUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
        MyUser user = userOptional.get();
        List<FileEntity> fileList = fileRepository.findAllByUser(user, pageable);
        return fileList.stream()
                .map(file -> new FileListResponse(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());

    }

    // Метод скачивания файлов
    public byte[] downloadFile(String fileName, Long userId) throws FileNotFoundException, IOException {
        Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName, userId);
        if (optionalFile.isPresent()) {
            FileEntity fileEntity = optionalFile.get();
            return fileEntity.getFileData();
        } else {
            throw new FileNotFoundException("File not found or you don't have access to it: " + fileName);
        }
    }


    // Метод для добавления файла
    public FileListResponse uploadFile(Long userId, String filename, MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(filename);
        fileEntity.setFileData(file.getBytes());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setUser(userRepository.findById(userId).orElseThrow());
        fileRepository.saveAndFlush(fileEntity);
        FileListResponse fileListResponse = new FileListResponse();
        fileListResponse.setFilename(fileEntity.getFileName());
        fileListResponse.setSize(fileEntity.getFileSize());
        return fileListResponse;
    }

    // Метод для удаления файла
    public void deleteFile(String fileName, Long userId) throws FileNotFoundException {
        Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName, userId);
        if (optionalFile.isPresent()) {
            fileRepository.delete(optionalFile.get());
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    // Метод для Изменение файла
    public void editFileContent(String fileName, EditFileNameRequest editFileNameRequest, Long userId) {
        try {
            Optional<FileEntity> optionalFile = fileRepository.findByFileNameAndUserId(fileName, userId);
            if (optionalFile.isPresent()) {
                FileEntity fileEntity = optionalFile.get();
                if (editFileNameRequest.getFilename() != null && !editFileNameRequest.getFilename().isEmpty()) {
                    fileEntity.setFileName(editFileNameRequest.getFilename());
                    fileRepository.save(fileEntity);
                } else {
                    throw new IllegalArgumentException("New filename is null or empty");
                }
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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

