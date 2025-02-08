import json

_str_map = {}

def _load_json():
    global _str_map
    with open("char_map.json", "r") as f:
        map = json.load(f)
        map1 = {}
        for key in map:
            map1[map[key]] = int(key, 16)
        _str_map = map1
_load_json()

def to_osc_string(text: str) -> str:
    str_map = _str_map
    target = ""

    while len(text) > 0:
        if len(text) == 1:
            target += text
            break
        key = text[0:2]
        if key in str_map:
            target += chr(str_map[key])
            text = text[2:]
            continue
        target += text[0]
        text = text[1:]
    return target