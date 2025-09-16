import {
  Badge,
  Box,
  Center,
  Code,
  Grid,
  Heading,
  Spacer,
  Spinner,
  Text,
  VStack,
} from "@chakra-ui/react";
import type { ArpltnResponse, SearchResponse } from "../../types";
import EmptyList from "../ui/empty-list";

interface SearchBoxProps {
  searchedResult: SearchResponse | null;
  isLoading: boolean;
}

export default function SearchedList({
  searchedResult,
  isLoading,
}: SearchBoxProps) {
  const hasResults = searchedResult && searchedResult.totalCount > 0;
  return (
    <Box
      p={8}
      maxW="45vw"
      borderWidth={1}
      borderRadius="lg"
      overflow="hidden"
      boxShadow="lg"
      margin={3}
    >
      <VStack gap={6}>
        <Heading as="h2" size="xl">
          검색 결과
        </Heading>
        {/* Case 1: 로딩 중 */}
        {isLoading ? (
          <Center py={10}>
            <VStack gap={4}>
              <Spinner
                borderWidth="4px"
                animationDuration="1s"
                color="blue.500"
                size="xl"
              />
              <Text>데이터를 불러오는 중입니다...</Text>
            </VStack>
          </Center>
        ) : (
          // Case 2 & 3: 로딩 완료
          <>
            {hasResults ? (
              // Case 3: 데이터가 있을 때
              <VStack align="flex-start" gap={2}>
                <Text>
                  총 검색 데이터 수: <Code>{searchedResult.dataSize}</Code> 행
                </Text>
                <Text>
                  API 상태: <Code>{searchedResult.status}</Code>
                </Text>
                <Spacer />
                <Grid
                  gap={3}
                  templateColumns={{ base: "1fr", md: "repeat(2, 1fr)" }}
                >
                  {searchedResult.data?.arpltnResponseList.map(
                    (item: ArpltnResponse, index: number) => (
                      <Box
                        key={index}
                        p={4}
                        borderWidth="1px"
                        borderRadius="md"
                        w="full"
                        shadow="sm"
                        position={"relative"}
                      >
                        {/* 번호 Badge 추가 */}
                        <Badge
                          position="absolute"
                          top="1"
                          right="1"
                          colorScheme="blue"
                          borderRadius="full"
                          px="2"
                          py="1"
                          fontSize="0.8em"
                        >
                          {index + 1}
                        </Badge>
                        <VStack align="stretch" gap={2}>
                          {/* Grid 컴포넌트로 각 데이터 항목을 두 열로 분리 */}
                          <Grid
                            templateColumns="repeat(2, 1fr)"
                            gap={1}
                            alignItems="center"
                          >
                            <Text fontWeight="bold">측정소</Text>
                            <Text>{item.stationName}</Text>

                            <Text fontWeight="bold">측정일</Text>
                            <Text>{item.airQualityData.date}</Text>

                            <Text fontWeight="bold">PM10 (㎍/㎥)</Text>
                            <Text>{item.airQualityData.pm10_value}</Text>

                            <Text fontWeight="bold">PM2.5 (㎍/㎥)</Text>
                            <Text>{item.airQualityData.pm25_value}</Text>

                            <Text fontWeight="bold">SO₂ (ppm)</Text>
                            <Text>{item.airQualityData.so2_value}</Text>

                            <Text fontWeight="bold">O₃ (ppm)</Text>
                            <Text>{item.airQualityData.o3_value}</Text>

                            <Text fontWeight="bold">NO₂ (ppm)</Text>
                            <Text>{item.airQualityData.no2_value}</Text>

                            <Text fontWeight="bold">CO (ppm)</Text>
                            <Text>{item.airQualityData.co_value}</Text>

                            <Text fontWeight="bold">χ</Text>
                            <Text>
                              {item.airQualityData.khai_value !== null
                                ? item.airQualityData.khai_value
                                : "N/A"}
                            </Text>
                          </Grid>
                        </VStack>
                      </Box>
                    )
                  )}
                </Grid>
              </VStack>
            ) : (
              // Case 2: 데이터가 없을 때
              <EmptyList />
            )}
          </>
        )}
      </VStack>
    </Box>
  );
}
