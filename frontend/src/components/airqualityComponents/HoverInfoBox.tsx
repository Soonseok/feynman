import { Box, Text, VStack } from "@chakra-ui/react";
import type { HoverInfo } from "../../types";
import type { AirQualityApiResponse } from "../../types/ApiResponse";
import type { MapRef } from "react-map-gl";
import type { RefObject } from "react";

interface Props {
  hoverInfo: HoverInfo | null;
  stationData: AirQualityApiResponse | null;
  mapRef: RefObject<MapRef | null>;
}

export default function HoverInfoBox({
  hoverInfo,
  stationData,
  mapRef,
}: Props) {
  if (!hoverInfo || !mapRef.current) return null;

  const p = mapRef.current.project(hoverInfo.lngLat);
  const container = mapRef.current.getContainer();
  const rect = container.getBoundingClientRect();

  const left = p.x + rect.left;
  const top = p.y + rect.top;

  return (
    <Box
      position="absolute"
      pointerEvents="none"
      bg="bg"
      p={2}
      borderRadius="md"
      boxShadow="md"
      zIndex={10}
      left={`${left}px`}
      top={`${top}px`}
      transform="translate(-102%, -102%)"
      fontSize="sm"
    >
      <VStack align="flex-start" gap={1}>
        <Text fontWeight="bold">
          {stationData?.data.stationName || "정보 없음"}
        </Text>
        <Text>Code: {hoverInfo.feature.properties?.A1}</Text>
        {stationData?.data.airQualityData?.date && (
          <Text>측정일 : {stationData.data.airQualityData.date}</Text>
        )}
        {stationData?.data.airQualityData?.pm10_value && (
          <Text>PM10 : {stationData.data.airQualityData.pm10_value} ㎍/㎥</Text>
        )}
        {stationData?.data.airQualityData?.pm25_value && (
          <Text>PM2.5 : {stationData.data.airQualityData.pm25_value} ㎍/㎥</Text>
        )}
        {stationData?.data.airQualityData?.so2_value && (
          <Text>SO₂ : {stationData.data.airQualityData.so2_value} ppm</Text>
        )}
        {stationData?.data.airQualityData?.o3_value && (
          <Text>O₃ : {stationData.data.airQualityData.o3_value} ppm</Text>
        )}
        {stationData?.data.airQualityData?.no2_value && (
          <Text>NO₂ : {stationData.data.airQualityData.no2_value} ppm</Text>
        )}
        {stationData?.data.airQualityData?.co_value && (
          <Text>CO : {stationData.data.airQualityData.co_value} ppm</Text>
        )}
        {stationData?.data.airQualityData?.khai_value && (
          <Text>χ : {stationData.data.airQualityData.khai_value}</Text>
        )}
      </VStack>
    </Box>
  );
}
