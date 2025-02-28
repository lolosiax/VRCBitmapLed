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


class PixelFontTextureGenerator14Bit:
    modules: List[object]
    font: FreeTypeFont
    font_size: int = 16
    currant_index: int = 0
    currant_image: Image
    currant_draw: ImageDraw
    char_count: int = 0b11_111111_111111 + 1
    cols: int = 16
    rows: int = 16
    char_map = {}

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
        self.char_map = {}
        self.search_modules()
        self.load_font()
        self.draw_all()
        self.save_char_map()

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
        if not os.path.exists("raw_14bit"):
            os.mkdir("raw_14bit")
        # index_hex = hex(index)[2:].upper()
        # if len(index_hex) == 1:
        #     index_hex = '0' + index_hex
        target_file = f'raw_14bit/{index}.png'
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
        f_size = self.font_size

        # 动态模块绘制
        for module in self.modules:
            try:
                if module.rule(codepoint):  # 条件判断，测试是否支持该字符绘制
                    module.transform(
                        generator=self, codepoint=codepoint,
                        image=self.currant_draw, x=col * f_size, y=line * f_size, row=line, col=col
                    )
                    raise Break("col")
            except Break as e:
                raise e
            except Exception as e:
                print(e)

        self.draw_default_char(codepoint, line, col)

    def draw_default_char(self, codepoint: int, line: int, col: int):
        # 常规绘制
        try:
            masked = codepoint >> 12 & 0b11
            if masked == 0b00:
                self.draw_00_ascii(codepoint, line, col)
            elif masked == 0b01:
                pass
            else:
                self.draw_gb2312(codepoint, line, col)
        except Exception as e:
            print(e)

    def draw_00_ascii(self, codepoint: int, line: int, col: int):
        """
        绘制ASCII区域，该区域每个全角字内有两个半角字
        """
        f_size = self.font_size
        chars = " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-"
        code0 = codepoint & 0b111111
        code1 = codepoint >> 6 & 0b111111
        self.currant_draw.text((col * f_size, line * f_size), chars[code0], font=self.font, fill='white')
        self.currant_draw.text(((col + 0.5) * f_size, line * f_size), chars[code1], font=self.font, fill='white')

    def draw_gb2312(self, codepoint: int, line: int, col: int):
        f_size = self.font_size

        code = codepoint & 0b01_111111_111111
        gb2312 = PixelFontTextureGenerator14Bit.index_to_gb2312(code)
        char = gb2312.to_bytes(length=2).decode("gb2312")

        self.currant_draw.text((col * f_size, line * f_size), char, font=self.font, fill='white')
        pass

    @staticmethod
    def index_to_gb2312(index : int) -> int:
        if index < 864:  # A1A0-A9FF范围
            high = 0xA1 + (index // 96)
            low = 0xA0 + (index % 96)
        else:  # B0A0-F7FF范围
            adj_index = index - 864
            high = 0xB0 + (adj_index // 96)
            low = 0xA0 + (adj_index % 96)

        return (high << 8) | low

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

    def search_modules(self):
        """
        从 special_font 查找全部用于变换文字绘制的脚本
        """
        modules_files = os.listdir('special_font_14bit')
        modules = []
        for it in modules_files:
            if not it.endswith('.py'):
                continue
            if it == "__init__.py":
                continue
            module_name = it.removesuffix('.py')
            module_name = f"special_font_14bit.{module_name}"
            try:
                modules.append(import_module(module_name))
            except Exception as e:
                print(f"导入模块{module_name}失败: {e}")
        self.modules = modules

    def save_char_map(self):
        map1 = {}
        for key in self.char_map:
            code = hex(key)[2:].upper()
            while len(code) < 4:
                code = "0" + code
            map1[code] = self.char_map[key]

        with open("char_map-14bit.json", "w") as f:
            json.dump(map1, f, indent=2)


class Break(BaseException):
    label: str

    def __init__(self, label: str):
        self.label = label


if __name__ == '__main__':
    generator = PixelFontTextureGenerator14Bit()
    generator.run()
