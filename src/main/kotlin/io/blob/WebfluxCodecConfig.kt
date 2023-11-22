package io.blob

import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader
import org.springframework.http.codec.multipart.MultipartHttpMessageReader
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.nio.file.Paths

@Configuration
class WebfluxCodecConfig : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val partReader = DefaultPartHttpMessageReader()
        partReader.setMaxHeadersSize(16 * 1024)
        partReader.isEnableLoggingRequestDetails = true
        partReader.setFileStorageDirectory(Paths.get("/tmp/spring-multipart"))
        val multipartReader = MultipartHttpMessageReader(partReader)
        multipartReader.isEnableLoggingRequestDetails = true
        configurer.defaultCodecs().multipartReader(multipartReader)
    }
}
