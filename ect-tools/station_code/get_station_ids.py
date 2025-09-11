import json

input_file = "asos_stations.json"
output_file = "station_ids.json"

with open(input_file, "r", encoding="utf-8") as f:
    data = json.load(f)

station_ids = []
for i in data:
    station_ids.append(i["stationId"])

with open("station_ids.json", "w", encoding="utf-8") as f:
    json.dump(station_ids, f, ensure_ascii=False, indent=2)