package dev.soon.feynman.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soon.feynman.asosDalyInfoService.service.AsosInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsosDailyInfoScheduler {

    private final ObjectMapper objectMapper;
    private final AsosInfoService asosInfoService;

    @Scheduled(cron = "0 30 2 * * *")
    public void scheduleDailyAsosInfo() {
        String fileName = "static/asos/station_ids.json";
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            List<String> stationName = objectMapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            log.error("Failed to read station list file: {}", fileName, e);
        }
    }
}
