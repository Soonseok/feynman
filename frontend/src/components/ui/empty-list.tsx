import { EmptyState, Image, List, VStack } from "@chakra-ui/react";
import { useColorModeValue } from "./color-mode";

const EmptyList = () => {
  const currentColorMode = useColorModeValue("invert(0)", "invert(1)");
  return (
    <EmptyState.Root>
      <EmptyState.Content>
        <EmptyState.Indicator>
          {/* <HiColorSwatch /> */}
          <Image
            src="images/empty.svg"
            w='25%'
            opacity={0.6}
            style={{ filter: currentColorMode }}
          />
        </EmptyState.Indicator>
        <VStack textAlign="center">
          <EmptyState.Title>No results found</EmptyState.Title>
          <EmptyState.Description>검색 결과가 없습니다.</EmptyState.Description>
        </VStack>
        <List.Root variant="marker">
          <List.Item>Try removing filters</List.Item>
          <List.Item>Try different keywords</List.Item>
        </List.Root>
      </EmptyState.Content>
    </EmptyState.Root>
  );
};

export default EmptyList;
