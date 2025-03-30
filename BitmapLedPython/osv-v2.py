import time
from pythonosc import udp_client
from typing import List, Dict

# ==================== 配置部分 ====================
OSC_IP = "127.0.0.1"  # 修改为接收方IP
OSC_PORT = 9000  # 修改为接收方端口
lines = """
一乙二十丁厂七卜八人入儿匕几九刁
了刀力乃又三干于亏工土士才下寸大
丈与万上小口山巾千乞川亿个夕久么
勺凡丸及广亡门丫义之尸己已巳弓子
卫也女刃飞习叉马乡丰王开井天夫元
无云专丐扎艺木五支厅不犬太区历歹
友尤匹车巨牙屯戈比互切瓦止少曰日
中贝冈内水见午牛手气毛壬升夭长仁
什片仆化仇币仍仅斤爪反介父从仑今
凶分乏公仓月氏勿欠风丹匀乌勾凤六
文亢方火为斗忆计订户认冗讥心尺引
丑巴孔队办以允予邓劝双书幻玉刊未
末示击打巧正扑卉扒功扔去甘世艾古
节本术可丙左厉石右布夯戊龙平灭轧
东卡北占凸卢业旧帅归旦目且叶甲申
叮电号田由只叭史央兄叽叼叫叩叨另
""".removeprefix("\n").split("\n")  # 要发送的文本（不超过255字符）
for i in range(16):
    line: str = lines[i]
    line = f"{line:16}"
    lines[i] = line
del i, line

TEXT_TO_SEND = "".join(lines)


# ==================================================

class Chinese13BitParser:
    chars = ""

    def __init__(self):
        self.load_file()

    def load_file(self):
        with open("chinese-13bit.txt", "r", encoding="utf-8") as f:
            lines = f.readlines()
            for line in lines:
                if " " not in line:
                    continue
                line = line.split(" ")[1].replace("\r", "").replace("\n", "")
                self.chars += line
            self.chars = " " + self.chars[1:]

    def get_code(self, char: str) -> int:
        if char in self.chars:
            return self.chars.index(char)
        return -1


class OscSender:
    client = udp_client.SimpleUDPClient(OSC_IP, OSC_PORT)
    parser = Chinese13BitParser()
    pointer = 0
    text = TEXT_TO_SEND.ljust(259)
    bit_addresses = [f"/avatar/parameters/BitmapLed/Sync/Bit{i}" for i in range(64)]

    def _send_bits(self, bits: int):
        """发送64位数据到OSC"""
        for i in range(63, -1, -1):
            bit_value = (bits >> i) & 1  # 高位在前
            self.client.send_message(self.bit_addresses[i], bit_value)
            print(bit_value, end="")
        print("\t", end="")

    def main_loop(self):
        while True:
            chars = self.text[self.pointer:self.pointer + 4]
            fast_points = [self.parser.get_code(it) for it in chars]
            if -1 in fast_points:
                data = (self.pointer & 0xff) << 52
                char0 = ord(chars[0])
                char1 = ord(chars[1])
                data = data | (char0 << 24) | char1
                self._send_bits(data)
                print(chars[:2])
                self.pointer += 2
            else:
                data = 1 << 60
                data = data | ((self.pointer & 0xff) << 52)
                for i in range(4):
                    shl = (3 - i) * 13
                    data = data | ((fast_points[i] & 0xffff) << shl)
                self._send_bits(data)
                print(chars)
                self.pointer += 4

            self.pointer = self.pointer % 256
            time.sleep(0.5)


if __name__ == '__main__':
    osc = OscSender()
    osc.main_loop()
