export interface AirQualityData {
    sido: string;
    date: string | null;
    dataType: string | null;
    pm10_value: number | null;
    so2_value: number | null;
    o3_value: number | null;
    co_value: number | null;
    no2_value: number | null;
    pm25_value: number | null;
    khai_value: number | null;
}

export interface StationDetailData {
    stationName: string;
    stationCode: string;
    airQualityData: AirQualityData | null;
}

export interface ArpltnResponse {
    stationName: string;
    stationCode: string;
    airQualityData: AirQualityData;
}

export interface SearchResponseData {
    arpltnResponseList: ArpltnResponse[];
}

export interface SearchResponse {
    status: string;
    totalCount: number;
    dataSize: number;
    data: SearchResponseData;
}

export interface SearchCriteria {
    startDate?: string;
    endDate?: string;
    stationName?: string;
    stationCode?: string;
    dataType?: string;
    measurementType?: string[];
}