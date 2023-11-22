package io.blob

import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.Duration
import java.time.Instant

@Configuration
@EnableScheduling
class CleanMultipartTempFiles {
    @Scheduled(cron = "0 * * * * *") // Cron definition
    fun clean() {
        logger.info("Multipart temporary directory cleanup cron started!")

        val now = Instant.now()
        val ttl = Duration.ofSeconds(30)

        // Cleaning files
        File("/tmp/spring-multipart").listFiles()?.map { exportEntry ->
            this.deleteOlderThanTtl(exportEntry, ttl, now)
        }

        logger.info("Multipart temporary directory cleanup cron finished!")
    }

    fun deleteOlderThanTtl(entry: File, entryTimeToLive: Duration, now: Instant) {
        val entryCreationInstant = Instant.ofEpochMilli(Files.readAttributes(entry.toPath(), BasicFileAttributes::class.java).creationTime().toMillis())
        val entryTimeToLiveInstant = entryCreationInstant.plus(entryTimeToLive)

        if (entryTimeToLiveInstant.isBefore(now)) {
            try {
                entry.delete()
                logger.info("Cleaned '${entry.absolutePath}'")
            } catch (t: Throwable) {
                logger.info("Could not delete temporary file '${entry.absolutePath}': ${t.message}")
            }
        } else {
            logger.info("Kept '${entry.absolutePath}'")
        }
    }

    companion object {
        private val logger = LogFactory.getLog(CleanMultipartTempFiles::class.java)
    }
}
