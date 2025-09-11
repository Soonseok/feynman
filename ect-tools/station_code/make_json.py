import pandas as pd

txt_file = "법정동코드 전체자료.txt"
json_file = "station_code_full.json"

# cp949로 읽기
df = pd.read_csv(txt_file, sep="\t", encoding="utf-8", dtype=str)

# 앞 8자리만 station_code로, 법정동명 그대로 station_name
df["station_code"] = df["법정동코드"].str[:8]
df["station_name"] = df["법정동명"]

# 필요한 컬럼만
df = df[["station_code", "station_name"]]

# JSON 저장
df.to_json(json_file, orient="records", force_ascii=False, indent=2)
