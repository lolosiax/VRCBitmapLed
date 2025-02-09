import re
from typing import List, Optional

element = """
  - type: 3
    name: BitmapLed/Data$index
    source: BitmapLed/DataX16
    value: 0
    valueMin: 0
    valueMax: 0
    chance: 0
    convertRange: 0
    sourceMin: 0
    sourceMax: 0
    destMin: 0
    destMax: 0
"""

text: List[str]

with open("BitmapLedParamCopyX16Animator.yaml", "r") as f:
    text = f.readlines()

drivers = dict()
translations = dict()
states = dict()
index_map = dict()

last_cache: List[str] = []
last_type = ""
last_id = ""
def split():
    global last_cache

    if last_type == "driver":
        drivers[last_id] = last_cache
    elif last_type == "tran":
        translations[last_id] = last_cache
    elif last_type == "state":
        states[last_id] = last_cache
    last_cache = []

for line in text:
    if line.startswith("---"):
        split()

        last_id = re.findall(r"-?\d+$", line)[0]
        if "!u!114" in line:
            last_type = "driver"
        elif "!u!1101" in line:
            last_type = "tran"
        elif "!u!1102" in line:
            last_type = "state"

    last_cache.append(line)
split()

for key in [k for k in translations.keys()]:
    lines: List[str] = translations[key]
    sid: Optional[str] = None
    for l in lines:
        if "m_DstState:" in l:
            sid = re.findall(r"-?\d+", l)[0]
            break
    if sid is None:
        print(f"sid is None: {key}")
        translations.pop(key)
        continue

    lines = states[sid]
    index: Optional[str] = None
    for l in lines:
        if "m_Name:" in l and "Data" in l:
            index = re.findall(r"\d+", l)[0]
            break
    if index is None:
        print(f"index is None: {key}")
        translations.pop(key)
        continue

    did: Optional[str] = None
    for l in lines:
        if l.startswith("  -"):
            did = re.findall(r"-?\d+", l)[0]
            break

    if did is None:
        print(f"did is None: {key}")
        translations.pop(key)
        continue

    index_map[key] = index
    index_map[did] = index

# for key in index_map:
#     print(f"{key}:\t{index_map[key]}")

for i in range(len(text)):
    line = text[i]
    if line.startswith("---"):
        last_id = re.findall(r"-?\d+$", line)[0]
        if last_id in translations:
            last_type = "tran"
        elif last_id in drivers:
            last_type = "driver"
        else:
            last_type = "other"
    if last_type != "driver":
        continue

    try:
        index = index_map[last_id]
        if line.startswith("  parameters:"):
            text[i] += "\n"
            text[i] += element.replace("$index", str(int(index) * 2))
        elif line.startswith("    name:"):
            text[i] = f"    name: BitmapLed/Data{str(int(index) * 2 + 1)}\n"
    except KeyError as e:
        print(f"{last_id}错误： {e}")

with open("BitmapLedParamCopyX16Animator-Target.yaml", "w") as f:
    f.write("".join(text))