package dev.soon.feynman.apiClientTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soon.feynman.FeynmanApplication;
import dev.soon.feynman.asosDalyInfoService.api.AsosInfoApiClientImpl;
import dev.soon.feynman.asosDalyInfoService.dto.AsosDalyInfoApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FeynmanApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class AsosApiClientTest {

    @Autowired
    private AsosInfoApiClientImpl asosInfoApiClient;

    @Test
    @DisplayName("ASOS API Client 테스트")
    void getAsosApiTest() {
        String stnIds = "108";
        String startDt = "20250810";
        String endDt = "20250810";
        AsosDalyInfoApiResponse response = asosInfoApiClient.callApi(stnIds, startDt, endDt, 100, 1);
        try {
            String stationName = getStationNameFromStnIds(stnIds);
            System.out.println("\n>>> station name : "+ stationName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        System.out.println(response);
    }

    public String getStationNameFromStnIds(String stnIds) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/api_files/asos/asos_stations.json")) {
            if (is == null) {
                throw new IOException("File not found in classpath: static/asos/asos_stations.json");
            }
            JsonNode root = mapper.readTree(is);
            int targetId = Integer.parseInt(stnIds);
            String stationName = null;

            for (JsonNode node : root) {
                if (node.get("stationId").asInt() == targetId) {
                    stationName = node.get("stationName").asText();
                    break;
                }
            }
            return stationName;
        }
    }
}
