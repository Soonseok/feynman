import { Box, Button, Checkbox, CheckboxGroup, Field, Fieldset, Heading, HStack, Input, RadioGroup, Stack, VStack } from '@chakra-ui/react';
import DatePicker from 'react-datepicker';
import { useForm, Controller } from 'react-hook-form';
import { toaster } from '../ui/toaster';
import axios from 'axios';
import 'react-datepicker/dist/react-datepicker.css';

interface SearchFormData {
  startDate: Date | null;
  endDate: Date | null;
  stationName: string;
  stationCode: string;
  dataType: string;
  measurementType: string[];
}

const measurementTypes = ['pm10', 'so2', 'o3', 'co', 'no2', 'pm25', 'khai'];
const dataTypes = ['', 'DAILY', 'HOURLY'];

const SearchForm = () => {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<SearchFormData>({
    defaultValues: {
      startDate: null,
      endDate: null,
      stationName: '',
      stationCode: '',
      dataType: '',
      measurementType: [],
    },
  });

  const onSubmit = async (data: SearchFormData) => {
    try {
      const requestData = {
        startDate: data.startDate?.toISOString().slice(0, 10),
        endDate: data.endDate?.toISOString().slice(0, 10),
        stationName: data.stationName || undefined, 
        stationCode: data.stationCode ? `${data.stationCode}%` : undefined, 
        dataType: data.dataType || undefined,
        measurementType: data.measurementType,
      };
      console.log(requestData); // 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그 디버그

      if (requestData.measurementType && !Array.isArray(requestData.measurementType)) {
        requestData.measurementType = [requestData.measurementType];
      }

      const apiUrl = "/api/v1/arpltn-search/searchData";

      const response = await axios.post(apiUrl, requestData);

      console.log('API Response:', response.data);

      toaster.create({
        title: "검색 성공",
        description: "데이터를 성공적으로 조회했습니다.",
        type: "info",
      });

    } catch (error) {
      console.error('API Error:', error);

      toaster.create({
        title: "검색 실패",
        description: "데이터 조회 중 오류가 발생했습니다.",
        type: "error",
      });
    }
  };

  const invalidStartDate = !!errors.startDate;
  const invalidEndDate = !!errors.endDate;
  const invalidDataType = !!errors.dataType;
  const invalidMeasurementType = !!errors.measurementType;

  return (
    <Box p={8} maxW="md" borderWidth={1} borderRadius="lg" overflow="hidden" boxShadow="lg" margin={3}>
      <VStack gap={6}>
        <Heading as="h2" size="xl">
          대기오염 데이터 검색
        </Heading>
        <form onSubmit={handleSubmit(onSubmit)} style={{ width: '100%' }}>
          <VStack gap={4}>
            {/* 날짜/시간 선택 */}
            <Field.Root invalid={invalidStartDate}>
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
              {errors.startDate && <Field.ErrorText>{errors.startDate.message}</Field.ErrorText>}
            </Field.Root>
            <Field.Root invalid={invalidEndDate}>
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
              {errors.endDate && <Field.ErrorText>{errors.endDate.message}</Field.ErrorText>}
            </Field.Root>

            {/* 측정소 정보 입력 */}
            <Field.Root>
              <Field.Label>측정소 이름</Field.Label>
              <Controller
                name="stationName"
                control={control}
                render={({ field }) => <Input {...field} placeholder="예: 강남구" />}
              />
            </Field.Root>
            <Field.Root>
              <Field.Label>측정소 코드</Field.Label>
              <Controller
                name="stationCode"
                control={control}
                render={({ field }) => <Input {...field} placeholder="예: 11111100" />}
              />
            </Field.Root>

            {/* 데이터 타입 선택 */}
            <Fieldset.Root>
              <Fieldset.Legend>데이터 타입</Fieldset.Legend>
              <Controller
                name="dataType"
                control={control}
                render={({ field }) => (
                  <RadioGroup.Root defaultValue={field.value} onValueChange={(e) => field.onChange(e.value)}  >
                    <HStack gap="6">
                      {dataTypes.map((type) => (
                        <RadioGroup.Item key={type || 'none'} value={type}>
                          <RadioGroup.ItemHiddenInput />
                          <RadioGroup.ItemIndicator />
                          <RadioGroup.ItemText>{type === '' ? '선택 없음' : type}</RadioGroup.ItemText>
                        </RadioGroup.Item>
                      ))}
                    </HStack>
                  </RadioGroup.Root>
                )}
              />
            </Fieldset.Root>

            {/* 측정 유형 선택 */}
            <Fieldset.Root invalid={invalidMeasurementType}>
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
                            <Checkbox.Label>{type.toUpperCase()}</Checkbox.Label>
                          </Checkbox.Root>
                        ))}
                      </Stack>
                    </Fieldset.Content>
                  </CheckboxGroup>
                )}
              />
              {errors.measurementType && <Field.ErrorText>{errors.measurementType.message}</Field.ErrorText>}
            </Fieldset.Root>

            <Button mt={4} colorScheme="blue" type="submit">
              검색
            </Button>
          </VStack>
        </form>
      </VStack>
    </Box>
  );
};

export default SearchForm;