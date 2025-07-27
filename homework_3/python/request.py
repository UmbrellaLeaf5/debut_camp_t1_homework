import json
import sys

import requests


url = "http://localhost:8082/command"
params = {
  "commandType": "ENGINE",
  "initiator": "WAYLAND_YUTANI_OFFICER",
  "priority": "COMMON",
  "author": "System",
  "time": "2025-07-27T14:30:00Z",
}

try:
  response = requests.post(url, params=params)
  response.raise_for_status()

  print("Request successful! Response:")

  print(f"\tStatus: {response.status_code}")
  print(f"\tURL: {response.url}")

  try:
    json_data = response.json()
    print("\tJSON:")
    print(json.dumps(json_data, indent=2))

  except ValueError:
    print(f"\tNon-JSON Response: {response.text}")

except requests.exceptions.RequestException as e:
  print("\nRequest failed:")
  print(f"Error: {e}")

  if hasattr(e, "response") and e.response is not None:
    print(f"Status Code: {e.response.status_code}")
    print(f"Response Text: {e.response.text}")

  sys.exit(1)
