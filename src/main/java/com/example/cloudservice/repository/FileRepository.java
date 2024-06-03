package com.example.cloudservice.repository;

import com.example.cloudservice.entity.FileEntity;
import com.example.cloudservice.entity.MyUser;
import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFileName(String fileName);

    Optional<FileEntity> findByFileNameAndUserId(String fileName, Long userId);


    List<FileEntity> findDistinctByUser(MyUser user);

    List<FileEntity> findAllByUser(MyUser user , Pageable pageable);
}
