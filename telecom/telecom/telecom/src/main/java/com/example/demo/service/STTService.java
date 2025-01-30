package com.example.demo.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class STTService {
    @Value("${google.project-id}")
    private String projectId;

    @Value("${google.credentials-path}")
    private String credentialsPath;

    private SpeechClient speechClient;
    @PostConstruct
    public void initialize() throws IOException {
        log.info("Starting STT Service initialization...");
        log.info("Project ID: {}", projectId);
        log.info("Credentials Path: {}", credentialsPath);

        try {
            // 로그 찍어보기
            ClassPathResource resource = new ClassPathResource(credentialsPath);
            log.info("Resource exists: {}", resource.exists());
            log.info("Using credentials path: {}", credentialsPath);

            // credentials 설정
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    resource.getInputStream());

            SpeechSettings settings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            this.speechClient = SpeechClient.create(settings);
            log.info("STT Service initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize STT Service", e);
            throw e;
        }
    }

    public String convertToText(byte[] recordingData, int frequency) throws IOException {
        try {
            ByteString audioData = ByteString.copyFrom(recordingData);

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(8000)  // MP3 표준 샘플레이트
                    .setLanguageCode("ko-KR")
                    .setModel("default")
                    .build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            RecognizeResponse response = this.speechClient.recognize(recognitionConfig, recognitionAudio);

            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript.append(alternative.getTranscript());
            }

            return transcript.toString();
        } catch (Exception e) {
            log.error("STT 변환 중 오류 발생", e);
            throw new IOException("음성 인식 처리 중 오류가 발생했습니다.", e);
        }
    }
}