import sys

import requests



url = "http://localhost:8082/command"
params = {
  "commandType": "ENGINE",
  "initiator": "WAYLAND_YUTANI_OFFICER",
  "priority": "COMMON",
  "author": "System",
  "time": "2025-07-27T14:30:00",
}

try:
  response = requests.post(url, params=params)
  response.raise_for_status()

  print(
    "Request successful!\nResponse:\n\t"
    f"Status:{response.status_code}\n\t"
    f"Text:{response.text}\n\t"
    f"URL:{response.url}\n\t"
    f"JSON:{response.json()}\n\t"
  )

except requests.exceptions.RequestException as e:
  print(f"Request failed:\n\t{e}")
  sys.exit(1)
