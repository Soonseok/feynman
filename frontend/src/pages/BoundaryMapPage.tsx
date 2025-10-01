// BoundaryMapPage.tsx
import { VStack, Heading, Spacer } from "@chakra-ui/react";
import { useState } from "react";
import type { MapType } from "../types";
import MapChooseRadioBtn from "../components/airqualityComponents/MapChooseRadioBtn";
import SearchBox from "../components/layout/SearchBox";
import GeoMapViewer from "./GeoMapViewer";

export default function BoundaryMapPage() {
  const [mapType, setMapType] = useState<MapType>("ne:sido");
  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Heading fontSize={"2rem"} paddingTop={5} mb={4}>
        경계 지도 (GeoServer WMS)
      </Heading>

      {/* MapChooseRadioBtn은 mapType을 GeoServer 레이어 이름으로 설정한다고 가정 */}
      <MapChooseRadioBtn mapType={mapType} setMapType={setMapType} />
      
      {/* 뷰포트 정보 출력 (현재는 WMS에서 관리되므로 주석 처리) */}
      {/*
      <Text>
        중심 좌표: {viewport.longitude.toFixed(3)},{" "}
        {viewport.latitude.toFixed(3)}
      </Text>
      */}
      <Spacer />

      {/* MapContainer를 GeoMapViewer로 대체 */}
      <GeoMapViewer mapType={mapType} />

      {/* HoverInfoBox는 Mapbox feature 속성에 의존하므로, WMS 상호작용 구현 전까지는 제거/주석 처리 */}
      {/*
      <HoverInfoBox
        hoverInfo={hoverInfo || fixedHoverInfo}
        stationData={stationData}
        mapRef={mapRef}
      />
      */}

      <Spacer />
      
      <SearchBox />
    </VStack>
  );
}
