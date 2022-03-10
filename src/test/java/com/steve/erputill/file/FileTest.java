package com.steve.erputill.file;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class FileTest {

    @Test
    public void fileTest() throws Exception{
        String resultUuid = UUID.randomUUID().toString();
        log.info(resultUuid);

    }

}