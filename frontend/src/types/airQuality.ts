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