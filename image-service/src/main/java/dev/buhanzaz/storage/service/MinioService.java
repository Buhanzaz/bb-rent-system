package dev.buhanzaz.storage.service;

import dev.buhanzaz.storage.domain.model.*;

import java.util.List;

public interface MinioService {
    void upload(UploadModel model);
    void update(UpdateModel model);
    List<String> getTemporaryUrls(DownloadModel model);

    void deleteByName(DeleteByNameModel model);

    void deleteAllWithPath(DeleteWithPathModel model);
}
