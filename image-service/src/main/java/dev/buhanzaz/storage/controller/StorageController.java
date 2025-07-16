package dev.buhanzaz.storage.controller;

import dev.buhanzaz.storage.domain.dto.request.*;
import dev.buhanzaz.storage.domain.dto.response.DownloadResponse;
import dev.buhanzaz.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping(path = "/")
    public DownloadResponse getTemporaryUrls(
            @RequestBody DownloadRequest request
    ) {
        log.debug("DownloadRequest: {}", request.toString());

        DownloadResponse response = storageService.getTemporaryUrls(request);

        log.debug("DownloadResponse: {}", response.toString());

        return response;
    }

    @PostMapping(path = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void upload(
            @RequestPart("request") UploadRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        log.debug("UploadRequest: {}", request.toString());

        storageService.upload(request, file);

        log.debug("Upload file successful");
    }

    @PutMapping(path = "/")
    public void update(
            @RequestPart("request") UpdateRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        log.debug("UpdateRequest: {}", request.toString());

        storageService.update(request, file);

        log.debug("Update file successful");
    }


    @DeleteMapping(path = "/")
    public void deleteByName(
            @RequestBody DeleteByNameRequest request
    ) {
        log.debug("DeleteByNameRequest: {}", request.toString());

        storageService.deleteByName(request);

        log.debug("The file is successfully deleted");
    }

    @DeleteMapping(path = "/prefix")
    public void deleteAllWithPrefix(
            @RequestBody DeleteWithPathRequest request
    ) {
        log.debug("DeleteWithPathRequest: {}", request.toString());

        storageService.deleteAllWithPath(request);

        log.debug("The folder with all contents is remove");
    }
}
