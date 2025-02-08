"""
该脚本用于将 256张黑白字体合并到8张RGBA图像中
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
import os.path

import numpy as np
from PIL import Image

if not os.path.exists("merged"):
    os.mkdir("merged")

# 加载所有输入图片并转换为二进制数组
input_arrays = np.zeros((256, 256, 256), dtype=np.uint8)

for i in range(256):
    # 根据实际路径修改文件名
    hex_index = hex(i)[2:].upper()
    if len(hex_index) == 1:
        hex_index = '0' + hex_index
    img = Image.open(f"raw/{hex_index}.png")
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
num_output_images = 8  # 256 / 32 = 8

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
    output_image.save(f"merged/{k}.png")