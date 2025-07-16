package dev.buhanzaz.storage.mapper;

import dev.buhanzaz.storage.domain.dto.request.*;
import dev.buhanzaz.storage.domain.dto.response.DownloadResponse;
import dev.buhanzaz.storage.domain.model.*;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    UploadModel requestToModel(UploadRequest request, MultipartFile file, Long iteration);

    DownloadModel requestToModel(DownloadRequest request);

    DownloadResponse modelToResponse(DownloadModel model, List<String> temporaryURLs);

    DeleteWithPathModel requestToModel(DeleteWithPathRequest request);

    DeleteByNameModel requestToModel(DeleteByNameRequest request);

    UpdateModel requestToModel(UpdateRequest request, MultipartFile file);
}
