"""
这是字体生成器，用于生成屏幕上绘制文字所需的贴图，完成后去运行 merge.py
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
from PIL import Image, ImageDraw, ImageFont


# 加载点阵字体（需要替换为你的宋体路径）
try:
    #font = ImageFont.truetype(r'C:\Users\20988\AppData\Local\Microsoft\Windows\Fonts\Sarasa-Regular.ttc', 16, index=0)  # 使用点阵字体
    font = ImageFont.truetype(r'unifont-16.0.02.otf', 16, index=0)  # 使用点阵字体
except:
    raise Exception("请确认字体路径，或尝试调整字体索引")
for index in range(256):
    # 创建图像
    img = Image.new('RGB', (256, 256), color='black')
    draw = ImageDraw.Draw(img)
    for row in range(16):
        for col in range(16):
            codepoint = index * 256 + row * 16 + col
            if codepoint < 32:
                continue

            try:
                char = chr(codepoint)
                # 使用精确的点阵绘制模式
                draw.text((col * 16, row * 16), char, font=font, fill='white')
            except Exception as e:
                print(e)

    target_file = f'raw/unifont-{index}.png'
    # 保存结果
    img.convert('RGB').save(target_file)
    print(f"图像已保存为 {target_file}")

# 绘制半透明红色网格
# for i in range(0, 4096, 16):
#     # 创建透明覆盖层
#     overlay = Image.new('RGBA', img.size)
#     overlay_draw = ImageDraw.Draw(overlay)
#
#     # 绘制垂直线
#     overlay_draw.line([(i, 0), (i, 4095)], fill=(255, 0, 0, 64))
#     # 绘制水平线
#     overlay_draw.line([(0, i), (4095, i)], fill=(255, 0, 0, 64))
#
#     # 合并图层
#     img = Image.alpha_composite(img.convert('RGBA'), overlay)
