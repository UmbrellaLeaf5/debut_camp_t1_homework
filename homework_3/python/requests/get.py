import json
from pathlib import Path

import requests

from ..verbose_logger import LogMode, VerboseLogger, loguru


LOG_DIR = "./requests"
Path(LOG_DIR).mkdir(parents=True, exist_ok=True)

EXCEPTION_FILE = Path(LOG_DIR) / "get.e"
EXCEPTION_FILE.touch()

OUTPUT_FILE = Path(LOG_DIR) / "get.o"
OUTPUT_FILE.touch()

LOG_DIR = "./requests/get"
Path(LOG_DIR).mkdir(parents=True, exist_ok=True)

v_logger = VerboseLogger(
  logger=loguru.logger,
  log_mode=LogMode.RETICENTLY,
  message_ljust=75,
  exceptions_file=str(EXCEPTION_FILE),
  standard_output=OUTPUT_FILE.open("w", encoding="utf-8"),
)

v_logger.UpdateFormat("GET_REQUEST", "fg #64B78C")

URL = "http://localhost:8082"
ENDPOINTS = ["/actuator", "/actuator/health", "/actuator/info", "/actuator/metrics"]


def SaveResponseToJSON(url: str, filename: str):
  try:
    response = requests.get(url)
    response.raise_for_status()

    v_logger.info("Request successful! Response:")

    v_logger.info(f"Status: {response.status_code}")
    v_logger.info(f"URL: {response.url}")

    try:
      json_data = response.json()
      v_logger.info("JSON response achieved!")

      with open(filename, "w") as f:
        json.dump(json_data, f, indent=2)

      v_logger.info(f"JSON response saved successfully: {filename}")

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


for endpoint in ENDPOINTS:
  full_url = URL + endpoint

  filename = str(Path(LOG_DIR) / (endpoint.replace("/", "_")[1:] + ".json"))

  SaveResponseToJSON(full_url, filename)
  v_logger.info("-")
