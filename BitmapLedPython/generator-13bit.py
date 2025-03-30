"""
这是14bit字体生成器，用于生成屏幕上绘制文字所需的贴图
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
"""
import json
import os
from importlib import import_module
from typing import List

from PIL import Image, ImageDraw, ImageFont
from PIL.ImageFont import FreeTypeFont


class PixelFontTextureGenerator13Bit:
    modules: List[object]
    font: FreeTypeFont
    font_size: int = 16
    currant_index: int = 0
    currant_image: Image
    currant_draw: ImageDraw
    char_count: int = 0
    chars = ""
    cols: int = 16
    rows: int = 16

    @property
    def chars_per_image(self) -> int:
        return self.cols * self.rows

    @property
    def image_count(self) -> int:
        per = self.chars_per_image
        c = self.char_count // per
        remainder = self.char_count % per
        if remainder != 0:
            c = c + 1
        return c

    def run(self):
        self.load_file()
        self.load_font()
        self.draw_all()

    def load_file(self):
        with open("chinese-13bit.txt", "r", encoding="utf-8") as f:
            lines = f.readlines()
            for line in lines:
                if " " not in line:
                    continue
                line = line.split(" ")[1].replace("\r", "").replace("\n", "")
                self.chars += line
            self.char_count = len(self.chars)

    def load_font(self):
        # 可以在这里更换你的字体路径
        font_path = "unifont-16.0.02.otf"
        try:
            self.font = ImageFont.truetype(font_path, self.font_size, index=0)  # 使用点阵字体
        except:
            raise Exception("请确认字体路径，或尝试调整字体索引")

    def draw_all(self):
        for i in range(self.image_count):
            try:
                self.draw_image(i)
            except Break as e:
                if e.label != "image":
                    raise e

    def draw_image(self, index: int):
        if not os.path.exists("raw_13bit"):
            os.mkdir("raw_13bit")
        # index_hex = hex(index)[2:].upper()
        # if len(index_hex) == 1:
        #     index_hex = '0' + index_hex
        target_file = f'raw_13bit/{index}.png'
        print(f"绘制图像 {target_file}...")
        self.currant_index = index
        self.currant_image = Image.new('RGB', (self.font_size * self.cols, self.font_size * self.rows), color='black')
        self.currant_draw = ImageDraw.Draw(self.currant_image)
        try:
            for i in range(self.rows):
                try:
                    self.draw_line(i)
                except Break as e:
                    if e.label != "line":
                        raise e
        finally:
            # self.draw_grid()

            # 保存结果
            self.currant_image.convert('RGB').save(target_file)
            print(f"图像已保存为 {target_file}")

    def draw_line(self, line: int):
        for i in range(self.cols):
            try:
                self.draw_char(line, i)
            except Break as e:
                if e.label != "col":
                    raise e

    def draw_char(self, line: int, col: int):
        codepoint = self.currant_index * self.chars_per_image + line * self.cols + col

        self.draw_default_char(codepoint, line, col)

    def draw_default_char(self, codepoint: int, line: int, col: int):
        # 常规绘制
        try:
            f_size = self.font_size
            if codepoint < len(self.chars):
                char = self.chars[codepoint]
            else:
                char = " "

            self.currant_draw.text((col * f_size, line * f_size), char, font=self.font, fill='white')
        except Exception as e:
            print(e)

    def draw_grid(self):
        # 绘制半透明红色网格
        for i in range(0, max(self.cols, self.rows) * self.font_size, self.font_size):
            # 创建透明覆盖层
            overlay = Image.new('RGBA', self.currant_image.size)
            overlay_draw = ImageDraw.Draw(overlay)

            # 绘制垂直线
            overlay_draw.line([(i, 0), (i, 4095)], fill=(255, 0, 0, 64))
            # 绘制水平线
            overlay_draw.line([(0, i), (4095, i)], fill=(255, 0, 0, 64))

            # 合并图层
            self.currant_image = Image.alpha_composite(self.currant_image.convert('RGBA'), overlay)

class Break(BaseException):
    label: str

    def __init__(self, label: str):
        self.label = label


if __name__ == '__main__':
    generator = PixelFontTextureGenerator13Bit()
    generator.run()
