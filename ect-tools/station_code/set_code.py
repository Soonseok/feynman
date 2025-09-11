import csv

station_code = []

with open("법정동코드 전체자료.txt", encoding="cp949") as f, \
     open("admin_mapping.csv", "w", newline="", encoding="utf-8") as out:

    fieldnames = ["법정동코드", "법정동명", "폐지여부"]
    reader = csv.DictReader(f, delimiter="\t", fieldnames=fieldnames)
    
    writer = csv.writer(out)
    writer.writerow(["admin_code_8", "lowest_name"])

    next(reader)  # 기존 파일 헤더가 있으면 건너뜀

    for row in reader:
        if row["폐지여부"] != "존재":
            continue

        code10 = row["법정동코드"]
        code8 = code10[:8]

        # 법정동명에서 마지막 단어를 최하위 행정구역 이름으로 사용
        name_parts = row["법정동명"].split()
        lowest = name_parts[-1]

        station_code.append({"station_code": code8, "lowest_name": lowest})
        writer.writerow([code8, lowest])
