"""
这是带有OSC接收功能的彩色版本屏幕控制器
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
"""
import threading
from time import sleep
import time
from typing import List, Tuple

from pythonosc import dispatcher, osc_server, udp_client
from scheduler import DisplayScheduler
from special_font import to_osc_string
from nearest import find_nearest_background, find_nearest_foreground


def print_handler(address, *args):
    print(f"Received message: {address} {args}")


left_top = 0
left_bottom = 0
local_pressed = False

remote_left_top = 0
remote_left_bottom = 0
remote_pressed = False


def update_local(top, bottom):
    global left_top
    global left_bottom
    if top >= 0: left_top = top
    if bottom >= 0: left_bottom = bottom


def update_remote(top, bottom):
    global remote_left_top
    global remote_left_bottom
    if top >= 0: remote_left_top = top
    if bottom >= 0: remote_left_bottom = bottom


def update_pressed(pl, pr):
    global local_pressed, remote_pressed
    if pl is not None: local_pressed = bool(pl)
    if pr is not None: remote_pressed = bool(pr)


def find_point(a: float, b: float) -> tuple[int, int]:
    y = (1 + b ** 2 - a ** 2) / 2
    x_squared = b ** 2 - y ** 2
    if x_squared < 0:
        return -1, -1
    x = x_squared ** 0.5
    return int(round(x * 16)), int(round(y * 16))


def get_local() -> Tuple[float, float]:
    return find_point(left_top, left_bottom)


def get_remote() -> Tuple[float, float]:
    return find_point(remote_left_top, remote_left_bottom)


dispatcher = dispatcher.Dispatcher()
dispatcher.map('/avatar/parameters/Clothes', print_handler)
dispatcher.map('/avatar/parameters/BitmapLed/LocalLeftTop', lambda _, value: update_local(value, -1))
dispatcher.map('/avatar/parameters/BitmapLed/LocalLeftBottom', lambda _, value: update_local(-1, value))
dispatcher.map('/avatar/parameters/BitmapLed/RemoteLeftTop', lambda _, value: update_remote(value, -1))
dispatcher.map('/avatar/parameters/BitmapLed/RemoteLeftBottom', lambda _, value: update_remote(-1, value))
dispatcher.map('/avatar/parameters/BitmapLed/LocalPressed', lambda _, value: update_pressed(value, None))
dispatcher.map('/avatar/parameters/BitmapLed/RemotePressed', lambda _, value: update_pressed(None, value))

server = osc_server.ThreadingOSCUDPServer(('127.0.0.1', 9001), dispatcher)
print("Serving on {}".format(server.server_address))

# 配置OSC客户端
client = udp_client.SimpleUDPClient("127.0.0.1", 9000)  # IP和端口
sche = DisplayScheduler()


def send_osc(pointer: int, data: Tuple[int, int, int]) -> None:
    print(f"Sending to {pointer}: {data}")

    # 发送BitmapLed/Data
    client.send_message("/avatar/parameters/BitmapLed/Pointer", pointer)
    client.send_message("/avatar/parameters/BitmapLed/DataX24", data[0])
    client.send_message("/avatar/parameters/BitmapLed/DataX16", data[1])
    client.send_message("/avatar/parameters/BitmapLed/Data", data[2])


sche.on_send = send_osc


def scheduler_loop():
    sche.run()


scheduler_thread = threading.Thread(target=scheduler_loop)
scheduler_thread.start()


def str_to_tuple(text: str, color: int, bc: bool = False) -> List[Tuple[int, int, int]]:
    text = to_osc_string(text)
    tuples = []
    for char in text:
        point = ord(char)
        h = point >> 8 & 0xff
        l = point & 0xff
        r = color >> 16 & 0xff
        g = color >> 8 & 0xff
        b = color & 0xff
        c = find_nearest_background(r, g, b) if bc else find_nearest_foreground(r, g, b)
        tuples.append((c, h, l))
    return tuples


def do_send(point: Tuple[int, int], text: str, color: int, bc: bool = False) -> None:
    tuples = str_to_tuple(text, color, bc)
    # print(tuples)
    for i in range(len(tuples)):
        sche.update_memory(point[0] * 16 + point[1] + i, tuples[i])


def b_u(b: bool) -> str:
    return "◆" if b else " "


def send_loop():
    while True:
        text = time.strftime('%Y年%m月%d日 %H:%M:%S')
        do_send((0, 0), text, 0x00ffff, True)
        text = f"本地"
        do_send((1, 0), text, 0xFFFF3F, False)
        text = f"按下"
        do_send((1, 2), text, 0xFFFF3F, local_pressed)

        text = f"{(round(left_top, 2), round(left_bottom, 2))}"
        do_send((1, 5), text, 0xFFFF3F)

        text = f"远程"
        do_send((3, 0), text, 0x9F3FFF, False)
        text = f"按下"
        do_send((3, 2), text, 0x9F3FFF, remote_pressed)
        text = f"{get_remote()}"
        do_send((3, 5), text, 0x9F3FFF)
        sleep(1)
    pass


send_thread = threading.Thread(target=send_loop)
send_thread.start()

server.serve_forever()
