package dev.buhanzaz.storage.service.impl;

import dev.buhanzaz.storage.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RedisSequenceServiceImpl implements SequenceService {

    /**
     * Prefix for Redis keys used to store file sequence iterations.
     */
    private static final String PREFIX = "file-sequence";

    /**
     * RedisTemplate for performing operations on Redis with String keys and Long values.
     */
    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public Long incrementIterationByPath(String path) {
        Long result = redisTemplate.opsForValue().increment(getRedisKey(path), 1);

        validator(result);

        return result;
    }

    @Override
    public void decrementIterationByPath(String path) {
        Long beforeDecrement = getCurrentIterationByPath(path);


        Long afterDecrement = redisTemplate.opsForValue().decrement(getRedisKey(path), 1);


        decrementValidator(beforeDecrement, afterDecrement);
    }

    private void decrementValidator(Long beforeDecrement, Long afterDecrement) {
        validator(afterDecrement);

        if (beforeDecrement - 1 != afterDecrement) {
            throw new IllegalArgumentException("Value must be decremented by exactly 1. Before: %d, After: %d"
                    .formatted(beforeDecrement, afterDecrement));
        }
    }

    @Override
    public Long getCurrentIterationByPath(String path) {
        Long result = redisTemplate.opsForValue().get(getRedisKey(path));

        validator(result);

        return result;
    }

    @Override
    public void setIterationByPath(String path, Long value) {
        redisTemplate.opsForValue().set(getRedisKey(path), value);
    }

    @Override
    public void resetIterationByPath(String path) {
        redisTemplate.opsForValue().set(getRedisKey(path), 0L);
    }


    /**
     * Constructs the Redis key for a given identifier.
     *
     * @param path The identifier for which to construct the Redis key.
     * @return The constructed Redis key.
     */
    private String getRedisKey(String path) {
        return "%s : %s".formatted(PREFIX, path);
    }

    /**
     * Validates the result from Redis operations.
     * If the result is null, it throws an IllegalArgumentException.
     *
     * @param result The result to validate.
     * @throws IllegalArgumentException if the result is null.
     */
    private void validator(Long result) {
        if (result == null) {
            throw new IllegalArgumentException("The result from Redis is null. " +
                    "This may indicate that the key does not exist or has not been initialized.");
        }
    }
}
