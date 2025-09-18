package pl.oliwawyplywa.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;

@Configuration
public class WebFluxConfig {

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        ServerCodecConfigurer configurer = ServerCodecConfigurer.create();

        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
        partReader.setMaxParts(2);
        partReader.setMaxDiskUsagePerPart(10 * 1024 * 1024);
        partReader.setMaxInMemorySize(512 * 1024);
        configurer.customCodecs().register(new MultipartHttpMessageReader(partReader));

        return configurer;
    }
}
