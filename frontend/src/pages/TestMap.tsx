import { Box, Button, Text, VStack } from "@chakra-ui/react";
import Header from "../components/layout/Header";
import { Heading } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";
import Footer from "../components/layout/Footer";
import { useRef, useState } from "react";
import Map, {
  Layer,
  Source,
  type MapGeoJSONFeature,
  type MapRef,
} from "react-map-gl";
import type { MapboxGeoJSONFeature, Style } from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";

// A0 : 숫자 (시도 코드)
// A1 : 행정구역 코드 (string)
// A2 : 한글 행정구역 이름
// A3 : 날짜 비슷한 값
// A4 : 코드 (중복 느낌)

// 우분투 /lib/systemd/system/nginx.service에 환경변수 저장 함
const MAPBOX_TOKEN = import.meta.env.VITE_MAPBOX_TOKEN;
//const SIGU_ID = import.meta.env.VITE_SIGU_TILESET_ID; // 시도 경계, source-layer="AL_D001_00_20250804SIG-d0wsd0", zoom extent:9~15
const UMD_ID = import.meta.env.VITE_UMD_TILESET_ID; // 읍면동 경계, source-layer="AL_D001_00_20250804EMD-0q23o5", zoom extent:9~15

const emptyStyle: Style = {
  version: 8,
  sources: {},
  layers: [],
};

export default function TestMap() {
  const [mousePos, setMousePos] = useState<[number, number] | null>(null);

  const [hoverInfo, setHoverInfo] = useState<{
    feature: MapboxGeoJSONFeature;
    lngLat: [number, number];
  } | null>(null);

  const [viewport, setViewport] = useState({
    latitude: 37.5,
    longitude: 127,
    zoom: 9,
  });

  const mapRef = useRef<MapRef | null>(null);

  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Header />
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
            // 1) 마우스 좌표 항상 갱신
            setMousePos(e.lngLat.toArray() as [number, number]);

            // 2) hover 피처 찾기
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
                  <Text fontWeight="bold">
                    {hoverInfo.feature.properties?.A2 /* 예: 구/군 이름 */}
                  </Text>
                  <Text>A0: {hoverInfo.feature.properties?.A0}</Text>
                  <Text>A1: {hoverInfo.feature.properties?.A1}</Text>
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
      <ColorModeButton />
      <Footer />
    </VStack>
  );
}
