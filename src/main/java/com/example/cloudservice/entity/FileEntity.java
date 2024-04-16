package com.example.cloudservice.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "files", schema = "diplom")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_content")
    private byte[] fileData;


}
