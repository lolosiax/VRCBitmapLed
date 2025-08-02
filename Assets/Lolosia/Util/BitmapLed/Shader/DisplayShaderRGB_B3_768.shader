/**
 * 这是绘制点阵屏幕所需的Shader, 文件由 C# 脚本自动生成，请勿修改
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */

Shader "Unlit/DisplayShaderRGB_B3_768"
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
        [IntRange] _Byte256("Byte256", Range(0, 255)) = 0
        [IntRange] _Byte257("Byte257", Range(0, 255)) = 0
        [IntRange] _Byte258("Byte258", Range(0, 255)) = 0
        [IntRange] _Byte259("Byte259", Range(0, 255)) = 0
        [IntRange] _Byte260("Byte260", Range(0, 255)) = 0
        [IntRange] _Byte261("Byte261", Range(0, 255)) = 0
        [IntRange] _Byte262("Byte262", Range(0, 255)) = 0
        [IntRange] _Byte263("Byte263", Range(0, 255)) = 0
        [IntRange] _Byte264("Byte264", Range(0, 255)) = 0
        [IntRange] _Byte265("Byte265", Range(0, 255)) = 0
        [IntRange] _Byte266("Byte266", Range(0, 255)) = 0
        [IntRange] _Byte267("Byte267", Range(0, 255)) = 0
        [IntRange] _Byte268("Byte268", Range(0, 255)) = 0
        [IntRange] _Byte269("Byte269", Range(0, 255)) = 0
        [IntRange] _Byte270("Byte270", Range(0, 255)) = 0
        [IntRange] _Byte271("Byte271", Range(0, 255)) = 0
        [IntRange] _Byte272("Byte272", Range(0, 255)) = 0
        [IntRange] _Byte273("Byte273", Range(0, 255)) = 0
        [IntRange] _Byte274("Byte274", Range(0, 255)) = 0
        [IntRange] _Byte275("Byte275", Range(0, 255)) = 0
        [IntRange] _Byte276("Byte276", Range(0, 255)) = 0
        [IntRange] _Byte277("Byte277", Range(0, 255)) = 0
        [IntRange] _Byte278("Byte278", Range(0, 255)) = 0
        [IntRange] _Byte279("Byte279", Range(0, 255)) = 0
        [IntRange] _Byte280("Byte280", Range(0, 255)) = 0
        [IntRange] _Byte281("Byte281", Range(0, 255)) = 0
        [IntRange] _Byte282("Byte282", Range(0, 255)) = 0
        [IntRange] _Byte283("Byte283", Range(0, 255)) = 0
        [IntRange] _Byte284("Byte284", Range(0, 255)) = 0
        [IntRange] _Byte285("Byte285", Range(0, 255)) = 0
        [IntRange] _Byte286("Byte286", Range(0, 255)) = 0
        [IntRange] _Byte287("Byte287", Range(0, 255)) = 0
        [IntRange] _Byte288("Byte288", Range(0, 255)) = 0
        [IntRange] _Byte289("Byte289", Range(0, 255)) = 0
        [IntRange] _Byte290("Byte290", Range(0, 255)) = 0
        [IntRange] _Byte291("Byte291", Range(0, 255)) = 0
        [IntRange] _Byte292("Byte292", Range(0, 255)) = 0
        [IntRange] _Byte293("Byte293", Range(0, 255)) = 0
        [IntRange] _Byte294("Byte294", Range(0, 255)) = 0
        [IntRange] _Byte295("Byte295", Range(0, 255)) = 0
        [IntRange] _Byte296("Byte296", Range(0, 255)) = 0
        [IntRange] _Byte297("Byte297", Range(0, 255)) = 0
        [IntRange] _Byte298("Byte298", Range(0, 255)) = 0
        [IntRange] _Byte299("Byte299", Range(0, 255)) = 0
        [IntRange] _Byte300("Byte300", Range(0, 255)) = 0
        [IntRange] _Byte301("Byte301", Range(0, 255)) = 0
        [IntRange] _Byte302("Byte302", Range(0, 255)) = 0
        [IntRange] _Byte303("Byte303", Range(0, 255)) = 0
        [IntRange] _Byte304("Byte304", Range(0, 255)) = 0
        [IntRange] _Byte305("Byte305", Range(0, 255)) = 0
        [IntRange] _Byte306("Byte306", Range(0, 255)) = 0
        [IntRange] _Byte307("Byte307", Range(0, 255)) = 0
        [IntRange] _Byte308("Byte308", Range(0, 255)) = 0
        [IntRange] _Byte309("Byte309", Range(0, 255)) = 0
        [IntRange] _Byte310("Byte310", Range(0, 255)) = 0
        [IntRange] _Byte311("Byte311", Range(0, 255)) = 0
        [IntRange] _Byte312("Byte312", Range(0, 255)) = 0
        [IntRange] _Byte313("Byte313", Range(0, 255)) = 0
        [IntRange] _Byte314("Byte314", Range(0, 255)) = 0
        [IntRange] _Byte315("Byte315", Range(0, 255)) = 0
        [IntRange] _Byte316("Byte316", Range(0, 255)) = 0
        [IntRange] _Byte317("Byte317", Range(0, 255)) = 0
        [IntRange] _Byte318("Byte318", Range(0, 255)) = 0
        [IntRange] _Byte319("Byte319", Range(0, 255)) = 0
        [IntRange] _Byte320("Byte320", Range(0, 255)) = 0
        [IntRange] _Byte321("Byte321", Range(0, 255)) = 0
        [IntRange] _Byte322("Byte322", Range(0, 255)) = 0
        [IntRange] _Byte323("Byte323", Range(0, 255)) = 0
        [IntRange] _Byte324("Byte324", Range(0, 255)) = 0
        [IntRange] _Byte325("Byte325", Range(0, 255)) = 0
        [IntRange] _Byte326("Byte326", Range(0, 255)) = 0
        [IntRange] _Byte327("Byte327", Range(0, 255)) = 0
        [IntRange] _Byte328("Byte328", Range(0, 255)) = 0
        [IntRange] _Byte329("Byte329", Range(0, 255)) = 0
        [IntRange] _Byte330("Byte330", Range(0, 255)) = 0
        [IntRange] _Byte331("Byte331", Range(0, 255)) = 0
        [IntRange] _Byte332("Byte332", Range(0, 255)) = 0
        [IntRange] _Byte333("Byte333", Range(0, 255)) = 0
        [IntRange] _Byte334("Byte334", Range(0, 255)) = 0
        [IntRange] _Byte335("Byte335", Range(0, 255)) = 0
        [IntRange] _Byte336("Byte336", Range(0, 255)) = 0
        [IntRange] _Byte337("Byte337", Range(0, 255)) = 0
        [IntRange] _Byte338("Byte338", Range(0, 255)) = 0
        [IntRange] _Byte339("Byte339", Range(0, 255)) = 0
        [IntRange] _Byte340("Byte340", Range(0, 255)) = 0
        [IntRange] _Byte341("Byte341", Range(0, 255)) = 0
        [IntRange] _Byte342("Byte342", Range(0, 255)) = 0
        [IntRange] _Byte343("Byte343", Range(0, 255)) = 0
        [IntRange] _Byte344("Byte344", Range(0, 255)) = 0
        [IntRange] _Byte345("Byte345", Range(0, 255)) = 0
        [IntRange] _Byte346("Byte346", Range(0, 255)) = 0
        [IntRange] _Byte347("Byte347", Range(0, 255)) = 0
        [IntRange] _Byte348("Byte348", Range(0, 255)) = 0
        [IntRange] _Byte349("Byte349", Range(0, 255)) = 0
        [IntRange] _Byte350("Byte350", Range(0, 255)) = 0
        [IntRange] _Byte351("Byte351", Range(0, 255)) = 0
        [IntRange] _Byte352("Byte352", Range(0, 255)) = 0
        [IntRange] _Byte353("Byte353", Range(0, 255)) = 0
        [IntRange] _Byte354("Byte354", Range(0, 255)) = 0
        [IntRange] _Byte355("Byte355", Range(0, 255)) = 0
        [IntRange] _Byte356("Byte356", Range(0, 255)) = 0
        [IntRange] _Byte357("Byte357", Range(0, 255)) = 0
        [IntRange] _Byte358("Byte358", Range(0, 255)) = 0
        [IntRange] _Byte359("Byte359", Range(0, 255)) = 0
        [IntRange] _Byte360("Byte360", Range(0, 255)) = 0
        [IntRange] _Byte361("Byte361", Range(0, 255)) = 0
        [IntRange] _Byte362("Byte362", Range(0, 255)) = 0
        [IntRange] _Byte363("Byte363", Range(0, 255)) = 0
        [IntRange] _Byte364("Byte364", Range(0, 255)) = 0
        [IntRange] _Byte365("Byte365", Range(0, 255)) = 0
        [IntRange] _Byte366("Byte366", Range(0, 255)) = 0
        [IntRange] _Byte367("Byte367", Range(0, 255)) = 0
        [IntRange] _Byte368("Byte368", Range(0, 255)) = 0
        [IntRange] _Byte369("Byte369", Range(0, 255)) = 0
        [IntRange] _Byte370("Byte370", Range(0, 255)) = 0
        [IntRange] _Byte371("Byte371", Range(0, 255)) = 0
        [IntRange] _Byte372("Byte372", Range(0, 255)) = 0
        [IntRange] _Byte373("Byte373", Range(0, 255)) = 0
        [IntRange] _Byte374("Byte374", Range(0, 255)) = 0
        [IntRange] _Byte375("Byte375", Range(0, 255)) = 0
        [IntRange] _Byte376("Byte376", Range(0, 255)) = 0
        [IntRange] _Byte377("Byte377", Range(0, 255)) = 0
        [IntRange] _Byte378("Byte378", Range(0, 255)) = 0
        [IntRange] _Byte379("Byte379", Range(0, 255)) = 0
        [IntRange] _Byte380("Byte380", Range(0, 255)) = 0
        [IntRange] _Byte381("Byte381", Range(0, 255)) = 0
        [IntRange] _Byte382("Byte382", Range(0, 255)) = 0
        [IntRange] _Byte383("Byte383", Range(0, 255)) = 0
        [IntRange] _Byte384("Byte384", Range(0, 255)) = 0
        [IntRange] _Byte385("Byte385", Range(0, 255)) = 0
        [IntRange] _Byte386("Byte386", Range(0, 255)) = 0
        [IntRange] _Byte387("Byte387", Range(0, 255)) = 0
        [IntRange] _Byte388("Byte388", Range(0, 255)) = 0
        [IntRange] _Byte389("Byte389", Range(0, 255)) = 0
        [IntRange] _Byte390("Byte390", Range(0, 255)) = 0
        [IntRange] _Byte391("Byte391", Range(0, 255)) = 0
        [IntRange] _Byte392("Byte392", Range(0, 255)) = 0
        [IntRange] _Byte393("Byte393", Range(0, 255)) = 0
        [IntRange] _Byte394("Byte394", Range(0, 255)) = 0
        [IntRange] _Byte395("Byte395", Range(0, 255)) = 0
        [IntRange] _Byte396("Byte396", Range(0, 255)) = 0
        [IntRange] _Byte397("Byte397", Range(0, 255)) = 0
        [IntRange] _Byte398("Byte398", Range(0, 255)) = 0
        [IntRange] _Byte399("Byte399", Range(0, 255)) = 0
        [IntRange] _Byte400("Byte400", Range(0, 255)) = 0
        [IntRange] _Byte401("Byte401", Range(0, 255)) = 0
        [IntRange] _Byte402("Byte402", Range(0, 255)) = 0
        [IntRange] _Byte403("Byte403", Range(0, 255)) = 0
        [IntRange] _Byte404("Byte404", Range(0, 255)) = 0
        [IntRange] _Byte405("Byte405", Range(0, 255)) = 0
        [IntRange] _Byte406("Byte406", Range(0, 255)) = 0
        [IntRange] _Byte407("Byte407", Range(0, 255)) = 0
        [IntRange] _Byte408("Byte408", Range(0, 255)) = 0
        [IntRange] _Byte409("Byte409", Range(0, 255)) = 0
        [IntRange] _Byte410("Byte410", Range(0, 255)) = 0
        [IntRange] _Byte411("Byte411", Range(0, 255)) = 0
        [IntRange] _Byte412("Byte412", Range(0, 255)) = 0
        [IntRange] _Byte413("Byte413", Range(0, 255)) = 0
        [IntRange] _Byte414("Byte414", Range(0, 255)) = 0
        [IntRange] _Byte415("Byte415", Range(0, 255)) = 0
        [IntRange] _Byte416("Byte416", Range(0, 255)) = 0
        [IntRange] _Byte417("Byte417", Range(0, 255)) = 0
        [IntRange] _Byte418("Byte418", Range(0, 255)) = 0
        [IntRange] _Byte419("Byte419", Range(0, 255)) = 0
        [IntRange] _Byte420("Byte420", Range(0, 255)) = 0
        [IntRange] _Byte421("Byte421", Range(0, 255)) = 0
        [IntRange] _Byte422("Byte422", Range(0, 255)) = 0
        [IntRange] _Byte423("Byte423", Range(0, 255)) = 0
        [IntRange] _Byte424("Byte424", Range(0, 255)) = 0
        [IntRange] _Byte425("Byte425", Range(0, 255)) = 0
        [IntRange] _Byte426("Byte426", Range(0, 255)) = 0
        [IntRange] _Byte427("Byte427", Range(0, 255)) = 0
        [IntRange] _Byte428("Byte428", Range(0, 255)) = 0
        [IntRange] _Byte429("Byte429", Range(0, 255)) = 0
        [IntRange] _Byte430("Byte430", Range(0, 255)) = 0
        [IntRange] _Byte431("Byte431", Range(0, 255)) = 0
        [IntRange] _Byte432("Byte432", Range(0, 255)) = 0
        [IntRange] _Byte433("Byte433", Range(0, 255)) = 0
        [IntRange] _Byte434("Byte434", Range(0, 255)) = 0
        [IntRange] _Byte435("Byte435", Range(0, 255)) = 0
        [IntRange] _Byte436("Byte436", Range(0, 255)) = 0
        [IntRange] _Byte437("Byte437", Range(0, 255)) = 0
        [IntRange] _Byte438("Byte438", Range(0, 255)) = 0
        [IntRange] _Byte439("Byte439", Range(0, 255)) = 0
        [IntRange] _Byte440("Byte440", Range(0, 255)) = 0
        [IntRange] _Byte441("Byte441", Range(0, 255)) = 0
        [IntRange] _Byte442("Byte442", Range(0, 255)) = 0
        [IntRange] _Byte443("Byte443", Range(0, 255)) = 0
        [IntRange] _Byte444("Byte444", Range(0, 255)) = 0
        [IntRange] _Byte445("Byte445", Range(0, 255)) = 0
        [IntRange] _Byte446("Byte446", Range(0, 255)) = 0
        [IntRange] _Byte447("Byte447", Range(0, 255)) = 0
        [IntRange] _Byte448("Byte448", Range(0, 255)) = 0
        [IntRange] _Byte449("Byte449", Range(0, 255)) = 0
        [IntRange] _Byte450("Byte450", Range(0, 255)) = 0
        [IntRange] _Byte451("Byte451", Range(0, 255)) = 0
        [IntRange] _Byte452("Byte452", Range(0, 255)) = 0
        [IntRange] _Byte453("Byte453", Range(0, 255)) = 0
        [IntRange] _Byte454("Byte454", Range(0, 255)) = 0
        [IntRange] _Byte455("Byte455", Range(0, 255)) = 0
        [IntRange] _Byte456("Byte456", Range(0, 255)) = 0
        [IntRange] _Byte457("Byte457", Range(0, 255)) = 0
        [IntRange] _Byte458("Byte458", Range(0, 255)) = 0
        [IntRange] _Byte459("Byte459", Range(0, 255)) = 0
        [IntRange] _Byte460("Byte460", Range(0, 255)) = 0
        [IntRange] _Byte461("Byte461", Range(0, 255)) = 0
        [IntRange] _Byte462("Byte462", Range(0, 255)) = 0
        [IntRange] _Byte463("Byte463", Range(0, 255)) = 0
        [IntRange] _Byte464("Byte464", Range(0, 255)) = 0
        [IntRange] _Byte465("Byte465", Range(0, 255)) = 0
        [IntRange] _Byte466("Byte466", Range(0, 255)) = 0
        [IntRange] _Byte467("Byte467", Range(0, 255)) = 0
        [IntRange] _Byte468("Byte468", Range(0, 255)) = 0
        [IntRange] _Byte469("Byte469", Range(0, 255)) = 0
        [IntRange] _Byte470("Byte470", Range(0, 255)) = 0
        [IntRange] _Byte471("Byte471", Range(0, 255)) = 0
        [IntRange] _Byte472("Byte472", Range(0, 255)) = 0
        [IntRange] _Byte473("Byte473", Range(0, 255)) = 0
        [IntRange] _Byte474("Byte474", Range(0, 255)) = 0
        [IntRange] _Byte475("Byte475", Range(0, 255)) = 0
        [IntRange] _Byte476("Byte476", Range(0, 255)) = 0
        [IntRange] _Byte477("Byte477", Range(0, 255)) = 0
        [IntRange] _Byte478("Byte478", Range(0, 255)) = 0
        [IntRange] _Byte479("Byte479", Range(0, 255)) = 0
        [IntRange] _Byte480("Byte480", Range(0, 255)) = 0
        [IntRange] _Byte481("Byte481", Range(0, 255)) = 0
        [IntRange] _Byte482("Byte482", Range(0, 255)) = 0
        [IntRange] _Byte483("Byte483", Range(0, 255)) = 0
        [IntRange] _Byte484("Byte484", Range(0, 255)) = 0
        [IntRange] _Byte485("Byte485", Range(0, 255)) = 0
        [IntRange] _Byte486("Byte486", Range(0, 255)) = 0
        [IntRange] _Byte487("Byte487", Range(0, 255)) = 0
        [IntRange] _Byte488("Byte488", Range(0, 255)) = 0
        [IntRange] _Byte489("Byte489", Range(0, 255)) = 0
        [IntRange] _Byte490("Byte490", Range(0, 255)) = 0
        [IntRange] _Byte491("Byte491", Range(0, 255)) = 0
        [IntRange] _Byte492("Byte492", Range(0, 255)) = 0
        [IntRange] _Byte493("Byte493", Range(0, 255)) = 0
        [IntRange] _Byte494("Byte494", Range(0, 255)) = 0
        [IntRange] _Byte495("Byte495", Range(0, 255)) = 0
        [IntRange] _Byte496("Byte496", Range(0, 255)) = 0
        [IntRange] _Byte497("Byte497", Range(0, 255)) = 0
        [IntRange] _Byte498("Byte498", Range(0, 255)) = 0
        [IntRange] _Byte499("Byte499", Range(0, 255)) = 0
        [IntRange] _Byte500("Byte500", Range(0, 255)) = 0
        [IntRange] _Byte501("Byte501", Range(0, 255)) = 0
        [IntRange] _Byte502("Byte502", Range(0, 255)) = 0
        [IntRange] _Byte503("Byte503", Range(0, 255)) = 0
        [IntRange] _Byte504("Byte504", Range(0, 255)) = 0
        [IntRange] _Byte505("Byte505", Range(0, 255)) = 0
        [IntRange] _Byte506("Byte506", Range(0, 255)) = 0
        [IntRange] _Byte507("Byte507", Range(0, 255)) = 0
        [IntRange] _Byte508("Byte508", Range(0, 255)) = 0
        [IntRange] _Byte509("Byte509", Range(0, 255)) = 0
        [IntRange] _Byte510("Byte510", Range(0, 255)) = 0
        [IntRange] _Byte511("Byte511", Range(0, 255)) = 0
        [IntRange] _Byte512("Byte512", Range(0, 255)) = 0
        [IntRange] _Byte513("Byte513", Range(0, 255)) = 0
        [IntRange] _Byte514("Byte514", Range(0, 255)) = 0
        [IntRange] _Byte515("Byte515", Range(0, 255)) = 0
        [IntRange] _Byte516("Byte516", Range(0, 255)) = 0
        [IntRange] _Byte517("Byte517", Range(0, 255)) = 0
        [IntRange] _Byte518("Byte518", Range(0, 255)) = 0
        [IntRange] _Byte519("Byte519", Range(0, 255)) = 0
        [IntRange] _Byte520("Byte520", Range(0, 255)) = 0
        [IntRange] _Byte521("Byte521", Range(0, 255)) = 0
        [IntRange] _Byte522("Byte522", Range(0, 255)) = 0
        [IntRange] _Byte523("Byte523", Range(0, 255)) = 0
        [IntRange] _Byte524("Byte524", Range(0, 255)) = 0
        [IntRange] _Byte525("Byte525", Range(0, 255)) = 0
        [IntRange] _Byte526("Byte526", Range(0, 255)) = 0
        [IntRange] _Byte527("Byte527", Range(0, 255)) = 0
        [IntRange] _Byte528("Byte528", Range(0, 255)) = 0
        [IntRange] _Byte529("Byte529", Range(0, 255)) = 0
        [IntRange] _Byte530("Byte530", Range(0, 255)) = 0
        [IntRange] _Byte531("Byte531", Range(0, 255)) = 0
        [IntRange] _Byte532("Byte532", Range(0, 255)) = 0
        [IntRange] _Byte533("Byte533", Range(0, 255)) = 0
        [IntRange] _Byte534("Byte534", Range(0, 255)) = 0
        [IntRange] _Byte535("Byte535", Range(0, 255)) = 0
        [IntRange] _Byte536("Byte536", Range(0, 255)) = 0
        [IntRange] _Byte537("Byte537", Range(0, 255)) = 0
        [IntRange] _Byte538("Byte538", Range(0, 255)) = 0
        [IntRange] _Byte539("Byte539", Range(0, 255)) = 0
        [IntRange] _Byte540("Byte540", Range(0, 255)) = 0
        [IntRange] _Byte541("Byte541", Range(0, 255)) = 0
        [IntRange] _Byte542("Byte542", Range(0, 255)) = 0
        [IntRange] _Byte543("Byte543", Range(0, 255)) = 0
        [IntRange] _Byte544("Byte544", Range(0, 255)) = 0
        [IntRange] _Byte545("Byte545", Range(0, 255)) = 0
        [IntRange] _Byte546("Byte546", Range(0, 255)) = 0
        [IntRange] _Byte547("Byte547", Range(0, 255)) = 0
        [IntRange] _Byte548("Byte548", Range(0, 255)) = 0
        [IntRange] _Byte549("Byte549", Range(0, 255)) = 0
        [IntRange] _Byte550("Byte550", Range(0, 255)) = 0
        [IntRange] _Byte551("Byte551", Range(0, 255)) = 0
        [IntRange] _Byte552("Byte552", Range(0, 255)) = 0
        [IntRange] _Byte553("Byte553", Range(0, 255)) = 0
        [IntRange] _Byte554("Byte554", Range(0, 255)) = 0
        [IntRange] _Byte555("Byte555", Range(0, 255)) = 0
        [IntRange] _Byte556("Byte556", Range(0, 255)) = 0
        [IntRange] _Byte557("Byte557", Range(0, 255)) = 0
        [IntRange] _Byte558("Byte558", Range(0, 255)) = 0
        [IntRange] _Byte559("Byte559", Range(0, 255)) = 0
        [IntRange] _Byte560("Byte560", Range(0, 255)) = 0
        [IntRange] _Byte561("Byte561", Range(0, 255)) = 0
        [IntRange] _Byte562("Byte562", Range(0, 255)) = 0
        [IntRange] _Byte563("Byte563", Range(0, 255)) = 0
        [IntRange] _Byte564("Byte564", Range(0, 255)) = 0
        [IntRange] _Byte565("Byte565", Range(0, 255)) = 0
        [IntRange] _Byte566("Byte566", Range(0, 255)) = 0
        [IntRange] _Byte567("Byte567", Range(0, 255)) = 0
        [IntRange] _Byte568("Byte568", Range(0, 255)) = 0
        [IntRange] _Byte569("Byte569", Range(0, 255)) = 0
        [IntRange] _Byte570("Byte570", Range(0, 255)) = 0
        [IntRange] _Byte571("Byte571", Range(0, 255)) = 0
        [IntRange] _Byte572("Byte572", Range(0, 255)) = 0
        [IntRange] _Byte573("Byte573", Range(0, 255)) = 0
        [IntRange] _Byte574("Byte574", Range(0, 255)) = 0
        [IntRange] _Byte575("Byte575", Range(0, 255)) = 0
        [IntRange] _Byte576("Byte576", Range(0, 255)) = 0
        [IntRange] _Byte577("Byte577", Range(0, 255)) = 0
        [IntRange] _Byte578("Byte578", Range(0, 255)) = 0
        [IntRange] _Byte579("Byte579", Range(0, 255)) = 0
        [IntRange] _Byte580("Byte580", Range(0, 255)) = 0
        [IntRange] _Byte581("Byte581", Range(0, 255)) = 0
        [IntRange] _Byte582("Byte582", Range(0, 255)) = 0
        [IntRange] _Byte583("Byte583", Range(0, 255)) = 0
        [IntRange] _Byte584("Byte584", Range(0, 255)) = 0
        [IntRange] _Byte585("Byte585", Range(0, 255)) = 0
        [IntRange] _Byte586("Byte586", Range(0, 255)) = 0
        [IntRange] _Byte587("Byte587", Range(0, 255)) = 0
        [IntRange] _Byte588("Byte588", Range(0, 255)) = 0
        [IntRange] _Byte589("Byte589", Range(0, 255)) = 0
        [IntRange] _Byte590("Byte590", Range(0, 255)) = 0
        [IntRange] _Byte591("Byte591", Range(0, 255)) = 0
        [IntRange] _Byte592("Byte592", Range(0, 255)) = 0
        [IntRange] _Byte593("Byte593", Range(0, 255)) = 0
        [IntRange] _Byte594("Byte594", Range(0, 255)) = 0
        [IntRange] _Byte595("Byte595", Range(0, 255)) = 0
        [IntRange] _Byte596("Byte596", Range(0, 255)) = 0
        [IntRange] _Byte597("Byte597", Range(0, 255)) = 0
        [IntRange] _Byte598("Byte598", Range(0, 255)) = 0
        [IntRange] _Byte599("Byte599", Range(0, 255)) = 0
        [IntRange] _Byte600("Byte600", Range(0, 255)) = 0
        [IntRange] _Byte601("Byte601", Range(0, 255)) = 0
        [IntRange] _Byte602("Byte602", Range(0, 255)) = 0
        [IntRange] _Byte603("Byte603", Range(0, 255)) = 0
        [IntRange] _Byte604("Byte604", Range(0, 255)) = 0
        [IntRange] _Byte605("Byte605", Range(0, 255)) = 0
        [IntRange] _Byte606("Byte606", Range(0, 255)) = 0
        [IntRange] _Byte607("Byte607", Range(0, 255)) = 0
        [IntRange] _Byte608("Byte608", Range(0, 255)) = 0
        [IntRange] _Byte609("Byte609", Range(0, 255)) = 0
        [IntRange] _Byte610("Byte610", Range(0, 255)) = 0
        [IntRange] _Byte611("Byte611", Range(0, 255)) = 0
        [IntRange] _Byte612("Byte612", Range(0, 255)) = 0
        [IntRange] _Byte613("Byte613", Range(0, 255)) = 0
        [IntRange] _Byte614("Byte614", Range(0, 255)) = 0
        [IntRange] _Byte615("Byte615", Range(0, 255)) = 0
        [IntRange] _Byte616("Byte616", Range(0, 255)) = 0
        [IntRange] _Byte617("Byte617", Range(0, 255)) = 0
        [IntRange] _Byte618("Byte618", Range(0, 255)) = 0
        [IntRange] _Byte619("Byte619", Range(0, 255)) = 0
        [IntRange] _Byte620("Byte620", Range(0, 255)) = 0
        [IntRange] _Byte621("Byte621", Range(0, 255)) = 0
        [IntRange] _Byte622("Byte622", Range(0, 255)) = 0
        [IntRange] _Byte623("Byte623", Range(0, 255)) = 0
        [IntRange] _Byte624("Byte624", Range(0, 255)) = 0
        [IntRange] _Byte625("Byte625", Range(0, 255)) = 0
        [IntRange] _Byte626("Byte626", Range(0, 255)) = 0
        [IntRange] _Byte627("Byte627", Range(0, 255)) = 0
        [IntRange] _Byte628("Byte628", Range(0, 255)) = 0
        [IntRange] _Byte629("Byte629", Range(0, 255)) = 0
        [IntRange] _Byte630("Byte630", Range(0, 255)) = 0
        [IntRange] _Byte631("Byte631", Range(0, 255)) = 0
        [IntRange] _Byte632("Byte632", Range(0, 255)) = 0
        [IntRange] _Byte633("Byte633", Range(0, 255)) = 0
        [IntRange] _Byte634("Byte634", Range(0, 255)) = 0
        [IntRange] _Byte635("Byte635", Range(0, 255)) = 0
        [IntRange] _Byte636("Byte636", Range(0, 255)) = 0
        [IntRange] _Byte637("Byte637", Range(0, 255)) = 0
        [IntRange] _Byte638("Byte638", Range(0, 255)) = 0
        [IntRange] _Byte639("Byte639", Range(0, 255)) = 0
        [IntRange] _Byte640("Byte640", Range(0, 255)) = 0
        [IntRange] _Byte641("Byte641", Range(0, 255)) = 0
        [IntRange] _Byte642("Byte642", Range(0, 255)) = 0
        [IntRange] _Byte643("Byte643", Range(0, 255)) = 0
        [IntRange] _Byte644("Byte644", Range(0, 255)) = 0
        [IntRange] _Byte645("Byte645", Range(0, 255)) = 0
        [IntRange] _Byte646("Byte646", Range(0, 255)) = 0
        [IntRange] _Byte647("Byte647", Range(0, 255)) = 0
        [IntRange] _Byte648("Byte648", Range(0, 255)) = 0
        [IntRange] _Byte649("Byte649", Range(0, 255)) = 0
        [IntRange] _Byte650("Byte650", Range(0, 255)) = 0
        [IntRange] _Byte651("Byte651", Range(0, 255)) = 0
        [IntRange] _Byte652("Byte652", Range(0, 255)) = 0
        [IntRange] _Byte653("Byte653", Range(0, 255)) = 0
        [IntRange] _Byte654("Byte654", Range(0, 255)) = 0
        [IntRange] _Byte655("Byte655", Range(0, 255)) = 0
        [IntRange] _Byte656("Byte656", Range(0, 255)) = 0
        [IntRange] _Byte657("Byte657", Range(0, 255)) = 0
        [IntRange] _Byte658("Byte658", Range(0, 255)) = 0
        [IntRange] _Byte659("Byte659", Range(0, 255)) = 0
        [IntRange] _Byte660("Byte660", Range(0, 255)) = 0
        [IntRange] _Byte661("Byte661", Range(0, 255)) = 0
        [IntRange] _Byte662("Byte662", Range(0, 255)) = 0
        [IntRange] _Byte663("Byte663", Range(0, 255)) = 0
        [IntRange] _Byte664("Byte664", Range(0, 255)) = 0
        [IntRange] _Byte665("Byte665", Range(0, 255)) = 0
        [IntRange] _Byte666("Byte666", Range(0, 255)) = 0
        [IntRange] _Byte667("Byte667", Range(0, 255)) = 0
        [IntRange] _Byte668("Byte668", Range(0, 255)) = 0
        [IntRange] _Byte669("Byte669", Range(0, 255)) = 0
        [IntRange] _Byte670("Byte670", Range(0, 255)) = 0
        [IntRange] _Byte671("Byte671", Range(0, 255)) = 0
        [IntRange] _Byte672("Byte672", Range(0, 255)) = 0
        [IntRange] _Byte673("Byte673", Range(0, 255)) = 0
        [IntRange] _Byte674("Byte674", Range(0, 255)) = 0
        [IntRange] _Byte675("Byte675", Range(0, 255)) = 0
        [IntRange] _Byte676("Byte676", Range(0, 255)) = 0
        [IntRange] _Byte677("Byte677", Range(0, 255)) = 0
        [IntRange] _Byte678("Byte678", Range(0, 255)) = 0
        [IntRange] _Byte679("Byte679", Range(0, 255)) = 0
        [IntRange] _Byte680("Byte680", Range(0, 255)) = 0
        [IntRange] _Byte681("Byte681", Range(0, 255)) = 0
        [IntRange] _Byte682("Byte682", Range(0, 255)) = 0
        [IntRange] _Byte683("Byte683", Range(0, 255)) = 0
        [IntRange] _Byte684("Byte684", Range(0, 255)) = 0
        [IntRange] _Byte685("Byte685", Range(0, 255)) = 0
        [IntRange] _Byte686("Byte686", Range(0, 255)) = 0
        [IntRange] _Byte687("Byte687", Range(0, 255)) = 0
        [IntRange] _Byte688("Byte688", Range(0, 255)) = 0
        [IntRange] _Byte689("Byte689", Range(0, 255)) = 0
        [IntRange] _Byte690("Byte690", Range(0, 255)) = 0
        [IntRange] _Byte691("Byte691", Range(0, 255)) = 0
        [IntRange] _Byte692("Byte692", Range(0, 255)) = 0
        [IntRange] _Byte693("Byte693", Range(0, 255)) = 0
        [IntRange] _Byte694("Byte694", Range(0, 255)) = 0
        [IntRange] _Byte695("Byte695", Range(0, 255)) = 0
        [IntRange] _Byte696("Byte696", Range(0, 255)) = 0
        [IntRange] _Byte697("Byte697", Range(0, 255)) = 0
        [IntRange] _Byte698("Byte698", Range(0, 255)) = 0
        [IntRange] _Byte699("Byte699", Range(0, 255)) = 0
        [IntRange] _Byte700("Byte700", Range(0, 255)) = 0
        [IntRange] _Byte701("Byte701", Range(0, 255)) = 0
        [IntRange] _Byte702("Byte702", Range(0, 255)) = 0
        [IntRange] _Byte703("Byte703", Range(0, 255)) = 0
        [IntRange] _Byte704("Byte704", Range(0, 255)) = 0
        [IntRange] _Byte705("Byte705", Range(0, 255)) = 0
        [IntRange] _Byte706("Byte706", Range(0, 255)) = 0
        [IntRange] _Byte707("Byte707", Range(0, 255)) = 0
        [IntRange] _Byte708("Byte708", Range(0, 255)) = 0
        [IntRange] _Byte709("Byte709", Range(0, 255)) = 0
        [IntRange] _Byte710("Byte710", Range(0, 255)) = 0
        [IntRange] _Byte711("Byte711", Range(0, 255)) = 0
        [IntRange] _Byte712("Byte712", Range(0, 255)) = 0
        [IntRange] _Byte713("Byte713", Range(0, 255)) = 0
        [IntRange] _Byte714("Byte714", Range(0, 255)) = 0
        [IntRange] _Byte715("Byte715", Range(0, 255)) = 0
        [IntRange] _Byte716("Byte716", Range(0, 255)) = 0
        [IntRange] _Byte717("Byte717", Range(0, 255)) = 0
        [IntRange] _Byte718("Byte718", Range(0, 255)) = 0
        [IntRange] _Byte719("Byte719", Range(0, 255)) = 0
        [IntRange] _Byte720("Byte720", Range(0, 255)) = 0
        [IntRange] _Byte721("Byte721", Range(0, 255)) = 0
        [IntRange] _Byte722("Byte722", Range(0, 255)) = 0
        [IntRange] _Byte723("Byte723", Range(0, 255)) = 0
        [IntRange] _Byte724("Byte724", Range(0, 255)) = 0
        [IntRange] _Byte725("Byte725", Range(0, 255)) = 0
        [IntRange] _Byte726("Byte726", Range(0, 255)) = 0
        [IntRange] _Byte727("Byte727", Range(0, 255)) = 0
        [IntRange] _Byte728("Byte728", Range(0, 255)) = 0
        [IntRange] _Byte729("Byte729", Range(0, 255)) = 0
        [IntRange] _Byte730("Byte730", Range(0, 255)) = 0
        [IntRange] _Byte731("Byte731", Range(0, 255)) = 0
        [IntRange] _Byte732("Byte732", Range(0, 255)) = 0
        [IntRange] _Byte733("Byte733", Range(0, 255)) = 0
        [IntRange] _Byte734("Byte734", Range(0, 255)) = 0
        [IntRange] _Byte735("Byte735", Range(0, 255)) = 0
        [IntRange] _Byte736("Byte736", Range(0, 255)) = 0
        [IntRange] _Byte737("Byte737", Range(0, 255)) = 0
        [IntRange] _Byte738("Byte738", Range(0, 255)) = 0
        [IntRange] _Byte739("Byte739", Range(0, 255)) = 0
        [IntRange] _Byte740("Byte740", Range(0, 255)) = 0
        [IntRange] _Byte741("Byte741", Range(0, 255)) = 0
        [IntRange] _Byte742("Byte742", Range(0, 255)) = 0
        [IntRange] _Byte743("Byte743", Range(0, 255)) = 0
        [IntRange] _Byte744("Byte744", Range(0, 255)) = 0
        [IntRange] _Byte745("Byte745", Range(0, 255)) = 0
        [IntRange] _Byte746("Byte746", Range(0, 255)) = 0
        [IntRange] _Byte747("Byte747", Range(0, 255)) = 0
        [IntRange] _Byte748("Byte748", Range(0, 255)) = 0
        [IntRange] _Byte749("Byte749", Range(0, 255)) = 0
        [IntRange] _Byte750("Byte750", Range(0, 255)) = 0
        [IntRange] _Byte751("Byte751", Range(0, 255)) = 0
        [IntRange] _Byte752("Byte752", Range(0, 255)) = 0
        [IntRange] _Byte753("Byte753", Range(0, 255)) = 0
        [IntRange] _Byte754("Byte754", Range(0, 255)) = 0
        [IntRange] _Byte755("Byte755", Range(0, 255)) = 0
        [IntRange] _Byte756("Byte756", Range(0, 255)) = 0
        [IntRange] _Byte757("Byte757", Range(0, 255)) = 0
        [IntRange] _Byte758("Byte758", Range(0, 255)) = 0
        [IntRange] _Byte759("Byte759", Range(0, 255)) = 0
        [IntRange] _Byte760("Byte760", Range(0, 255)) = 0
        [IntRange] _Byte761("Byte761", Range(0, 255)) = 0
        [IntRange] _Byte762("Byte762", Range(0, 255)) = 0
        [IntRange] _Byte763("Byte763", Range(0, 255)) = 0
        [IntRange] _Byte764("Byte764", Range(0, 255)) = 0
        [IntRange] _Byte765("Byte765", Range(0, 255)) = 0
        [IntRange] _Byte766("Byte766", Range(0, 255)) = 0
        [IntRange] _Byte767("Byte767", Range(0, 255)) = 0
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
            #define INT_MEMORY_LENGTH 192

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
            
            uint _Byte0, _Byte1, _Byte2, _Byte3, _Byte4, _Byte5, _Byte6, _Byte7;
            uint _Byte8, _Byte9, _Byte10, _Byte11, _Byte12, _Byte13, _Byte14, _Byte15;
            uint _Byte16, _Byte17, _Byte18, _Byte19, _Byte20, _Byte21, _Byte22, _Byte23;
            uint _Byte24, _Byte25, _Byte26, _Byte27, _Byte28, _Byte29, _Byte30, _Byte31;
            uint _Byte32, _Byte33, _Byte34, _Byte35, _Byte36, _Byte37, _Byte38, _Byte39;
            uint _Byte40, _Byte41, _Byte42, _Byte43, _Byte44, _Byte45, _Byte46, _Byte47;
            uint _Byte48, _Byte49, _Byte50, _Byte51, _Byte52, _Byte53, _Byte54, _Byte55;
            uint _Byte56, _Byte57, _Byte58, _Byte59, _Byte60, _Byte61, _Byte62, _Byte63;
            uint _Byte64, _Byte65, _Byte66, _Byte67, _Byte68, _Byte69, _Byte70, _Byte71;
            uint _Byte72, _Byte73, _Byte74, _Byte75, _Byte76, _Byte77, _Byte78, _Byte79;
            uint _Byte80, _Byte81, _Byte82, _Byte83, _Byte84, _Byte85, _Byte86, _Byte87;
            uint _Byte88, _Byte89, _Byte90, _Byte91, _Byte92, _Byte93, _Byte94, _Byte95;
            uint _Byte96, _Byte97, _Byte98, _Byte99, _Byte100, _Byte101, _Byte102, _Byte103;
            uint _Byte104, _Byte105, _Byte106, _Byte107, _Byte108, _Byte109, _Byte110, _Byte111;
            uint _Byte112, _Byte113, _Byte114, _Byte115, _Byte116, _Byte117, _Byte118, _Byte119;
            uint _Byte120, _Byte121, _Byte122, _Byte123, _Byte124, _Byte125, _Byte126, _Byte127;
            uint _Byte128, _Byte129, _Byte130, _Byte131, _Byte132, _Byte133, _Byte134, _Byte135;
            uint _Byte136, _Byte137, _Byte138, _Byte139, _Byte140, _Byte141, _Byte142, _Byte143;
            uint _Byte144, _Byte145, _Byte146, _Byte147, _Byte148, _Byte149, _Byte150, _Byte151;
            uint _Byte152, _Byte153, _Byte154, _Byte155, _Byte156, _Byte157, _Byte158, _Byte159;
            uint _Byte160, _Byte161, _Byte162, _Byte163, _Byte164, _Byte165, _Byte166, _Byte167;
            uint _Byte168, _Byte169, _Byte170, _Byte171, _Byte172, _Byte173, _Byte174, _Byte175;
            uint _Byte176, _Byte177, _Byte178, _Byte179, _Byte180, _Byte181, _Byte182, _Byte183;
            uint _Byte184, _Byte185, _Byte186, _Byte187, _Byte188, _Byte189, _Byte190, _Byte191;
            uint _Byte192, _Byte193, _Byte194, _Byte195, _Byte196, _Byte197, _Byte198, _Byte199;
            uint _Byte200, _Byte201, _Byte202, _Byte203, _Byte204, _Byte205, _Byte206, _Byte207;
            uint _Byte208, _Byte209, _Byte210, _Byte211, _Byte212, _Byte213, _Byte214, _Byte215;
            uint _Byte216, _Byte217, _Byte218, _Byte219, _Byte220, _Byte221, _Byte222, _Byte223;
            uint _Byte224, _Byte225, _Byte226, _Byte227, _Byte228, _Byte229, _Byte230, _Byte231;
            uint _Byte232, _Byte233, _Byte234, _Byte235, _Byte236, _Byte237, _Byte238, _Byte239;
            uint _Byte240, _Byte241, _Byte242, _Byte243, _Byte244, _Byte245, _Byte246, _Byte247;
            uint _Byte248, _Byte249, _Byte250, _Byte251, _Byte252, _Byte253, _Byte254, _Byte255;
            uint _Byte256, _Byte257, _Byte258, _Byte259, _Byte260, _Byte261, _Byte262, _Byte263;
            uint _Byte264, _Byte265, _Byte266, _Byte267, _Byte268, _Byte269, _Byte270, _Byte271;
            uint _Byte272, _Byte273, _Byte274, _Byte275, _Byte276, _Byte277, _Byte278, _Byte279;
            uint _Byte280, _Byte281, _Byte282, _Byte283, _Byte284, _Byte285, _Byte286, _Byte287;
            uint _Byte288, _Byte289, _Byte290, _Byte291, _Byte292, _Byte293, _Byte294, _Byte295;
            uint _Byte296, _Byte297, _Byte298, _Byte299, _Byte300, _Byte301, _Byte302, _Byte303;
            uint _Byte304, _Byte305, _Byte306, _Byte307, _Byte308, _Byte309, _Byte310, _Byte311;
            uint _Byte312, _Byte313, _Byte314, _Byte315, _Byte316, _Byte317, _Byte318, _Byte319;
            uint _Byte320, _Byte321, _Byte322, _Byte323, _Byte324, _Byte325, _Byte326, _Byte327;
            uint _Byte328, _Byte329, _Byte330, _Byte331, _Byte332, _Byte333, _Byte334, _Byte335;
            uint _Byte336, _Byte337, _Byte338, _Byte339, _Byte340, _Byte341, _Byte342, _Byte343;
            uint _Byte344, _Byte345, _Byte346, _Byte347, _Byte348, _Byte349, _Byte350, _Byte351;
            uint _Byte352, _Byte353, _Byte354, _Byte355, _Byte356, _Byte357, _Byte358, _Byte359;
            uint _Byte360, _Byte361, _Byte362, _Byte363, _Byte364, _Byte365, _Byte366, _Byte367;
            uint _Byte368, _Byte369, _Byte370, _Byte371, _Byte372, _Byte373, _Byte374, _Byte375;
            uint _Byte376, _Byte377, _Byte378, _Byte379, _Byte380, _Byte381, _Byte382, _Byte383;
            uint _Byte384, _Byte385, _Byte386, _Byte387, _Byte388, _Byte389, _Byte390, _Byte391;
            uint _Byte392, _Byte393, _Byte394, _Byte395, _Byte396, _Byte397, _Byte398, _Byte399;
            uint _Byte400, _Byte401, _Byte402, _Byte403, _Byte404, _Byte405, _Byte406, _Byte407;
            uint _Byte408, _Byte409, _Byte410, _Byte411, _Byte412, _Byte413, _Byte414, _Byte415;
            uint _Byte416, _Byte417, _Byte418, _Byte419, _Byte420, _Byte421, _Byte422, _Byte423;
            uint _Byte424, _Byte425, _Byte426, _Byte427, _Byte428, _Byte429, _Byte430, _Byte431;
            uint _Byte432, _Byte433, _Byte434, _Byte435, _Byte436, _Byte437, _Byte438, _Byte439;
            uint _Byte440, _Byte441, _Byte442, _Byte443, _Byte444, _Byte445, _Byte446, _Byte447;
            uint _Byte448, _Byte449, _Byte450, _Byte451, _Byte452, _Byte453, _Byte454, _Byte455;
            uint _Byte456, _Byte457, _Byte458, _Byte459, _Byte460, _Byte461, _Byte462, _Byte463;
            uint _Byte464, _Byte465, _Byte466, _Byte467, _Byte468, _Byte469, _Byte470, _Byte471;
            uint _Byte472, _Byte473, _Byte474, _Byte475, _Byte476, _Byte477, _Byte478, _Byte479;
            uint _Byte480, _Byte481, _Byte482, _Byte483, _Byte484, _Byte485, _Byte486, _Byte487;
            uint _Byte488, _Byte489, _Byte490, _Byte491, _Byte492, _Byte493, _Byte494, _Byte495;
            uint _Byte496, _Byte497, _Byte498, _Byte499, _Byte500, _Byte501, _Byte502, _Byte503;
            uint _Byte504, _Byte505, _Byte506, _Byte507, _Byte508, _Byte509, _Byte510, _Byte511;
            uint _Byte512, _Byte513, _Byte514, _Byte515, _Byte516, _Byte517, _Byte518, _Byte519;
            uint _Byte520, _Byte521, _Byte522, _Byte523, _Byte524, _Byte525, _Byte526, _Byte527;
            uint _Byte528, _Byte529, _Byte530, _Byte531, _Byte532, _Byte533, _Byte534, _Byte535;
            uint _Byte536, _Byte537, _Byte538, _Byte539, _Byte540, _Byte541, _Byte542, _Byte543;
            uint _Byte544, _Byte545, _Byte546, _Byte547, _Byte548, _Byte549, _Byte550, _Byte551;
            uint _Byte552, _Byte553, _Byte554, _Byte555, _Byte556, _Byte557, _Byte558, _Byte559;
            uint _Byte560, _Byte561, _Byte562, _Byte563, _Byte564, _Byte565, _Byte566, _Byte567;
            uint _Byte568, _Byte569, _Byte570, _Byte571, _Byte572, _Byte573, _Byte574, _Byte575;
            uint _Byte576, _Byte577, _Byte578, _Byte579, _Byte580, _Byte581, _Byte582, _Byte583;
            uint _Byte584, _Byte585, _Byte586, _Byte587, _Byte588, _Byte589, _Byte590, _Byte591;
            uint _Byte592, _Byte593, _Byte594, _Byte595, _Byte596, _Byte597, _Byte598, _Byte599;
            uint _Byte600, _Byte601, _Byte602, _Byte603, _Byte604, _Byte605, _Byte606, _Byte607;
            uint _Byte608, _Byte609, _Byte610, _Byte611, _Byte612, _Byte613, _Byte614, _Byte615;
            uint _Byte616, _Byte617, _Byte618, _Byte619, _Byte620, _Byte621, _Byte622, _Byte623;
            uint _Byte624, _Byte625, _Byte626, _Byte627, _Byte628, _Byte629, _Byte630, _Byte631;
            uint _Byte632, _Byte633, _Byte634, _Byte635, _Byte636, _Byte637, _Byte638, _Byte639;
            uint _Byte640, _Byte641, _Byte642, _Byte643, _Byte644, _Byte645, _Byte646, _Byte647;
            uint _Byte648, _Byte649, _Byte650, _Byte651, _Byte652, _Byte653, _Byte654, _Byte655;
            uint _Byte656, _Byte657, _Byte658, _Byte659, _Byte660, _Byte661, _Byte662, _Byte663;
            uint _Byte664, _Byte665, _Byte666, _Byte667, _Byte668, _Byte669, _Byte670, _Byte671;
            uint _Byte672, _Byte673, _Byte674, _Byte675, _Byte676, _Byte677, _Byte678, _Byte679;
            uint _Byte680, _Byte681, _Byte682, _Byte683, _Byte684, _Byte685, _Byte686, _Byte687;
            uint _Byte688, _Byte689, _Byte690, _Byte691, _Byte692, _Byte693, _Byte694, _Byte695;
            uint _Byte696, _Byte697, _Byte698, _Byte699, _Byte700, _Byte701, _Byte702, _Byte703;
            uint _Byte704, _Byte705, _Byte706, _Byte707, _Byte708, _Byte709, _Byte710, _Byte711;
            uint _Byte712, _Byte713, _Byte714, _Byte715, _Byte716, _Byte717, _Byte718, _Byte719;
            uint _Byte720, _Byte721, _Byte722, _Byte723, _Byte724, _Byte725, _Byte726, _Byte727;
            uint _Byte728, _Byte729, _Byte730, _Byte731, _Byte732, _Byte733, _Byte734, _Byte735;
            uint _Byte736, _Byte737, _Byte738, _Byte739, _Byte740, _Byte741, _Byte742, _Byte743;
            uint _Byte744, _Byte745, _Byte746, _Byte747, _Byte748, _Byte749, _Byte750, _Byte751;
            uint _Byte752, _Byte753, _Byte754, _Byte755, _Byte756, _Byte757, _Byte758, _Byte759;
            uint _Byte760, _Byte761, _Byte762, _Byte763, _Byte764, _Byte765, _Byte766, _Byte767;

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

                    ((_Byte0 & 0xFF) << 24) | ((_Byte1 & 0xFF) << 16) | ((_Byte2 & 0xFF) << 8) | (_Byte3 & 0xFF),
                    ((_Byte4 & 0xFF) << 24) | ((_Byte5 & 0xFF) << 16) | ((_Byte6 & 0xFF) << 8) | (_Byte7 & 0xFF),
                    ((_Byte8 & 0xFF) << 24) | ((_Byte9 & 0xFF) << 16) | ((_Byte10 & 0xFF) << 8) | (_Byte11 & 0xFF),
                    ((_Byte12 & 0xFF) << 24) | ((_Byte13 & 0xFF) << 16) | ((_Byte14 & 0xFF) << 8) | (_Byte15 & 0xFF),
                    ((_Byte16 & 0xFF) << 24) | ((_Byte17 & 0xFF) << 16) | ((_Byte18 & 0xFF) << 8) | (_Byte19 & 0xFF),
                    ((_Byte20 & 0xFF) << 24) | ((_Byte21 & 0xFF) << 16) | ((_Byte22 & 0xFF) << 8) | (_Byte23 & 0xFF),
                    ((_Byte24 & 0xFF) << 24) | ((_Byte25 & 0xFF) << 16) | ((_Byte26 & 0xFF) << 8) | (_Byte27 & 0xFF),
                    ((_Byte28 & 0xFF) << 24) | ((_Byte29 & 0xFF) << 16) | ((_Byte30 & 0xFF) << 8) | (_Byte31 & 0xFF),
                    ((_Byte32 & 0xFF) << 24) | ((_Byte33 & 0xFF) << 16) | ((_Byte34 & 0xFF) << 8) | (_Byte35 & 0xFF),
                    ((_Byte36 & 0xFF) << 24) | ((_Byte37 & 0xFF) << 16) | ((_Byte38 & 0xFF) << 8) | (_Byte39 & 0xFF),
                    ((_Byte40 & 0xFF) << 24) | ((_Byte41 & 0xFF) << 16) | ((_Byte42 & 0xFF) << 8) | (_Byte43 & 0xFF),
                    ((_Byte44 & 0xFF) << 24) | ((_Byte45 & 0xFF) << 16) | ((_Byte46 & 0xFF) << 8) | (_Byte47 & 0xFF),
                    ((_Byte48 & 0xFF) << 24) | ((_Byte49 & 0xFF) << 16) | ((_Byte50 & 0xFF) << 8) | (_Byte51 & 0xFF),
                    ((_Byte52 & 0xFF) << 24) | ((_Byte53 & 0xFF) << 16) | ((_Byte54 & 0xFF) << 8) | (_Byte55 & 0xFF),
                    ((_Byte56 & 0xFF) << 24) | ((_Byte57 & 0xFF) << 16) | ((_Byte58 & 0xFF) << 8) | (_Byte59 & 0xFF),
                    ((_Byte60 & 0xFF) << 24) | ((_Byte61 & 0xFF) << 16) | ((_Byte62 & 0xFF) << 8) | (_Byte63 & 0xFF),
                    ((_Byte64 & 0xFF) << 24) | ((_Byte65 & 0xFF) << 16) | ((_Byte66 & 0xFF) << 8) | (_Byte67 & 0xFF),
                    ((_Byte68 & 0xFF) << 24) | ((_Byte69 & 0xFF) << 16) | ((_Byte70 & 0xFF) << 8) | (_Byte71 & 0xFF),
                    ((_Byte72 & 0xFF) << 24) | ((_Byte73 & 0xFF) << 16) | ((_Byte74 & 0xFF) << 8) | (_Byte75 & 0xFF),
                    ((_Byte76 & 0xFF) << 24) | ((_Byte77 & 0xFF) << 16) | ((_Byte78 & 0xFF) << 8) | (_Byte79 & 0xFF),
                    ((_Byte80 & 0xFF) << 24) | ((_Byte81 & 0xFF) << 16) | ((_Byte82 & 0xFF) << 8) | (_Byte83 & 0xFF),
                    ((_Byte84 & 0xFF) << 24) | ((_Byte85 & 0xFF) << 16) | ((_Byte86 & 0xFF) << 8) | (_Byte87 & 0xFF),
                    ((_Byte88 & 0xFF) << 24) | ((_Byte89 & 0xFF) << 16) | ((_Byte90 & 0xFF) << 8) | (_Byte91 & 0xFF),
                    ((_Byte92 & 0xFF) << 24) | ((_Byte93 & 0xFF) << 16) | ((_Byte94 & 0xFF) << 8) | (_Byte95 & 0xFF),
                    ((_Byte96 & 0xFF) << 24) | ((_Byte97 & 0xFF) << 16) | ((_Byte98 & 0xFF) << 8) | (_Byte99 & 0xFF),
                    ((_Byte100 & 0xFF) << 24) | ((_Byte101 & 0xFF) << 16) | ((_Byte102 & 0xFF) << 8) | (_Byte103 & 0xFF),
                    ((_Byte104 & 0xFF) << 24) | ((_Byte105 & 0xFF) << 16) | ((_Byte106 & 0xFF) << 8) | (_Byte107 & 0xFF),
                    ((_Byte108 & 0xFF) << 24) | ((_Byte109 & 0xFF) << 16) | ((_Byte110 & 0xFF) << 8) | (_Byte111 & 0xFF),
                    ((_Byte112 & 0xFF) << 24) | ((_Byte113 & 0xFF) << 16) | ((_Byte114 & 0xFF) << 8) | (_Byte115 & 0xFF),
                    ((_Byte116 & 0xFF) << 24) | ((_Byte117 & 0xFF) << 16) | ((_Byte118 & 0xFF) << 8) | (_Byte119 & 0xFF),
                    ((_Byte120 & 0xFF) << 24) | ((_Byte121 & 0xFF) << 16) | ((_Byte122 & 0xFF) << 8) | (_Byte123 & 0xFF),
                    ((_Byte124 & 0xFF) << 24) | ((_Byte125 & 0xFF) << 16) | ((_Byte126 & 0xFF) << 8) | (_Byte127 & 0xFF),
                    ((_Byte128 & 0xFF) << 24) | ((_Byte129 & 0xFF) << 16) | ((_Byte130 & 0xFF) << 8) | (_Byte131 & 0xFF),
                    ((_Byte132 & 0xFF) << 24) | ((_Byte133 & 0xFF) << 16) | ((_Byte134 & 0xFF) << 8) | (_Byte135 & 0xFF),
                    ((_Byte136 & 0xFF) << 24) | ((_Byte137 & 0xFF) << 16) | ((_Byte138 & 0xFF) << 8) | (_Byte139 & 0xFF),
                    ((_Byte140 & 0xFF) << 24) | ((_Byte141 & 0xFF) << 16) | ((_Byte142 & 0xFF) << 8) | (_Byte143 & 0xFF),
                    ((_Byte144 & 0xFF) << 24) | ((_Byte145 & 0xFF) << 16) | ((_Byte146 & 0xFF) << 8) | (_Byte147 & 0xFF),
                    ((_Byte148 & 0xFF) << 24) | ((_Byte149 & 0xFF) << 16) | ((_Byte150 & 0xFF) << 8) | (_Byte151 & 0xFF),
                    ((_Byte152 & 0xFF) << 24) | ((_Byte153 & 0xFF) << 16) | ((_Byte154 & 0xFF) << 8) | (_Byte155 & 0xFF),
                    ((_Byte156 & 0xFF) << 24) | ((_Byte157 & 0xFF) << 16) | ((_Byte158 & 0xFF) << 8) | (_Byte159 & 0xFF),
                    ((_Byte160 & 0xFF) << 24) | ((_Byte161 & 0xFF) << 16) | ((_Byte162 & 0xFF) << 8) | (_Byte163 & 0xFF),
                    ((_Byte164 & 0xFF) << 24) | ((_Byte165 & 0xFF) << 16) | ((_Byte166 & 0xFF) << 8) | (_Byte167 & 0xFF),
                    ((_Byte168 & 0xFF) << 24) | ((_Byte169 & 0xFF) << 16) | ((_Byte170 & 0xFF) << 8) | (_Byte171 & 0xFF),
                    ((_Byte172 & 0xFF) << 24) | ((_Byte173 & 0xFF) << 16) | ((_Byte174 & 0xFF) << 8) | (_Byte175 & 0xFF),
                    ((_Byte176 & 0xFF) << 24) | ((_Byte177 & 0xFF) << 16) | ((_Byte178 & 0xFF) << 8) | (_Byte179 & 0xFF),
                    ((_Byte180 & 0xFF) << 24) | ((_Byte181 & 0xFF) << 16) | ((_Byte182 & 0xFF) << 8) | (_Byte183 & 0xFF),
                    ((_Byte184 & 0xFF) << 24) | ((_Byte185 & 0xFF) << 16) | ((_Byte186 & 0xFF) << 8) | (_Byte187 & 0xFF),
                    ((_Byte188 & 0xFF) << 24) | ((_Byte189 & 0xFF) << 16) | ((_Byte190 & 0xFF) << 8) | (_Byte191 & 0xFF),
                    ((_Byte192 & 0xFF) << 24) | ((_Byte193 & 0xFF) << 16) | ((_Byte194 & 0xFF) << 8) | (_Byte195 & 0xFF),
                    ((_Byte196 & 0xFF) << 24) | ((_Byte197 & 0xFF) << 16) | ((_Byte198 & 0xFF) << 8) | (_Byte199 & 0xFF),
                    ((_Byte200 & 0xFF) << 24) | ((_Byte201 & 0xFF) << 16) | ((_Byte202 & 0xFF) << 8) | (_Byte203 & 0xFF),
                    ((_Byte204 & 0xFF) << 24) | ((_Byte205 & 0xFF) << 16) | ((_Byte206 & 0xFF) << 8) | (_Byte207 & 0xFF),
                    ((_Byte208 & 0xFF) << 24) | ((_Byte209 & 0xFF) << 16) | ((_Byte210 & 0xFF) << 8) | (_Byte211 & 0xFF),
                    ((_Byte212 & 0xFF) << 24) | ((_Byte213 & 0xFF) << 16) | ((_Byte214 & 0xFF) << 8) | (_Byte215 & 0xFF),
                    ((_Byte216 & 0xFF) << 24) | ((_Byte217 & 0xFF) << 16) | ((_Byte218 & 0xFF) << 8) | (_Byte219 & 0xFF),
                    ((_Byte220 & 0xFF) << 24) | ((_Byte221 & 0xFF) << 16) | ((_Byte222 & 0xFF) << 8) | (_Byte223 & 0xFF),
                    ((_Byte224 & 0xFF) << 24) | ((_Byte225 & 0xFF) << 16) | ((_Byte226 & 0xFF) << 8) | (_Byte227 & 0xFF),
                    ((_Byte228 & 0xFF) << 24) | ((_Byte229 & 0xFF) << 16) | ((_Byte230 & 0xFF) << 8) | (_Byte231 & 0xFF),
                    ((_Byte232 & 0xFF) << 24) | ((_Byte233 & 0xFF) << 16) | ((_Byte234 & 0xFF) << 8) | (_Byte235 & 0xFF),
                    ((_Byte236 & 0xFF) << 24) | ((_Byte237 & 0xFF) << 16) | ((_Byte238 & 0xFF) << 8) | (_Byte239 & 0xFF),
                    ((_Byte240 & 0xFF) << 24) | ((_Byte241 & 0xFF) << 16) | ((_Byte242 & 0xFF) << 8) | (_Byte243 & 0xFF),
                    ((_Byte244 & 0xFF) << 24) | ((_Byte245 & 0xFF) << 16) | ((_Byte246 & 0xFF) << 8) | (_Byte247 & 0xFF),
                    ((_Byte248 & 0xFF) << 24) | ((_Byte249 & 0xFF) << 16) | ((_Byte250 & 0xFF) << 8) | (_Byte251 & 0xFF),
                    ((_Byte252 & 0xFF) << 24) | ((_Byte253 & 0xFF) << 16) | ((_Byte254 & 0xFF) << 8) | (_Byte255 & 0xFF),
                    ((_Byte256 & 0xFF) << 24) | ((_Byte257 & 0xFF) << 16) | ((_Byte258 & 0xFF) << 8) | (_Byte259 & 0xFF),
                    ((_Byte260 & 0xFF) << 24) | ((_Byte261 & 0xFF) << 16) | ((_Byte262 & 0xFF) << 8) | (_Byte263 & 0xFF),
                    ((_Byte264 & 0xFF) << 24) | ((_Byte265 & 0xFF) << 16) | ((_Byte266 & 0xFF) << 8) | (_Byte267 & 0xFF),
                    ((_Byte268 & 0xFF) << 24) | ((_Byte269 & 0xFF) << 16) | ((_Byte270 & 0xFF) << 8) | (_Byte271 & 0xFF),
                    ((_Byte272 & 0xFF) << 24) | ((_Byte273 & 0xFF) << 16) | ((_Byte274 & 0xFF) << 8) | (_Byte275 & 0xFF),
                    ((_Byte276 & 0xFF) << 24) | ((_Byte277 & 0xFF) << 16) | ((_Byte278 & 0xFF) << 8) | (_Byte279 & 0xFF),
                    ((_Byte280 & 0xFF) << 24) | ((_Byte281 & 0xFF) << 16) | ((_Byte282 & 0xFF) << 8) | (_Byte283 & 0xFF),
                    ((_Byte284 & 0xFF) << 24) | ((_Byte285 & 0xFF) << 16) | ((_Byte286 & 0xFF) << 8) | (_Byte287 & 0xFF),
                    ((_Byte288 & 0xFF) << 24) | ((_Byte289 & 0xFF) << 16) | ((_Byte290 & 0xFF) << 8) | (_Byte291 & 0xFF),
                    ((_Byte292 & 0xFF) << 24) | ((_Byte293 & 0xFF) << 16) | ((_Byte294 & 0xFF) << 8) | (_Byte295 & 0xFF),
                    ((_Byte296 & 0xFF) << 24) | ((_Byte297 & 0xFF) << 16) | ((_Byte298 & 0xFF) << 8) | (_Byte299 & 0xFF),
                    ((_Byte300 & 0xFF) << 24) | ((_Byte301 & 0xFF) << 16) | ((_Byte302 & 0xFF) << 8) | (_Byte303 & 0xFF),
                    ((_Byte304 & 0xFF) << 24) | ((_Byte305 & 0xFF) << 16) | ((_Byte306 & 0xFF) << 8) | (_Byte307 & 0xFF),
                    ((_Byte308 & 0xFF) << 24) | ((_Byte309 & 0xFF) << 16) | ((_Byte310 & 0xFF) << 8) | (_Byte311 & 0xFF),
                    ((_Byte312 & 0xFF) << 24) | ((_Byte313 & 0xFF) << 16) | ((_Byte314 & 0xFF) << 8) | (_Byte315 & 0xFF),
                    ((_Byte316 & 0xFF) << 24) | ((_Byte317 & 0xFF) << 16) | ((_Byte318 & 0xFF) << 8) | (_Byte319 & 0xFF),
                    ((_Byte320 & 0xFF) << 24) | ((_Byte321 & 0xFF) << 16) | ((_Byte322 & 0xFF) << 8) | (_Byte323 & 0xFF),
                    ((_Byte324 & 0xFF) << 24) | ((_Byte325 & 0xFF) << 16) | ((_Byte326 & 0xFF) << 8) | (_Byte327 & 0xFF),
                    ((_Byte328 & 0xFF) << 24) | ((_Byte329 & 0xFF) << 16) | ((_Byte330 & 0xFF) << 8) | (_Byte331 & 0xFF),
                    ((_Byte332 & 0xFF) << 24) | ((_Byte333 & 0xFF) << 16) | ((_Byte334 & 0xFF) << 8) | (_Byte335 & 0xFF),
                    ((_Byte336 & 0xFF) << 24) | ((_Byte337 & 0xFF) << 16) | ((_Byte338 & 0xFF) << 8) | (_Byte339 & 0xFF),
                    ((_Byte340 & 0xFF) << 24) | ((_Byte341 & 0xFF) << 16) | ((_Byte342 & 0xFF) << 8) | (_Byte343 & 0xFF),
                    ((_Byte344 & 0xFF) << 24) | ((_Byte345 & 0xFF) << 16) | ((_Byte346 & 0xFF) << 8) | (_Byte347 & 0xFF),
                    ((_Byte348 & 0xFF) << 24) | ((_Byte349 & 0xFF) << 16) | ((_Byte350 & 0xFF) << 8) | (_Byte351 & 0xFF),
                    ((_Byte352 & 0xFF) << 24) | ((_Byte353 & 0xFF) << 16) | ((_Byte354 & 0xFF) << 8) | (_Byte355 & 0xFF),
                    ((_Byte356 & 0xFF) << 24) | ((_Byte357 & 0xFF) << 16) | ((_Byte358 & 0xFF) << 8) | (_Byte359 & 0xFF),
                    ((_Byte360 & 0xFF) << 24) | ((_Byte361 & 0xFF) << 16) | ((_Byte362 & 0xFF) << 8) | (_Byte363 & 0xFF),
                    ((_Byte364 & 0xFF) << 24) | ((_Byte365 & 0xFF) << 16) | ((_Byte366 & 0xFF) << 8) | (_Byte367 & 0xFF),
                    ((_Byte368 & 0xFF) << 24) | ((_Byte369 & 0xFF) << 16) | ((_Byte370 & 0xFF) << 8) | (_Byte371 & 0xFF),
                    ((_Byte372 & 0xFF) << 24) | ((_Byte373 & 0xFF) << 16) | ((_Byte374 & 0xFF) << 8) | (_Byte375 & 0xFF),
                    ((_Byte376 & 0xFF) << 24) | ((_Byte377 & 0xFF) << 16) | ((_Byte378 & 0xFF) << 8) | (_Byte379 & 0xFF),
                    ((_Byte380 & 0xFF) << 24) | ((_Byte381 & 0xFF) << 16) | ((_Byte382 & 0xFF) << 8) | (_Byte383 & 0xFF),
                    ((_Byte384 & 0xFF) << 24) | ((_Byte385 & 0xFF) << 16) | ((_Byte386 & 0xFF) << 8) | (_Byte387 & 0xFF),
                    ((_Byte388 & 0xFF) << 24) | ((_Byte389 & 0xFF) << 16) | ((_Byte390 & 0xFF) << 8) | (_Byte391 & 0xFF),
                    ((_Byte392 & 0xFF) << 24) | ((_Byte393 & 0xFF) << 16) | ((_Byte394 & 0xFF) << 8) | (_Byte395 & 0xFF),
                    ((_Byte396 & 0xFF) << 24) | ((_Byte397 & 0xFF) << 16) | ((_Byte398 & 0xFF) << 8) | (_Byte399 & 0xFF),
                    ((_Byte400 & 0xFF) << 24) | ((_Byte401 & 0xFF) << 16) | ((_Byte402 & 0xFF) << 8) | (_Byte403 & 0xFF),
                    ((_Byte404 & 0xFF) << 24) | ((_Byte405 & 0xFF) << 16) | ((_Byte406 & 0xFF) << 8) | (_Byte407 & 0xFF),
                    ((_Byte408 & 0xFF) << 24) | ((_Byte409 & 0xFF) << 16) | ((_Byte410 & 0xFF) << 8) | (_Byte411 & 0xFF),
                    ((_Byte412 & 0xFF) << 24) | ((_Byte413 & 0xFF) << 16) | ((_Byte414 & 0xFF) << 8) | (_Byte415 & 0xFF),
                    ((_Byte416 & 0xFF) << 24) | ((_Byte417 & 0xFF) << 16) | ((_Byte418 & 0xFF) << 8) | (_Byte419 & 0xFF),
                    ((_Byte420 & 0xFF) << 24) | ((_Byte421 & 0xFF) << 16) | ((_Byte422 & 0xFF) << 8) | (_Byte423 & 0xFF),
                    ((_Byte424 & 0xFF) << 24) | ((_Byte425 & 0xFF) << 16) | ((_Byte426 & 0xFF) << 8) | (_Byte427 & 0xFF),
                    ((_Byte428 & 0xFF) << 24) | ((_Byte429 & 0xFF) << 16) | ((_Byte430 & 0xFF) << 8) | (_Byte431 & 0xFF),
                    ((_Byte432 & 0xFF) << 24) | ((_Byte433 & 0xFF) << 16) | ((_Byte434 & 0xFF) << 8) | (_Byte435 & 0xFF),
                    ((_Byte436 & 0xFF) << 24) | ((_Byte437 & 0xFF) << 16) | ((_Byte438 & 0xFF) << 8) | (_Byte439 & 0xFF),
                    ((_Byte440 & 0xFF) << 24) | ((_Byte441 & 0xFF) << 16) | ((_Byte442 & 0xFF) << 8) | (_Byte443 & 0xFF),
                    ((_Byte444 & 0xFF) << 24) | ((_Byte445 & 0xFF) << 16) | ((_Byte446 & 0xFF) << 8) | (_Byte447 & 0xFF),
                    ((_Byte448 & 0xFF) << 24) | ((_Byte449 & 0xFF) << 16) | ((_Byte450 & 0xFF) << 8) | (_Byte451 & 0xFF),
                    ((_Byte452 & 0xFF) << 24) | ((_Byte453 & 0xFF) << 16) | ((_Byte454 & 0xFF) << 8) | (_Byte455 & 0xFF),
                    ((_Byte456 & 0xFF) << 24) | ((_Byte457 & 0xFF) << 16) | ((_Byte458 & 0xFF) << 8) | (_Byte459 & 0xFF),
                    ((_Byte460 & 0xFF) << 24) | ((_Byte461 & 0xFF) << 16) | ((_Byte462 & 0xFF) << 8) | (_Byte463 & 0xFF),
                    ((_Byte464 & 0xFF) << 24) | ((_Byte465 & 0xFF) << 16) | ((_Byte466 & 0xFF) << 8) | (_Byte467 & 0xFF),
                    ((_Byte468 & 0xFF) << 24) | ((_Byte469 & 0xFF) << 16) | ((_Byte470 & 0xFF) << 8) | (_Byte471 & 0xFF),
                    ((_Byte472 & 0xFF) << 24) | ((_Byte473 & 0xFF) << 16) | ((_Byte474 & 0xFF) << 8) | (_Byte475 & 0xFF),
                    ((_Byte476 & 0xFF) << 24) | ((_Byte477 & 0xFF) << 16) | ((_Byte478 & 0xFF) << 8) | (_Byte479 & 0xFF),
                    ((_Byte480 & 0xFF) << 24) | ((_Byte481 & 0xFF) << 16) | ((_Byte482 & 0xFF) << 8) | (_Byte483 & 0xFF),
                    ((_Byte484 & 0xFF) << 24) | ((_Byte485 & 0xFF) << 16) | ((_Byte486 & 0xFF) << 8) | (_Byte487 & 0xFF),
                    ((_Byte488 & 0xFF) << 24) | ((_Byte489 & 0xFF) << 16) | ((_Byte490 & 0xFF) << 8) | (_Byte491 & 0xFF),
                    ((_Byte492 & 0xFF) << 24) | ((_Byte493 & 0xFF) << 16) | ((_Byte494 & 0xFF) << 8) | (_Byte495 & 0xFF),
                    ((_Byte496 & 0xFF) << 24) | ((_Byte497 & 0xFF) << 16) | ((_Byte498 & 0xFF) << 8) | (_Byte499 & 0xFF),
                    ((_Byte500 & 0xFF) << 24) | ((_Byte501 & 0xFF) << 16) | ((_Byte502 & 0xFF) << 8) | (_Byte503 & 0xFF),
                    ((_Byte504 & 0xFF) << 24) | ((_Byte505 & 0xFF) << 16) | ((_Byte506 & 0xFF) << 8) | (_Byte507 & 0xFF),
                    ((_Byte508 & 0xFF) << 24) | ((_Byte509 & 0xFF) << 16) | ((_Byte510 & 0xFF) << 8) | (_Byte511 & 0xFF),
                    ((_Byte512 & 0xFF) << 24) | ((_Byte513 & 0xFF) << 16) | ((_Byte514 & 0xFF) << 8) | (_Byte515 & 0xFF),
                    ((_Byte516 & 0xFF) << 24) | ((_Byte517 & 0xFF) << 16) | ((_Byte518 & 0xFF) << 8) | (_Byte519 & 0xFF),
                    ((_Byte520 & 0xFF) << 24) | ((_Byte521 & 0xFF) << 16) | ((_Byte522 & 0xFF) << 8) | (_Byte523 & 0xFF),
                    ((_Byte524 & 0xFF) << 24) | ((_Byte525 & 0xFF) << 16) | ((_Byte526 & 0xFF) << 8) | (_Byte527 & 0xFF),
                    ((_Byte528 & 0xFF) << 24) | ((_Byte529 & 0xFF) << 16) | ((_Byte530 & 0xFF) << 8) | (_Byte531 & 0xFF),
                    ((_Byte532 & 0xFF) << 24) | ((_Byte533 & 0xFF) << 16) | ((_Byte534 & 0xFF) << 8) | (_Byte535 & 0xFF),
                    ((_Byte536 & 0xFF) << 24) | ((_Byte537 & 0xFF) << 16) | ((_Byte538 & 0xFF) << 8) | (_Byte539 & 0xFF),
                    ((_Byte540 & 0xFF) << 24) | ((_Byte541 & 0xFF) << 16) | ((_Byte542 & 0xFF) << 8) | (_Byte543 & 0xFF),
                    ((_Byte544 & 0xFF) << 24) | ((_Byte545 & 0xFF) << 16) | ((_Byte546 & 0xFF) << 8) | (_Byte547 & 0xFF),
                    ((_Byte548 & 0xFF) << 24) | ((_Byte549 & 0xFF) << 16) | ((_Byte550 & 0xFF) << 8) | (_Byte551 & 0xFF),
                    ((_Byte552 & 0xFF) << 24) | ((_Byte553 & 0xFF) << 16) | ((_Byte554 & 0xFF) << 8) | (_Byte555 & 0xFF),
                    ((_Byte556 & 0xFF) << 24) | ((_Byte557 & 0xFF) << 16) | ((_Byte558 & 0xFF) << 8) | (_Byte559 & 0xFF),
                    ((_Byte560 & 0xFF) << 24) | ((_Byte561 & 0xFF) << 16) | ((_Byte562 & 0xFF) << 8) | (_Byte563 & 0xFF),
                    ((_Byte564 & 0xFF) << 24) | ((_Byte565 & 0xFF) << 16) | ((_Byte566 & 0xFF) << 8) | (_Byte567 & 0xFF),
                    ((_Byte568 & 0xFF) << 24) | ((_Byte569 & 0xFF) << 16) | ((_Byte570 & 0xFF) << 8) | (_Byte571 & 0xFF),
                    ((_Byte572 & 0xFF) << 24) | ((_Byte573 & 0xFF) << 16) | ((_Byte574 & 0xFF) << 8) | (_Byte575 & 0xFF),
                    ((_Byte576 & 0xFF) << 24) | ((_Byte577 & 0xFF) << 16) | ((_Byte578 & 0xFF) << 8) | (_Byte579 & 0xFF),
                    ((_Byte580 & 0xFF) << 24) | ((_Byte581 & 0xFF) << 16) | ((_Byte582 & 0xFF) << 8) | (_Byte583 & 0xFF),
                    ((_Byte584 & 0xFF) << 24) | ((_Byte585 & 0xFF) << 16) | ((_Byte586 & 0xFF) << 8) | (_Byte587 & 0xFF),
                    ((_Byte588 & 0xFF) << 24) | ((_Byte589 & 0xFF) << 16) | ((_Byte590 & 0xFF) << 8) | (_Byte591 & 0xFF),
                    ((_Byte592 & 0xFF) << 24) | ((_Byte593 & 0xFF) << 16) | ((_Byte594 & 0xFF) << 8) | (_Byte595 & 0xFF),
                    ((_Byte596 & 0xFF) << 24) | ((_Byte597 & 0xFF) << 16) | ((_Byte598 & 0xFF) << 8) | (_Byte599 & 0xFF),
                    ((_Byte600 & 0xFF) << 24) | ((_Byte601 & 0xFF) << 16) | ((_Byte602 & 0xFF) << 8) | (_Byte603 & 0xFF),
                    ((_Byte604 & 0xFF) << 24) | ((_Byte605 & 0xFF) << 16) | ((_Byte606 & 0xFF) << 8) | (_Byte607 & 0xFF),
                    ((_Byte608 & 0xFF) << 24) | ((_Byte609 & 0xFF) << 16) | ((_Byte610 & 0xFF) << 8) | (_Byte611 & 0xFF),
                    ((_Byte612 & 0xFF) << 24) | ((_Byte613 & 0xFF) << 16) | ((_Byte614 & 0xFF) << 8) | (_Byte615 & 0xFF),
                    ((_Byte616 & 0xFF) << 24) | ((_Byte617 & 0xFF) << 16) | ((_Byte618 & 0xFF) << 8) | (_Byte619 & 0xFF),
                    ((_Byte620 & 0xFF) << 24) | ((_Byte621 & 0xFF) << 16) | ((_Byte622 & 0xFF) << 8) | (_Byte623 & 0xFF),
                    ((_Byte624 & 0xFF) << 24) | ((_Byte625 & 0xFF) << 16) | ((_Byte626 & 0xFF) << 8) | (_Byte627 & 0xFF),
                    ((_Byte628 & 0xFF) << 24) | ((_Byte629 & 0xFF) << 16) | ((_Byte630 & 0xFF) << 8) | (_Byte631 & 0xFF),
                    ((_Byte632 & 0xFF) << 24) | ((_Byte633 & 0xFF) << 16) | ((_Byte634 & 0xFF) << 8) | (_Byte635 & 0xFF),
                    ((_Byte636 & 0xFF) << 24) | ((_Byte637 & 0xFF) << 16) | ((_Byte638 & 0xFF) << 8) | (_Byte639 & 0xFF),
                    ((_Byte640 & 0xFF) << 24) | ((_Byte641 & 0xFF) << 16) | ((_Byte642 & 0xFF) << 8) | (_Byte643 & 0xFF),
                    ((_Byte644 & 0xFF) << 24) | ((_Byte645 & 0xFF) << 16) | ((_Byte646 & 0xFF) << 8) | (_Byte647 & 0xFF),
                    ((_Byte648 & 0xFF) << 24) | ((_Byte649 & 0xFF) << 16) | ((_Byte650 & 0xFF) << 8) | (_Byte651 & 0xFF),
                    ((_Byte652 & 0xFF) << 24) | ((_Byte653 & 0xFF) << 16) | ((_Byte654 & 0xFF) << 8) | (_Byte655 & 0xFF),
                    ((_Byte656 & 0xFF) << 24) | ((_Byte657 & 0xFF) << 16) | ((_Byte658 & 0xFF) << 8) | (_Byte659 & 0xFF),
                    ((_Byte660 & 0xFF) << 24) | ((_Byte661 & 0xFF) << 16) | ((_Byte662 & 0xFF) << 8) | (_Byte663 & 0xFF),
                    ((_Byte664 & 0xFF) << 24) | ((_Byte665 & 0xFF) << 16) | ((_Byte666 & 0xFF) << 8) | (_Byte667 & 0xFF),
                    ((_Byte668 & 0xFF) << 24) | ((_Byte669 & 0xFF) << 16) | ((_Byte670 & 0xFF) << 8) | (_Byte671 & 0xFF),
                    ((_Byte672 & 0xFF) << 24) | ((_Byte673 & 0xFF) << 16) | ((_Byte674 & 0xFF) << 8) | (_Byte675 & 0xFF),
                    ((_Byte676 & 0xFF) << 24) | ((_Byte677 & 0xFF) << 16) | ((_Byte678 & 0xFF) << 8) | (_Byte679 & 0xFF),
                    ((_Byte680 & 0xFF) << 24) | ((_Byte681 & 0xFF) << 16) | ((_Byte682 & 0xFF) << 8) | (_Byte683 & 0xFF),
                    ((_Byte684 & 0xFF) << 24) | ((_Byte685 & 0xFF) << 16) | ((_Byte686 & 0xFF) << 8) | (_Byte687 & 0xFF),
                    ((_Byte688 & 0xFF) << 24) | ((_Byte689 & 0xFF) << 16) | ((_Byte690 & 0xFF) << 8) | (_Byte691 & 0xFF),
                    ((_Byte692 & 0xFF) << 24) | ((_Byte693 & 0xFF) << 16) | ((_Byte694 & 0xFF) << 8) | (_Byte695 & 0xFF),
                    ((_Byte696 & 0xFF) << 24) | ((_Byte697 & 0xFF) << 16) | ((_Byte698 & 0xFF) << 8) | (_Byte699 & 0xFF),
                    ((_Byte700 & 0xFF) << 24) | ((_Byte701 & 0xFF) << 16) | ((_Byte702 & 0xFF) << 8) | (_Byte703 & 0xFF),
                    ((_Byte704 & 0xFF) << 24) | ((_Byte705 & 0xFF) << 16) | ((_Byte706 & 0xFF) << 8) | (_Byte707 & 0xFF),
                    ((_Byte708 & 0xFF) << 24) | ((_Byte709 & 0xFF) << 16) | ((_Byte710 & 0xFF) << 8) | (_Byte711 & 0xFF),
                    ((_Byte712 & 0xFF) << 24) | ((_Byte713 & 0xFF) << 16) | ((_Byte714 & 0xFF) << 8) | (_Byte715 & 0xFF),
                    ((_Byte716 & 0xFF) << 24) | ((_Byte717 & 0xFF) << 16) | ((_Byte718 & 0xFF) << 8) | (_Byte719 & 0xFF),
                    ((_Byte720 & 0xFF) << 24) | ((_Byte721 & 0xFF) << 16) | ((_Byte722 & 0xFF) << 8) | (_Byte723 & 0xFF),
                    ((_Byte724 & 0xFF) << 24) | ((_Byte725 & 0xFF) << 16) | ((_Byte726 & 0xFF) << 8) | (_Byte727 & 0xFF),
                    ((_Byte728 & 0xFF) << 24) | ((_Byte729 & 0xFF) << 16) | ((_Byte730 & 0xFF) << 8) | (_Byte731 & 0xFF),
                    ((_Byte732 & 0xFF) << 24) | ((_Byte733 & 0xFF) << 16) | ((_Byte734 & 0xFF) << 8) | (_Byte735 & 0xFF),
                    ((_Byte736 & 0xFF) << 24) | ((_Byte737 & 0xFF) << 16) | ((_Byte738 & 0xFF) << 8) | (_Byte739 & 0xFF),
                    ((_Byte740 & 0xFF) << 24) | ((_Byte741 & 0xFF) << 16) | ((_Byte742 & 0xFF) << 8) | (_Byte743 & 0xFF),
                    ((_Byte744 & 0xFF) << 24) | ((_Byte745 & 0xFF) << 16) | ((_Byte746 & 0xFF) << 8) | (_Byte747 & 0xFF),
                    ((_Byte748 & 0xFF) << 24) | ((_Byte749 & 0xFF) << 16) | ((_Byte750 & 0xFF) << 8) | (_Byte751 & 0xFF),
                    ((_Byte752 & 0xFF) << 24) | ((_Byte753 & 0xFF) << 16) | ((_Byte754 & 0xFF) << 8) | (_Byte755 & 0xFF),
                    ((_Byte756 & 0xFF) << 24) | ((_Byte757 & 0xFF) << 16) | ((_Byte758 & 0xFF) << 8) | (_Byte759 & 0xFF),
                    ((_Byte760 & 0xFF) << 24) | ((_Byte761 & 0xFF) << 16) | ((_Byte762 & 0xFF) << 8) | (_Byte763 & 0xFF),
                    ((_Byte764 & 0xFF) << 24) | ((_Byte765 & 0xFF) << 16) | ((_Byte766 & 0xFF) << 8) | (_Byte767 & 0xFF),
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