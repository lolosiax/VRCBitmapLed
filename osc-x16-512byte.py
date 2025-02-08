"""
这是一次出一个字的OSC版本，25 bits参数版本。
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
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

# 16行 16列
lines : List[str] = """






　　欢迎使用洛洛希雅点阵屏幕

　　无信号，请检查信号源输入






　　　　　　　ＮＯ ＳＩＧＮＡＬ
""".removeprefix("\n").split("\n")
for i in range(16):
    line: str = lines[i]
    line = f"{line:16}"
    lines[i] = line

text = "".join(lines)
data = string_to_unicode_bytes(text)
for i in range(512 - len(data)):
    data.append(0)

# for i in range(256):
#     client.send_message(f"/avatar/parameters/BitmapLed/Data{i}", data[i])

count = 0
index = 255
while True:
    index += 1
    if index > 255:
        index = 0
        count += 1
        if count > 9:
            count = 0
    # data = string_to_unicode_bytes(str(count) * 256)
    # 发送BitmapLed/Pointer
    client.send_message("/avatar/parameters/BitmapLed/Pointer", index)

    high_index = index * 2
    low_index = index * 2 + 1

    # 发送BitmapLed/Data
    client.send_message("/avatar/parameters/BitmapLed/DataX16", data[high_index])
    client.send_message("/avatar/parameters/BitmapLed/Data", data[low_index])

    char = text[index] if len(text) > index else ""
    # print(f"\r{index}: {data[index]}, {char}", end="")
    print(f"\r{index * 2}: {data[high_index]}, {data[low_index]}, {char}")
    sleep(0.2)
