# BitmapLED-VRC点阵屏幕

## 概述

该项目用于在VRChat中绘制一个点阵屏幕。

- 使用OSC进行控制。
- 有 17 bits 和 25 bits 两种同步参数版本可选。
- 默认大小为 16x8 字符，最大显示256个汉字，支持U+0000 ~ U+FFFF。
- 显存占用 2.4 MB。
- <del>十分卡顿，能从80帧掉到40帧</del>

## 效果展示

![img](./img/3d14ffe4d66bd87a9f73031ffd1d4e99.png)

## 安装与运行

### 下载与安装

1. 请从右侧的 [Release部分](https://github.com/lolosiax/VRCBitmapLed/releases) 依次选择 `? tags` > `v0.0.x` > `BitmapLED-VRC.zip` 进行下载。  
2. 然后将该zip解压到一个合适的位置。  
3. 从该解压目录下找到`BitmapLed.unitypackage`，拖入Unity中。

此时，你可以在`Assets/Lolosia/Util/BitmapLed`路径下找到所需的预制件，选择合适的一个将其丢入你的虚拟形象中。  

小Tip：你可以调整材质中的行数和列数以控制显示布局。

#### 类型介绍  
你可以参考这份表格决定选择使用哪个预制件。

|           | 标准版              | 英文版                | X16速率版                   | 256字符版                                   |
|-----------|------------------|--------------------|--------------------------|------------------------------------------|
| 参数        | 17bits           | 17bits             | 25bits                   | 25bits                                   |
| 字数        | 128字             | 256字               | 128字                     | 256字                                     |
| 速率        | 2.5字/秒           | 5字/秒               | 5字/秒                     | 5字/秒                                     |
| Pointer长度 | 256              | 256                | 128                      | 256                                      |
| 完全刷新      | 1分钟              | 1分钟                | 25秒                      | 1分钟                                      |
| 列与行       | 16*8             | 32*8               | 16*8                     | 16*16                                    |
| 编码支持      | UTF-16BE         | ISO-8859-1         | UTF-16 BE                | UTF-16 BE                                |
| 预制件       | BitmapLedDisplay | 无，请自行替换材质          | BitmapLedDisplayX16      | BitmapLedDisplayX16-512byte              |
| 材质与着色器    | DisplayShader    | DisplayShaderASCII | DisplayShader            | DisplayShader512                         |
| OSC脚本名称   | [osc.py](osc.py) | 无                  | [osc-x16.py](osc-x16.py) | [osc-x16-512byte.py](osc-x16-512byte.py) |


### 发送OSC

在继续查看之前，我推荐你使用以下项目接入点阵屏：  
**[VoiceLinkVR/VRCLS](https://github.com/VoiceLinkVR/VRCLS)**：一个用于在VRChat中使用语音来控制模型或作为翻译器输出内容的超轻量级本地部署程序

以下是本项目的原生接入方式：

1. 首先你需要一个Python 3.12或更高的版本，[点击下载](https://www.python.org/ftp/python/3.13.2/python-3.13.2-amd64.exe)
2. 在安装好 Python 后，进入到你解压的zip的目录。
3. Win11 鼠标右键单击空白处，然后“在终端中打开”，Win10 请按住Shift键，右键单击空白处，选择“在此处打开Powershell窗口”
4. 输入以下内容，然后按下回车安装所需依赖库。
```powershell
pip install -r requirements.txt
```
5. 请按照你所安装的预制件类型，在记事本中修改[osc.py](osc.py)系列脚本，在指定区域内输入你想显示的内容。
6. 输入以下内容，并按下回车，开始发送OSC信息，以256字符版为例。
```powershell
python osc-x16-512byte.py
```

## DIY接入方式

### 文字修改方式
字符类型为 UTF-16 BE，最大支持对512个字节进行寻址，以下为修改方式：

设置修改参数所需的地址索引。

```plaintext
/avatar/parameters/BitmapLed/Pointer
```

17 bits 版本需要设置的参数

```plaintext
/avatar/parameters/BitmapLed/Data
```

25 bits 版本需要设置的参数

```plaintext
# 高八位
/avatar/parameters/BitmapLed/DataX16
# 低八位
/avatar/parameters/BitmapLed/Data
```

### 同步速率

由于VRC的同步速率问题，每隔140ms写入一次信息是个甜点间隔，丢包率比较低；  
当为 100ms 时，丢包率较大；  
当为 120ms 时，每隔约 5 个包丢包 1 次；  
当为 130ms 时，每隔约 7 个包丢包 1 次；  
当为 140ms 时，丢包不常见；  
不过我们**一般采用保守的 200ms 作为传输间隔**，这个时间下不会产生丢包问题。

### 特殊控制符

在`BitmapLed/Pointer`值为`255`时传入`0x0004`可以将屏幕清空。
> 17 bits 版本需要先修改位于`254`的值为0，再修改位于`255`的值为`4`。

## 部分原理

### 减少同步参数

该项目使用一个动画层对Pointer指向的256个不同的非同步参数进行拷贝，以减少同步参数占用。
![9e79b60e3af7da642ef7c7acf9700d25.png](img/9e79b60e3af7da642ef7c7acf9700d25.png)

### 字体库

RGBA32图像中每个色彩通道里面每个颜色是8bit，这样我们可以将8张图对应的像素合并到这一个像素内，
然后由于RGBA有四个通道，且每个通道能塞8张，结果就是我们可以把32张黑白图像合并到1张贴图内。

示例：  
![unifont-2.png](Assets/Lolosia/Util/BitmapLed/Texture/unifont-2.png)