package com.steve.erputill.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class FilePathConfig {
    @Value("${file.upload.receipt}")
    private String receiptPath;
    @Value("${file.upload.certification}")
    private String certificationPath;
    @Value("${file.upload.banner}")
    private String bannerPath;


    /**
     * 파일을 저장할 디렉토리 초기화
     */
    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(Paths.get(receiptPath));
            Files.createDirectories(Paths.get(certificationPath));
            Files.createDirectories(Paths.get(bannerPath));
            log.info("fileInit");
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }


    /**
     * 파일저장경로지정
     */
    String determinePath(FileUsage usage) {
        String path = "";
        switch (usage) {
            case RECEIPT:
                path = this.receiptPath;
                break;
            case CERTIFICATION:
                path = this.certificationPath;
                break;
            case BANNER:
                path = this.bannerPath;
        }
        return path;
    }

}
