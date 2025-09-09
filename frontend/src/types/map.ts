import type { MapboxGeoJSONFeature } from 'mapbox-gl';

export interface HoverInfo {
  feature: MapboxGeoJSONFeature;
  lngLat: [number, number];
}