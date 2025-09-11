import json
import pymysql

# DB 접속 설정 (본인 환경에 맞게 수정)
conn = pymysql.connect(
    host="144.24.71.50",      # DB 주소
    user="einstein",           # DB 사용자
    password="1q2w3e4r1!",    # DB 비밀번호
    database="maxwell",    # DB 이름
    charset="utf8mb4"
)

cursor = conn.cursor()


# JSON 파일 불러오기
with open("station_code_full.json", "r", encoding="utf-8") as f:
    data = json.load(f)

# 데이터 삽입
for record in data:
    station_code = str(record["station_code"]).zfill(8)  # 혹시 숫자가 0으로 시작 안 하면 맞춰줌
    station_name = record["station_name"]
    try:
        cursor.execute(
            "INSERT IGNORE INTO administrative_district (district_code, district_name) VALUES (%s, %s)",
            (station_code, station_name)
        )
    except Exception as e:
        print("삽입 오류:", record, e)

# 커밋 후 연결 종료
conn.commit()
cursor.close()
conn.close()

print("SON 데이터 DB 삽입 완료")
