package dev.buhanzaz.storage.service.impl;

import dev.buhanzaz.storage.domain.dto.request.*;
import dev.buhanzaz.storage.domain.dto.response.DownloadResponse;
import dev.buhanzaz.storage.domain.model.*;
import dev.buhanzaz.storage.exception.MinioServiceException;
import dev.buhanzaz.storage.mapper.StorageMapper;
import dev.buhanzaz.storage.service.MinioService;
import dev.buhanzaz.storage.service.SequenceService;
import dev.buhanzaz.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "objects")
class StorageServiceImpl implements StorageService {
    private final MinioService minioService;
    private final SequenceService sequenceService;
    private final StorageMapper mapper;

    @Override
    @CacheEvict(value = "objects", key = "#request.path")
    public void upload(UploadRequest request, MultipartFile file) {
        Long iteration = sequenceService.incrementIterationByPath(request.path());
        UploadModel model = mapper.requestToModel(request, file, iteration);

        try {
            minioService.upload(model);
        } catch (Exception e) {
            sequenceService.decrementIterationByPath(request.path());

            throw new MinioServiceException("Failed to upload file", e);
        }
    }

    @Override
    @Cacheable(value = "objects", key = "#request.path")
    public DownloadResponse getTemporaryUrls(DownloadRequest request) {
        DownloadModel model = mapper.requestToModel(request);

        List<String> temporaryURLs = minioService.getTemporaryUrls(model);

        return mapper.modelToResponse(model, temporaryURLs);
    }

    @Override
    @CacheEvict(value = "objects", key = "#request.path")
    public void deleteByName(DeleteByNameRequest request) {
        DeleteByNameModel model = mapper.requestToModel(request);

        minioService.deleteByName(model);
    }

    @Override
    @CacheEvict(value = "objects", key = "#request.path")
    public void deleteAllWithPath(DeleteWithPathRequest request) {
        DeleteWithPathModel model = mapper.requestToModel(request);

        minioService.deleteAllWithPath(model);
        sequenceService.resetIterationByPath(request.path());
    }

    @Override
    public void update(UpdateRequest request, MultipartFile file) {
        UpdateModel model = mapper.requestToModel(request, file);

        minioService.update(model);
    }
}
