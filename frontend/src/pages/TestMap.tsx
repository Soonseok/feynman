import { Box, Button, VStack } from "@chakra-ui/react";
import Header from "../components/layout/Header";
import { Heading } from "lucide-react";
import { Link } from "react-router-dom";
import { ColorModeButton } from "../components/ui/color-mode";
import Footer from "../components/layout/Footer";
import { useEffect, useState } from "react";
import axios from "axios";
import Map, { Layer, Source } from "react-map-gl";
import type { LayerProps } from "react-map-gl";
import 'mapbox-gl/dist/mapbox-gl.css';

interface AirQualityData {
  sido: string;
  date: string;
  pm10_value: number;
}


const MAPBOX_TOKEN = "pk.eyJ1Ijoic29vbnNlb2siLCJhIjoiY21mMmJ4NDc2MmF5YzJzcXlkOHduY3VibSJ9.LCh6tUQzhgKUGOsjQhZYkQ";


export default function TestMap() {
    const [apiData, setApiData] = useState<AirQualityData[]>([]);
    const [viewport, setViewport] = useState({
        latitude: 34.7,
        longitude: 125.5,
        zoom: 7,
    });

    useEffect(() => {
        axios.get('/api/v1/arpltn-call/test')
            .then((response) => {
                setApiData(response.data.data);
            });
    }, []);

    const getColorForPm10 = (value: number) => {
        if (value <= 30) return "#42a4f4"; // 좋음
        if (value <= 80) return "#42f492"; // 보통
        if (value <= 150) return "#f4be41"; // 나쁨
        return "#f44141"; // 매우 나쁨
  };

  const apiDataMap = apiData.reduce<Record<string, number>>((acc, current) => {
    acc[current.sido] = current.pm10_value;
    return acc;
  }, {});

  const sigunguLayerStyle: LayerProps = {
  id: "sigungu",
  type: "fill",
  source: "sigungu-source",
  "source-layer": "sigungu",
  layout: {},
  paint: {
    "fill-color": [
      "match",
      ["get", "SIG_KOR_NM"],
      ...Object.keys(apiDataMap).flatMap((key) => [
        key,
        getColorForPm10(apiDataMap[key]),
      ]),
      "#ccc",
    ],
    "fill-opacity": 0.8,
    "fill-outline-color": "#fff",
  },
};
    
  return (
    <VStack justifyContent={"center"} minH="100vh">
      <Header />
      <Heading>Just for Fun</Heading>
      <Box>
      <Map
        {...viewport}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100%" }}
        onMove={(evt) => setViewport(evt.viewState)}
        mapStyle="mapbox://styles/mapbox/streets-v11"
      >
        <Source
          id="sigungu-source"
          type="vector"
          tiles={["http://localhost:8080/data/sigungu/{z}/{x}/{y}.pbf"]}
        >
          <Layer {...sigunguLayerStyle} />
        </Source>
      </Map>
      </Box>
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
