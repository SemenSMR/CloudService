package com.example.cloudservice.repository;

import com.example.cloudservice.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
    Optional<FileEntity> findByFileName(String fileName);
}
