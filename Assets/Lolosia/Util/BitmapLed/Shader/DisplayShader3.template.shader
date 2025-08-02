/**
 * 这是绘制点阵屏幕所需的Shader, 文件由 C# 脚本自动生成，请勿修改
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */

/*%_Line_Shader_Name_%*/ Shader "Unlit/DisplayShaderTemplate_B3"
{
    Properties
    {
        _Size ("Size", Vector) = (32,4,0,0)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontColor ("Font Color", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas ("Font Atlas", 2DArray) = "white" {}
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
                bool invert;
            };

            float2 _Size;
            fixed4 _ShadowColor;
            float4 _BackgroundColor;
            float2 _ShadowOffset;
            sampler2D _FontColor;
            UNITY_DECLARE_TEX2DARRAY(_FontAtlas);

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
                return UNITY_SAMPLE_TEX2DARRAY(_FontAtlas, float3(uv, index));
            }

            character GetCharFromMemory(uint ints[INT_MEMORY_LENGTH], uint index)
            {
                uint bytes[3];
                for (int i = 0; i < 2; i++)
                {
                    uint currentByteIndex = index * 2 + i;
                    uint intIndex = currentByteIndex / 4; // 计算当前int在数组中的索引
                    uint byteOffset = currentByteIndex % 4; // 计算int内的字节偏移（0~3）
                    uint currentInt = ints[intIndex]; // 获取对应的int值

                    // 通过移位和掩码提取目标字节
                    uint shift = 24 - byteOffset * 8;
                    bytes[i] = currentInt >> shift & 0xFF;
                }
                {
                    uint currentByteIndex = 256 + index;
                    uint intIndex = currentByteIndex / 4; // 计算当前int在数组中的索引
                    uint byteOffset = currentByteIndex % 4; // 计算int内的字节偏移（0~3）
                    uint currentInt = ints[intIndex]; // 获取对应的int值

                    // 通过移位和掩码提取目标字节
                    uint shift = 24 - byteOffset * 8;
                    bytes[2] = currentInt >> shift & 0xFF;
                }

                character rs;
                rs.code_point = bytes[0] << 8 | bytes[1];

                // 获取字体颜色所在行列
                int fontColorCol = (bytes[2] >> 4) & 0xf;
                int fontColorRow = bytes[2] & 0xf;
                float2 fontColorUV = float2(fontColorCol / 16.0, fontColorRow / 16.0);
                rs.color = tex2D(_FontColor, fontColorUV);
                rs.invert = fontColorCol > 13;

                return rs;
            }

            fixed4 DrawUnicode(float2 uv, character ch);
            fixed4 DrawCommonText(float2 uv, character ch, uint group);

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

                return DrawUnicode(charLocalUV, cChar);
            }

            fixed4 DrawUnicode(float2 uv, character ch)
            {
                return DrawCommonText(uv, ch, 0);
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
                half3 drawColorInvert = lerp(ch.color, float3(0, 0, 0), float(showMain));
                half3 drawColor = lerp(drawColorDefault, drawColorInvert, float(ch.invert));

                fixed4 rs = lerp(_BackgroundColor, fixed4(drawColor, 1), float(hasColor));

                return rs;
            }
            ENDCG
        }
    }
}