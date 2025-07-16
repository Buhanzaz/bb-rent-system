package dev.buhanzaz.storage.service;

import dev.buhanzaz.storage.domain.dto.request.*;
import dev.buhanzaz.storage.domain.dto.response.DownloadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void upload(UploadRequest request, MultipartFile file);

    DownloadResponse getTemporaryUrls(DownloadRequest request);

    void deleteByName(DeleteByNameRequest request);

    void deleteAllWithPath(DeleteWithPathRequest request);

    void update(UpdateRequest request, MultipartFile file);
}