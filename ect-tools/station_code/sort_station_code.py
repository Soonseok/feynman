import pandas as pd

df = pd.read_csv("station_code_clean.csv", encoding="utf-8")
df = df[['station_code', 'station_name']].sort_values(by='station_code')
df.to_json("station_code_clean.json", orient="records", force_ascii=False)
