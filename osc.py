"""
这是一次出半个字的OSC版本，17 bits参数版本。
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
from time import sleep
from typing import List

from pythonosc import udp_client

def string_to_unicode_bytes(s) -> List[int]:
    bytes_array = []
    for char in s:
        bytes_array.extend(char.encode('utf-16-be'))
    return bytes_array

# 配置OSC客户端
client = udp_client.SimpleUDPClient("127.0.0.1", 9000)  # IP和端口

# 8行 16列
lines : List[str] = """

————————————————
  欢迎使用洛洛希雅点阵屏幕
   ＷＥＬＣＯＭ ＵＳＥ
 ＰＩＸＥＬ  ＤＩＳＰＬＡＹ
————————————————


""".removeprefix("\n").split("\n")
for i in range(8):
    line: str = lines[i]
    line = f"{line:16}"
    lines[i] = line

text = "".join(lines)
data = string_to_unicode_bytes(text)
for i in range(256 - len(data)):
    data.append(0)

index = 255
while True:
    index += 1
    if index >= 256:
        index = 0
    # 发送BitmapLed/Pointer
    client.send_message("/avatar/parameters/BitmapLed/Pointer", index)

    # 发送BitmapLed/Data
    client.send_message("/avatar/parameters/BitmapLed/Data", data[index])
    char_index = index // 2
    char = text[char_index] if len(text) > char_index else ""

    print(f"\r{index}: {data[index]}, {char}", end="")
    # print(f"\r{index}: {data[index]}, {char}")
    sleep(0.2)
