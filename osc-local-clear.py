"""
该脚本用于一键清除屏幕上的字符，非同步。
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
from pythonosc import udp_client

# 配置OSC客户端
client = udp_client.SimpleUDPClient("127.0.0.1", 9000)  # IP和端口

client.send_message("/avatar/parameters/BitmapLed/Pointer", 0)
client.send_message("/avatar/parameters/BitmapLed/Data", 0)

for i in range(256):
    client.send_message(f"/avatar/parameters/BitmapLed/Data{i}", 0)