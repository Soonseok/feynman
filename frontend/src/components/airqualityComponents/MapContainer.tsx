import Map, {
  Layer,
  Source,
  type MapGeoJSONFeature,
  type MapRef,
} from "react-map-gl";
import { Box } from "@chakra-ui/react";
import { useCallback } from "react";
import type { Expression, Style } from "mapbox-gl";
import type { HoverInfo } from "../../types"; 

const emptyStyle: Style = {
  version: 8,
  sources: {},
  layers: [],
};

const MAPBOX_TOKEN = import.meta.env.VITE_MAPBOX_TOKEN;
const UMD_ID = import.meta.env.VITE_UMD_TILESET_ID;
const SGG_ID = import.meta.env.VITE_SIGU_TILESET_ID;

interface MapContainerProps {
  mapType: "umd" | "sgg";
  viewport: { latitude: number; longitude: number; zoom: number };
  onViewportChange: (vp: { latitude: number; longitude: number; zoom: number }) => void;
  hoverInfo: HoverInfo | null;
  setHoverInfo: (info: HoverInfo | null) => void;
  fixedHoverInfo: HoverInfo | null;
  setFixedHoverInfo: (info: HoverInfo | null) => void;
  highlightedRegions: string[] | null;
  mapRef: React.MutableRefObject<MapRef | null>;
}

export default function MapContainer({
  mapType,
  viewport,
  onViewportChange,
  hoverInfo,
  setHoverInfo,
  fixedHoverInfo,
  setFixedHoverInfo,
  highlightedRegions,
  mapRef,
}: MapContainerProps) {
  const MAP_ID = mapType === "umd" ? UMD_ID : SGG_ID;
  const SOURCE_LAYER =
    mapType === "umd"
      ? "AL_D001_00_20250804EMD-0q23o5"
      : "AL_D001_00_20250804SIG-d0wsd0";

  // Fill color 계산
  const getFillColor = useCallback((): Expression | string => {
    if (!highlightedRegions || highlightedRegions.length === 0) {
      return "#52525b";
    }
    return [
      "match",
      ["get", "A1"],
      highlightedRegions,
      "#60a5fa", // 하이라이트
      "#52525b", // 기본
    ] as Expression;
  }, [highlightedRegions]);

  return (
    <Box
      w="85%"
      h="85vh"
      borderWidth="3px"
      borderColor="border.inverted"
      borderRadius="md"
    >
      <Map
        ref={mapRef}
        {...viewport}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100%" }}
        onMove={(evt) => onViewportChange(evt.viewState)}
        onClick={(e) => {
          if (fixedHoverInfo) {
            setFixedHoverInfo(null);
          } else {
            const feats = e.features as MapGeoJSONFeature[] | undefined;
            const hovered = feats?.find((f) => f.layer.id === "sigungu");
            if (hovered) {
              setFixedHoverInfo({
                feature: hovered,
                lngLat: e.lngLat.toArray() as [number, number],
              });
            }
          }
        }}
        onMouseMove={(e) => {
          if (fixedHoverInfo) return;
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
        <Source id="sigungu-source" type="vector" url={`mapbox://${MAP_ID}`}>
          <Layer
            id="sigungu"
            type="fill"
            source="sigungu-source"
            source-layer={SOURCE_LAYER}
            paint={{
              "fill-color": getFillColor(),
              "fill-opacity": 0.8,
              "fill-outline-color": "#000",
            }}
          />
        </Source>
      </Map>
    </Box>
  );
}
