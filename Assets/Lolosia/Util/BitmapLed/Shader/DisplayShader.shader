/**
 * 这是绘制点阵屏幕所需的Shader
 * 文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/DisplayShader"
{
    Properties
    {
        _Size ("Size", Vector) = (32,4,0,0)
        _MainColor ("Color", Color) = (1,1,1,1)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontAtlas0 ("Font Atlas 0", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas1 ("Font Atlas 1", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas2 ("Font Atlas 2", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas3 ("Font Atlas 3", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas4 ("Font Atlas 4", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas5 ("Font Atlas 5", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas6 ("Font Atlas 6", 2D) = "white" {}
        [NoScaleOffset] _FontAtlas7 ("Font Atlas 7", 2D) = "white" {}
        // 声明256个byte属性。
        [IntRange] _Byte0("Byte0", Range(0, 255)) = 0
        [IntRange] _Byte1("Byte1", Range(0, 255)) = 0
        [IntRange] _Byte2("Byte2", Range(0, 255)) = 0
        [IntRange] _Byte3("Byte3", Range(0, 255)) = 0
        [IntRange] _Byte4("Byte4", Range(0, 255)) = 0
        [IntRange] _Byte5("Byte5", Range(0, 255)) = 0
        [IntRange] _Byte6("Byte6", Range(0, 255)) = 0
        [IntRange] _Byte7("Byte7", Range(0, 255)) = 0
        [IntRange] _Byte8("Byte8", Range(0, 255)) = 0
        [IntRange] _Byte9("Byte9", Range(0, 255)) = 0
        [IntRange] _Byte10("Byte10", Range(0, 255)) = 0
        [IntRange] _Byte11("Byte11", Range(0, 255)) = 0
        [IntRange] _Byte12("Byte12", Range(0, 255)) = 0
        [IntRange] _Byte13("Byte13", Range(0, 255)) = 0
        [IntRange] _Byte14("Byte14", Range(0, 255)) = 0
        [IntRange] _Byte15("Byte15", Range(0, 255)) = 0
        [IntRange] _Byte16("Byte16", Range(0, 255)) = 0
        [IntRange] _Byte17("Byte17", Range(0, 255)) = 0
        [IntRange] _Byte18("Byte18", Range(0, 255)) = 0
        [IntRange] _Byte19("Byte19", Range(0, 255)) = 0
        [IntRange] _Byte20("Byte20", Range(0, 255)) = 0
        [IntRange] _Byte21("Byte21", Range(0, 255)) = 0
        [IntRange] _Byte22("Byte22", Range(0, 255)) = 0
        [IntRange] _Byte23("Byte23", Range(0, 255)) = 0
        [IntRange] _Byte24("Byte24", Range(0, 255)) = 0
        [IntRange] _Byte25("Byte25", Range(0, 255)) = 0
        [IntRange] _Byte26("Byte26", Range(0, 255)) = 0
        [IntRange] _Byte27("Byte27", Range(0, 255)) = 0
        [IntRange] _Byte28("Byte28", Range(0, 255)) = 0
        [IntRange] _Byte29("Byte29", Range(0, 255)) = 0
        [IntRange] _Byte30("Byte30", Range(0, 255)) = 0
        [IntRange] _Byte31("Byte31", Range(0, 255)) = 0
        [IntRange] _Byte32("Byte32", Range(0, 255)) = 0
        [IntRange] _Byte33("Byte33", Range(0, 255)) = 0
        [IntRange] _Byte34("Byte34", Range(0, 255)) = 0
        [IntRange] _Byte35("Byte35", Range(0, 255)) = 0
        [IntRange] _Byte36("Byte36", Range(0, 255)) = 0
        [IntRange] _Byte37("Byte37", Range(0, 255)) = 0
        [IntRange] _Byte38("Byte38", Range(0, 255)) = 0
        [IntRange] _Byte39("Byte39", Range(0, 255)) = 0
        [IntRange] _Byte40("Byte40", Range(0, 255)) = 0
        [IntRange] _Byte41("Byte41", Range(0, 255)) = 0
        [IntRange] _Byte42("Byte42", Range(0, 255)) = 0
        [IntRange] _Byte43("Byte43", Range(0, 255)) = 0
        [IntRange] _Byte44("Byte44", Range(0, 255)) = 0
        [IntRange] _Byte45("Byte45", Range(0, 255)) = 0
        [IntRange] _Byte46("Byte46", Range(0, 255)) = 0
        [IntRange] _Byte47("Byte47", Range(0, 255)) = 0
        [IntRange] _Byte48("Byte48", Range(0, 255)) = 0
        [IntRange] _Byte49("Byte49", Range(0, 255)) = 0
        [IntRange] _Byte50("Byte50", Range(0, 255)) = 0
        [IntRange] _Byte51("Byte51", Range(0, 255)) = 0
        [IntRange] _Byte52("Byte52", Range(0, 255)) = 0
        [IntRange] _Byte53("Byte53", Range(0, 255)) = 0
        [IntRange] _Byte54("Byte54", Range(0, 255)) = 0
        [IntRange] _Byte55("Byte55", Range(0, 255)) = 0
        [IntRange] _Byte56("Byte56", Range(0, 255)) = 0
        [IntRange] _Byte57("Byte57", Range(0, 255)) = 0
        [IntRange] _Byte58("Byte58", Range(0, 255)) = 0
        [IntRange] _Byte59("Byte59", Range(0, 255)) = 0
        [IntRange] _Byte60("Byte60", Range(0, 255)) = 0
        [IntRange] _Byte61("Byte61", Range(0, 255)) = 0
        [IntRange] _Byte62("Byte62", Range(0, 255)) = 0
        [IntRange] _Byte63("Byte63", Range(0, 255)) = 0
        [IntRange] _Byte64("Byte64", Range(0, 255)) = 0
        [IntRange] _Byte65("Byte65", Range(0, 255)) = 0
        [IntRange] _Byte66("Byte66", Range(0, 255)) = 0
        [IntRange] _Byte67("Byte67", Range(0, 255)) = 0
        [IntRange] _Byte68("Byte68", Range(0, 255)) = 0
        [IntRange] _Byte69("Byte69", Range(0, 255)) = 0
        [IntRange] _Byte70("Byte70", Range(0, 255)) = 0
        [IntRange] _Byte71("Byte71", Range(0, 255)) = 0
        [IntRange] _Byte72("Byte72", Range(0, 255)) = 0
        [IntRange] _Byte73("Byte73", Range(0, 255)) = 0
        [IntRange] _Byte74("Byte74", Range(0, 255)) = 0
        [IntRange] _Byte75("Byte75", Range(0, 255)) = 0
        [IntRange] _Byte76("Byte76", Range(0, 255)) = 0
        [IntRange] _Byte77("Byte77", Range(0, 255)) = 0
        [IntRange] _Byte78("Byte78", Range(0, 255)) = 0
        [IntRange] _Byte79("Byte79", Range(0, 255)) = 0
        [IntRange] _Byte80("Byte80", Range(0, 255)) = 0
        [IntRange] _Byte81("Byte81", Range(0, 255)) = 0
        [IntRange] _Byte82("Byte82", Range(0, 255)) = 0
        [IntRange] _Byte83("Byte83", Range(0, 255)) = 0
        [IntRange] _Byte84("Byte84", Range(0, 255)) = 0
        [IntRange] _Byte85("Byte85", Range(0, 255)) = 0
        [IntRange] _Byte86("Byte86", Range(0, 255)) = 0
        [IntRange] _Byte87("Byte87", Range(0, 255)) = 0
        [IntRange] _Byte88("Byte88", Range(0, 255)) = 0
        [IntRange] _Byte89("Byte89", Range(0, 255)) = 0
        [IntRange] _Byte90("Byte90", Range(0, 255)) = 0
        [IntRange] _Byte91("Byte91", Range(0, 255)) = 0
        [IntRange] _Byte92("Byte92", Range(0, 255)) = 0
        [IntRange] _Byte93("Byte93", Range(0, 255)) = 0
        [IntRange] _Byte94("Byte94", Range(0, 255)) = 0
        [IntRange] _Byte95("Byte95", Range(0, 255)) = 0
        [IntRange] _Byte96("Byte96", Range(0, 255)) = 0
        [IntRange] _Byte97("Byte97", Range(0, 255)) = 0
        [IntRange] _Byte98("Byte98", Range(0, 255)) = 0
        [IntRange] _Byte99("Byte99", Range(0, 255)) = 0
        [IntRange] _Byte100("Byte100", Range(0, 255)) = 0
        [IntRange] _Byte101("Byte101", Range(0, 255)) = 0
        [IntRange] _Byte102("Byte102", Range(0, 255)) = 0
        [IntRange] _Byte103("Byte103", Range(0, 255)) = 0
        [IntRange] _Byte104("Byte104", Range(0, 255)) = 0
        [IntRange] _Byte105("Byte105", Range(0, 255)) = 0
        [IntRange] _Byte106("Byte106", Range(0, 255)) = 0
        [IntRange] _Byte107("Byte107", Range(0, 255)) = 0
        [IntRange] _Byte108("Byte108", Range(0, 255)) = 0
        [IntRange] _Byte109("Byte109", Range(0, 255)) = 0
        [IntRange] _Byte110("Byte110", Range(0, 255)) = 0
        [IntRange] _Byte111("Byte111", Range(0, 255)) = 0
        [IntRange] _Byte112("Byte112", Range(0, 255)) = 0
        [IntRange] _Byte113("Byte113", Range(0, 255)) = 0
        [IntRange] _Byte114("Byte114", Range(0, 255)) = 0
        [IntRange] _Byte115("Byte115", Range(0, 255)) = 0
        [IntRange] _Byte116("Byte116", Range(0, 255)) = 0
        [IntRange] _Byte117("Byte117", Range(0, 255)) = 0
        [IntRange] _Byte118("Byte118", Range(0, 255)) = 0
        [IntRange] _Byte119("Byte119", Range(0, 255)) = 0
        [IntRange] _Byte120("Byte120", Range(0, 255)) = 0
        [IntRange] _Byte121("Byte121", Range(0, 255)) = 0
        [IntRange] _Byte122("Byte122", Range(0, 255)) = 0
        [IntRange] _Byte123("Byte123", Range(0, 255)) = 0
        [IntRange] _Byte124("Byte124", Range(0, 255)) = 0
        [IntRange] _Byte125("Byte125", Range(0, 255)) = 0
        [IntRange] _Byte126("Byte126", Range(0, 255)) = 0
        [IntRange] _Byte127("Byte127", Range(0, 255)) = 0
        [IntRange] _Byte128("Byte128", Range(0, 255)) = 0
        [IntRange] _Byte129("Byte129", Range(0, 255)) = 0
        [IntRange] _Byte130("Byte130", Range(0, 255)) = 0
        [IntRange] _Byte131("Byte131", Range(0, 255)) = 0
        [IntRange] _Byte132("Byte132", Range(0, 255)) = 0
        [IntRange] _Byte133("Byte133", Range(0, 255)) = 0
        [IntRange] _Byte134("Byte134", Range(0, 255)) = 0
        [IntRange] _Byte135("Byte135", Range(0, 255)) = 0
        [IntRange] _Byte136("Byte136", Range(0, 255)) = 0
        [IntRange] _Byte137("Byte137", Range(0, 255)) = 0
        [IntRange] _Byte138("Byte138", Range(0, 255)) = 0
        [IntRange] _Byte139("Byte139", Range(0, 255)) = 0
        [IntRange] _Byte140("Byte140", Range(0, 255)) = 0
        [IntRange] _Byte141("Byte141", Range(0, 255)) = 0
        [IntRange] _Byte142("Byte142", Range(0, 255)) = 0
        [IntRange] _Byte143("Byte143", Range(0, 255)) = 0
        [IntRange] _Byte144("Byte144", Range(0, 255)) = 0
        [IntRange] _Byte145("Byte145", Range(0, 255)) = 0
        [IntRange] _Byte146("Byte146", Range(0, 255)) = 0
        [IntRange] _Byte147("Byte147", Range(0, 255)) = 0
        [IntRange] _Byte148("Byte148", Range(0, 255)) = 0
        [IntRange] _Byte149("Byte149", Range(0, 255)) = 0
        [IntRange] _Byte150("Byte150", Range(0, 255)) = 0
        [IntRange] _Byte151("Byte151", Range(0, 255)) = 0
        [IntRange] _Byte152("Byte152", Range(0, 255)) = 0
        [IntRange] _Byte153("Byte153", Range(0, 255)) = 0
        [IntRange] _Byte154("Byte154", Range(0, 255)) = 0
        [IntRange] _Byte155("Byte155", Range(0, 255)) = 0
        [IntRange] _Byte156("Byte156", Range(0, 255)) = 0
        [IntRange] _Byte157("Byte157", Range(0, 255)) = 0
        [IntRange] _Byte158("Byte158", Range(0, 255)) = 0
        [IntRange] _Byte159("Byte159", Range(0, 255)) = 0
        [IntRange] _Byte160("Byte160", Range(0, 255)) = 0
        [IntRange] _Byte161("Byte161", Range(0, 255)) = 0
        [IntRange] _Byte162("Byte162", Range(0, 255)) = 0
        [IntRange] _Byte163("Byte163", Range(0, 255)) = 0
        [IntRange] _Byte164("Byte164", Range(0, 255)) = 0
        [IntRange] _Byte165("Byte165", Range(0, 255)) = 0
        [IntRange] _Byte166("Byte166", Range(0, 255)) = 0
        [IntRange] _Byte167("Byte167", Range(0, 255)) = 0
        [IntRange] _Byte168("Byte168", Range(0, 255)) = 0
        [IntRange] _Byte169("Byte169", Range(0, 255)) = 0
        [IntRange] _Byte170("Byte170", Range(0, 255)) = 0
        [IntRange] _Byte171("Byte171", Range(0, 255)) = 0
        [IntRange] _Byte172("Byte172", Range(0, 255)) = 0
        [IntRange] _Byte173("Byte173", Range(0, 255)) = 0
        [IntRange] _Byte174("Byte174", Range(0, 255)) = 0
        [IntRange] _Byte175("Byte175", Range(0, 255)) = 0
        [IntRange] _Byte176("Byte176", Range(0, 255)) = 0
        [IntRange] _Byte177("Byte177", Range(0, 255)) = 0
        [IntRange] _Byte178("Byte178", Range(0, 255)) = 0
        [IntRange] _Byte179("Byte179", Range(0, 255)) = 0
        [IntRange] _Byte180("Byte180", Range(0, 255)) = 0
        [IntRange] _Byte181("Byte181", Range(0, 255)) = 0
        [IntRange] _Byte182("Byte182", Range(0, 255)) = 0
        [IntRange] _Byte183("Byte183", Range(0, 255)) = 0
        [IntRange] _Byte184("Byte184", Range(0, 255)) = 0
        [IntRange] _Byte185("Byte185", Range(0, 255)) = 0
        [IntRange] _Byte186("Byte186", Range(0, 255)) = 0
        [IntRange] _Byte187("Byte187", Range(0, 255)) = 0
        [IntRange] _Byte188("Byte188", Range(0, 255)) = 0
        [IntRange] _Byte189("Byte189", Range(0, 255)) = 0
        [IntRange] _Byte190("Byte190", Range(0, 255)) = 0
        [IntRange] _Byte191("Byte191", Range(0, 255)) = 0
        [IntRange] _Byte192("Byte192", Range(0, 255)) = 0
        [IntRange] _Byte193("Byte193", Range(0, 255)) = 0
        [IntRange] _Byte194("Byte194", Range(0, 255)) = 0
        [IntRange] _Byte195("Byte195", Range(0, 255)) = 0
        [IntRange] _Byte196("Byte196", Range(0, 255)) = 0
        [IntRange] _Byte197("Byte197", Range(0, 255)) = 0
        [IntRange] _Byte198("Byte198", Range(0, 255)) = 0
        [IntRange] _Byte199("Byte199", Range(0, 255)) = 0
        [IntRange] _Byte200("Byte200", Range(0, 255)) = 0
        [IntRange] _Byte201("Byte201", Range(0, 255)) = 0
        [IntRange] _Byte202("Byte202", Range(0, 255)) = 0
        [IntRange] _Byte203("Byte203", Range(0, 255)) = 0
        [IntRange] _Byte204("Byte204", Range(0, 255)) = 0
        [IntRange] _Byte205("Byte205", Range(0, 255)) = 0
        [IntRange] _Byte206("Byte206", Range(0, 255)) = 0
        [IntRange] _Byte207("Byte207", Range(0, 255)) = 0
        [IntRange] _Byte208("Byte208", Range(0, 255)) = 0
        [IntRange] _Byte209("Byte209", Range(0, 255)) = 0
        [IntRange] _Byte210("Byte210", Range(0, 255)) = 0
        [IntRange] _Byte211("Byte211", Range(0, 255)) = 0
        [IntRange] _Byte212("Byte212", Range(0, 255)) = 0
        [IntRange] _Byte213("Byte213", Range(0, 255)) = 0
        [IntRange] _Byte214("Byte214", Range(0, 255)) = 0
        [IntRange] _Byte215("Byte215", Range(0, 255)) = 0
        [IntRange] _Byte216("Byte216", Range(0, 255)) = 0
        [IntRange] _Byte217("Byte217", Range(0, 255)) = 0
        [IntRange] _Byte218("Byte218", Range(0, 255)) = 0
        [IntRange] _Byte219("Byte219", Range(0, 255)) = 0
        [IntRange] _Byte220("Byte220", Range(0, 255)) = 0
        [IntRange] _Byte221("Byte221", Range(0, 255)) = 0
        [IntRange] _Byte222("Byte222", Range(0, 255)) = 0
        [IntRange] _Byte223("Byte223", Range(0, 255)) = 0
        [IntRange] _Byte224("Byte224", Range(0, 255)) = 0
        [IntRange] _Byte225("Byte225", Range(0, 255)) = 0
        [IntRange] _Byte226("Byte226", Range(0, 255)) = 0
        [IntRange] _Byte227("Byte227", Range(0, 255)) = 0
        [IntRange] _Byte228("Byte228", Range(0, 255)) = 0
        [IntRange] _Byte229("Byte229", Range(0, 255)) = 0
        [IntRange] _Byte230("Byte230", Range(0, 255)) = 0
        [IntRange] _Byte231("Byte231", Range(0, 255)) = 0
        [IntRange] _Byte232("Byte232", Range(0, 255)) = 0
        [IntRange] _Byte233("Byte233", Range(0, 255)) = 0
        [IntRange] _Byte234("Byte234", Range(0, 255)) = 0
        [IntRange] _Byte235("Byte235", Range(0, 255)) = 0
        [IntRange] _Byte236("Byte236", Range(0, 255)) = 0
        [IntRange] _Byte237("Byte237", Range(0, 255)) = 0
        [IntRange] _Byte238("Byte238", Range(0, 255)) = 0
        [IntRange] _Byte239("Byte239", Range(0, 255)) = 0
        [IntRange] _Byte240("Byte240", Range(0, 255)) = 0
        [IntRange] _Byte241("Byte241", Range(0, 255)) = 0
        [IntRange] _Byte242("Byte242", Range(0, 255)) = 0
        [IntRange] _Byte243("Byte243", Range(0, 255)) = 0
        [IntRange] _Byte244("Byte244", Range(0, 255)) = 0
        [IntRange] _Byte245("Byte245", Range(0, 255)) = 0
        [IntRange] _Byte246("Byte246", Range(0, 255)) = 0
        [IntRange] _Byte247("Byte247", Range(0, 255)) = 0
        [IntRange] _Byte248("Byte248", Range(0, 255)) = 0
        [IntRange] _Byte249("Byte249", Range(0, 255)) = 0
        [IntRange] _Byte250("Byte250", Range(0, 255)) = 0
        [IntRange] _Byte251("Byte251", Range(0, 255)) = 0
        [IntRange] _Byte252("Byte252", Range(0, 255)) = 0
        [IntRange] _Byte253("Byte253", Range(0, 255)) = 0
        [IntRange] _Byte254("Byte254", Range(0, 255)) = 0
        [IntRange] _Byte255("Byte255", Range(0, 255)) = 0
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

            float2 _Size;
            fixed4 _MainColor;
            fixed4 _ShadowColor;
            float4 _BackgroundColor;
            float2 _ShadowOffset;
            sampler2D _FontAtlas0;
            sampler2D _FontAtlas1;
            sampler2D _FontAtlas2;
            sampler2D _FontAtlas3;
            sampler2D _FontAtlas4;
            sampler2D _FontAtlas5;
            sampler2D _FontAtlas6;
            sampler2D _FontAtlas7;
            // byte[256]
            int _Byte0, _Byte1, _Byte2, _Byte3, _Byte4, _Byte5, _Byte6, _Byte7;
            int _Byte8, _Byte9, _Byte10, _Byte11, _Byte12, _Byte13, _Byte14, _Byte15;
            int _Byte16, _Byte17, _Byte18, _Byte19, _Byte20, _Byte21, _Byte22, _Byte23;
            int _Byte24, _Byte25, _Byte26, _Byte27, _Byte28, _Byte29, _Byte30, _Byte31;
            int _Byte32, _Byte33, _Byte34, _Byte35, _Byte36, _Byte37, _Byte38, _Byte39;
            int _Byte40, _Byte41, _Byte42, _Byte43, _Byte44, _Byte45, _Byte46, _Byte47;
            int _Byte48, _Byte49, _Byte50, _Byte51, _Byte52, _Byte53, _Byte54, _Byte55;
            int _Byte56, _Byte57, _Byte58, _Byte59, _Byte60, _Byte61, _Byte62, _Byte63;
            int _Byte64, _Byte65, _Byte66, _Byte67, _Byte68, _Byte69, _Byte70, _Byte71;
            int _Byte72, _Byte73, _Byte74, _Byte75, _Byte76, _Byte77, _Byte78, _Byte79;
            int _Byte80, _Byte81, _Byte82, _Byte83, _Byte84, _Byte85, _Byte86, _Byte87;
            int _Byte88, _Byte89, _Byte90, _Byte91, _Byte92, _Byte93, _Byte94, _Byte95;
            int _Byte96, _Byte97, _Byte98, _Byte99, _Byte100, _Byte101, _Byte102, _Byte103;
            int _Byte104, _Byte105, _Byte106, _Byte107, _Byte108, _Byte109, _Byte110, _Byte111;
            int _Byte112, _Byte113, _Byte114, _Byte115, _Byte116, _Byte117, _Byte118, _Byte119;
            int _Byte120, _Byte121, _Byte122, _Byte123, _Byte124, _Byte125, _Byte126, _Byte127;
            int _Byte128, _Byte129, _Byte130, _Byte131, _Byte132, _Byte133, _Byte134, _Byte135;
            int _Byte136, _Byte137, _Byte138, _Byte139, _Byte140, _Byte141, _Byte142, _Byte143;
            int _Byte144, _Byte145, _Byte146, _Byte147, _Byte148, _Byte149, _Byte150, _Byte151;
            int _Byte152, _Byte153, _Byte154, _Byte155, _Byte156, _Byte157, _Byte158, _Byte159;
            int _Byte160, _Byte161, _Byte162, _Byte163, _Byte164, _Byte165, _Byte166, _Byte167;
            int _Byte168, _Byte169, _Byte170, _Byte171, _Byte172, _Byte173, _Byte174, _Byte175;
            int _Byte176, _Byte177, _Byte178, _Byte179, _Byte180, _Byte181, _Byte182, _Byte183;
            int _Byte184, _Byte185, _Byte186, _Byte187, _Byte188, _Byte189, _Byte190, _Byte191;
            int _Byte192, _Byte193, _Byte194, _Byte195, _Byte196, _Byte197, _Byte198, _Byte199;
            int _Byte200, _Byte201, _Byte202, _Byte203, _Byte204, _Byte205, _Byte206, _Byte207;
            int _Byte208, _Byte209, _Byte210, _Byte211, _Byte212, _Byte213, _Byte214, _Byte215;
            int _Byte216, _Byte217, _Byte218, _Byte219, _Byte220, _Byte221, _Byte222, _Byte223;
            int _Byte224, _Byte225, _Byte226, _Byte227, _Byte228, _Byte229, _Byte230, _Byte231;
            int _Byte232, _Byte233, _Byte234, _Byte235, _Byte236, _Byte237, _Byte238, _Byte239;
            int _Byte240, _Byte241, _Byte242, _Byte243, _Byte244, _Byte245, _Byte246, _Byte247;
            int _Byte248, _Byte249, _Byte250, _Byte251, _Byte252, _Byte253, _Byte254, _Byte255;

            v2f vert(appdata v)
            {
                v2f o;
                o.vertex = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                return o;
            }

            float4 SampleFontAtlas(int index, float2 uv)
            {
                // 根据贴图索引选择对应的纹理
                if (index == 0) return tex2D(_FontAtlas0, uv);
                if (index == 1) return tex2D(_FontAtlas1, uv);
                if (index == 2) return tex2D(_FontAtlas2, uv);
                if (index == 3) return tex2D(_FontAtlas3, uv);
                if (index == 4) return tex2D(_FontAtlas4, uv);
                if (index == 5) return tex2D(_FontAtlas5, uv);
                if (index == 6) return tex2D(_FontAtlas6, uv);
                return tex2D(_FontAtlas7, uv);
            }

            int GetCodePoint(int index)
            {
                // 根据索引返回对应的编码（需补全所有128个条件）
                if (index == 0) return _Byte1 | _Byte0 << 8;
                if (index == 1) return _Byte3 | _Byte2 << 8;
                if (index == 2) return _Byte5 | _Byte4 << 8;
                if (index == 3) return _Byte7 | _Byte6 << 8;
                if (index == 4) return _Byte9 | _Byte8 << 8;
                if (index == 5) return _Byte11 | _Byte10 << 8;
                if (index == 6) return _Byte13 | _Byte12 << 8;
                if (index == 7) return _Byte15 | _Byte14 << 8;
                if (index == 8) return _Byte17 | _Byte16 << 8;
                if (index == 9) return _Byte19 | _Byte18 << 8;
                if (index == 10) return _Byte21 | _Byte20 << 8;
                if (index == 11) return _Byte23 | _Byte22 << 8;
                if (index == 12) return _Byte25 | _Byte24 << 8;
                if (index == 13) return _Byte27 | _Byte26 << 8;
                if (index == 14) return _Byte29 | _Byte28 << 8;
                if (index == 15) return _Byte31 | _Byte30 << 8;
                if (index == 16) return _Byte33 | _Byte32 << 8;
                if (index == 17) return _Byte35 | _Byte34 << 8;
                if (index == 18) return _Byte37 | _Byte36 << 8;
                if (index == 19) return _Byte39 | _Byte38 << 8;
                if (index == 20) return _Byte41 | _Byte40 << 8;
                if (index == 21) return _Byte43 | _Byte42 << 8;
                if (index == 22) return _Byte45 | _Byte44 << 8;
                if (index == 23) return _Byte47 | _Byte46 << 8;
                if (index == 24) return _Byte49 | _Byte48 << 8;
                if (index == 25) return _Byte51 | _Byte50 << 8;
                if (index == 26) return _Byte53 | _Byte52 << 8;
                if (index == 27) return _Byte55 | _Byte54 << 8;
                if (index == 28) return _Byte57 | _Byte56 << 8;
                if (index == 29) return _Byte59 | _Byte58 << 8;
                if (index == 30) return _Byte61 | _Byte60 << 8;
                if (index == 31) return _Byte63 | _Byte62 << 8;
                if (index == 32) return _Byte65 | _Byte64 << 8;
                if (index == 33) return _Byte67 | _Byte66 << 8;
                if (index == 34) return _Byte69 | _Byte68 << 8;
                if (index == 35) return _Byte71 | _Byte70 << 8;
                if (index == 36) return _Byte73 | _Byte72 << 8;
                if (index == 37) return _Byte75 | _Byte74 << 8;
                if (index == 38) return _Byte77 | _Byte76 << 8;
                if (index == 39) return _Byte79 | _Byte78 << 8;
                if (index == 40) return _Byte81 | _Byte80 << 8;
                if (index == 41) return _Byte83 | _Byte82 << 8;
                if (index == 42) return _Byte85 | _Byte84 << 8;
                if (index == 43) return _Byte87 | _Byte86 << 8;
                if (index == 44) return _Byte89 | _Byte88 << 8;
                if (index == 45) return _Byte91 | _Byte90 << 8;
                if (index == 46) return _Byte93 | _Byte92 << 8;
                if (index == 47) return _Byte95 | _Byte94 << 8;
                if (index == 48) return _Byte97 | _Byte96 << 8;
                if (index == 49) return _Byte99 | _Byte98 << 8;
                if (index == 50) return _Byte101 | _Byte100 << 8;
                if (index == 51) return _Byte103 | _Byte102 << 8;
                if (index == 52) return _Byte105 | _Byte104 << 8;
                if (index == 53) return _Byte107 | _Byte106 << 8;
                if (index == 54) return _Byte109 | _Byte108 << 8;
                if (index == 55) return _Byte111 | _Byte110 << 8;
                if (index == 56) return _Byte113 | _Byte112 << 8;
                if (index == 57) return _Byte115 | _Byte114 << 8;
                if (index == 58) return _Byte117 | _Byte116 << 8;
                if (index == 59) return _Byte119 | _Byte118 << 8;
                if (index == 60) return _Byte121 | _Byte120 << 8;
                if (index == 61) return _Byte123 | _Byte122 << 8;
                if (index == 62) return _Byte125 | _Byte124 << 8;
                if (index == 63) return _Byte127 | _Byte126 << 8;
                if (index == 64) return _Byte129 | _Byte128 << 8;
                if (index == 65) return _Byte131 | _Byte130 << 8;
                if (index == 66) return _Byte133 | _Byte132 << 8;
                if (index == 67) return _Byte135 | _Byte134 << 8;
                if (index == 68) return _Byte137 | _Byte136 << 8;
                if (index == 69) return _Byte139 | _Byte138 << 8;
                if (index == 70) return _Byte141 | _Byte140 << 8;
                if (index == 71) return _Byte143 | _Byte142 << 8;
                if (index == 72) return _Byte145 | _Byte144 << 8;
                if (index == 73) return _Byte147 | _Byte146 << 8;
                if (index == 74) return _Byte149 | _Byte148 << 8;
                if (index == 75) return _Byte151 | _Byte150 << 8;
                if (index == 76) return _Byte153 | _Byte152 << 8;
                if (index == 77) return _Byte155 | _Byte154 << 8;
                if (index == 78) return _Byte157 | _Byte156 << 8;
                if (index == 79) return _Byte159 | _Byte158 << 8;
                if (index == 80) return _Byte161 | _Byte160 << 8;
                if (index == 81) return _Byte163 | _Byte162 << 8;
                if (index == 82) return _Byte165 | _Byte164 << 8;
                if (index == 83) return _Byte167 | _Byte166 << 8;
                if (index == 84) return _Byte169 | _Byte168 << 8;
                if (index == 85) return _Byte171 | _Byte170 << 8;
                if (index == 86) return _Byte173 | _Byte172 << 8;
                if (index == 87) return _Byte175 | _Byte174 << 8;
                if (index == 88) return _Byte177 | _Byte176 << 8;
                if (index == 89) return _Byte179 | _Byte178 << 8;
                if (index == 90) return _Byte181 | _Byte180 << 8;
                if (index == 91) return _Byte183 | _Byte182 << 8;
                if (index == 92) return _Byte185 | _Byte184 << 8;
                if (index == 93) return _Byte187 | _Byte186 << 8;
                if (index == 94) return _Byte189 | _Byte188 << 8;
                if (index == 95) return _Byte191 | _Byte190 << 8;
                if (index == 96) return _Byte193 | _Byte192 << 8;
                if (index == 97) return _Byte195 | _Byte194 << 8;
                if (index == 98) return _Byte197 | _Byte196 << 8;
                if (index == 99) return _Byte199 | _Byte198 << 8;
                if (index == 100) return _Byte201 | _Byte200 << 8;
                if (index == 101) return _Byte203 | _Byte202 << 8;
                if (index == 102) return _Byte205 | _Byte204 << 8;
                if (index == 103) return _Byte207 | _Byte206 << 8;
                if (index == 104) return _Byte209 | _Byte208 << 8;
                if (index == 105) return _Byte211 | _Byte210 << 8;
                if (index == 106) return _Byte213 | _Byte212 << 8;
                if (index == 107) return _Byte215 | _Byte214 << 8;
                if (index == 108) return _Byte217 | _Byte216 << 8;
                if (index == 109) return _Byte219 | _Byte218 << 8;
                if (index == 110) return _Byte221 | _Byte220 << 8;
                if (index == 111) return _Byte223 | _Byte222 << 8;
                if (index == 112) return _Byte225 | _Byte224 << 8;
                if (index == 113) return _Byte227 | _Byte226 << 8;
                if (index == 114) return _Byte229 | _Byte228 << 8;
                if (index == 115) return _Byte231 | _Byte230 << 8;
                if (index == 116) return _Byte233 | _Byte232 << 8;
                if (index == 117) return _Byte235 | _Byte234 << 8;
                if (index == 118) return _Byte237 | _Byte236 << 8;
                if (index == 119) return _Byte239 | _Byte238 << 8;
                if (index == 120) return _Byte241 | _Byte240 << 8;
                if (index == 121) return _Byte243 | _Byte242 << 8;
                if (index == 122) return _Byte245 | _Byte244 << 8;
                if (index == 123) return _Byte247 | _Byte246 << 8;
                if (index == 124) return _Byte249 | _Byte248 << 8;
                if (index == 125) return _Byte251 | _Byte250 << 8;
                if (index == 126) return _Byte253 | _Byte252 << 8;
                if (index == 127) return _Byte255 | _Byte254 << 8;
                return 0;
            }

            fixed4 frag(v2f i) : SV_Target
            {
                int2 size = int2(floor(_Size.x), floor(_Size.y));
                // 计算当前UV对应的字符行列
                int row = floor((1 - i.uv.y) * size.y); // 反转Y轴
                int col = floor(i.uv.x * size.x); // 32列
                int index = row * size.x + col; // 字符索引 (0-127)
                int codePoint = GetCodePoint(index);

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
                if (showShadow) rs = _ShadowColor;
                if (showMain) rs = _MainColor;

                return rs;
            }
            ENDCG
        }
    }
}