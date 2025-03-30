/**
 * 这是绘制点阵屏幕所需的Shader, 文件由 C# 脚本自动生成，请勿修改
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */

/*%_Line_Shader_Name_%*/ Shader "Unlit/DisplayShaderTemplate"
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
        /*%_Data_Prop_Def_%*/
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
            #define /*%_Line_Int_Memory_Length_%*/ INT_MEMORY_LENGTH 1

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
            /*%_Data_Field_Def_%*/

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
                    /*%_Line_Int_Array_Content_%*/ 0
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