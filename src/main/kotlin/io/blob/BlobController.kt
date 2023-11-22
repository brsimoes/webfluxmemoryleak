package io.blob

import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@RestController
@RequestMapping(value = ["/blob"])
class BlobController {
    // Upload blob
    @PutMapping(value = ["/test"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadBlobAsync(@RequestPart("blob", required = true) blob: Mono<FilePart>, ): Mono<ResponseEntity<String>> {

        logger.info("Uploading @{}", LocalDateTime.now())

        return blob
            .flatMap { filePart ->
                filePart
                    .content()
                    .map { dataBuffer ->
                        try {
                            val bytes = ByteArray(dataBuffer.readableByteCount())
                            dataBuffer.read(bytes)
                            bytes
                        } finally {
                            DataBufferUtils.release(dataBuffer)
                        }
                    }
                    .collectList()
                    .map { _ ->
                        ResponseEntity.ok("OK")
                    }
            }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BlobController::class.java)
    }
}
