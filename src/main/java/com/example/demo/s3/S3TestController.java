package com.example.demo.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class S3TestController {

    @Autowired
    private S3FileHandler s3FileHandler;

    @PostMapping("/api/upload")
    public ResponseEntity updateUserImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String url = s3FileHandler.uploadFile(multipartFile, "temp");
        return new ResponseEntity(url, HttpStatus.OK);
    }
}
