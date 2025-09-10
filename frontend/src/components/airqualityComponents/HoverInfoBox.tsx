import { Box, Text, VStack } from "@chakra-ui/react";
import type { HoverInfo } from "../../types";
import type { AirQualityApiResponse } from "../../types/ApiResponse";
import type { MapRef } from "react-map-gl";

interface Props {
  hoverInfo: HoverInfo | null;
  stationData: AirQualityApiResponse | null;
  mapRef: MapRef | null;
}

export default function HoverInfoBox({ hoverInfo, stationData, mapRef }: Props) {
  if (!hoverInfo || !mapRef) return null;

  const p = mapRef.project(hoverInfo.lngLat);

  return (
    <Box
      position="absolute"
      pointerEvents="none"
      bg="bg"
      p={2}
      borderRadius="md"
      boxShadow="md"
      zIndex={10}
      left={`${p.x}px`}
      top={`${p.y}px`}
      transform="translate(-50%, -120%)"
      fontSize="sm"
    >
      <VStack align="flex-start" gap={1}>
        <Text fontWeight="bold">{stationData?.data.stationName || "정보 없음"}</Text>
        <Text>Code: {hoverInfo.feature.properties?.A1}</Text>
        {stationData?.data.airQualityData?.date && (
          <Text>측정일: {stationData.data.airQualityData.date}</Text>
        )}
        {stationData?.data.airQualityData?.pm10_value && (
          <Text>PM10: {stationData.data.airQualityData.pm10_value} ㎍/㎥</Text>
        )}
        {stationData?.data.airQualityData?.so2_value && (
          <Text>SO₂: {stationData.data.airQualityData.so2_value} ppm</Text>
        )}
      </VStack>
    </Box>
  );
}
