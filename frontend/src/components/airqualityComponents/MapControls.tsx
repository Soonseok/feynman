import { Box, RadioGroup, Stack, Text } from "@chakra-ui/react";

interface Props {
  mapType: string;
  setMapType: (val: string) => void;
  zoom: number;
}

export default function MapControls({ mapType, setMapType, zoom }: Props) {
  return (
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
        현재 줌: {zoom.toFixed(2)}
      </Text>
      <Text color="fg">가능한 줌 범위: 9 ~ 15</Text>

      <RadioGroup value={mapType} onChange={setMapType}>
        <Stack direction="row">
          <Radio value="umd">읍면동</Radio>
          <Radio value="sgg">시군구</Radio>
        </Stack>
      </RadioGroup>
    </Box>
  );
}
