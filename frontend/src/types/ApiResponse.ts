import type { StationDetailData } from "./airQuality";

export interface ApiResponse<T> {
    status: string;
    totalCount: number;
    data: T;
}

export type AirQualityApiResponse = ApiResponse<StationDetailData>;