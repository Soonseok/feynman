import { RadioGroup, HStack } from "@chakra-ui/react";
import type { MapType } from "../../types";

interface Props {
  mapType: string;
  setMapType: (value: MapType) => void;
}

export default function MapChooseRadioBtn({ mapType, setMapType }: Props) {
  const items: { label: string; value: MapType }[] = [
    { label: "시군구 지도", value: "sgg" },
    { label: "읍면동 지도", value: "umd" },
    { label: "광역자치단체 지도", value: "sid" },
  ];

  return (
    <RadioGroup.Root
      value={mapType}
      onValueChange={(e) => setMapType(e.value as MapType)}
    >
      <HStack gap={6}>
        {items.map((item) => (
          <RadioGroup.Item key={item.value} value={item.value}>
            <RadioGroup.ItemHiddenInput />
            <RadioGroup.ItemIndicator />
            <RadioGroup.ItemText>{item.label}</RadioGroup.ItemText>
          </RadioGroup.Item>
        ))}
      </HStack>
    </RadioGroup.Root>
  );
}
