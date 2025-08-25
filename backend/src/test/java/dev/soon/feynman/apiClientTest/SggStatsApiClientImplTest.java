package dev.soon.feynman.apiClientTest;

import dev.soon.feynman.arpltnStatsSvc.api.SggStatsApiClientImpl;
import dev.soon.feynman.arpltnStatsSvc.dto.SggStatsApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SggStatsApiClientImplTest {

    @Autowired
    private SggStatsApiClientImpl client;

    @Test
    @DisplayName("시군구별 API 호출 테스트")
    void getSggStatsTest() {
        // GIVEN: 테스트에 필요한 매개변수
        String sidoName = "서울";
        String searchCondition = "DAILY"; // HOUR 또는 DAILY

        // WHEN: API 클라이언트 메서드 호출
        SggStatsApiResponse response = client.getSggStats(sidoName, searchCondition);

        // THEN: 응답이 null이 아니고 데이터가 존재하는지 확인
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertNotNull(response.getResponse().getBody());
        assertTrue(response.getResponse().getBody().getItems().size() > 0);

        System.out.println("SggStats API 응답: " + response);
    }
}