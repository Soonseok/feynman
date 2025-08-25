package dev.soon.feynman.arpltnStatsSvc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 측정소별 일평균 DTO <br/>
 * API 응답을 그대로 매핑
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyStatsApiResponse {

    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode; // 결과메시지, 아래 설명 참고
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Integer totalCount;
        private Integer numOfRows;
        private Integer pageNo;

        /**
         * 실제 데이터 목록을 담음
         */
        @JsonProperty("items")
        private List<Item> items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String msurDt;
        private String msrstnName;
        private String so2Value;
        private String coValue;
        private String o3Value;
        private String no2Value;
        private String pm10Value;
        private String pm25Value;
    }
}
/**
 * 01	Application Error	서비스 제공 상태가 원활하지 않습니다
 * 02	DB Error	서비스 제공 상태가 원활하지 않습니다
 * 03	No Data	데이터 없음 에러
 * 04	HTTP Error	서비스 제공 상태가 원활하지 않습니다
 * 05	service time out	서비스 제공 상태가 원활하지 않습니다
 * 10	잘못된 요청 파라미터 에러	OpenAPI 요청시 파라미터값 요청이 잘못되었습니다
 * 11	필수 요청 파라미터 없음	요청하신 OpenAPI의 필수 파라미터가 누락되었습니다
 * 12	해당 오픈API 서비스가 없거나 폐기됨	OpenAPI 호출시 URL이 잘못됨
 * 20	서비스 접근 거부	활용 신청하지 않은 OpenAPI 호출
 * 22	서비스 요청 제한 횟수 초과 에러	하루 트래픽 제한을 초과함
 * 30	등록하지 않은 서비스키	잘못된 서비스키를 사용하였거나 서비스키를 URL 인코딩하지 않음
 * 31	서비스키 사용 기간 만료	OpenAPI 사용기간이 만료됨 (활용기간 연장신청 후 사용가능)
 * 32	등록하지 않은 도메인명 또는 IP주소	활용신청한 서버의 IP와 실제 OpenAPI 호출한 서버가 다를 경우
 */