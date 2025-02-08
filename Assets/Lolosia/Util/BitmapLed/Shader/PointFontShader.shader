/**
 * 这是绘制一个字所需的Shader
 * 文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/PointFontShader"
{
    Properties
    {
        _High ("High Byte", Int) = 0
        _Low ("Low Byte", Int) = 0
        _MainColor ("Color", Color) = (1,1,1,1)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontAtlas0 ("Font Atlas 0", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas1 ("Font Atlas 1", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas2 ("Font Atlas 2", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas3 ("Font Atlas 3", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas4 ("Font Atlas 4", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas5 ("Font Atlas 5", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas6 ("Font Atlas 6", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas7 ("Font Atlas 7", 2D) = "white" {}
    }

    SubShader
    {
        Tags { "RenderType"="TransparentCutout" "Queue"="AlphaTest" }
        LOD 100

        Pass
        {
            AlphaToMask On
            CGPROGRAM
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

            int _High;
            int _Low;
            fixed4 _MainColor;
            fixed4 _ShadowColor;
            float2 _ShadowOffset;
            sampler2D _FontAtlas0;
            sampler2D _FontAtlas1;
            sampler2D _FontAtlas2;
            sampler2D _FontAtlas3;
            sampler2D _FontAtlas4;
            sampler2D _FontAtlas5;
            sampler2D _FontAtlas6;
            sampler2D _FontAtlas7;

            v2f vert (appdata v)
            {
                v2f o;
                o.vertex = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                return o;
            }

            float4 SampleFontAtlas(int index, float2 uv)
            {
                // 根据贴图索引选择对应的纹理
                if(index == 0) return tex2D(_FontAtlas0, uv);
                if(index == 1) return tex2D(_FontAtlas1, uv);
                if(index == 2) return tex2D(_FontAtlas2, uv);
                if(index == 3) return tex2D(_FontAtlas3, uv);
                if(index == 4) return tex2D(_FontAtlas4, uv);
                if(index == 5) return tex2D(_FontAtlas5, uv);
                if(index == 6) return tex2D(_FontAtlas6, uv);
                return tex2D(_FontAtlas7, uv);
            }

            fixed4 frag (v2f i) : SV_Target
            {
                // 组合字符编码
                int codePoint = (_High << 8) | _Low;
                
                // 计算贴图参数
                int fontIndex = codePoint >> 8;        // 原始贴图索引
                int atlasIndex = fontIndex / 32;       // 合并贴图索引 (0-7)
                int channelIndex = (fontIndex % 32) / 8; // 通道索引
                int bitIndex = 7 - (fontIndex % 8);      // 位索引

                // 计算字符位置
                int2 charPos = int2(i.uv * 16);
                int2 gridPos = int2(codePoint % 256 % 16, 15 - codePoint % 256 / 16);
                int2 pixelPos = gridPos * 16 + charPos;
                float2 uv = float2(pixelPos) / 256.0;

                // 采样主字符
                float4 atlasData = SampleFontAtlas(atlasIndex, uv);
                float channel = atlasData[channelIndex];
                uint bits = (uint)(channel * 255 + 0.5);
                bool showMain = (bits & (1 << bitIndex)) != 0;

                // 采样阴影
                float2 shadowUV = i.uv - _ShadowOffset / int2(16.0, -16.0);
                int2 shadowCharPos = int2(shadowUV * 16);
                int2 shadowPixelPos = gridPos * 16 + shadowCharPos;
                float2 shadowUVCoord = float2(shadowPixelPos) / 256.0;
                
                // 边界检查
                bool validShadow = all(shadowUV >= 0) && all(shadowUV <= 1);
                float4 shadowData = validShadow ? 
                    SampleFontAtlas(atlasIndex, shadowUVCoord) : float4(0,0,0,0);
                float shadowChannel = shadowData[channelIndex];
                uint shadowBits = (uint)(shadowChannel * 255 + 0.5);
                bool showShadow = validShadow && (shadowBits & (1 << bitIndex)) != 0;

                // 混合颜色
                fixed4 col = fixed4(0,0,0,0);
                if(showShadow) col = _ShadowColor;
                if(showMain) col = _MainColor;
                
                // 如果没有元素，设置为透明
                if (!showMain && !showShadow) {
                    col.a = 0;
                }
                
                return col;
            }
            ENDCG
        }
    }
}