package dev.soon.feynman.apiClientTest;

import dev.soon.feynman.FeynmanApplication;
import dev.soon.feynman.arpltnStatsSvc.api.DailyStatsApiClientImpl;
import dev.soon.feynman.arpltnStatsSvc.dto.DailyStatsApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FeynmanApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
class DailyStatsApiClientImplTest {

    @Autowired
    private DailyStatsApiClientImpl client;

    @Test
    @DisplayName("일일 통계 API 호출 테스트")
    void getArpltnStatsTest() {
        String stationName = "강남구";
        String startDate = "20250810";
        String endDate = "20250811";

        DailyStatsApiResponse response = client.callApi(stationName, startDate, endDate, 100, 1);

        assertNotNull(response);
        System.out.println(response); // 콘솔에 응답 내용 출력
    }
}