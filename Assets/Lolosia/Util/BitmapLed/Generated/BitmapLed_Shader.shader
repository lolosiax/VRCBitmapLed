/**
 * 这是绘制点阵屏幕所需的Shader, 文件由 C# 脚本自动生成，请勿修改
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */

Shader "Unlit/DisplayShaderRGB50"
{
    Properties
    {
        _Size ("Size", Vector) = (32,4,0,0)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontAtlas ("Font Atlas", 2DArray) = "white" {}
        [NoScaleOffset] _FontAtlasGB2312 ("Font Atlas GB2312", 2DArray) = "white" {}
        // [IntRange] _Data0("Data0", Range(0, 255)) = 0
        
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
        [IntRange] _Data20("Data20", Range(0, 255)) = 0
        [IntRange] _Data21("Data21", Range(0, 255)) = 0
        [IntRange] _Data22("Data22", Range(0, 255)) = 0
        [IntRange] _Data23("Data23", Range(0, 255)) = 0
        [IntRange] _Data24("Data24", Range(0, 255)) = 0
        [IntRange] _Data25("Data25", Range(0, 255)) = 0
        [IntRange] _Data26("Data26", Range(0, 255)) = 0
        [IntRange] _Data27("Data27", Range(0, 255)) = 0
        [IntRange] _Data28("Data28", Range(0, 255)) = 0
        [IntRange] _Data29("Data29", Range(0, 255)) = 0
        [IntRange] _Data30("Data30", Range(0, 255)) = 0
        [IntRange] _Data31("Data31", Range(0, 255)) = 0
        [IntRange] _Data32("Data32", Range(0, 255)) = 0
        [IntRange] _Data33("Data33", Range(0, 255)) = 0
        [IntRange] _Data34("Data34", Range(0, 255)) = 0
        [IntRange] _Data35("Data35", Range(0, 255)) = 0
        [IntRange] _Data36("Data36", Range(0, 255)) = 0
        [IntRange] _Data37("Data37", Range(0, 255)) = 0
        [IntRange] _Data38("Data38", Range(0, 255)) = 0
        [IntRange] _Data39("Data39", Range(0, 255)) = 0
        [IntRange] _Data40("Data40", Range(0, 255)) = 0
        [IntRange] _Data41("Data41", Range(0, 255)) = 0
        [IntRange] _Data42("Data42", Range(0, 255)) = 0
        [IntRange] _Data43("Data43", Range(0, 255)) = 0
        [IntRange] _Data44("Data44", Range(0, 255)) = 0
        [IntRange] _Data45("Data45", Range(0, 255)) = 0
        [IntRange] _Data46("Data46", Range(0, 255)) = 0
        [IntRange] _Data47("Data47", Range(0, 255)) = 0
        [IntRange] _Data48("Data48", Range(0, 255)) = 0
        [IntRange] _Data49("Data49", Range(0, 255)) = 0
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
            #pragma vertex vert
            #pragma fragment frag

            #include "UnityCG.cginc"
            #define INT_MEMORY_LENGTH 13

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
                uint code_point;
                fixed3 color;
                fixed3 color_ext;
                bool invert;
            };

            float2 _Size;
            fixed4 _ShadowColor;
            float4 _BackgroundColor;
            float2 _ShadowOffset;
            UNITY_DECLARE_TEX2DARRAY(_FontAtlas);
            UNITY_DECLARE_TEX2DARRAY(_FontAtlasGB2312);

            uint _Pointer, _Mask;
            // uint _Data0, _Data1, _Data2, _Data3, _Data4;
            
            uint _Data0, _Data1, _Data2, _Data3, _Data4, _Data5, _Data6, _Data7;
            uint _Data8, _Data9, _Data10, _Data11, _Data12, _Data13, _Data14, _Data15;
            uint _Data16, _Data17, _Data18, _Data19, _Data20, _Data21, _Data22, _Data23;
            uint _Data24, _Data25, _Data26, _Data27, _Data28, _Data29, _Data30, _Data31;
            uint _Data32, _Data33, _Data34, _Data35, _Data36, _Data37, _Data38, _Data39;
            uint _Data40, _Data41, _Data42, _Data43, _Data44, _Data45, _Data46, _Data47;
            uint _Data48, _Data49, _Data50, _Data51, _Data52, _Data53, _Data54, _Data55;

            v2f vert(appdata v)
            {
                v2f o;
                o.vertex = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                return o;
            }

            float4 SampleFontAtlas(uint group, int index, float2 uv)
            {
                float4 groups[2] = {
                    UNITY_SAMPLE_TEX2DARRAY(_FontAtlas, float3(uv, index)),
                    UNITY_SAMPLE_TEX2DARRAY(_FontAtlasGB2312, float3(uv, index))
                };
                return groups[group];
            }

            character GetCharFromMemory(uint ints[INT_MEMORY_LENGTH], uint index)
            {
                uint bytes[5];
                for (int i = 0; i < 5; i++)
                {
                    uint currentByteIndex = index * 5 + i;
                    uint intIndex = currentByteIndex / 4; // 计算当前int在数组中的索引
                    uint byteOffset = currentByteIndex % 4; // 计算int内的字节偏移（0~3）
                    uint currentInt = ints[intIndex]; // 获取对应的int值

                    // 通过移位和掩码提取目标字节
                    uint shift = 24 - byteOffset * 8;
                    bytes[i] = currentInt >> shift & 0xFF;
                }

                character rs;
                rs.code_point = bytes[0] << 16 | bytes[1] << 8 | bytes[2];
                rs.invert = bytes[3] >> 7 & 1;

                uint rgbs = bytes[3] << 8 | bytes[4];

                float3 rgb555 = float3(
                    rgbs >> 10 & 0x1F,
                    rgbs >> 5 & 0x1F,
                    rgbs & 0x1F
                ) * (1.0 / 31.0);

                float3 rgb444 = float3(
                    rgbs >> 8 & 0xF,
                    rgbs >> 4 & 0xF,
                    rgbs & 0xF
                ) * (1.0 / 15.0);

                rs.color = lerp(rgb555, rgb444, float(rs.invert));
                rs.color_ext = float3(bytes[3] >> 6 & 1, bytes[3] >> 5 & 1, bytes[3] >> 4 & 1) * rs.invert;
                return rs;
            }

            fixed4 DrawUnicode(float2 uv, character ch);
            fixed4 DrawGB2312(float2 uv, character ch);
            fixed4 DrawCommonText(float2 uv, character ch, uint group);
            fixed4 DrawBitmap(float2 uv, character ch);

            fixed4 frag(v2f i) : SV_Target
            {
                uint bytes[INT_MEMORY_LENGTH] = {
                    // ((_Data0 & 0xFF) << 24) | ((_Data1 & 0xFF) << 16) | ((_Data2 & 0xFF) << 8) | (_Data3 & 0xFF),

                    ((_Data0 & 0xFF) << 24) | ((_Data1 & 0xFF) << 16) | ((_Data2 & 0xFF) << 8) | (_Data3 & 0xFF),
                    ((_Data4 & 0xFF) << 24) | ((_Data5 & 0xFF) << 16) | ((_Data6 & 0xFF) << 8) | (_Data7 & 0xFF),
                    ((_Data8 & 0xFF) << 24) | ((_Data9 & 0xFF) << 16) | ((_Data10 & 0xFF) << 8) | (_Data11 & 0xFF),
                    ((_Data12 & 0xFF) << 24) | ((_Data13 & 0xFF) << 16) | ((_Data14 & 0xFF) << 8) | (_Data15 & 0xFF),
                    ((_Data16 & 0xFF) << 24) | ((_Data17 & 0xFF) << 16) | ((_Data18 & 0xFF) << 8) | (_Data19 & 0xFF),
                    ((_Data20 & 0xFF) << 24) | ((_Data21 & 0xFF) << 16) | ((_Data22 & 0xFF) << 8) | (_Data23 & 0xFF),
                    ((_Data24 & 0xFF) << 24) | ((_Data25 & 0xFF) << 16) | ((_Data26 & 0xFF) << 8) | (_Data27 & 0xFF),
                    ((_Data28 & 0xFF) << 24) | ((_Data29 & 0xFF) << 16) | ((_Data30 & 0xFF) << 8) | (_Data31 & 0xFF),
                    ((_Data32 & 0xFF) << 24) | ((_Data33 & 0xFF) << 16) | ((_Data34 & 0xFF) << 8) | (_Data35 & 0xFF),
                    ((_Data36 & 0xFF) << 24) | ((_Data37 & 0xFF) << 16) | ((_Data38 & 0xFF) << 8) | (_Data39 & 0xFF),
                    ((_Data40 & 0xFF) << 24) | ((_Data41 & 0xFF) << 16) | ((_Data42 & 0xFF) << 8) | (_Data43 & 0xFF),
                    ((_Data44 & 0xFF) << 24) | ((_Data45 & 0xFF) << 16) | ((_Data46 & 0xFF) << 8) | (_Data47 & 0xFF),
                    ((_Data48 & 0xFF) << 24) | ((_Data49 & 0xFF) << 16) | ((_Data50 & 0xFF) << 8) | (_Data51 & 0xFF),
                };

                int2 size = int2(floor(_Size.x), floor(_Size.y));
                // 计算当前UV对应的字符行列
                uint row = floor((1 - i.uv.y) * size.y); // 反转Y轴
                uint col = floor(i.uv.x * size.x); // 32列
                uint index = row * size.x + col; // 字符索引 (0-127)
                character cChar = GetCharFromMemory(bytes, index);

                // 计算字符局部UV（归一化到当前字符）
                float2 charLocalUV = float2(
                    frac(i.uv.x * size.x), // 字符内X坐标 [0,1)
                    frac((1 - i.uv.y) * size.y) // 字符内Y坐标 [0,1)
                );

                fixed4 colors[3] = {
                    DrawUnicode(charLocalUV, cChar),
                    DrawGB2312(charLocalUV, cChar),
                    DrawBitmap(charLocalUV, cChar)
                };

                uint bmp_layer = cChar.code_point >> 16 & 0xff;
                bool isBitmap = bmp_layer == 255;
                bool isGb2312 = bmp_layer == 254;

                uint index1 = uint(isBitmap) * 2 + uint(isGb2312);

                fixed4 rs = colors[index1];

                return rs;
            }

            fixed4 DrawUnicode(float2 uv, character ch)
            {
                return DrawCommonText(uv, ch, 0);
            }

            fixed4 DrawGB2312(float2 uv, character ch)
            {
                ch.code_point = ch.code_point & 0xffff;
                return DrawCommonText(uv, ch, 1);
            }

            fixed4 DrawCommonText(float2 uv, character ch, uint group)
            {
                uint codePoint = ch.code_point;

                // 计算贴图参数
                uint fontIndex = codePoint >> 8; // 原始贴图索引
                uint atlasIndex = fontIndex / 32; // 合并贴图索引 (0-7)
                uint channelIndex = fontIndex % 32 / 8; // 通道索引
                uint bitIndex = 7 - fontIndex % 8; // 位索引

                // 计算阴影局部UV（基于字符内偏移）
                float2 shadowLocalUV = uv - _ShadowOffset / 16.0;

                // 计算字符位置
                int2 gridPos = int2(codePoint % 256 % 16, codePoint % 256 / 16);
                int2 charPos = int2(uv * 16);
                int2 pixelPos = gridPos * 16 + charPos;
                float2 fa_uv = float2(pixelPos) / 256.0;
                fa_uv.y = 1.0 - fa_uv.y - 1.0 / 256.0;

                // 采样主字符
                float4 atlasData = SampleFontAtlas(group, atlasIndex, fa_uv);
                float channel = atlasData[channelIndex];
                uint bits = (uint)(channel * 255 + 0.5);
                bool showMain = (bits & 1 << bitIndex) != 0;

                // 采样阴影

                int2 shadowPixelPos = gridPos * 16 + int2(shadowLocalUV * 16);
                float2 shadowUVCoord = float2(shadowPixelPos) / 256.0;
                shadowUVCoord.y = 1.0 - shadowUVCoord.y - 1.0 / 256.0;

                // 边界检查
                bool validShadow = all(shadowLocalUV >= 0) && all(shadowLocalUV <= 1);
                float4 shadowData = validShadow
            ? SampleFontAtlas(group, atlasIndex, shadowUVCoord)
            : float4(0, 0, 0, 0);
                float shadowChannel = shadowData[channelIndex];
                uint shadowBits = (uint)(shadowChannel * 255 + 0.5);
                bool showShadow = validShadow && (shadowBits & 1 << bitIndex) != 0;

                bool hasColor = ch.invert | showShadow | showMain;
                half3 drawColorDefault = lerp(_ShadowColor, ch.color, float(showMain));
                half3 drawColorInvert = lerp(ch.color, ch.color_ext, float(showMain));
                half3 drawColor = lerp(drawColorDefault, drawColorInvert, float(ch.invert));

                fixed4 rs = lerp(_BackgroundColor, fixed4(drawColor, 1), float(hasColor));

                return rs;
            }

            fixed4 DrawBitmap(float2 uv, character ch)
            {
                uint data = ch.code_point & 0xffff;
                // 将UV映射到4x4网格坐标
                uint col = floor(uv.x * 4); // X轴对应列 [0-3]
                uint row = floor((1 - uv.y) * 4); // 反转Y轴对应行 [0-3]

                // 计算bit索引（行优先布局）
                // 最高位bit15 -> 第0行第0列
                // bit14 -> 第0行第1列 ... bit0 -> 第3行第3列
                uint bit_index = row * 4 + col;

                // 提取对应bit的值（从高位开始）
                uint bit = data >> 15 - bit_index & 1;

                // 计算颜色：1=白色，0=黑色
                fixed3 color = fixed3(bit, bit, bit);

                return fixed4(color, 1.0);
            }
            ENDCG
        }
    }
}