"""
该脚本用于转换RGB色彩为彩色点阵屏幕所使用的色彩索引
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
"""

import colorsys

# 预生成颜色表以提高效率
_foreground_colors = None
_background_colors = None


def _get_foreground_colors():
    global _foreground_colors
    if _foreground_colors is None:
        _foreground_colors = []
        for x in range(13):
            for img_y in range(16):
                y = img_y
                if x == 0:
                    gray_value = 1 - y / 15.0
                    r = int(gray_value * 255)
                    g = r
                    b = r
                else:
                    hue = (x - 1) * 30 / 360.0
                    s = y // 4
                    v = y % 4
                    sat = 0.25 + s * 0.25
                    val = 0.25 + v * 0.25
                    r, g, b = colorsys.hsv_to_rgb(hue, sat, val)
                    r = int(r * 255)
                    g = int(g * 255)
                    b = int(b * 255)
                _foreground_colors.append((r, g, b, x, img_y))
    return _foreground_colors


def _get_background_colors():
    global _background_colors
    if _background_colors is None:
        _background_colors = []
        # 处理彩色背景部分
        for x in [13, 14, 15]:
            for y_img in range(12):
                hue = y_img * 30 / 360.0
                if x == 13:
                    sat, val = 1.0, 0.25
                elif x == 14:
                    sat, val = 1.0, 1.0
                else:
                    sat, val = 0.5, 1.0
                r, g, b = colorsys.hsv_to_rgb(hue, sat, val)
                r = int(r * 255)
                g = int(g * 255)
                b = int(b * 255)
                _background_colors.append((r, g, b, x, y_img))
        # 处理灰阶背景部分
        for x_bg in range(3):
            for y_bg in range(4):
                gray_value = (x_bg * 3 + y_bg) / 9.0
                color_value = int(gray_value * 255)
                r = g = b = color_value
                column = (2 - x_bg) + 13
                row = y_bg + 12
                _background_colors.append((r, g, b, column, row))
    return _background_colors


def find_nearest_foreground(r: int, g: int, b: int) -> int:
    """
    查找前景色索引
    :param r: 红色
    :param g: 绿色
    :param b: 蓝色
    :return: 色彩索引 0~255
    """
    min_dist = float('inf')
    best_col = best_row = 0
    for cr, cg, cb, cx, cy in _get_foreground_colors():
        dist = (r - cr) ** 2 + (g - cg) ** 2 + (b - cb) ** 2
        if dist < min_dist:
            min_dist, best_col, best_row = dist, cx, cy
    return best_col * 16 + best_row


def find_nearest_background(r: int, g: int, b: int) -> int:
    """
    查找背景色索引
    :param r: 红色
    :param g: 绿色
    :param b: 蓝色
    :return: 色彩索引 0~255
    """
    min_dist = float('inf')
    best_col = best_row = 0
    for cr, cg, cb, cx, cy in _get_background_colors():
        dist = (r - cr) ** 2 + (g - cg) ** 2 + (b - cb) ** 2
        if dist < min_dist:
            min_dist, best_col, best_row = dist, cx, cy
    return best_col * 16 + (15 - best_row)
