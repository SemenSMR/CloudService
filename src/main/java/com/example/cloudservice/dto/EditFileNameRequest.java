package com.example.cloudservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditFileNameRequest {
    private String filename;
    // private byte[] fileData;

}
