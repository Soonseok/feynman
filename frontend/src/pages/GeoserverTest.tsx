// GeoserverTest.tsx (최종 완성 코드)
import React, { useEffect } from 'react';
import { Map, View } from 'ol';
import TileLayer from 'ol/layer/Tile';
import TileWMS from 'ol/source/TileWMS';
import 'ol/ol.css';

// 1. proj4 라이브러리 및 좌표계 정의 추가
import proj4 from 'proj4';
import { register } from 'ol/proj/proj4';

proj4.defs(
  'EPSG:5186',
  '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'
);
register(proj4);

export default function GeoserverTest() {
  useEffect(() => {
    const mapContainer = document.getElementById('map');
    if (!mapContainer) return;

    const geoserverSource = new TileWMS({
      url: 'http://localhost:8080/geoserver/ne/wms',
      params: {
        'LAYERS': 'ne:sigungu',
        'TILED': true,
        'CRS': 'EPSG:5186'
      },
      projection: 'EPSG:5186',
      serverType: 'geoserver',
    });

    const geoserverLayer = new TileLayer({
      source: geoserverSource,
    });
    
    const view = new View({
      center: [953000, 1950000],
      zoom: 9,
      projection: 'EPSG:5186',
      extent: [740000, 1400000, 1200000, 2500000],
      resolutions: [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25],
    });

    const map = new Map({
      target: 'map',
      layers: [geoserverLayer],
      view: view,
    });

    return () => {
      map.setTarget(undefined);
    };
  }, []);

  return (
    <>
      <span>Geoserver Map Test</span>
      <div id="map" style={{ width: '100%', height: '800px' }}></div>
    </>
  );
}