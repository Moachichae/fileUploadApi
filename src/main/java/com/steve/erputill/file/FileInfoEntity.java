package com.steve.erputill.file;

import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //createFile 메서드로 객체 생성
public class FileInfoEntity {

    //TODO: pk 설계
    @Id
    private String name;

    @Enumerated(EnumType.STRING)
    private FileUsage usage;

    private String originalName;

    private String path;

    private LocalDateTime savingDate;

    private Long memberId;

    private FileInfoEntity(String name, FileUsage usage,
                           String originalName, String path,
                           LocalDateTime savingDate, Long memberId, MultipartFile multipartFile) {
        this.name = name;
        this.usage = usage;
        this.originalName = originalName;
        this.path = path;
        this.savingDate = savingDate;
        this.memberId = memberId;

        this.saveToLocal(multipartFile); //로컬 pc에 저장
    }

    //==생성 메서드 ==//
    /**
     * 로컬 pc에 파일 저장 후 fileEntity 생성
     */
    public static FileInfoEntity createFileInfo(MultipartFile multipartFile, FileUsage usage, String uploadPath, Long memberId) {
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename()); //파일 확장자 꺼내기
        String name = UUID.randomUUID() + "." + ext;

        return new FileInfoEntity(name,
                usage,
                multipartFile.getOriginalFilename(),
                uploadPath,
                LocalDateTime.now(),
                memberId, multipartFile);
    }

    /**
     * 파일 로컬 pc에 저장 후 저장한 경로 리턴
     */
    private void saveToLocal(MultipartFile multipartFile) {
        Path path = getPullPath();
        try {
            Files.copy(multipartFile.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * 로컬 pc 파일 삭제
     */
    public void delete() {
        FileSystemUtils.deleteRecursively(getPullPath().toFile());
    }

    /**
     * 파일이름을 절대경로에 결합(ex: c:Path\filename)
     */
    public Path getPullPath() {
        return Paths.get(this.path).resolve(this.name);
    }

}
