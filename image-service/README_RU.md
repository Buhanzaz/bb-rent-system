# Storage Service
### Version 0.0.1-SNAPSHOT
Микросервис для загрузки, хранения и управления файлами в системе BB Rent System.

## Основные возможности
- Загрузка файлов через multipart/form-data
- Хранение файлов в MinIO (S3-совместимое хранилище)
- Получение временных URL для скачивания или просмотра изображений
- Обновление и удаление файлов
- Redis кэширование для оптимизации производительности
- Автоматическая итерация id файла привязанная к пути хранения (Я подумал, и решил что это не имеет смысла. 
В версии 0.0.2 будет удалена)

## Технологии
- Java 17+
- Spring Boot 3.x
- MinIO (S3-совместимое хранилище)
- Redis (кэширование и sequence management)
- MapStruct (маппинг DTO)
- Docker Compose

## Быстрый старт

### 1. Клонирование репозитория
```bash
    git clone <repo-url>
    cd bb-rent-system/image-service
```

### 2. Запуск инфраструктуры
```bash
    docker compose up -d
```

### 3. Конфигурация
Настройте `application.yml`:
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

### 4. Сборка и запуск
```bash
    ./mvnw clean package
    java -jar target/image-service-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Загрузка файла
`POST /api/v1/storage/`
- Content-Type: multipart/form-data
- request: UploadRequest (bucketName, path)
- file: MultipartFile

### Получение временных URL
`GET /api/v1/storage/`
- Content-Type: application/json
- Body: DownloadRequest (bucketName, path, expires, timeUnit, method)

### Обновление файла
`PUT /api/v1/storage/`
- Content-Type: multipart/form-data
- request: UpdateRequest (bucketName, path, fileName)
- file: MultipartFile

### Удаление файла
`DELETE /api/v1/storage/`
- Body: DeleteByNameRequest (bucketName, path, objectName)

### Удаление папки
`DELETE /api/v1/storage/prefix`
- Body: DeleteWithPathRequest (bucketName, path)

## Структура проекта
```
src/main/java/dev/buhanzaz/storage/
├── config/          # Конфигурация MinIO и Redis
├── controller/      # REST контроллеры
├── domains/
│   ├── dto/         # Request/Response DTO
│   └── model/       # Внутренние модели
├── exceptions/      # Обработка исключений
├── mapper/          # MapStruct маппинг
└── services/        # Бизнес-логика
    └── impl/        # Реализации сервисов
```

## Особенности
- Автоматическая итерация id файла привязанная к пути хранения
- Кэширование списков файлов с TTL 18 часов
- Поддержка файлов до 10MB
- Типобезопасные конфигурации через @ConfigurationProperties
- Comprehensive error handling

## Future
- Реализация версионирования файлов и управления версиями.
- Документация
- Управление Bucket (CRUD)
- Тестирование
- Добавление метаданных для файлов и сервис для работы с ними
- Валидация
## Лицензия
MIT

