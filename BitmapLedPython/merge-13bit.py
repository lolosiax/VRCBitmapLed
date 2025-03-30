"""
该脚本用于将 256张黑白字体合并到8张RGBA图像中
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
"""
import os.path

import numpy as np
from PIL import Image

if not os.path.exists("merged_13bit"):
    os.mkdir("merged_13bit")

# 加载所有输入图片并转换为二进制数组
input_arrays = np.zeros((256, 256, 256), dtype=np.uint8)

for i in range(32):
    # 根据实际路径修改文件名
    # hex_index = hex(i)[2:].upper()
    # if len(hex_index) == 1:
    #     hex_index = '0' + hex_index
    img = Image.open(f"raw_13bit/{i}.png")
    arr = np.array(img)
    # 提取R通道，转换为0或1
    if arr.ndim == 3:  # RGB或RGBA图像
        r_channel = arr[:, :, 0]
    else:  # 灰度图像，假设R通道等于灰度值
        r_channel = arr
    binary_r = (r_channel == 255).astype(np.uint8)
    input_arrays[i] = binary_r

# 定义每个通道的位权重
weights = np.array([128, 64, 32, 16, 8, 4, 2, 1], dtype=np.uint8).reshape(8, 1, 1)

# 处理每个输出图像
num_output_images = 2  # 64 / 32 = 2

for k in range(num_output_images):
    start = k * 32
    # 计算每个通道的数据
    # R通道：start ~ start+7
    r_part = input_arrays[start:start + 8]
    r_channel = np.sum(r_part * weights, axis=0).astype(np.uint8)

    # G通道：start+8 ~ start+15
    g_part = input_arrays[start + 8:start + 16]
    g_channel = np.sum(g_part * weights, axis=0).astype(np.uint8)

    # B通道：start+16 ~ start+23
    b_part = input_arrays[start + 16:start + 24]
    b_channel = np.sum(b_part * weights, axis=0).astype(np.uint8)

    # A通道：start+24 ~ start+31
    a_part = input_arrays[start + 24:start + 32]
    a_channel = np.sum(a_part * weights, axis=0).astype(np.uint8)

    # 合并为RGBA图像
    rgba_array = np.dstack((r_channel, g_channel, b_channel, a_channel))

    # 保存图像
    output_image = Image.fromarray(rgba_array, mode='RGBA')
    output_image.save(f"merged_13bit/{k}.png")


# 预加载所有图像
images = []
for num in range(num_output_images):
    with Image.open(f"merged_13bit/{num}.png") as img:
        # 确保颜色格式为 RGBA
        if img.mode != 'RGBA':
            raise ValueError(f"图像 {num}.png 的格式为 {img.mode}，非 RGBA 格式")
        images.append(img.copy())

# 获取基准参数
base_width = images[0].width
single_height = images[0].height
total_height = single_height * num_output_images

# 创建画布
canvas = Image.new('RGBA', (base_width, total_height))

# 纵向拼接
for idx, img in enumerate(images):
    canvas.paste(img, (0, idx * single_height))

# 无损保存
canvas.save("merged_13bit/chinese.png", format="PNG", compress_level=0)