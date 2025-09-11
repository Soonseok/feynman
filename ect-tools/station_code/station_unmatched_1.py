import pandas as pd

# 파일 경로
unmatched_file = "station_unmatched_data.csv"
dongcode_file = "법정동코드 전체자료.txt"
output_file = "station_unmatched_1.csv"

# 1) 법정동코드 전체자료 읽기 (CP949)
dong_df = pd.read_csv(
    dongcode_file,
    sep="\t",
    names=["station_code", "lowest_name", "existence"],
    dtype=str,
    encoding="cp949"
)

# 앞 8자리만 사용
dong_df["station_code_8"] = dong_df["station_code"].str[:8]

# 2) station_unmatched_data 읽기
station_df = pd.read_csv(unmatched_file, dtype=str, encoding="utf-8-sig")

# 3) addr를 이용해서 매칭
results = []

for idx, row in station_df.iterrows():
    addr = row.get("addr", "")
    station_name = row.get("stationName", "")
    # 법정동 이름이 addr에 포함되어 있으면 매칭
    matched = dong_df[dong_df["lowest_name"].apply(lambda x: x in addr)]
    if not matched.empty:
        # 여러 개가 매칭될 경우, 가장 긴 이름 우선
        best_match = matched.sort_values("lowest_name", key=lambda s: s.str.len(), ascending=False).iloc[0]
        results.append({
            "station_code": best_match["station_code_8"],
            "stationName": station_name
        })
    else:
        # 매칭 실패 시 코드 빈칸
        results.append({
            "stationName": station_name,
            "station_code": ""
        })

# 4) 결과 CSV 저장 (stationName과 station_code만)
pd.DataFrame(results).to_csv(output_file, index=False, encoding="utf-8-sig")
print(f"매칭 완료. 결과: {output_file}")
