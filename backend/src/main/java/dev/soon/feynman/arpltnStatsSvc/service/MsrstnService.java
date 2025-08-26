package dev.soon.feynman.arpltnStatsSvc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 측정소 이름(stationName)을 불러와 메모리에 적재.
 */
@Service
public class MsrstnService {

    private List<String> stationNames;

    @PostConstruct
    public void loadMsrstnData() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/static/msrstn_list.json")) {
            List<LinkedHashMap<String, Object>> rawData = mapper.readValue(is, new TypeReference<>() {});
            this.stationNames = rawData.stream()
                    .map(item -> (String) item.get("stationName"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllStationNames() {
        return this.stationNames;
    }
}
