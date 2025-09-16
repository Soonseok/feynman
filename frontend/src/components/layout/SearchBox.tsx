import { Box, Flex } from "@chakra-ui/react";
import SearchForm from "../airqualityComponents/SearchForm";
import { useState } from "react";
import SearchedList from "../airqualityComponents/SearchedList";
import type { SearchCriteria, SearchResponse } from "../../types";
import axios from "axios";
import { toaster } from "../ui/toaster";

export default function SearchBox() {
  const [searchedResult, setSearchResult] = useState<SearchResponse | null>(
    null
  );
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isAppending, setIsAppending] = useState<boolean>(false);
  const [page, setPage] = useState<number>(0);
  const [lastSearchCriteria, setLastSearchCriteria] = useState<unknown | null>(
    null
  );

  const fetchData = async (criteria: SearchCriteria, pageNum: number) => {
    if (pageNum === 0) {
      setIsLoading(true);
    } else {
      setIsAppending(true);
    }

    const requestBody = {
      ...criteria,
    };

    try {
      const response = await axios.post(
        "api/v1/arpltn-search/searchData",
        requestBody,
      {
        params: {
          page: pageNum,
          size: 30,
        },
      }
      );
      const newResult = response.data as SearchResponse;

      if (pageNum === 0) {
        setSearchResult(newResult);
      } else {
        setSearchResult((prev) => {
          if (!prev) return newResult;
          return {
            ...newResult,
            data: {
              arpltnResponseList: [
                ...prev.data.arpltnResponseList,
                ...newResult.data.arpltnResponseList,
              ],
            },
          };
        });
        setPage(pageNum);
      }
      setLastSearchCriteria(criteria);
      toaster.create({
        title: "검색 성공",
        description: "데이터를 성공적으로 조회했습니다.",
        type: "info",
      });
    } catch (error) {
      console.error("API ERROR: ", error);
      setSearchResult(null);
      toaster.create({
        title: "검색 실패",
        description: "데이터 조회 중 오류가 발생했습니다.",
        type: "error",
      });
    } finally {
      setIsLoading(false);
      setIsAppending(false);
    }
  };

  const loadMore = () => {
    if (lastSearchCriteria) {
      fetchData(lastSearchCriteria, page + 1);
    }
  };

  return (
    <Flex
      direction={{ base: "column", md: "row" }}
      gap={3}
      p={3}
      alignItems={{ base: "stretch", md: "flex-start" }}
      wrap="wrap"
      width={"85%"}
      margin="auto"
    >
      <Box flex="none" minW={{ base: "full", md: "40%" }}>
        <SearchForm onSearch={(criteria) => fetchData(criteria, 0)} isLoading={isLoading} />
      </Box>

      <Box flex="1" minW={{ base: "full", md: "40%" }}>
        <SearchedList
          searchedResult={searchedResult}
          isLoading={isLoading}
          isAppending={isAppending}
          onLoadMore={loadMore}
        />
      </Box>
    </Flex>
  );
}
