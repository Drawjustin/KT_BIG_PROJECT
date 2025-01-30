package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {
//    private final AmazonS3 s3Client;

//    @Value("${aws.s3.bucket}")
//    private String bucketName;

    public String uploadFile(byte[] fileData, String fileName) {
        return "업로드됨";
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
    }
    public String uploadText(String text, String fileName) {
//        try {
//            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
//            PutObjectRequest request = new PutObjectRequest(bucketName, fileName,
//                    new ByteArrayInputStream(textBytes), new ObjectMetadata());
//            s3Client.putObject(request);
//            return s3Client.getUrl(bucketName, fileName).toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to upload text to S3", e);
//        }
        return "업로드 완료";
    }
}
