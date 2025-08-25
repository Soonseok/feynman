package dev.soon.feynman.apiClientTest;

import dev.soon.feynman.arpltnStatsSvc.api.MonthlyStatsApiClientImpl;
import dev.soon.feynman.arpltnStatsSvc.dto.MonthlyStatsApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class MonthlyStatsApiClientImplTest {

    @Autowired
    private MonthlyStatsApiClientImpl client;

    @Test
    @DisplayName("월별 통계 API 호출 테스트")
    void getMonthlyStatsTest() {
        // GIVEN: 테스트에 필요한 매개변수
        String stationName = "강남구";
        String beginMonth = "202501";
        String endMonth = "202503";

        // WHEN: API 클라이언트 메서드 호출
        MonthlyStatsApiResponse response = client.getMonthlyStats(stationName, beginMonth, endMonth);

        // THEN: 응답이 null이 아니고 데이터가 존재하는지 확인
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getBody());
        assertTrue(response.getResponse().getBody().getItems().size() > 0);

        System.out.println("MonthlyStats API 응답: " + response);
    }
}
