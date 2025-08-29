//package dev.soon.feynman.scheduler;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.soon.feynman.asosDalyInfoService.service.AsosInfoService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AsosDailyInfoScheduler {
//
//    private final ObjectMapper objectMapper;
//    private final AsosInfoService asosInfoService;
//
//    /**
//     * 매일 새벽 한 시에 전날 데이터 조회
//     * cron = "0 0 1 * * *"
//     */
//    @Scheduled(cron = "0 0 1 * * *")
//    public void scheduleDailyAsosInfo() {
//        LocalDate yesterdayLD = LocalDate.now().minusDays(1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String yesterday = yesterdayLD.format(formatter);
//
//        String fileName = "static/asos/station_ids.json";
//        try (InputStream is = ClassLoader.getSystemResourceAsStream(fileName)) {
//            if (is == null) {
//                log.error("파일을 찾을 수 없습니다: {}", fileName);
//                return;
//            }
//            List<String> stationName = objectMapper.readValue(is, new TypeReference<>() {});
//            for (String station : stationName) {
//                asosInfoService.getDailyStats(station, yesterday, yesterday);
//                try {
//                    TimeUnit.SECONDS.sleep(3);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    log.error("ASOS daily info API call sleep was interrupted", e);
//                }
//            }
//            log.info("ASOS Daily Info job finished for file: {}. Processed {} stations.", fileName, stationName.size());
//        } catch (IOException e) {
//            log.error("Failed to read station list file: {}", fileName, e);
//        }
//    }
//}
