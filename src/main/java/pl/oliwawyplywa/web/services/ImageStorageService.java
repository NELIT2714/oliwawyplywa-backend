package pl.oliwawyplywa.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ImageStorageService {

    private final Path storagePath;

    public ImageStorageService(@Value("${cdn.storage.path}") String storagePath) {
        this.storagePath = Paths.get(storagePath);
    }

    public Mono<String> saveImage(FilePart filePart) {
        return generateFilePaths(filePart)
            .flatMap(paths -> saveToTempFile(filePart, (Path) paths[1])
                .then(convertToWebP((Path) paths[0], (Path) paths[1]))
                .map(filename -> ((Path) paths[0]).getFileName().toString())
            )
            .onErrorResume(e -> Mono.error(new RuntimeException("Error file saving" + e.getMessage())));
    }

    private Mono<Object[]> generateFilePaths(FilePart filePart) {
        return Mono.fromCallable(() -> {
            String filename = UUID.randomUUID() + "_" + Instant.now().toEpochMilli() + ".webp";
            Path filePath = storagePath.resolve(filename);

            Files.createDirectories(storagePath);
            Path tempFile = storagePath.resolve("temp_" + filePart.filename());

            return new Object[]{filePath, tempFile};
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Void> saveToTempFile(FilePart filePart, Path tempFile) {
        return filePart.transferTo(tempFile);
    }

    private Mono<String> convertToWebP(Path filePath, Path tempFile) {
        return Mono.fromCallable(() -> {
            ProcessBuilder pb = new ProcessBuilder("cwebp", "-q", "70", tempFile.toString(), "-o", filePath.toString());
            Process p = pb.start();

            p.waitFor(5, TimeUnit.SECONDS);
            if (p.exitValue() != 0) throw new RuntimeException("cwebp failed");

            Files.deleteIfExists(tempFile);

            return filePath.getFileName().toString();
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
