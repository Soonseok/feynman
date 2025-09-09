import { Box, Button, Text, VStack } from "@chakra-ui/react";
import { Heading } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import Map, {
  Layer,
  Source,
  type MapGeoJSONFeature,
  type MapRef,
} from "react-map-gl";
import type { Style } from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import axios from "axios";
import type { StationData, HoverInfo } from "../types";

const MAPBOX_TOKEN = import.meta.env.VITE_MAPBOX_TOKEN;
const UMD_ID = import.meta.env.VITE_UMD_TILESET_ID;

const emptyStyle: Style = {
  version: 8,
  sources: {},
  layers: [],
};

export default function TestMap() {
  const [mousePos, setMousePos] = useState<[number, number] | null>(null);
  const mapRef = useRef<MapRef | null>(null);
  const [stationData, setStationData] = useState<StationData | null>(null);
  const [hoverInfo, setHoverInfo] = useState<HoverInfo | null>(null);

  const [viewport, setViewport] = useState({
    latitude: 37.5,
    longitude: 127,
    zoom: 9,
  });

  // 이전에 보냈던 code 저장
  const prevCodeRef = useRef<string | null>(null);

  useEffect(() => {
    const currentCode = hoverInfo?.feature?.properties?.A1;

    // 현재 code가 없거나, 이전 code와 같으면 요청 보내지 않음
    if (!currentCode || currentCode === prevCodeRef.current) {
      if (!currentCode) {
        setStationData(null);
      }
      return;
    }

    // 이전 code와 다를 경우에만 API 요청
    axios
      .get<StationData>(`/api/v1/arpltn-call/${currentCode}`)
      .then((res) => {
        setStationData(res.data);
      })
      .catch(() => {
        setStationData(null);
      })
      .finally(() => {
        // 요청이 완료되면 현재 code를 이전 code로 업데이트
        prevCodeRef.current = currentCode;
      });
  }, [hoverInfo]); // hoverInfo가 변경될 때마다 이펙트를 실행

  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Heading>읍면동 경계 지도</Heading>
      <Box
        w="85%"
        h="60vh"
        borderWidth="3px"
        borderColor="border.inverted"
        borderRadius="md"
      >
        <Map
          ref={mapRef}
          {...viewport}
          mapboxAccessToken={MAPBOX_TOKEN}
          style={{ width: "100%", height: "100%" }}
          onMove={(evt) => setViewport(evt.viewState)}
          onMouseMove={(e) => {
            setMousePos(e.lngLat.toArray() as [number, number]);

            const feats = e.features as MapGeoJSONFeature[] | undefined;
            const hovered = feats?.find((f) => f.layer.id === "sigungu");
            setHoverInfo(
              hovered
                ? {
                    feature: hovered,
                    lngLat: e.lngLat.toArray() as [number, number],
                  }
                : null
            );
          }}
          interactiveLayerIds={["sigungu"]}
          mapStyle={emptyStyle}
          minZoom={9}
          maxZoom={15}
        >
          <Source id="sigungu-source" type="vector" url={`mapbox://${UMD_ID}`}>
            <Layer
              id="sigungu"
              type="fill"
              source="sigungu-source"
              source-layer="AL_D001_00_20250804EMD-0q23o5"
              paint={{
                "fill-color": "#60a5fa", // 파란색
                "fill-opacity": 0.8,
                "fill-outline-color": "#000",
              }}
            />
          </Source>
          <Box
            position="absolute"
            top="1%"
            right="1%"
            bg="bg"
            p={2.5}
            opacity={0.8}
            borderRadius="md"
            fontSize="sm"
          >
            <Text fontWeight="bold" color="fg">
              현재 줌: {viewport.zoom.toFixed(2)}
            </Text>
            <Text color="fg">가능한 줌 범위: 9 ~ 15</Text>
          </Box>
          {/* 툴팁: 지도 좌표 -> 픽셀 좌표 변환 */}
          {hoverInfo &&
            mapRef.current &&
            (() => {
              const p = mapRef.current.project(hoverInfo.lngLat); // {x,y}
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
                    {/* 💡 stationData 객체에서 필요한 값 사용 */}
                    <Text fontWeight="bold">
                      {stationData?.station_name || "정보 없음"}
                    </Text>
                    <Text>Code: {hoverInfo.feature.properties?.A1}</Text>
                    {stationData && (
                      <>
                        <Text>측정일: {stationData.date}</Text>
                        <Text>
                          PM10 (단위: ㎍/㎥): {stationData.pm10_value}
                        </Text>
                      </>
                    )}
                  </VStack>
                </Box>
              );
            })()}
        </Map>
      </Box>
      <Text>
        중심 좌표: {viewport.longitude.toFixed(3)},{" "}
        {viewport.latitude.toFixed(3)}
      </Text>
      {mousePos && (
        <Text>
          마우스: {mousePos[0].toFixed(3)}, {mousePos[1].toFixed(3)}
        </Text>
      )}
      <Link to="/">
        <Button colorScheme="red" variant={"ghost"}>
          Go Main &rarr;
        </Button>
      </Link>
    </VStack>
  );
}