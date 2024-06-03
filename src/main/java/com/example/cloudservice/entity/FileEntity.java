package com.example.cloudservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "files", schema = "diplom")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_content")
    private byte[] fileData;
    @Column(name = "file_size")
    private Long fileSize;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private MyUser user;

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
