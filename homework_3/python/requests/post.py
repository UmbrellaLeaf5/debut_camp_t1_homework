import json
from pathlib import Path

import requests

from ..verbose_logger import LogMode, VerboseLogger, loguru


LOG_DIR = "./requests"
Path(LOG_DIR).mkdir(parents=True, exist_ok=True)

EXCEPTION_FILE = Path(LOG_DIR) / "post.e"
EXCEPTION_FILE.touch()

OUTPUT_FILE = Path(LOG_DIR) / "post.o"
OUTPUT_FILE.touch()


v_logger = VerboseLogger(
  logger=loguru.logger,
  log_mode=LogMode.RETICENTLY,
  message_ljust=75,
  exceptions_file=str(EXCEPTION_FILE),
  standard_output=OUTPUT_FILE.open("w", encoding="utf-8"),
)

v_logger.UpdateFormat("POST_REQUEST", "fg #61B78C")

URL = "http://localhost:8082/command"
PARAMS = {
  "commandType": "ENGINE",
  "initiator": "WAYLAND_YUTANI_OFFICER",
  "priority": "COMMON",
  "author": "System",
  "time": "2025-07-27T14:30:00Z",
}


try:
  response = requests.post(URL, params=PARAMS)
  response.raise_for_status()

  v_logger.info("Request successful! Response:")

  v_logger.info(f"Status: {response.status_code}")
  v_logger.info(f"URL: {response.url}")

  try:
    json_data = response.json()
    v_logger.info("JSON:")
    v_logger.info(json.dumps(json_data, indent=2))

  except ValueError:
    v_logger.info(f"Non-JSON Response: {response.text}")

except requests.exceptions.RequestException as e:
  v_logger.error("\nRequest failed:")
  v_logger.error(f"Error: {e}")

  if hasattr(e, "response") and e.response is not None:
    v_logger.error(f"Status Code: {e.response.status_code}")
    v_logger.error(f"Response Text: {e.response.text}")

except Exception as e:
  v_logger.error("\nRequest failed:")
  v_logger.error(f"Error: {e}")
