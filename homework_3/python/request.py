import sys

import requests


url = "http://localhost:8082/command"
params = {"commandType": "ALERT", "initiator": "WAYLAND_YUTANI_OFFICER"}

try:
  response = requests.post(url, params=params)
  response.raise_for_status()

  print("Request successful! Response:", response.text)

except requests.exceptions.RequestException as e:
  print("Request failed:", str(e))
  sys.exit(1)
