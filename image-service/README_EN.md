# Storage Service
### Version 0.0.1-SNAPSHOT
Microservice for file upload, storage and management in BB Rent System.

## Key Features
- File upload via multipart/form-data
- File storage in MinIO (S3-compatible storage)
- Generate temporary URLs for download or viewing
- File update and deletion
- Redis caching for performance optimization
- Automatic file ID iteration tied to storage path (deprecated, will be removed in v0.0.2)

## Technologies
- Java 17+
- Spring Boot 3.x
- MinIO (S3-compatible storage)
- Redis (caching and sequence management)
- MapStruct (DTO mapping)
- Docker Compose

## Quick Start

### 1. Clone Repository
```bash
    git clone <repo-url>
    cd bb-rent-system/image-service
```

### 2. Start Infrastructure
```bash
    docker compose up -d
```

### 3. Configuration
Configure `application.yml`:
```yaml
minio:
  credentials:
    endpoint: http://localhost:9000
    access-key: your-access-key
    secret-key: your-secret-key
    region: ru

spring:
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. Build and Run
```bash
    ./mvnw clean package
    java -jar target/image-service-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Upload File
`POST /api/v1/storage/`
- Content-Type: multipart/form-data
- request: UploadRequest (bucketName, path)
- file: MultipartFile

### Get Temporary URLs
`GET /api/v1/storage/`
- Content-Type: application/json
- Body: DownloadRequest (bucketName, path, expires, timeUnit, method)

### Update File
`PUT /api/v1/storage/`
- Content-Type: multipart/form-data
- request: UpdateRequest (bucketName, path, fileName)
- file: MultipartFile

### Delete File
`DELETE /api/v1/storage/`
- Body: DeleteByNameRequest (bucketName, path, objectName)

### Delete Folder
`DELETE /api/v1/storage/prefix`
- Body: DeleteWithPathRequest (bucketName, path)

## Project Structure
```
src/main/java/dev/buhanzaz/storage/
├── config/          # MinIO and Redis configuration
├── controller/      # REST controllers
├── domains/
│   ├── dto/         # Request/Response DTO
│   └── model/       # Internal models
├── exceptions/      # Exception handling
├── mapper/          # MapStruct mapping
└── services/        # Business logic
    └── impl/        # Service implementations
```

## Features
- Automatic file ID iteration tied to storage path
- File list caching with 18-hour TTL
- Support for files up to 10MB
- Type-safe configurations via @ConfigurationProperties
- Comprehensive error handling

## Future Plans
- File versioning and version management implementation
- Documentation
- Bucket management (CRUD)
- Testing
- File metadata addition and service for working with them
- Validation

## License
MIT

