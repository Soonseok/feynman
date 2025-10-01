//GeoMapViewer.tsx
import { useCallback, useEffect, useRef, useState } from "react";
import { Map, View } from "ol";
import TileLayer from "ol/layer/Tile";
import TileWMS from "ol/source/TileWMS";
import "ol/ol.css";

import proj4 from "proj4";
import { register } from "ol/proj/proj4";

proj4.defs(
  "EPSG:5186",
  "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs"
);
register(proj4);

interface GeoMapViewerProps {
  mapType: "ne:sigungu" | "ne:umd" | "ne:sido";
}

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

      const map = new Map({
         target: mapElement,
         layers: [geoserverLayer],
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
    
      const tileLayer = olMapRef.current.getLayers().getArray()[0] as TileLayer;
      const source = tileLayer.getSource() as TileWMS;
    
      source.updateParams({ 
         LAYERS: mapType 
      });
    
      olMapRef.current.updateSize(); 
    
   }, [mapType]);
   
   return (
     <div 
     ref={mapRef}
     id="map-viewer-root"
     style={{ width: "85%", height: "120vh", border: "3px solid #718096", borderRadius: "8px" }}
     >
    </div>
  );
}