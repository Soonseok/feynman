import { Button, Text, VStack, Heading, Spacer } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import type { MapRef } from "react-map-gl";
import type { AirQualityApiResponse } from "../types/ApiResponse";
import type { HoverInfo, MapType } from "../types";
import MapContainer from "../components/airqualityComponents/MapContainer";
import HoverInfoBox from "../components/airqualityComponents/HoverInfoBox";
import MapChooseRadioBtn from "../components/airqualityComponents/MapChooseRadioBtn";

export default function TestMap() {
  const [mapType, setMapType] = useState<MapType>("sid");
  const [stationData, setStationData] = useState<AirQualityApiResponse | null>(
    null
  );
  const [hoverInfo, setHoverInfo] = useState<HoverInfo | null>(null);
  const [fixedHoverInfo, setFixedHoverInfo] = useState<HoverInfo | null>(null);
  const [highlightedRegions, setHighlightedRegions] = useState<string[]>([]);
  const [viewport, setViewport] = useState({
    latitude: 37.5,
    longitude: 127,
    zoom: 9,
  });

  const mapRef = useRef<MapRef | null>(null);
  const prevCodeRef = useRef<string | null>(null);

  useEffect(() => {
    const fetchCodeList = async () => {
      try {
        const response = await axios.get("/api/v1/arpltn-call/have_station");
        const processedCodes = (response.data || [])
          .filter(
            (code: unknown): code is string =>
              typeof code === "string" && code.length > 0
          )
          .map((code: string) => {
            if (mapType === "sgg") {
              // 코드가 8자리이고, 뒤 3자리가 '000'인 경우만 5자리로 변환
              if (code.length === 8 && code.slice(-3) === "000") {
                return code.substring(0, 5);
              }
            }
            return code;
          });
        const uniqueCodes: string[] = [...new Set(processedCodes as string[])];
        setHighlightedRegions(uniqueCodes);
      } catch (error) {
        console.error("Failed to fetch station codes:", error);
        setHighlightedRegions([]);
      }
    };
    fetchCodeList();
  }, [mapType]);

  useEffect(() => {
    const infoToFetch = fixedHoverInfo || hoverInfo;
    let currentCode = infoToFetch?.feature?.properties?.A1;
    if (mapType === "sgg" && currentCode && currentCode.length === 5) {
      currentCode = `${currentCode}000`;
    }
    if (!currentCode || currentCode === prevCodeRef.current) {
      if (!currentCode) setStationData(null);
      return;
    }
    axios
      .get<AirQualityApiResponse>(`/api/v1/arpltn-call/${currentCode}`)
      .then((res) => setStationData(res.data))
      .catch(() => setStationData(null))
      .finally(() => {
        prevCodeRef.current = currentCode;
      });
  }, [hoverInfo, fixedHoverInfo, mapType]);

  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Heading fontSize={"2rem"} paddingTop={5} mb={4}>
        경계 지도
      </Heading>

      {/* 라디오 버튼 컴포넌트를 사용하고 mapType 상태를 props로 전달 */}
      <MapChooseRadioBtn mapType={mapType} setMapType={setMapType} />

      <Spacer />

      {/* MapContainer에 필요한 모든 props를 전달 */}
      <MapContainer
        mapType={mapType}
        viewport={viewport}
        onViewportChange={setViewport}
        hoverInfo={hoverInfo}
        setHoverInfo={setHoverInfo}
        fixedHoverInfo={fixedHoverInfo}
        setFixedHoverInfo={setFixedHoverInfo}
        highlightedRegions={highlightedRegions}
        mapRef={mapRef}
      />

      {/* 툴팁 컴포넌트에 필요한 props를 전달 */}
      <HoverInfoBox
        hoverInfo={hoverInfo || fixedHoverInfo}
        stationData={stationData}
        mapRef={mapRef}
      />

      <Spacer />
      <Text>
        중심 좌표: {viewport.longitude.toFixed(3)},{" "}
        {viewport.latitude.toFixed(3)}
      </Text>
      <Link to="/">
        <Button colorScheme="red" variant={"ghost"}>
          Go Main &rarr;
        </Button>
      </Link>
    </VStack>
  );
}
