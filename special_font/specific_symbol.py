"""
网格字体编辑器，自定义字体可以通过当前脚本来替换。
右键运行该文件以打开字体编辑器。
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
import os.path
from typing import List

from PIL import Image, ImageFont
from PIL.ImageDraw import ImageDraw, Draw
from PIL.ImageFont import FreeTypeFont

from generator import PixelFontTextureGenerator
import tkinter as tk
from tkinter import messagebox

def rule(char: int) -> bool:
    return os.path.exists(f"special_font/specific_symbol/{hex(char)[2:]}.txt")


def transform(
        codepoint: int, generator: PixelFontTextureGenerator,
        row: int, col: int,
        x: int, y: int, **kwargs
):
    # 导入调用和直接运行当前文件的路径不一致。
    with open(f"special_font/specific_symbol/{hex(codepoint)[2:]}.txt", "r") as f:
        text = f.readlines()
    for dy in range(min(16, len(text))):
        line = text[dy]
        for dx in range(min(16, len(line))):
            if line[dx] == "#":
                # print((dx, dy))
                generator.currant_draw.point((x + dx, y + dy), "red")


class GridEditor:
    grid_rects: List[List[int]]
    erase_mode: bool = False
    font: FreeTypeFont = None

    def __init__(self, root):
        self.root = root
        self.root.title("字体编辑器")
        if os.path.exists(f"../unifont-16.0.02.otf"):
            self.font = ImageFont.truetype(f"../unifont-16.0.02.otf", 16, index=0)

        width = 300
        heigh = 320
        screenwidth = root.winfo_screenwidth()
        screenheight = root.winfo_screenheight()
        root.geometry('%dx%d+%d+%d' % (width, heigh, (screenwidth - width) / 2, (screenheight - heigh) / 2))

        # 初始化网格数据
        self.grid_data = [[0 for _ in range(16)] for _ in range(16)]

        # 创建界面组件
        self.create_widgets()
        self.draw_grid()

        # 绑定事件
        self.canvas.bind("<Button-1>", self.start_draw)
        self.canvas.bind("<Button-3>", self.start_draw_reverse)
        self.canvas.bind("<B1-Motion>", self.draw)
        self.canvas.bind("<B3-Motion>", self.draw)
        self.canvas.bind("<ButtonRelease-1>", self.save_grid)
        self.canvas.bind("<ButtonRelease-3>", self.save_grid)

        # 跟踪上次绘制位置
        self.last_pos = None
        self.entry_var.set("U+0000")

    def create_widgets(self):
        # 输入框和按钮框架
        frame = tk.Frame(self.root)
        frame.pack(pady=10)

        self.prev_btn = tk.Button(frame, text="上一个", command=self.prev_code)
        self.prev_btn.pack(side=tk.LEFT)

        self.entry_var = tk.StringVar()
        self.entry = tk.Entry(frame, textvariable=self.entry_var, width=20)
        self.entry.pack(side=tk.LEFT, padx=5)
        self.entry_var.trace_add("write", self.load_code)

        self.next_btn = tk.Button(frame, text="下一个", command=self.next_code)
        self.next_btn.pack(side=tk.LEFT)

        # 画布
        self.canvas = tk.Canvas(self.root, width=256, height=256, bg="white")
        self.canvas.pack()

        # 创建网格矩形引用
        self.grid_rects = [[0xffffffff for _ in range(16)] for _ in range(16)]

    def draw_grid(self):
        """绘制初始网格"""
        for i in range(16):
            for j in range(16):
                x1 = j * 16
                y1 = i * 16
                x2 = x1 + 16
                y2 = y1 + 16
                self.grid_rects[i][j] = self.canvas.create_rectangle(
                    x1, y1, x2, y2, fill="white", outline="gray"
                )

    def start_draw(self, event):
        """开始绘制时记录位置"""
        self.erase_mode = False
        self.last_pos = (event.x // 16, event.y // 16)
        self.draw(event)

    def start_draw_reverse(self, event):
        """开始绘制时记录位置"""
        self.erase_mode = True
        self.last_pos = (event.x // 16, event.y // 16)
        self.draw(event)

    def draw(self, event, color=None):
        """处理绘制逻辑"""
        current_x = event.x // 16
        current_y = event.y // 16

        if 0 <= current_x < 16 and 0 <= current_y < 16:
            # 获取绘制颜色
            new_value = 0 if self.erase_mode else 1

            # 处理连续绘制
            if self.last_pos:
                for x, y in self.get_line_coords(self.last_pos, (current_x, current_y)):
                    self.update_cell(x, y, new_value)

            self.update_cell(current_x, current_y, new_value)
            self.last_pos = (current_x, current_y)

    def get_line_coords(self, start, end):
        """使用Bresenham算法获取两点之间所有坐标"""
        x1, y1 = start
        x2, y2 = end
        dx = abs(x2 - x1)
        dy = abs(y2 - y1)
        sx = 1 if x1 < x2 else -1
        sy = 1 if y1 < y2 else -1
        err = dx - dy
        coords = []

        while True:
            coords.append((x1, y1))
            if x1 == x2 and y1 == y2:
                break
            e2 = 2 * err
            if e2 > -dy:
                err -= dy
                x1 += sx
            if e2 < dx:
                err += dx
                y1 += sy
        return coords

    def update_cell(self, x, y, value):
        """更新单元格状态和颜色"""
        if self.grid_data[y][x] != value:
            self.grid_data[y][x] = value
            color = "black" if value else "white"
            self.canvas.itemconfig(self.grid_rects[y][x], fill=color)

    def save_grid(self, event):
        """保存网格数据到文件"""
        code = self.parse_code()
        if code is None or code == "0000":
            return

        try:
            if not os.path.exists("specific_symbol"):
                os.makedirs("specific_symbol")

            with open(f"specific_symbol/{code}.txt", "w") as f:
                for row in self.grid_data:
                    f.write("".join(["#" if cell else " " for cell in row]) + "\n")
        except Exception as e:
            messagebox.showerror("保存错误", f"无法保存文件: {e}")

    def load_code(self, *args):
        """加载对应代码的网格数据"""
        code = self.parse_code()
        if code is None or int(code, 16) < 32:
            self.reset_grid()
            return

        if os.path.exists(f"specific_symbol/{code}.txt"):
            try:
                with open(f"specific_symbol/{code}.txt") as f:
                    for i, line in enumerate(f):
                        if i >= 16:
                            break
                        for j, char in enumerate(line):
                            if j >= 16:
                                break
                            self.grid_data[i][j] = 1 if char == "#" else 0
                self.refresh_grid()
                return
            except Exception as e:
                messagebox.showerror("加载错误", f"无效文件格式: {e}")

        if self.font:
            try:
                im = Image.new("RGB", (16, 16))
                draw = Draw(im)
                char = chr(int(code, 16))
                draw.text((0, 0), char, font=self.font, fill='white')

                for x in range(16):
                    for y in range(16):
                        (r, g, b) = im.getpixel((x, y))
                        self.grid_data[y][x] = 1 if r > 127 else 0
                self.refresh_grid()
                return
            except Exception as e:
                print(e)

        self.reset_grid()

    def parse_code(self):
        """解析输入框中的代码"""
        text = self.entry_var.get().strip()
        if text.startswith("U+"):
            return text[2:].strip().zfill(4).upper()
        return text if text.isdigit() else None

    def reset_grid(self):
        """重置网格为全白"""
        self.grid_data = [[0 for _ in range(16)] for _ in range(16)]
        self.refresh_grid()

    def refresh_grid(self):
        """刷新网格显示"""
        for i in range(16):
            for j in range(16):
                color = "black" if self.grid_data[i][j] else "white"
                self.canvas.itemconfig(self.grid_rects[i][j], fill=color)

    def prev_code(self):
        """上一个代码"""
        self.adjust_code(-1)

    def next_code(self):
        """下一个代码"""
        self.adjust_code(1)

    def adjust_code(self, delta):
        """调整代码值"""
        text = self.entry_var.get().strip()
        try:
            if text.startswith("U+"):
                num = int(text[2:], 16) + delta
                self.entry_var.set(f"U+{num:04X}")
            else:
                num = int(text) + delta
                self.entry_var.set(str(num))
        except ValueError:
            pass


if __name__ == "__main__":
    root = tk.Tk()
    app = GridEditor(root)
    root.mainloop()
