package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3Client;
    private final S3Config s3Config;

//    public String uploadFile(byte[] fileData, String fileName) {
//        return "업로드됨";
        //        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType("audio/mpeg");
//            metadata.setContentLength(fileData.length);
//
//            PutObjectRequest request = new PutObjectRequest(
//                    bucketName,
//                    fileName,
//                    new ByteArrayInputStream(fileData),
//                    metadata
//            ).withCannedAcl(CannedAccessControlList.PublicRead);
//
//            s3Client.putObject(request);
//
//            return s3Client.getUrl(bucketName, fileName).toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to upload file to S3", e);
//        }
//    }
public String uploadText(String text, String fileName) {
    try {

        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain");
        metadata.setContentLength(textBytes.length);

        PutObjectRequest request = new PutObjectRequest(s3Config.getBucketName(), fileName,
                new ByteArrayInputStream(textBytes), metadata);

        s3Client.putObject(request);
        return s3Client.getUrl(s3Config.getBucketName(), fileName).toString();
    } catch (Exception e) {
        throw new RuntimeException("Failed to upload text to S3", e);
    }
}
}
