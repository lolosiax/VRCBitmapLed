"""
该文件用于生成数字的自定义字体
文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
"""
from generator import PixelFontTextureGenerator

def rule(char: int) -> bool:
    return 0xD800 <= char < 0xD900


def transform(codepoint: int, generator: PixelFontTextureGenerator, x: int, y: int, row: int, col: int, **kwargs):
    i = codepoint - 0xD800
    if i < 10:
        generator.currant_draw.text((x, y), f"0{i}", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"0{i}"
        return
    i -= 10
    if i < 90:
        generator.currant_draw.text((x, y), str(i + 10), font=generator.font, fill='red')
        generator.char_map[codepoint] = str(i + 10)
        return
    i -= 90
    if i < 10:
        generator.currant_draw.text((x, y), f":{i}", font=generator.font, fill='red')
        generator.char_map[codepoint] = f":{i}"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"{i}:", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"{i}:"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"-{i}", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"-{i}"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"{i}.", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"{i}."
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f".{i}", font=generator.font, fill='red')
        generator.char_map[codepoint] = f".{i}"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"{i}%", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"{i}%"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"{i}/", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"{i}/"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x, y), f"/{i}", font=generator.font, fill='red')
        generator.char_map[codepoint] = f"/{i}"
        return
    i -= 10
    if i < 10:
        generator.currant_draw.text((x + 8, y), str(i), font=generator.font, fill='red')
        generator.char_map[codepoint] = f" {i}"
        generator.char_map[ord(str(i))] = f"{i} "
        return
    generator.draw_default_char(codepoint, row, col)
