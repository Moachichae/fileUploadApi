package com.steve.erputill.file;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequiredArgsConstructor
public class FileApiController {

    private final FileService fileService;
    private final FileRepository fileRepository;

    /**
     * 파일 업로드
     */
    @PostMapping(value = "/files")
    public ResponseEntity<String> uploadReceipt(@ModelAttribute @Valid FileRequest fileRequest) {
        String fileName = fileService.save(fileRequest.getFile(), fileRequest.usage, fileRequest.getMemberId());
        return new ResponseEntity<>(fileName, HttpStatus.OK);
    }

    @Data
    static class FileRequest {
        @NotNull
        private MultipartFile file;
        @NotNull
        private FileUsage usage;
        @NotNull
        private Long memberId;
    }

    /**
     * 파일 다운
     */
    @GetMapping(value = "/files/attach/{name}")
    public ResponseEntity<Resource> loadFile(@PathVariable String name) {
        Resource resource = fileService.loadAsResource(name);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    /**
     * 파일 이미지 조회
     */
    @GetMapping("/files/images/{name}")
    public ResponseEntity<Resource> showImage(@PathVariable String name) {
        Resource resource = fileService.loadAsResource(name);

        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(resource.getURI());
            header.add("Content-Type", Files.probeContentType(filePath));
            // 인풋으로 들어온 파일명 .png / .jpg 에 맞게 헤더 타입 설정
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 이미지 리턴 실시 [브라우저에서 get 주소 확인 가능]
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

    /**
     * 파일삭제
     */
    @PostMapping("/files/{name}/delete")
    public ResponseEntity<?> deleteFile(@PathVariable String name){
        fileService.deleteFile(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 파일정보 용도 별 전체조회
     */
    @GetMapping("/files/{usage}")
    public List<FileDTO> loadAllByUsage(@PathVariable FileUsage usage) {
        return fileRepository.findByUsage(usage)
                .stream().map(FileDTO::new)
                .collect(Collectors.toList());
    }




}
