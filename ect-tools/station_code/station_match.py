import csv
import re

# station_name.txt 읽기
with open("station_name.txt", encoding="utf-8") as f:
    station_names = [line.strip() for line in f if line.strip()]

# station_code.csv 읽기
lowest_names = {}
with open("station_code.csv", encoding="utf-8") as f:
    reader = csv.DictReader(f)
    for row in reader:
        lowest_names[row["lowest_name"]] = row["station_code"]

def normalize_name(name: str) -> str:
    # 괄호 제거, 공백 정리
    return re.sub(r"\(.*?\)", "", name).strip()

matches = {}
unmatched = []

for sname in station_names:
    norm = normalize_name(sname)

    code = None
    if norm in lowest_names:
        code = lowest_names[norm]
    elif norm + "구" in lowest_names:
        code = lowest_names[norm + "구"]
    elif norm + "시" in lowest_names:
        code = lowest_names[norm + "시"]
    elif norm + "군" in lowest_names:
        code = lowest_names[norm + "군"]

    if code:
        matches[sname] = code   # station_name은 원본 그대로 둔다
    else:
        unmatched.append(sname)

# 결과 저장
with open("station_matches.csv", "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["station_name", "station_code"])
    for k, v in matches.items():
        writer.writerow([k, v])

with open("station_unmatched.txt", "w", encoding="utf-8") as f:
    for name in unmatched:
        f.write(name + "\n")

print("매칭 성공:", len(matches))
print("매칭 실패:", len(unmatched))
