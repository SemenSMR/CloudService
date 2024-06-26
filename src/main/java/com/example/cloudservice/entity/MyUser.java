package com.example.cloudservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor

@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "users", schema = "diplom")
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<FileEntity> fileEntityList;

}

