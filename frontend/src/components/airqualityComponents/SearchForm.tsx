import {
  Box,
  Button,
  Checkbox,
  CheckboxGroup,
  Field,
  Fieldset,
  Heading,
  HStack,
  Input,
  RadioGroup,
  Stack,
  VStack,
} from "@chakra-ui/react";
import DatePicker from "react-datepicker";
import { useForm, Controller } from "react-hook-form";
import "react-datepicker/dist/react-datepicker.css";
import type { SearchCriteria } from "../../types";

interface SearchFormData {
  startDate: Date | null;
  endDate: Date | null;
  stationName: string;
  stationCode: string;
  dataType: string;
  measurementType: string[];
}

interface SearchFormProps {
  onSearch: (criteria: SearchCriteria) => void;
  isLoading: boolean;
}

const measurementTypes = ["pm10", "so2", "o3", "co", "no2", "pm25", "khai"];
const dataTypes = ["", "DAILY", "HOURLY", "MONTHLY"];

const SearchForm: React.FC<SearchFormProps> = ({ onSearch, isLoading }) => {
  const { control, handleSubmit } = useForm<SearchFormData>({
    defaultValues: {
      startDate: null,
      endDate: null,
      stationName: "",
      stationCode: "",
      dataType: "",
      measurementType: [],
    },
  });

  const onSubmit = (data: SearchFormData) => {
    const criteria = {
      startDate: data.startDate?.toISOString().slice(0, 10),
      endDate: data.endDate?.toISOString().slice(0, 10),
      stationName: data.stationName || undefined,
      stationCode: data.stationCode ? `${data.stationCode}%` : undefined,
      dataType: data.dataType || undefined,
      measurementType: data.measurementType,
    };
    onSearch(criteria);
  };

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
          대기오염 데이터 검색
        </Heading>
        <form onSubmit={handleSubmit(onSubmit)} style={{ width: "100%" }}>
          <VStack gap={4}>
            {/* 날짜/시간 선택 */}
            <Field.Root>
              <Field.Label>시작 날짜</Field.Label>
              <Controller
                control={control}
                name="startDate"
                render={({ field }) => (
                  <DatePicker
                    selected={field.value}
                    onChange={(date) => field.onChange(date)}
                    dateFormat="yyyy-MM-dd HH:mm:ss"
                    showTimeInput
                    customInput={<Input />}
                  />
                )}
              />
            </Field.Root>
            <Field.Root>
              <Field.Label>종료 날짜</Field.Label>
              <Controller
                control={control}
                name="endDate"
                render={({ field }) => (
                  <DatePicker
                    selected={field.value}
                    onChange={(date) => field.onChange(date)}
                    dateFormat="yyyy-MM-dd HH:mm:ss"
                    showTimeInput
                    customInput={<Input />}
                  />
                )}
              />
            </Field.Root>

            {/* 측정소 정보 입력 */}
            <Field.Root>
              <Field.Label>측정소 이름</Field.Label>
              <Controller
                name="stationName"
                control={control}
                render={({ field }) => (
                  <Input {...field} placeholder="예: 강남구" />
                )}
              />
            </Field.Root>
            <Field.Root>
              <Field.Label>측정소 코드</Field.Label>
              <Controller
                name="stationCode"
                control={control}
                render={({ field }) => (
                  <Input {...field} placeholder="예: 11111100" />
                )}
              />
            </Field.Root>

            {/* 데이터 타입 선택 */}
            <Fieldset.Root>
              <Fieldset.Legend>데이터 타입</Fieldset.Legend>
              <Controller
                name="dataType"
                control={control}
                render={({ field }) => (
                  <RadioGroup.Root
                    defaultValue={field.value}
                    onValueChange={(e) => field.onChange(e.value)}
                  >
                    <HStack gap="6">
                      {dataTypes.map((type) => (
                        <RadioGroup.Item key={type || "none"} value={type}>
                          <RadioGroup.ItemHiddenInput />
                          <RadioGroup.ItemIndicator />
                          <RadioGroup.ItemText>
                            {type === "" ? "선택 없음" : type}
                          </RadioGroup.ItemText>
                        </RadioGroup.Item>
                      ))}
                    </HStack>
                  </RadioGroup.Root>
                )}
              />
            </Fieldset.Root>

            {/* 측정 유형 선택 */}
            <Fieldset.Root>
              <Fieldset.Legend>측정 유형</Fieldset.Legend>
              <Controller
                name="measurementType"
                control={control}
                render={({ field }) => (
                  <CheckboxGroup
                    value={field.value}
                    onValueChange={field.onChange}
                    name={field.name}
                  >
                    <Fieldset.Content>
                      <Stack direction="row" wrap="wrap">
                        {measurementTypes.map((type) => (
                          <Checkbox.Root key={type} value={type}>
                            <Checkbox.HiddenInput />
                            <Checkbox.Control />
                            <Checkbox.Label>
                              {type.toUpperCase()}
                            </Checkbox.Label>
                          </Checkbox.Root>
                        ))}
                      </Stack>
                    </Fieldset.Content>
                  </CheckboxGroup>
                )}
              />
            </Fieldset.Root>

            <Button
              mt={4}
              colorScheme="blue"
              type="submit"
              onClick={handleSubmit(onSubmit)}
              loading={isLoading}
            >
              {isLoading ? "검색 중..." : "검색"}
            </Button>
          </VStack>
        </form>
      </VStack>
    </Box>
  );
};

export default SearchForm;