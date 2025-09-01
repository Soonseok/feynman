package dev.soon.feynman.asosDalyInfoService.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.List;

/**
 * 종관기상관측일자료 데이터 DTO<br/>
 * API 응답을 그대로 매핑
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsosDalyInfoApiResponse {

    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("header")
        private Header header;
        @JsonProperty("body")
        private Body body;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private String dataType;
        private Items items;
        private Integer pageNo;
        private Integer numOfRows;
        private Integer totalCount;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {

        /**
         * 실제 데이터 목록을 담음
         */
        @JsonProperty("item")
        private List<Item> item;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        // 필수 항목
        private String tm;
        private String stnId;
        private String stnNm;

        // 옵션 항목
        private String avgTa;
        private String minTa;
        private String minTaHrmt;
        private String maxTa;
        private String maxTaHrmt;
        private String mi10MaxRn;
        private String mi10MaxRnHrmt;
        private String hr1MaxRn;
        private String hr1MaxRnHrmt;
        private String sumRnDur;
        private String sumRn;
        private String maxInsWs;
        private String maxInsWsWd;
        private String maxInsWsHrmt;
        private String maxWs;
        private String maxWsWd;
        private String maxWsHrmt;
        private String avgWs;
        private String hr24SumRws;
        private String maxWd;
        private String avgTd;
        private String minRhm;
        private String minRhmHrmt;
        private String avgRhm;
        private String avgPv;
        private String avgPa;
        private String maxPs;
        private String maxPsHrmt;
        private String minPs;
        private String minPsHrmt;
        private String avgPs;
        private String ssDur;
        private String sumSsHr;
        private String hr1MaxIcsrHrmt;
        private String hr1MaxIcsr;
        private String sumGsr;
        private String ddMefs;
        private String ddMefsHrmt;
        private String ddMes;
        private String ddMesHrmt;
        private String sumDpthFhsc;
        private String avgTca;
        private String avgLmac;
        private String avgTs;
        private String minTg;
        private String avgCm5Te;
        private String avgCm10Te;
        private String avgCm20Te;
        private String avgCm30Te;
        private String avgM05Te;
        private String avgM10Te;
        private String avgM15Te;
        private String avgM30Te;
        private String avgM50Te;
        private String sumLrgEv;
        private String sumSmlEv;
        private String n99Rn;
        private String iscs;
        private String sumFogDur;
    }
}
