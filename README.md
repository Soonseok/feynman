# 1. 개요

이 문서는 대기 오염 정보 조회를 위한 API 엔드포인트들을 설명합니다. 각 엔드포인트는 특정 기능(실시간 데이터 조회, 검색, 통계 데이터 수집)을 수행합니다.

# 2. API 엔드포인트 목록

## 2.1. 대기 오염 데이터 조회
### 기본 URL: `/api/v1/arpltn-call`  
#### 엔드포인트: `GET /{code}`

특정 지역 코드를 기반으로 대기 오염 데이터를 조회합니다.  
경로 변수: `code` (String, 필수) - 조회할 지역 코드  
요청 예시: `GET /api/v1/arpltn-call/11110`  
응답: `200 OK` (성공), `404 Not Found` (해당 코드의 데이터 없음)  

#### 엔드포인트: `GET /have_station`  
측정소가 있는 지역 코드 목록을 조회합니다.  
요청 예시: `GET /api/v1/arpltn-call/have_station`  
응답: `200 OK` (성공), `List<String>` 형식의 지역 코드 목록 반환  

## 2.2. 대기 오염 데이터 검색
### 기본 URL: `/api/v1/arpltn-search`  
#### 엔드포인트: `POST /searchData`  
검색 조건을 사용하여 대기 오염 데이터를 검색하고 페이지별로 결과를 반환합니다.  
요청 본문: `SearchCriteriaDto` 객체 (검색 조건 포함)  
본문 구조: SearchCriteriaDto는 대기 오염 데이터 검색에 사용되는 조건을 담고 있는 객체입니다. 모든 필드는 Optional 타입으로 정의되어 있어, 필요한 검색 조건만 포함하여 요청할 수 있습니다.  
+ `startDate` (Optional<String>): 검색 시작 날짜입니다. 형식은 YYYY-MM-DD hh:mm:ss입니다.  
+ `endDate` (Optional<String>): 검색 종료 날짜입니다. 형식은 YYYY-MM-DD hh:mm:ss입니다.  
+ `stationName` (Optional<String>): 측정소 이름을 기준으로 검색합니다.  
+ `stationCode` (Optional<String>): 측정소 코드를 기준으로 검색합니다. 부분 일치를 위해 직접 요청 시 % 기호를 포함해야 합니다.  
+ `dataType` (Optional<String>): 데이터 유형을 지정합니다. (예: DAILY, HOUR)  
+ `measurementType` (Optional<List<String>>): 측정 항목을 지정합니다. (예: so2, o3, co, pm10) 값이  하나만 있더라도 반드시 리스트(배열) 형태로 요청해야 합니다.  

예시:  
```{JSON}
{
    "dataType": "DAILY",
    "startDate": "2025-09-01",
    "endDate": "2025-09-01",
    "stationCode": "11%",
    "measurementType": ["so2", "o3"]
}
```

쿼리 파라미터: `page` (int, 선택, 기본값 0) - 페이지 번호  
요청 예시: `POST /api/v1/arpltn-search/searchData?page=0`  
응답: `200 OK` (성공), 검색 결과(페이징 포함) 반환  

## 2.3. 통계 데이터 수집
### 기본 URL: `/api/v1/arpltn-stats`  
#### 엔드포인트: `GET /daily-stats`  
특정 기간 동안 측정소별 일평균 대기 오염 데이터를 수집하고 DB에 저장합니다.  
쿼리 파라미터: `msrstnName` (String, 필수), `inqBginDt` (String, 필수, YYYYMMDD), `inqEndDt` (String, 필수, YYYYMMDD)  
요청 예시: `GET /api/v1/arpltn-stats/daily-stats?msrstnName=강남구&inqBginDt=20250824&inqEndDt=20250825`  
응답: `200 OK` (성공), `400 Bad Request` (오류)  

#### 엔드포인트: `GET /monthly-stats`  
특정 기간 동안 측정소별 월평균 대기 오염 데이터를 수집하고 DB에 저장합니다.  
쿼리 파라미터: `msrstnName` (String, 필수), `inqBginMm` (String, 필수, YYYYMM), `inqEndMm` (String, 필수, YYYYMM)  
요청 예시: `GET /api/v1/arpltn-stats/monthly-stats?msrstnName=강남구&inqBginMm=202506&inqEndMm=202507`  
응답: `200 OK` (성공), `400 Bad Request` (오류)  

#### 엔드포인트: `GET /sgg-stats`  
특정 시도의 시군구별 평균 데이터를 수집하고 DB에 저장합니다.  
쿼리 파라미터: `sidoName` (String, 필수), `searchCondition` (String, 필수, `HOUR` 또는 `DAILY`)  
요청 예시: `GET /api/v1/arpltn-stats/sgg-stats?sidoName=서울&searchCondition=DAILY`  
응답: `200 OK` (성공), `400 Bad Request` (오류)  

#### 엔드포인트: `GET /sido-stats`  
특정 오염 물질과 기간 조건에 따른 시도별 평균 데이터를 수집하고 DB에 저장합니다.  
쿼리 파라미터: `itemCode` (String, 필수, `SO2`, `CO`, `O3`, `NO2`, `PM10`, `PM25`), `dataGubun` (String, 필수, `HOUR` 또는 `DAILY`), `searchCondition` (String, 필수, `WEEK` 또는 `MONTH`)  
요청 예시: `GET /api/v1/arpltn-stats/sido-stats?itemCode=SO2&dataGubun=HOUR&searchCondition=WEEK`  
응답: `200 OK` (성공), `400 Bad Request` (오류)  

## 2.4. 수동 데이터 작업
### 기본 URL: `/api/v1/manually`  
#### 엔드포인트: `GET /daily-stats`  
특정 파일을 기반으로 일평균 통계 작업을 수동으로 실행합니다. 시작 및 끝 측정소 범위를 지정할 수 있습니다.  
쿼리 파라미터: `file` (String, 필수), `startStation` (String, 선택), `endStation` (String, 선택)  
요청 예시: `GET /api/v1/manually/daily-stats?file=data.txt&startStation=강남구&endStation=송파구`  
응답: `200 OK` (성공), `500 Internal Server Error` (작업 실패)
