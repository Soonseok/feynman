import json
import csv

# 파일 경로
unmatched_file = "station_unmatched.txt"
json_file = "msrstn_list.json"
output_file = "station_unmatched_data.csv"

# 1. 매칭 실패한 station 불러오기
with open(unmatched_file, "r", encoding="utf-8") as f:
    unmatched_stations = [line.strip() for line in f if line.strip()]

# 2. JSON 파일 불러오기
with open(json_file, "r", encoding="utf-8") as f:
    data = json.load(f)

items = data["response"]["body"]["items"]

# 3. 매칭된 결과 저장할 리스트
results = []

# 4. unmatched_stations 각각에 대해 JSON에서 검색
for station in unmatched_stations:
    match = next((item for item in items if item["stationName"] == station), None)
    if match:
        results.append({
            "stationName": station,
            "addr": match.get("addr", ""),
            "dmX": match.get("dmX", ""),
            "dmY": match.get("dmY", "")
        })
    else:
        results.append({
            "stationName": station,
            "addr": "NOT FOUND",
            "dmX": "",
            "dmY": ""
        })

# 5. CSV 저장
with open(output_file, "w", newline="", encoding="utf-8-sig") as f:
    writer = csv.DictWriter(f, fieldnames=["stationName", "addr", "dmX", "dmY"])
    writer.writeheader()
    writer.writerows(results)

print(f"총 {len(results)}개 결과 저장 완료 → {output_file}")
