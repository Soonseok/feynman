import { Box, Flex } from "@chakra-ui/react";
import SearchForm from "../airqualityComponents/SearchForm";
import { useState } from "react";
import SearchedList from "../airqualityComponents/SearchedList";
import type { SearchResponse } from "../../types";

export default function SearchBox() {
  const [searchedResult, setSearchResult] = useState<SearchResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  return (
    <Flex
      direction={{ base: 'column', md: 'row' }}
      gap={3}
      p={3}
      alignItems={{ base: 'stretch', md: 'flex-start' }}
      wrap="wrap"
      width={'85%'}
    >
      <Box flex="none" minW={{ base: "full", md: "40%" }}>
        <SearchForm
          setSearchedResult={setSearchResult}
          setIsLoading={setIsLoading}
        />
      </Box>

      <Box flex="1" minW={{ base: "full", md: "40%" }}>
        <SearchedList searchedResult={searchedResult} isLoading={isLoading} />
      </Box>
    </Flex>
  );
}