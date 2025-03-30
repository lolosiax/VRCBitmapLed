/**
 * 这是绘制点阵屏幕所需的Shader, 512 Byte 版本
 * 文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/DisplayShader512"
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
            int _Byte256, _Byte257, _Byte258, _Byte259, _Byte260, _Byte261, _Byte262, _Byte263;
            int _Byte264, _Byte265, _Byte266, _Byte267, _Byte268, _Byte269, _Byte270, _Byte271;
            int _Byte272, _Byte273, _Byte274, _Byte275, _Byte276, _Byte277, _Byte278, _Byte279;
            int _Byte280, _Byte281, _Byte282, _Byte283, _Byte284, _Byte285, _Byte286, _Byte287;
            int _Byte288, _Byte289, _Byte290, _Byte291, _Byte292, _Byte293, _Byte294, _Byte295;
            int _Byte296, _Byte297, _Byte298, _Byte299, _Byte300, _Byte301, _Byte302, _Byte303;
            int _Byte304, _Byte305, _Byte306, _Byte307, _Byte308, _Byte309, _Byte310, _Byte311;
            int _Byte312, _Byte313, _Byte314, _Byte315, _Byte316, _Byte317, _Byte318, _Byte319;
            int _Byte320, _Byte321, _Byte322, _Byte323, _Byte324, _Byte325, _Byte326, _Byte327;
            int _Byte328, _Byte329, _Byte330, _Byte331, _Byte332, _Byte333, _Byte334, _Byte335;
            int _Byte336, _Byte337, _Byte338, _Byte339, _Byte340, _Byte341, _Byte342, _Byte343;
            int _Byte344, _Byte345, _Byte346, _Byte347, _Byte348, _Byte349, _Byte350, _Byte351;
            int _Byte352, _Byte353, _Byte354, _Byte355, _Byte356, _Byte357, _Byte358, _Byte359;
            int _Byte360, _Byte361, _Byte362, _Byte363, _Byte364, _Byte365, _Byte366, _Byte367;
            int _Byte368, _Byte369, _Byte370, _Byte371, _Byte372, _Byte373, _Byte374, _Byte375;
            int _Byte376, _Byte377, _Byte378, _Byte379, _Byte380, _Byte381, _Byte382, _Byte383;
            int _Byte384, _Byte385, _Byte386, _Byte387, _Byte388, _Byte389, _Byte390, _Byte391;
            int _Byte392, _Byte393, _Byte394, _Byte395, _Byte396, _Byte397, _Byte398, _Byte399;
            int _Byte400, _Byte401, _Byte402, _Byte403, _Byte404, _Byte405, _Byte406, _Byte407;
            int _Byte408, _Byte409, _Byte410, _Byte411, _Byte412, _Byte413, _Byte414, _Byte415;
            int _Byte416, _Byte417, _Byte418, _Byte419, _Byte420, _Byte421, _Byte422, _Byte423;
            int _Byte424, _Byte425, _Byte426, _Byte427, _Byte428, _Byte429, _Byte430, _Byte431;
            int _Byte432, _Byte433, _Byte434, _Byte435, _Byte436, _Byte437, _Byte438, _Byte439;
            int _Byte440, _Byte441, _Byte442, _Byte443, _Byte444, _Byte445, _Byte446, _Byte447;
            int _Byte448, _Byte449, _Byte450, _Byte451, _Byte452, _Byte453, _Byte454, _Byte455;
            int _Byte456, _Byte457, _Byte458, _Byte459, _Byte460, _Byte461, _Byte462, _Byte463;
            int _Byte464, _Byte465, _Byte466, _Byte467, _Byte468, _Byte469, _Byte470, _Byte471;
            int _Byte472, _Byte473, _Byte474, _Byte475, _Byte476, _Byte477, _Byte478, _Byte479;
            int _Byte480, _Byte481, _Byte482, _Byte483, _Byte484, _Byte485, _Byte486, _Byte487;
            int _Byte488, _Byte489, _Byte490, _Byte491, _Byte492, _Byte493, _Byte494, _Byte495;
            int _Byte496, _Byte497, _Byte498, _Byte499, _Byte500, _Byte501, _Byte502, _Byte503;
            int _Byte504, _Byte505, _Byte506, _Byte507, _Byte508, _Byte509, _Byte510, _Byte511;

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
                if (index == 128) return _Byte257 | _Byte256 << 8;
                if (index == 129) return _Byte259 | _Byte258 << 8;
                if (index == 130) return _Byte261 | _Byte260 << 8;
                if (index == 131) return _Byte263 | _Byte262 << 8;
                if (index == 132) return _Byte265 | _Byte264 << 8;
                if (index == 133) return _Byte267 | _Byte266 << 8;
                if (index == 134) return _Byte269 | _Byte268 << 8;
                if (index == 135) return _Byte271 | _Byte270 << 8;
                if (index == 136) return _Byte273 | _Byte272 << 8;
                if (index == 137) return _Byte275 | _Byte274 << 8;
                if (index == 138) return _Byte277 | _Byte276 << 8;
                if (index == 139) return _Byte279 | _Byte278 << 8;
                if (index == 140) return _Byte281 | _Byte280 << 8;
                if (index == 141) return _Byte283 | _Byte282 << 8;
                if (index == 142) return _Byte285 | _Byte284 << 8;
                if (index == 143) return _Byte287 | _Byte286 << 8;
                if (index == 144) return _Byte289 | _Byte288 << 8;
                if (index == 145) return _Byte291 | _Byte290 << 8;
                if (index == 146) return _Byte293 | _Byte292 << 8;
                if (index == 147) return _Byte295 | _Byte294 << 8;
                if (index == 148) return _Byte297 | _Byte296 << 8;
                if (index == 149) return _Byte299 | _Byte298 << 8;
                if (index == 150) return _Byte301 | _Byte300 << 8;
                if (index == 151) return _Byte303 | _Byte302 << 8;
                if (index == 152) return _Byte305 | _Byte304 << 8;
                if (index == 153) return _Byte307 | _Byte306 << 8;
                if (index == 154) return _Byte309 | _Byte308 << 8;
                if (index == 155) return _Byte311 | _Byte310 << 8;
                if (index == 156) return _Byte313 | _Byte312 << 8;
                if (index == 157) return _Byte315 | _Byte314 << 8;
                if (index == 158) return _Byte317 | _Byte316 << 8;
                if (index == 159) return _Byte319 | _Byte318 << 8;
                if (index == 160) return _Byte321 | _Byte320 << 8;
                if (index == 161) return _Byte323 | _Byte322 << 8;
                if (index == 162) return _Byte325 | _Byte324 << 8;
                if (index == 163) return _Byte327 | _Byte326 << 8;
                if (index == 164) return _Byte329 | _Byte328 << 8;
                if (index == 165) return _Byte331 | _Byte330 << 8;
                if (index == 166) return _Byte333 | _Byte332 << 8;
                if (index == 167) return _Byte335 | _Byte334 << 8;
                if (index == 168) return _Byte337 | _Byte336 << 8;
                if (index == 169) return _Byte339 | _Byte338 << 8;
                if (index == 170) return _Byte341 | _Byte340 << 8;
                if (index == 171) return _Byte343 | _Byte342 << 8;
                if (index == 172) return _Byte345 | _Byte344 << 8;
                if (index == 173) return _Byte347 | _Byte346 << 8;
                if (index == 174) return _Byte349 | _Byte348 << 8;
                if (index == 175) return _Byte351 | _Byte350 << 8;
                if (index == 176) return _Byte353 | _Byte352 << 8;
                if (index == 177) return _Byte355 | _Byte354 << 8;
                if (index == 178) return _Byte357 | _Byte356 << 8;
                if (index == 179) return _Byte359 | _Byte358 << 8;
                if (index == 180) return _Byte361 | _Byte360 << 8;
                if (index == 181) return _Byte363 | _Byte362 << 8;
                if (index == 182) return _Byte365 | _Byte364 << 8;
                if (index == 183) return _Byte367 | _Byte366 << 8;
                if (index == 184) return _Byte369 | _Byte368 << 8;
                if (index == 185) return _Byte371 | _Byte370 << 8;
                if (index == 186) return _Byte373 | _Byte372 << 8;
                if (index == 187) return _Byte375 | _Byte374 << 8;
                if (index == 188) return _Byte377 | _Byte376 << 8;
                if (index == 189) return _Byte379 | _Byte378 << 8;
                if (index == 190) return _Byte381 | _Byte380 << 8;
                if (index == 191) return _Byte383 | _Byte382 << 8;
                if (index == 192) return _Byte385 | _Byte384 << 8;
                if (index == 193) return _Byte387 | _Byte386 << 8;
                if (index == 194) return _Byte389 | _Byte388 << 8;
                if (index == 195) return _Byte391 | _Byte390 << 8;
                if (index == 196) return _Byte393 | _Byte392 << 8;
                if (index == 197) return _Byte395 | _Byte394 << 8;
                if (index == 198) return _Byte397 | _Byte396 << 8;
                if (index == 199) return _Byte399 | _Byte398 << 8;
                if (index == 200) return _Byte401 | _Byte400 << 8;
                if (index == 201) return _Byte403 | _Byte402 << 8;
                if (index == 202) return _Byte405 | _Byte404 << 8;
                if (index == 203) return _Byte407 | _Byte406 << 8;
                if (index == 204) return _Byte409 | _Byte408 << 8;
                if (index == 205) return _Byte411 | _Byte410 << 8;
                if (index == 206) return _Byte413 | _Byte412 << 8;
                if (index == 207) return _Byte415 | _Byte414 << 8;
                if (index == 208) return _Byte417 | _Byte416 << 8;
                if (index == 209) return _Byte419 | _Byte418 << 8;
                if (index == 210) return _Byte421 | _Byte420 << 8;
                if (index == 211) return _Byte423 | _Byte422 << 8;
                if (index == 212) return _Byte425 | _Byte424 << 8;
                if (index == 213) return _Byte427 | _Byte426 << 8;
                if (index == 214) return _Byte429 | _Byte428 << 8;
                if (index == 215) return _Byte431 | _Byte430 << 8;
                if (index == 216) return _Byte433 | _Byte432 << 8;
                if (index == 217) return _Byte435 | _Byte434 << 8;
                if (index == 218) return _Byte437 | _Byte436 << 8;
                if (index == 219) return _Byte439 | _Byte438 << 8;
                if (index == 220) return _Byte441 | _Byte440 << 8;
                if (index == 221) return _Byte443 | _Byte442 << 8;
                if (index == 222) return _Byte445 | _Byte444 << 8;
                if (index == 223) return _Byte447 | _Byte446 << 8;
                if (index == 224) return _Byte449 | _Byte448 << 8;
                if (index == 225) return _Byte451 | _Byte450 << 8;
                if (index == 226) return _Byte453 | _Byte452 << 8;
                if (index == 227) return _Byte455 | _Byte454 << 8;
                if (index == 228) return _Byte457 | _Byte456 << 8;
                if (index == 229) return _Byte459 | _Byte458 << 8;
                if (index == 230) return _Byte461 | _Byte460 << 8;
                if (index == 231) return _Byte463 | _Byte462 << 8;
                if (index == 232) return _Byte465 | _Byte464 << 8;
                if (index == 233) return _Byte467 | _Byte466 << 8;
                if (index == 234) return _Byte469 | _Byte468 << 8;
                if (index == 235) return _Byte471 | _Byte470 << 8;
                if (index == 236) return _Byte473 | _Byte472 << 8;
                if (index == 237) return _Byte475 | _Byte474 << 8;
                if (index == 238) return _Byte477 | _Byte476 << 8;
                if (index == 239) return _Byte479 | _Byte478 << 8;
                if (index == 240) return _Byte481 | _Byte480 << 8;
                if (index == 241) return _Byte483 | _Byte482 << 8;
                if (index == 242) return _Byte485 | _Byte484 << 8;
                if (index == 243) return _Byte487 | _Byte486 << 8;
                if (index == 244) return _Byte489 | _Byte488 << 8;
                if (index == 245) return _Byte491 | _Byte490 << 8;
                if (index == 246) return _Byte493 | _Byte492 << 8;
                if (index == 247) return _Byte495 | _Byte494 << 8;
                if (index == 248) return _Byte497 | _Byte496 << 8;
                if (index == 249) return _Byte499 | _Byte498 << 8;
                if (index == 250) return _Byte501 | _Byte500 << 8;
                if (index == 251) return _Byte503 | _Byte502 << 8;
                if (index == 252) return _Byte505 | _Byte504 << 8;
                if (index == 253) return _Byte507 | _Byte506 << 8;
                if (index == 254) return _Byte509 | _Byte508 << 8;
                if (index == 255) return _Byte511 | _Byte510 << 8;
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