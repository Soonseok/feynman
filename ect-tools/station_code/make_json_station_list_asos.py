import csv
import json

input_file = "stations.txt"     # 탭 구분 텍스트 파일
output_file = "asos_stations.json"   # 변환 후 저장할 파일

data = []
with open(input_file, "r", encoding="utf-8") as f:
    reader = csv.reader(f, delimiter="\t")
    headers = next(reader)  # 첫 줄(헤더) 건너뜀
    for row in reader:
        # 빈 칸(열이 3개 미만인 경우) 무시
        if len(row) >= 3 and row[0].strip():
            data.append({"stationId": int(row[0]), "stationName": row[1], "managingOffice": row[2]})
        if len(row) >= 6 and row[3].strip():
            data.append({"stationId": int(row[3]), "stationName": row[4], "managingOffice": row[5]})

with open(output_file, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=2)
