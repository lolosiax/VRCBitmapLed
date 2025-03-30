/**
 * 这是绘制纯英文点阵屏幕所需的Shader，版本比较老旧，不支持行列调整
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/DisplayShaderASCII"
{
    Properties
    {
        _Size ("Size", Vector) = (64,4,0,0)
        _MainColor ("Color", Color) = (1,1,1,1)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontAtlas ("Font Atlas", 2D) = "white" {}
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
            sampler2D _FontAtlas;
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

            int GetCodePoint(int index)
            {
                // 根据索引返回对应的编码（需补全所有128个条件）
                if (index == 0) return _Byte0;
                if (index == 1) return _Byte1;
                if (index == 2) return _Byte2;
                if (index == 3) return _Byte3;
                if (index == 4) return _Byte4;
                if (index == 5) return _Byte5;
                if (index == 6) return _Byte6;
                if (index == 7) return _Byte7;
                if (index == 8) return _Byte8;
                if (index == 9) return _Byte9;
                if (index == 10) return _Byte10;
                if (index == 11) return _Byte11;
                if (index == 12) return _Byte12;
                if (index == 13) return _Byte13;
                if (index == 14) return _Byte14;
                if (index == 15) return _Byte15;
                if (index == 16) return _Byte16;
                if (index == 17) return _Byte17;
                if (index == 18) return _Byte18;
                if (index == 19) return _Byte19;
                if (index == 20) return _Byte20;
                if (index == 21) return _Byte21;
                if (index == 22) return _Byte22;
                if (index == 23) return _Byte23;
                if (index == 24) return _Byte24;
                if (index == 25) return _Byte25;
                if (index == 26) return _Byte26;
                if (index == 27) return _Byte27;
                if (index == 28) return _Byte28;
                if (index == 29) return _Byte29;
                if (index == 30) return _Byte30;
                if (index == 31) return _Byte31;
                if (index == 32) return _Byte32;
                if (index == 33) return _Byte33;
                if (index == 34) return _Byte34;
                if (index == 35) return _Byte35;
                if (index == 36) return _Byte36;
                if (index == 37) return _Byte37;
                if (index == 38) return _Byte38;
                if (index == 39) return _Byte39;
                if (index == 40) return _Byte40;
                if (index == 41) return _Byte41;
                if (index == 42) return _Byte42;
                if (index == 43) return _Byte43;
                if (index == 44) return _Byte44;
                if (index == 45) return _Byte45;
                if (index == 46) return _Byte46;
                if (index == 47) return _Byte47;
                if (index == 48) return _Byte48;
                if (index == 49) return _Byte49;
                if (index == 50) return _Byte50;
                if (index == 51) return _Byte51;
                if (index == 52) return _Byte52;
                if (index == 53) return _Byte53;
                if (index == 54) return _Byte54;
                if (index == 55) return _Byte55;
                if (index == 56) return _Byte56;
                if (index == 57) return _Byte57;
                if (index == 58) return _Byte58;
                if (index == 59) return _Byte59;
                if (index == 60) return _Byte60;
                if (index == 61) return _Byte61;
                if (index == 62) return _Byte62;
                if (index == 63) return _Byte63;
                if (index == 64) return _Byte64;
                if (index == 65) return _Byte65;
                if (index == 66) return _Byte66;
                if (index == 67) return _Byte67;
                if (index == 68) return _Byte68;
                if (index == 69) return _Byte69;
                if (index == 70) return _Byte70;
                if (index == 71) return _Byte71;
                if (index == 72) return _Byte72;
                if (index == 73) return _Byte73;
                if (index == 74) return _Byte74;
                if (index == 75) return _Byte75;
                if (index == 76) return _Byte76;
                if (index == 77) return _Byte77;
                if (index == 78) return _Byte78;
                if (index == 79) return _Byte79;
                if (index == 80) return _Byte80;
                if (index == 81) return _Byte81;
                if (index == 82) return _Byte82;
                if (index == 83) return _Byte83;
                if (index == 84) return _Byte84;
                if (index == 85) return _Byte85;
                if (index == 86) return _Byte86;
                if (index == 87) return _Byte87;
                if (index == 88) return _Byte88;
                if (index == 89) return _Byte89;
                if (index == 90) return _Byte90;
                if (index == 91) return _Byte91;
                if (index == 92) return _Byte92;
                if (index == 93) return _Byte93;
                if (index == 94) return _Byte94;
                if (index == 95) return _Byte95;
                if (index == 96) return _Byte96;
                if (index == 97) return _Byte97;
                if (index == 98) return _Byte98;
                if (index == 99) return _Byte99;
                if (index == 100) return _Byte100;
                if (index == 101) return _Byte101;
                if (index == 102) return _Byte102;
                if (index == 103) return _Byte103;
                if (index == 104) return _Byte104;
                if (index == 105) return _Byte105;
                if (index == 106) return _Byte106;
                if (index == 107) return _Byte107;
                if (index == 108) return _Byte108;
                if (index == 109) return _Byte109;
                if (index == 110) return _Byte110;
                if (index == 111) return _Byte111;
                if (index == 112) return _Byte112;
                if (index == 113) return _Byte113;
                if (index == 114) return _Byte114;
                if (index == 115) return _Byte115;
                if (index == 116) return _Byte116;
                if (index == 117) return _Byte117;
                if (index == 118) return _Byte118;
                if (index == 119) return _Byte119;
                if (index == 120) return _Byte120;
                if (index == 121) return _Byte121;
                if (index == 122) return _Byte122;
                if (index == 123) return _Byte123;
                if (index == 124) return _Byte124;
                if (index == 125) return _Byte125;
                if (index == 126) return _Byte126;
                if (index == 127) return _Byte127;
                if (index == 128) return _Byte128;
                if (index == 129) return _Byte129;
                if (index == 130) return _Byte130;
                if (index == 131) return _Byte131;
                if (index == 132) return _Byte132;
                if (index == 133) return _Byte133;
                if (index == 134) return _Byte134;
                if (index == 135) return _Byte135;
                if (index == 136) return _Byte136;
                if (index == 137) return _Byte137;
                if (index == 138) return _Byte138;
                if (index == 139) return _Byte139;
                if (index == 140) return _Byte140;
                if (index == 141) return _Byte141;
                if (index == 142) return _Byte142;
                if (index == 143) return _Byte143;
                if (index == 144) return _Byte144;
                if (index == 145) return _Byte145;
                if (index == 146) return _Byte146;
                if (index == 147) return _Byte147;
                if (index == 148) return _Byte148;
                if (index == 149) return _Byte149;
                if (index == 150) return _Byte150;
                if (index == 151) return _Byte151;
                if (index == 152) return _Byte152;
                if (index == 153) return _Byte153;
                if (index == 154) return _Byte154;
                if (index == 155) return _Byte155;
                if (index == 156) return _Byte156;
                if (index == 157) return _Byte157;
                if (index == 158) return _Byte158;
                if (index == 159) return _Byte159;
                if (index == 160) return _Byte160;
                if (index == 161) return _Byte161;
                if (index == 162) return _Byte162;
                if (index == 163) return _Byte163;
                if (index == 164) return _Byte164;
                if (index == 165) return _Byte165;
                if (index == 166) return _Byte166;
                if (index == 167) return _Byte167;
                if (index == 168) return _Byte168;
                if (index == 169) return _Byte169;
                if (index == 170) return _Byte170;
                if (index == 171) return _Byte171;
                if (index == 172) return _Byte172;
                if (index == 173) return _Byte173;
                if (index == 174) return _Byte174;
                if (index == 175) return _Byte175;
                if (index == 176) return _Byte176;
                if (index == 177) return _Byte177;
                if (index == 178) return _Byte178;
                if (index == 179) return _Byte179;
                if (index == 180) return _Byte180;
                if (index == 181) return _Byte181;
                if (index == 182) return _Byte182;
                if (index == 183) return _Byte183;
                if (index == 184) return _Byte184;
                if (index == 185) return _Byte185;
                if (index == 186) return _Byte186;
                if (index == 187) return _Byte187;
                if (index == 188) return _Byte188;
                if (index == 189) return _Byte189;
                if (index == 190) return _Byte190;
                if (index == 191) return _Byte191;
                if (index == 192) return _Byte192;
                if (index == 193) return _Byte193;
                if (index == 194) return _Byte194;
                if (index == 195) return _Byte195;
                if (index == 196) return _Byte196;
                if (index == 197) return _Byte197;
                if (index == 198) return _Byte198;
                if (index == 199) return _Byte199;
                if (index == 200) return _Byte200;
                if (index == 201) return _Byte201;
                if (index == 202) return _Byte202;
                if (index == 203) return _Byte203;
                if (index == 204) return _Byte204;
                if (index == 205) return _Byte205;
                if (index == 206) return _Byte206;
                if (index == 207) return _Byte207;
                if (index == 208) return _Byte208;
                if (index == 209) return _Byte209;
                if (index == 210) return _Byte210;
                if (index == 211) return _Byte211;
                if (index == 212) return _Byte212;
                if (index == 213) return _Byte213;
                if (index == 214) return _Byte214;
                if (index == 215) return _Byte215;
                if (index == 216) return _Byte216;
                if (index == 217) return _Byte217;
                if (index == 218) return _Byte218;
                if (index == 219) return _Byte219;
                if (index == 220) return _Byte220;
                if (index == 221) return _Byte221;
                if (index == 222) return _Byte222;
                if (index == 223) return _Byte223;
                if (index == 224) return _Byte224;
                if (index == 225) return _Byte225;
                if (index == 226) return _Byte226;
                if (index == 227) return _Byte227;
                if (index == 228) return _Byte228;
                if (index == 229) return _Byte229;
                if (index == 230) return _Byte230;
                if (index == 231) return _Byte231;
                if (index == 232) return _Byte232;
                if (index == 233) return _Byte233;
                if (index == 234) return _Byte234;
                if (index == 235) return _Byte235;
                if (index == 236) return _Byte236;
                if (index == 237) return _Byte237;
                if (index == 238) return _Byte238;
                if (index == 239) return _Byte239;
                if (index == 240) return _Byte240;
                if (index == 241) return _Byte241;
                if (index == 242) return _Byte242;
                if (index == 243) return _Byte243;
                if (index == 244) return _Byte244;
                if (index == 245) return _Byte245;
                if (index == 246) return _Byte246;
                if (index == 247) return _Byte247;
                if (index == 248) return _Byte248;
                if (index == 249) return _Byte249;
                if (index == 250) return _Byte250;
                if (index == 251) return _Byte251;
                if (index == 252) return _Byte252;
                if (index == 253) return _Byte253;
                if (index == 254) return _Byte254;
                if (index == 255) return _Byte255;
                return 32;
            }

            fixed4 frag(v2f i) : SV_Target
            {
                int2 size = int2(floor(_Size.x), floor(_Size.y));
                // 计算当前UV对应的字符行列
                int row = floor((1 - i.uv.y) * size.y); // 反转Y轴
                int col = floor(i.uv.x * size.x); // 64列
                int index = row * size.x + col; // 字符索引 (0-127)
                int codePoint = GetCodePoint(index);

                // 计算贴图参数
                int fontIndex = codePoint >> 8; // 原始贴图索引
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
                int2 charPos = int2(charLocalUV.x * 8, charLocalUV.y * 16);
                int2 pixelPos = gridPos * 16 + charPos;
                float2 uv = float2(pixelPos) / 256.0;
                uv.y = 1.0 - uv.y - 1.0 / 256.0;

                // 采样主字符
                float4 atlasData = tex2D(_FontAtlas, uv);
                float channel = atlasData[channelIndex];
                uint bits = (uint)(channel * 255 + 0.5);
                bool showMain = (bits & (1 << bitIndex)) != 0;

                // 采样阴影

                int2 shadowPixelPos = gridPos * 16 + int2(shadowLocalUV.x * 8, shadowLocalUV.y * 16);
                float2 shadowUVCoord = float2(shadowPixelPos) / 256.0;
                shadowUVCoord.y = 1.0 - shadowUVCoord.y - 1.0 / 256.0;

                // 边界检查
                bool validShadow = all(shadowLocalUV >= 0) && all(shadowLocalUV <= 1);
                float4 shadowData = validShadow ? tex2D(_FontAtlas, shadowUVCoord) : float4(0, 0, 0, 0);
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