package com.steve.erputill.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
    private String filename;
    private String originalName;

    public FileDTO(FileInfoEntity fileInfo){
        filename = fileInfo.getName();
        originalName = fileInfo.getOriginalName();
    }
}
