package dev.soon.feynman;

import dev.soon.feynman.FeynmanApplication;
import dev.soon.feynman.arpltnStatsSvc.api.ArpltnStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FeynmanApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
class ArpltnStatsApiClientTest {

    @Autowired
    private ArpltnStatsApiClient client;

    @Test
    void getArpltnStatsTest() {
        String stationName = "강남구";
        String startDate = "20250810";
        String endDate = "20250811";

        ArpltnStatsApiResponse response = client.getArpltnStats(stationName, startDate, endDate);

        assertNotNull(response);
        System.out.println(response); // 콘솔에 응답 내용 출력
    }
}