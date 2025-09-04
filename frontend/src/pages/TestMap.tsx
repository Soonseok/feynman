import { Box, Button, Text, VStack } from "@chakra-ui/react";
import Header from "../components/layout/Header";
import { Heading } from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";
import Footer from "../components/layout/Footer";
import { useState } from "react";
import Map, { Layer, Source } from "react-map-gl";
import type { Style } from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";

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
  const [mousePos, setMousePos] = useState<{ lng: number; lat: number } | null>(
    null
  );

  const [viewport, setViewport] = useState({
    latitude: 37.5,
    longitude: 127,
    zoom: 9,
  });

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
          {...viewport}
          mapboxAccessToken={MAPBOX_TOKEN}
          style={{ width: "100%", height: "100%" }}
          onMove={(evt) => setViewport(evt.viewState)}
          onMouseMove={(e) => setMousePos(e.lngLat)}
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
        </Map>
      </Box>
      <Text>
        중심 좌표: {viewport.longitude.toFixed(3)},{" "}
        {viewport.latitude.toFixed(3)}
      </Text>
      {mousePos && (
        <Text>
          마우스: {mousePos.lng.toFixed(3)}, {mousePos.lat.toFixed(3)}
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
