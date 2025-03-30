/**
 * 这是绘制点阵屏幕所需的Shader, 512 Byte 版本
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/DisplayShaderEx"
{
    Properties
    {
        _Size ("Size", Vector) = (32,4,0,0)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontAtlas ("Font Atlas", 2DArray) = "white" {}
        [IntRange] _Pointer("Pointer", Range(0, 255)) = 0
        [IntRange] _Mask("DataMask", Range(0, 255)) = 0
        [IntRange] _Data0("Data0", Range(0, 255)) = 0
        [IntRange] _Data1("Data1", Range(0, 255)) = 0
        [IntRange] _Data2("Data2", Range(0, 255)) = 0
        [IntRange] _Data3("Data3", Range(0, 255)) = 0
        [IntRange] _Data4("Data4", Range(0, 255)) = 0
        [IntRange] _Data5("Data5", Range(0, 255)) = 0
        [IntRange] _Data6("Data6", Range(0, 255)) = 0
        [IntRange] _Data7("Data7", Range(0, 255)) = 0
        [IntRange] _Data8("Data8", Range(0, 255)) = 0
        [IntRange] _Data9("Data9", Range(0, 255)) = 0
        [IntRange] _Data10("Data10", Range(0, 255)) = 0
        [IntRange] _Data11("Data11", Range(0, 255)) = 0
        [IntRange] _Data12("Data12", Range(0, 255)) = 0
        [IntRange] _Data13("Data13", Range(0, 255)) = 0
        [IntRange] _Data14("Data14", Range(0, 255)) = 0
        [IntRange] _Data15("Data15", Range(0, 255)) = 0
        [IntRange] _Data16("Data16", Range(0, 255)) = 0
        [IntRange] _Data17("Data17", Range(0, 255)) = 0
        [IntRange] _Data18("Data18", Range(0, 255)) = 0
        [IntRange] _Data19("Data19", Range(0, 255)) = 0
    }

    SubShader
    {
        Tags
        {
            "RenderType"="Transparent"
            "Queue"="Overlay"
            "IgnoreProjector"="True"
        }
        LOD 100

        Pass
        {
            // AlphaToMask On
            ZWrite Off
            Blend SrcAlpha OneMinusSrcAlpha
            CGPROGRAM
// Upgrade NOTE: excluded shader from DX11, OpenGL ES 2.0 because it uses unsized arrays
#pragma exclude_renderers d3d11 gles
            #pragma vertex vert
            #pragma fragment frag

            #include "UnityCG.cginc"

            struct appdata
            {
                float4 vertex : POSITION;
                float2 uv : TEXCOORD0;
            };

            struct v2f
            {
                float2 uv : TEXCOORD0;
                float4 vertex : SV_POSITION;
            };

            struct character
            {
                int codePoint;
                fixed3 color;
                bool invert;
            };

            float2 _Size;
            fixed4 _ShadowColor;
            float4 _BackgroundColor;
            float2 _ShadowOffset;
            UNITY_DECLARE_TEX2DARRAY(_FontAtlas);
            uniform int _Bytes[1280];

            int _Pointer, _Mask;
            int _Data0, _Data1, _Data2, _Data3, _Data4;
            int _Data5, _Data6, _Data7, _Data8, _Data9;
            int _Data10, _Data11, _Data12, _Data13, _Data14;
            int _Data15, _Data16, _Data17, _Data18, _Data19;

            v2f vert(appdata v)
            {
                v2f o;
                o.vertex = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                return o;
            }

            float4 SampleFontAtlas(int index, float2 uv)
            {
                return UNITY_SAMPLE_TEX2DARRAY(_FontAtlas, float3(uv, index));
            }

            character GetCharFromMemory(int index)
            {
                character rs;
                rs.codePoint = (_Bytes[index * 5] & 0xFF) << 16
                    | (_Bytes[index * 5 + 1] & 0xFF) << 8
                    | (_Bytes[index * 5 + 2] & 0xFF);

                uint rgb555 = (_Bytes[index * 5 + 3] & 0xFF) << 8
                    | (_Bytes[index * 5 + 4] & 0xFF);
                rgb555 = (rgb555 >> 1) & 0x7FFF;

                rs.color = float3(
                    rgb555 >> 10 & 0x1F,
                    rgb555 >> 5 & 0x1F,
                    rgb555 & 0x1F
                ) * (1.0 / 31.0);
                rs.invert = _Bytes[index * 5 + 4] & 1;
                return rs;
            }

            void WriteMemorySync()
            {
                int data0 = _Bytes[_Pointer * 5];
                int data1 = _Bytes[_Pointer * 5 + 1];
                int data2 = _Bytes[_Pointer * 5 + 2];
                int data3 = _Bytes[_Pointer * 5 + 3];
                int data4 = _Bytes[_Pointer * 5 + 4];
                
                int data5 = _Bytes[(_Pointer + 1) * 5];
                int data6 = _Bytes[(_Pointer + 1) * 5 + 1];
                int data7 = _Bytes[(_Pointer + 1) * 5 + 2];
                int data8 = _Bytes[(_Pointer + 1) * 5 + 3];
                int data9 = _Bytes[(_Pointer + 1) * 5 + 4];
                
                int data10 = _Bytes[(_Pointer + 2) * 5];
                int data11 = _Bytes[(_Pointer + 2) * 5 + 1];
                int data12 = _Bytes[(_Pointer + 2) * 5 + 2];
                int data13 = _Bytes[(_Pointer + 2) * 5 + 3];
                int data14 = _Bytes[(_Pointer + 2) * 5 + 4];
                
                int data15 = _Bytes[(_Pointer + 2) * 5];
                int data16 = _Bytes[(_Pointer + 2) * 5 + 1];
                int data17 = _Bytes[(_Pointer + 2) * 5 + 2];
                int data18 = _Bytes[(_Pointer + 2) * 5 + 3];
                int data19 = _Bytes[(_Pointer + 2) * 5 + 4];

                int mask_char_0 = (_Mask > 7 & 1) & (_Mask > 3 & 1);
                int mask_char_1 = (_Mask > 7 & 1) & (_Mask > 2 & 1);
                int mask_char_2 = (_Mask > 7 & 1) & (_Mask > 1 & 1);
                int mask_char_3 = (_Mask > 7 & 1) & (_Mask & 1);
                
                int mask_color_0 = (_Mask > 6 & 1) & (_Mask > 3 & 1);
                int mask_color_1 = (_Mask > 6 & 1) & (_Mask > 2 & 1);
                int mask_color_2 = (_Mask > 6 & 1) & (_Mask > 1 & 1);
                int mask_color_3 = (_Mask > 6 & 1) & (_Mask & 1);

                _Bytes[_Pointer * 5] = _Data0 * mask_char_0 + data0 * (1 - mask_char_0);
                _Bytes[_Pointer * 5 + 1] = _Data1 * mask_char_0 + data1 * (1 - mask_char_0);
                _Bytes[_Pointer * 5 + 2] = _Data2 * mask_char_0 + data2 * (1 - mask_char_0);
                _Bytes[_Pointer * 5 + 3] = _Data3 * mask_color_0 + data3 * (1 - mask_color_0);
                _Bytes[_Pointer * 5 + 4] = _Data4 * mask_color_0 + data4 * (1 - mask_color_0);
                
                _Bytes[(_Pointer + 1) * 5] = _Data5 * mask_char_1 + data5 * (1 - mask_char_1);
                _Bytes[(_Pointer + 1) * 5 + 1] = _Data6 * mask_char_1 + data6 * (1 - mask_char_1);
                _Bytes[(_Pointer + 1) * 5 + 2] = _Data7 * mask_char_1 + data7 * (1 - mask_char_1);
                _Bytes[(_Pointer + 1) * 5 + 3] = _Data8 * mask_color_1 + data8 * (1 - mask_color_1);
                _Bytes[(_Pointer + 1) * 5 + 4] = _Data9 * mask_color_1 + data9 * (1 - mask_color_1);
                
                _Bytes[(_Pointer + 2) * 5] = _Data10 * mask_char_2 + data10 * (1 - mask_char_2);
                _Bytes[(_Pointer + 2) * 5 + 1] = _Data11 * mask_char_2 + data11 * (1 - mask_char_2);
                _Bytes[(_Pointer + 2) * 5 + 2] = _Data12 * mask_char_2 + data12 * (1 - mask_char_2);
                _Bytes[(_Pointer + 2) * 5 + 3] = _Data13 * mask_color_2 + data13 * (1 - mask_color_2);
                _Bytes[(_Pointer + 2) * 5 + 4] = _Data14 * mask_color_2 + data14 * (1 - mask_color_2);
                
                _Bytes[(_Pointer + 3) * 5] = _Data15 * mask_char_3 + data15 * (1 - mask_char_3);
                _Bytes[(_Pointer + 3) * 5 + 1] = _Data16 * mask_char_3 + data16 * (1 - mask_char_3);
                _Bytes[(_Pointer + 3) * 5 + 2] = _Data17 * mask_char_3 + data17 * (1 - mask_char_3);
                _Bytes[(_Pointer + 3) * 5 + 3] = _Data18 * mask_color_3 + data18 * (1 - mask_color_3);
                _Bytes[(_Pointer + 3) * 5 + 4] = _Data19 * mask_color_3 + data19 * (1 - mask_color_3);
            }

            fixed4 frag(v2f i) : SV_Target
            {
                WriteMemorySync();

                int2 size = int2(floor(_Size.x), floor(_Size.y));
                // 计算当前UV对应的字符行列
                int row = floor((1 - i.uv.y) * size.y); // 反转Y轴
                int col = floor(i.uv.x * size.x); // 32列
                int index = row * size.x + col; // 字符索引 (0-127)
                character cChar = GetCharFromMemory(index);
                int codePoint = cChar.codePoint;

                // 计算贴图参数
                int fontIndex = codePoint >> 8; // 原始贴图索引
                int atlasIndex = fontIndex / 32; // 合并贴图索引 (0-7)
                int channelIndex = (fontIndex % 32) / 8; // 通道索引
                int bitIndex = 7 - (fontIndex % 8); // 位索引

                // 计算字符局部UV（归一化到当前字符）
                float2 charLocalUV = float2(
                    frac(i.uv.x * size.x), // 字符内X坐标 [0,1)
                    frac((1 - i.uv.y) * size.y) // 字符内Y坐标 [0,1)
                );
                // 计算阴影局部UV（基于字符内偏移）
                float2 shadowLocalUV = charLocalUV - _ShadowOffset / 16.0;

                // 计算字符位置
                int2 gridPos = int2(codePoint % 256 % 16, codePoint % 256 / 16);
                int2 charPos = int2(charLocalUV * 16);
                int2 pixelPos = gridPos * 16 + charPos;
                float2 uv = float2(pixelPos) / 256.0;
                uv.y = 1.0 - uv.y - 1.0 / 256.0;

                // 采样主字符
                float4 atlasData = SampleFontAtlas(atlasIndex, uv);
                float channel = atlasData[channelIndex];
                uint bits = (uint)(channel * 255 + 0.5);
                bool showMain = (bits & (1 << bitIndex)) != 0;

                // 采样阴影

                int2 shadowPixelPos = gridPos * 16 + int2(shadowLocalUV * 16);
                float2 shadowUVCoord = float2(shadowPixelPos) / 256.0;
                shadowUVCoord.y = 1.0 - shadowUVCoord.y - 1.0 / 256.0;

                // 边界检查
                bool validShadow = all(shadowLocalUV >= 0) && all(shadowLocalUV <= 1);
                float4 shadowData = validShadow ? SampleFontAtlas(atlasIndex, shadowUVCoord) : float4(0, 0, 0, 0);
                float shadowChannel = shadowData[channelIndex];
                uint shadowBits = (uint)(shadowChannel * 255 + 0.5);
                bool showShadow = validShadow && (shadowBits & (1 << bitIndex)) != 0;


                // 混合颜色
                fixed4 rs = _BackgroundColor;

                if (cChar.invert) // 判断是否为绘制背景色
                {
                    if (showMain) rs = _ShadowColor;
                    else
                    {
                        rs = half4(cChar.color, 1);
                    }
                }
                else
                {
                    if (showShadow) rs = _ShadowColor;
                    if (showMain)
                    {
                        rs = half4(cChar.color, 1);
                    }
                }

                return rs;
            }
            ENDCG
        }
    }
}