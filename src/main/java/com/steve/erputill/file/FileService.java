package com.steve.erputill.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FilePathConfig pathConfig;


    /**
     * 파일 업로드
     */
    @Transactional
    public String save(MultipartFile multipartFile, FileUsage usage, Long memberId) {
        String uploadPath = pathConfig.determinePath(usage); //파일저장경로지정
        FileInfoEntity file = FileInfoEntity.createFileInfo(multipartFile, usage, uploadPath, memberId);
        fileRepository.save(file);
        return file.getName();
    }

    /**
     * 파일 다운로드
     */
    public Resource loadAsResource(String name) {
        FileInfoEntity fileInfo = fileRepository.findOne(name);
        Path filePath = fileInfo.getPullPath(); //파일이 저장되어있는 전체 경로
        try {
            Resource resource = new UrlResource(filePath.toUri()); //URI 생성
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + name);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + name, e);
        }
    }

    /**
     * 파일 삭제
     */
    @Transactional
    public void deleteFile(String name){
        FileInfoEntity file = fileRepository.findOne(name);
        fileRepository.delete(file);
        file.delete(); // pc에 저장된 파일삭제
    }




}
