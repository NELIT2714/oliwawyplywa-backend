package pl.oliwawyplywa.web.services;

import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path storagePath;

    public ImageStorageService(@Value("{cdn.storage.path}") String storagePath) {
        this.storagePath = Paths.get(storagePath);
    }

    public Mono<String> saveImage(FilePart filePart) {
        return generateFilePaths(filePart)
            .flatMap(paths -> saveToTempFile(filePart, (Path) paths[1])
                .then(convertToWebP((Path) paths[0], (Path) paths[1])))
            .onErrorResume(e -> Mono.error(new RuntimeException("Ошибка при сохранении файла: " + e.getMessage())));
    }

    private Mono<Object[]> generateFilePaths(FilePart filePart) {
        return Mono.fromCallable(() -> {
                // Генерируем имя файла: UUID_timestamp.webp
                String filename = UUID.randomUUID() + "_" + Instant.now().toEpochMilli() + ".webp";
                Path filePath = storagePath.resolve(filename);

                // Создаём директорию, если она не существует
                Files.createDirectories(storagePath);

                // Временный файл
                Path tempFile = storagePath.resolve("temp_" + filePart.filename());
                return new Object[]{filePath, tempFile};
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Void> saveToTempFile(FilePart filePart, Path tempFile) {
        return filePart.transferTo(tempFile);
    }

    private Mono<String> convertToWebP(Path filePath, Path tempFile) {
        return Mono.fromCallable(() -> {
                // Читаем изображение
                BufferedImage image = ImageIO.read(tempFile.toFile());

                // Находим WebP ImageWriter
                ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

                // Настраиваем параметры для lossless сжатия
                WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
                writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
                writeParam.setCompressionType("LOSSLESS");

                // Сохраняем в WebP
                try (FileImageOutputStream output = new FileImageOutputStream(filePath.toFile())) {
                    writer.setOutput(output);
                    writer.write(null, new IIOImage(image, null, null), writeParam);
                }
                writer.dispose();

                Files.deleteIfExists(tempFile); // Удаляем временный файл
                return filePath.getFileName().toString();
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

}
