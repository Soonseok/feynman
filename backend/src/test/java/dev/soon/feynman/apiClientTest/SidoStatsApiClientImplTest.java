package dev.soon.feynman.apiClientTest;

import dev.soon.feynman.arpltnStatsSvc.api.SidoStatsApiClientImpl;
import dev.soon.feynman.arpltnStatsSvc.dto.SidoStatsApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SidoStatsApiClientImplTest {

    @Autowired
    private SidoStatsApiClientImpl client;

    @Test
    @DisplayName("시도별 API 호출 테스트")
    void getSidoStatsTest() {
        // GIVEN: 테스트에 필요한 매개변수
        String itemCode = "PM10"; // SO2, CO, O3, NO2, PM10, PM2.5 등
        String dataGubun = "HOUR"; // HOUR 또는 DAILY
        String searchCondition = "MONTH"; // MONTH 또는 WEEK

        // WHEN: API 클라이언트 메서드 호출
        SidoStatsApiResponse response = client.getSidoStats(itemCode, dataGubun, searchCondition);

        // THEN: 응답이 null이 아니고 데이터가 존재하는지 확인
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getBody());
        assertTrue(response.getResponse().getBody().getItems().size() > 0);

        System.out.println("SidoStats API 응답: " + response);
    }
}