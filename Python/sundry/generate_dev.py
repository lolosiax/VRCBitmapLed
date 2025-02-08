from PIL import Image, ImageDraw
import os

# 定义3x5点阵字体
font_3x5 = {
    '0': [
        [True, True, True],
        [True, False, True],
        [True, False, True],
        [True, False, True],
        [True, True, True]
    ],
    '1': [
        [False, True, False],
        [False, True, False],
        [False, True, False],
        [False, True, False],
        [False, True, False]
    ],
    '2': [
        [True, True, True],
        [False, False, True],
        [True, True, True],
        [True, False, False],
        [True, True, True]
    ],
    '3': [
        [True, True, True],
        [False, False, True],
        [True, True, True],
        [False, False, True],
        [True, True, True]
    ],
    '4': [
        [True, False, True],
        [True, False, True],
        [True, True, True],
        [False, False, True],
        [False, False, True]
    ],
    '5': [
        [True, True, True],
        [True, False, False],
        [True, True, True],
        [False, False, True],
        [True, True, True]
    ],
    '6': [
        [True, True, True],
        [True, False, False],
        [True, True, True],
        [True, False, True],
        [True, True, True]
    ],
    '7': [
        [True, True, True],
        [False, False, True],
        [False, False, True],
        [False, False, True],
        [False, False, True]
    ],
    '8': [
        [True, True, True],
        [True, False, True],
        [True, True, True],
        [True, False, True],
        [True, True, True]
    ],
    '9': [
        [True, True, True],
        [True, False, True],
        [True, True, True],
        [False, False, True],
        [False, False, True]
    ]
}

# 定义4x8点阵字体
font_4x8 = {
    '0': [
        [True, True, True, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True]
    ],
    '1': [
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False],
        [False, False, True, False]
    ],
    '2': [
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [True, True, True, True],
        [True, False, False, False],
        [True, False, False, False],
        [True, True, True, True]
    ],
    '3': [
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [True, True, True, True]
    ],
    '4': [
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True]
    ],
    '5': [
        [True, True, True, True],
        [True, False, False, False],
        [True, False, False, False],
        [True, False, False, False],
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [True, True, True, True]
    ],
    '6': [
        [True, True, True, True],
        [True, False, False, False],
        [True, False, False, False],
        [True, False, False, False],
        [True, True, True, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True]
    ],
    '7': [
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True]
    ],
    '8': [
        [True, True, True, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True]
    ],
    '9': [
        [True, True, True, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, False, False, True],
        [True, True, True, True],
        [False, False, False, True],
        [False, False, False, True],
        [False, False, False, True]
    ]
}


# 绘制数字的函数
def draw_digit(draw, font, digit, x, y, color):
    for row_idx, row in enumerate(font[digit]):
        for col_idx, pixel in enumerate(row):
            if pixel:
                draw.point((x + col_idx, y + row_idx), fill=color)


# 生成图像
for index in range(256):
    # 创建图像
    img = Image.new('RGBA', (256, 256), color=(0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # 计算列和行
    col = index % 16
    row = index // 16
    col_str = f"{col:02d}"
    row_str = f"{row:02d}"
    index_str = f"{index:03d}"

    # 绘制列数字
    for i, digit in enumerate(col_str):
        draw_digit(draw, font_3x5, digit, i * 3, 0, (255, 255, 255, 255))

    # 绘制行数字
    for i, digit in enumerate(row_str):
        draw_digit(draw, font_3x5, digit, 256 - (i + 1) * 3, 0, (255, 255, 255, 255))

    # 绘制位置索引
    for i, digit in enumerate(index_str):
        draw_digit(draw, font_4x8, digit, i * 4, 256 - 8 - 1, (255, 255, 255, 255))

    # 保存图像
    target_file = f'generate_dev.png'
    img.save(target_file)
