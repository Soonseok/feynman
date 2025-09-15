package dev.soon.feynman.arpltnCallBack.service;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetArpltnDataServiceImpl implements GetArpltnDataService{

    private final GetArpltnData getArpltnData;

    @Override
    public List<AirQualityData> getTestDataService() {
        return null;
    }
}
