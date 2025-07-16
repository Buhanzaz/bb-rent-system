package dev.buhanzaz.storage.service;

public interface SequenceService {
    Long incrementIterationByPath(String path);

    void decrementIterationByPath(String path);

    Long getCurrentIterationByPath(String path);

    void setIterationByPath(String path, Long value);

    void resetIterationByPath(String path);
}
