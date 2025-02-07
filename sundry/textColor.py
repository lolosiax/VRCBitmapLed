from PIL import Image
import colorsys

# 贴图尺寸配置
WIDTH = 16    # 1列灰阶 + 15列色相
SAT = 4 # 4级亮度
VAL = 4 # 4级饱和度
IMAGE_MODE = "RGBA"

def generate_color_map():
    # 创建新图像（左下角为原点）
    img = Image.new(IMAGE_MODE, (WIDTH, SAT * VAL), (0, 0, 0, 255))
    pixels = img.load()

    # 此for循环为文本前景色
    for x in range(13):
        for s in range(SAT):
            for v in range(VAL):
                # 将逻辑Y坐标转换为图像Y坐标（翻转Y轴）
                y = s * 4 + v
                img_y = SAT * VAL - 1 - y

                if x == 0:  # 灰阶列
                    # 亮度映射：0.0 → 0.333 → 0.667 → 1.0
                    gray_value = 1 - y / 15.0
                    color = (
                        int(gray_value * 255),
                        int(gray_value * 255),
                        int(gray_value * 255),
                        255
                    )
                else:       # 色相列
                    # 色相计算（12列平分360°）
                    hue = (x-1) * 30 / 360.0
                    # 亮度映射：0.25 → 0.5 → 0.75 → 1.0
                    sat = 0.25 + s * 0.25
                    val = 0.25 + v * 0.25
                    # 转换为RGB
                    r, g, b = colorsys.hsv_to_rgb(hue, sat, val)
                    color = (
                        int(r * 255),
                        int(g * 255),
                        int(b * 255),
                        255
                    )

                pixels[x, img_y] = color

    # 以下为彩色文本背景色
    for y in range(12):
        # 色相计算（12列平分360°）
        hue = y * 30 / 360.0

        r, g, b = colorsys.hsv_to_rgb(hue, 1, 0.25)
        color = ( int(r * 255), int(g * 255), int(b * 255), 255 )
        pixels[13, y] = color

        r, g, b = colorsys.hsv_to_rgb(hue, 1, 1)
        color = ( int(r * 255), int(g * 255), int(b * 255), 255 )
        pixels[14, y] = color

        r, g, b = colorsys.hsv_to_rgb(hue, 0.5, 1)
        color = ( int(r * 255), int(g * 255), int(b * 255), 255 )
        pixels[15, y] = color

    # 以下为灰阶文本背景色
    for x in range(3):
        for y in range(4):
            # 亮度映射：0.0 → 0.333 → 0.667 → 1.0
            gray_value = (x * 3 + y) / 9.0
            print(gray_value)
            color = (
                int(gray_value * 255),
                int(gray_value * 255),
                int(gray_value * 255),
                255
            )

            pixels[(2 - x) + 13, y + 12] = color
    return img

# 生成并保存贴图
color_map = generate_color_map()
color_map.save("font_color.png")