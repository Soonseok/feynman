// GeoMapViewer.tsx
import { useEffect, useRef, useState, useCallback } from "react";
import { Map, View } from "ol";
import { bbox } from 'ol/loadingstrategy';
import Feature from 'ol/Feature';
import TileLayer from "ol/layer/Tile";
import TileWMS from "ol/source/TileWMS";
import "ol/ol.css";

// 벡터 레이어 관련 import
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";
import { Style, Fill, Stroke } from "ol/style";

// proj4 정의
import proj4 from "proj4";
import { register } from "ol/proj/proj4";
import type { Geometry } from "ol/geom";

proj4.defs(
  "EPSG:5186",
  "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"
);
register(proj4);

interface GeoMapViewerProps {
  mapType: "ne:sigungu" | "ne:umd" | "ne:sido";
}

// 행정구역에 적용할 기본 스타일 함수 정의(테스트)
const defaultVectorStyle = new Style({
  fill: new Fill({
    color: "rgba(255, 165, 0, 0.4)", // 테스트 색상: 주황색
  }),
  stroke: new Stroke({
    color: "rgba(50, 50, 50, 1.0)",
    width: 1.5,
  }),
});

export default function GeoMapViewer({ mapType }: GeoMapViewerProps) {
  const olMapRef = useRef<Map | null>(null);
  const [mapElement, setMapElement] = useState<HTMLDivElement | null>(null);

  const mapRef = useCallback((node: HTMLDivElement) => {
    if (node !== null) {
      setMapElement(node);
    }
  }, []);

  useEffect(() => {
    if (olMapRef.current || !mapElement) return;

    const initialMapType = mapType;

    const view = new View({
      center: [300000, 360000],
      zoom: 1,
      projection: "EPSG:5186",
      extent: [-10000, 57000, 633000, 670000],
      resolutions: [
        2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25,
      ],
    });

    const geoserverSource = new TileWMS({
      url: "http://localhost:8080/geoserver/ne/wms",
      params: {
        LAYERS: initialMapType,
        TILED: true,
        CRS: "EPSG:5186",
        FORMAT: "image/png",
        VERSION: "1.1.0",
      },
      projection: "EPSG:5186",
      serverType: "geoserver",
    });

    const geoserverLayer = new TileLayer({
      source: geoserverSource,
    });

    const vectorSource = new VectorSource({
      format: new GeoJSON<Feature<Geometry>>({
        dataProjection: "EPSG:4326",
        featureProjection: "EPSG:5186",
      }),
      strategy: bbox, 
      url: (extent, resolution, projection) => {
            const typeName = initialMapType;
            return `http://localhost:8080/geoserver/ne/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=${typeName}&outputFormat=application/json&srsName=EPSG:5186&bbox=${extent.join(',')},${projection.getCode()}`;
        },
    });

    const vectorLayer = new VectorLayer({
      source: vectorSource,
      style: defaultVectorStyle,
    });

    const map = new Map({
      target: mapElement,
      layers: [geoserverLayer, vectorLayer],
      view: view,
    });

    olMapRef.current = map;
    map.updateSize();

    return () => {
      if (olMapRef.current) {
        olMapRef.current.setTarget(undefined);
      }
    };
  }, [mapElement]);

  useEffect(() => {
    if (!olMapRef.current) return;

    const wmsLayer = olMapRef.current.getLayers().getArray()[0] as TileLayer;
    const wmsSource = wmsLayer.getSource() as TileWMS;
    wmsSource.updateParams({ LAYERS: mapType });

    const vectorLayer = olMapRef.current
      .getLayers()
      .getArray()[1] as VectorLayer;
    const vectorSource = vectorLayer.getSource() as VectorSource;

    const newUrl = `http://localhost:8080/geoserver/ne/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=${mapType}&outputFormat=application/json&srsName=EPSG:5186`;
    vectorSource.setUrl(newUrl);
    vectorSource.refresh();

    olMapRef.current.updateSize();
  }, [mapType]);

  return (
    <div
      ref={mapRef}
      id="map-viewer-root"
      style={{
        width: "90%",
        height: "120vh",
        border: "3px solid #718096",
        borderRadius: "8px",
      }}
    ></div>
  );
}
