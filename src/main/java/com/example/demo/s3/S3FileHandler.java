package com.example.demo.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class S3FileHandler {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadFile(MultipartFile multipartFile, String uploadPath) throws IOException {
        String randomFileName = UUID.randomUUID().toString();
        ObjectMetadata  objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        amazonS3.putObject(new PutObjectRequest(bucket,  uploadPath + File.separator + randomFileName, multipartFile.getInputStream(), objectMetadata));
        return randomFileName;
    }

    public String uploadFileLocal(MultipartFile multipartFile, String uploadPath) throws IOException {
        String randomFileName = UUID.randomUUID().toString();
        String uploadFileName = uploadPath + File.separator + randomFileName;
        Path localFilePath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(randomFileName);
        Files.copy(multipartFile.getInputStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
//        Files.write(localFilePath, multipartFile.getBytes());
        amazonS3.putObject(new PutObjectRequest(bucket,  uploadFileName, localFilePath.toFile()));
        Files.delete(localFilePath);
        //return amazonS3.getUrl(bucket, uploadFileName).toString();
        return randomFileName;
    }

    public Path downloadFile(String fullFileName) throws IOException {
        Path localFilePath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(fullFileName);
        Files.copy(amazonS3.getObject(bucket, fullFileName).getObjectContent(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
        return localFilePath;
    }

}
