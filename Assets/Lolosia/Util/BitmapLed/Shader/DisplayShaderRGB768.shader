/**
 * 这是绘制点阵屏幕所需的Shader, 512 Byte 版本
 * 文件遵循 MIT 协议，©2025 洛洛希雅 版权所有
 */
Shader "Unlit/DisplayShaderRGB768"
{
    Properties
    {
        _Size ("Size", Vector) = (32,4,0,0)
        _ShadowColor ("Shadow Color", Color) = (0,0,0,1)
        _BackgroundColor ("Background Color", Color) = (0,0,0,0)
        _ShadowOffset ("Shadow Offset", Vector) = (1,1,0,0)
        [NoScaleOffset] _FontColor ("Font Color", 2D) = "white" {}
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
            fixed4 _ShadowColor;
            float4 _BackgroundColor;
            float2 _ShadowOffset;
            sampler2D _FontColor;
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
            int _Byte512, _Byte513, _Byte514, _Byte515, _Byte516, _Byte517, _Byte518, _Byte519;
            int _Byte520, _Byte521, _Byte522, _Byte523, _Byte524, _Byte525, _Byte526, _Byte527;
            int _Byte528, _Byte529, _Byte530, _Byte531, _Byte532, _Byte533, _Byte534, _Byte535;
            int _Byte536, _Byte537, _Byte538, _Byte539, _Byte540, _Byte541, _Byte542, _Byte543;
            int _Byte544, _Byte545, _Byte546, _Byte547, _Byte548, _Byte549, _Byte550, _Byte551;
            int _Byte552, _Byte553, _Byte554, _Byte555, _Byte556, _Byte557, _Byte558, _Byte559;
            int _Byte560, _Byte561, _Byte562, _Byte563, _Byte564, _Byte565, _Byte566, _Byte567;
            int _Byte568, _Byte569, _Byte570, _Byte571, _Byte572, _Byte573, _Byte574, _Byte575;
            int _Byte576, _Byte577, _Byte578, _Byte579, _Byte580, _Byte581, _Byte582, _Byte583;
            int _Byte584, _Byte585, _Byte586, _Byte587, _Byte588, _Byte589, _Byte590, _Byte591;
            int _Byte592, _Byte593, _Byte594, _Byte595, _Byte596, _Byte597, _Byte598, _Byte599;
            int _Byte600, _Byte601, _Byte602, _Byte603, _Byte604, _Byte605, _Byte606, _Byte607;
            int _Byte608, _Byte609, _Byte610, _Byte611, _Byte612, _Byte613, _Byte614, _Byte615;
            int _Byte616, _Byte617, _Byte618, _Byte619, _Byte620, _Byte621, _Byte622, _Byte623;
            int _Byte624, _Byte625, _Byte626, _Byte627, _Byte628, _Byte629, _Byte630, _Byte631;
            int _Byte632, _Byte633, _Byte634, _Byte635, _Byte636, _Byte637, _Byte638, _Byte639;
            int _Byte640, _Byte641, _Byte642, _Byte643, _Byte644, _Byte645, _Byte646, _Byte647;
            int _Byte648, _Byte649, _Byte650, _Byte651, _Byte652, _Byte653, _Byte654, _Byte655;
            int _Byte656, _Byte657, _Byte658, _Byte659, _Byte660, _Byte661, _Byte662, _Byte663;
            int _Byte664, _Byte665, _Byte666, _Byte667, _Byte668, _Byte669, _Byte670, _Byte671;
            int _Byte672, _Byte673, _Byte674, _Byte675, _Byte676, _Byte677, _Byte678, _Byte679;
            int _Byte680, _Byte681, _Byte682, _Byte683, _Byte684, _Byte685, _Byte686, _Byte687;
            int _Byte688, _Byte689, _Byte690, _Byte691, _Byte692, _Byte693, _Byte694, _Byte695;
            int _Byte696, _Byte697, _Byte698, _Byte699, _Byte700, _Byte701, _Byte702, _Byte703;
            int _Byte704, _Byte705, _Byte706, _Byte707, _Byte708, _Byte709, _Byte710, _Byte711;
            int _Byte712, _Byte713, _Byte714, _Byte715, _Byte716, _Byte717, _Byte718, _Byte719;
            int _Byte720, _Byte721, _Byte722, _Byte723, _Byte724, _Byte725, _Byte726, _Byte727;
            int _Byte728, _Byte729, _Byte730, _Byte731, _Byte732, _Byte733, _Byte734, _Byte735;
            int _Byte736, _Byte737, _Byte738, _Byte739, _Byte740, _Byte741, _Byte742, _Byte743;
            int _Byte744, _Byte745, _Byte746, _Byte747, _Byte748, _Byte749, _Byte750, _Byte751;
            int _Byte752, _Byte753, _Byte754, _Byte755, _Byte756, _Byte757, _Byte758, _Byte759;
            int _Byte760, _Byte761, _Byte762, _Byte763, _Byte764, _Byte765, _Byte766, _Byte767;

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
                // 根据索引返回对应的编码
                // 高16位为码点，低8位为色彩
                if (index == 0) return _Byte512 | _Byte1 << 8 | _Byte0 << 16;
                if (index == 1) return _Byte513 | _Byte3 << 8 | _Byte2 << 16;
                if (index == 2) return _Byte514 | _Byte5 << 8 | _Byte4 << 16;
                if (index == 3) return _Byte515 | _Byte7 << 8 | _Byte6 << 16;
                if (index == 4) return _Byte516 | _Byte9 << 8 | _Byte8 << 16;
                if (index == 5) return _Byte517 | _Byte11 << 8 | _Byte10 << 16;
                if (index == 6) return _Byte518 | _Byte13 << 8 | _Byte12 << 16;
                if (index == 7) return _Byte519 | _Byte15 << 8 | _Byte14 << 16;
                if (index == 8) return _Byte520 | _Byte17 << 8 | _Byte16 << 16;
                if (index == 9) return _Byte521 | _Byte19 << 8 | _Byte18 << 16;
                if (index == 10) return _Byte522 | _Byte21 << 8 | _Byte20 << 16;
                if (index == 11) return _Byte523 | _Byte23 << 8 | _Byte22 << 16;
                if (index == 12) return _Byte524 | _Byte25 << 8 | _Byte24 << 16;
                if (index == 13) return _Byte525 | _Byte27 << 8 | _Byte26 << 16;
                if (index == 14) return _Byte526 | _Byte29 << 8 | _Byte28 << 16;
                if (index == 15) return _Byte527 | _Byte31 << 8 | _Byte30 << 16;
                if (index == 16) return _Byte528 | _Byte33 << 8 | _Byte32 << 16;
                if (index == 17) return _Byte529 | _Byte35 << 8 | _Byte34 << 16;
                if (index == 18) return _Byte530 | _Byte37 << 8 | _Byte36 << 16;
                if (index == 19) return _Byte531 | _Byte39 << 8 | _Byte38 << 16;
                if (index == 20) return _Byte532 | _Byte41 << 8 | _Byte40 << 16;
                if (index == 21) return _Byte533 | _Byte43 << 8 | _Byte42 << 16;
                if (index == 22) return _Byte534 | _Byte45 << 8 | _Byte44 << 16;
                if (index == 23) return _Byte535 | _Byte47 << 8 | _Byte46 << 16;
                if (index == 24) return _Byte536 | _Byte49 << 8 | _Byte48 << 16;
                if (index == 25) return _Byte537 | _Byte51 << 8 | _Byte50 << 16;
                if (index == 26) return _Byte538 | _Byte53 << 8 | _Byte52 << 16;
                if (index == 27) return _Byte539 | _Byte55 << 8 | _Byte54 << 16;
                if (index == 28) return _Byte540 | _Byte57 << 8 | _Byte56 << 16;
                if (index == 29) return _Byte541 | _Byte59 << 8 | _Byte58 << 16;
                if (index == 30) return _Byte542 | _Byte61 << 8 | _Byte60 << 16;
                if (index == 31) return _Byte543 | _Byte63 << 8 | _Byte62 << 16;
                if (index == 32) return _Byte544 | _Byte65 << 8 | _Byte64 << 16;
                if (index == 33) return _Byte545 | _Byte67 << 8 | _Byte66 << 16;
                if (index == 34) return _Byte546 | _Byte69 << 8 | _Byte68 << 16;
                if (index == 35) return _Byte547 | _Byte71 << 8 | _Byte70 << 16;
                if (index == 36) return _Byte548 | _Byte73 << 8 | _Byte72 << 16;
                if (index == 37) return _Byte549 | _Byte75 << 8 | _Byte74 << 16;
                if (index == 38) return _Byte550 | _Byte77 << 8 | _Byte76 << 16;
                if (index == 39) return _Byte551 | _Byte79 << 8 | _Byte78 << 16;
                if (index == 40) return _Byte552 | _Byte81 << 8 | _Byte80 << 16;
                if (index == 41) return _Byte553 | _Byte83 << 8 | _Byte82 << 16;
                if (index == 42) return _Byte554 | _Byte85 << 8 | _Byte84 << 16;
                if (index == 43) return _Byte555 | _Byte87 << 8 | _Byte86 << 16;
                if (index == 44) return _Byte556 | _Byte89 << 8 | _Byte88 << 16;
                if (index == 45) return _Byte557 | _Byte91 << 8 | _Byte90 << 16;
                if (index == 46) return _Byte558 | _Byte93 << 8 | _Byte92 << 16;
                if (index == 47) return _Byte559 | _Byte95 << 8 | _Byte94 << 16;
                if (index == 48) return _Byte560 | _Byte97 << 8 | _Byte96 << 16;
                if (index == 49) return _Byte561 | _Byte99 << 8 | _Byte98 << 16;
                if (index == 50) return _Byte562 | _Byte101 << 8 | _Byte100 << 16;
                if (index == 51) return _Byte563 | _Byte103 << 8 | _Byte102 << 16;
                if (index == 52) return _Byte564 | _Byte105 << 8 | _Byte104 << 16;
                if (index == 53) return _Byte565 | _Byte107 << 8 | _Byte106 << 16;
                if (index == 54) return _Byte566 | _Byte109 << 8 | _Byte108 << 16;
                if (index == 55) return _Byte567 | _Byte111 << 8 | _Byte110 << 16;
                if (index == 56) return _Byte568 | _Byte113 << 8 | _Byte112 << 16;
                if (index == 57) return _Byte569 | _Byte115 << 8 | _Byte114 << 16;
                if (index == 58) return _Byte570 | _Byte117 << 8 | _Byte116 << 16;
                if (index == 59) return _Byte571 | _Byte119 << 8 | _Byte118 << 16;
                if (index == 60) return _Byte572 | _Byte121 << 8 | _Byte120 << 16;
                if (index == 61) return _Byte573 | _Byte123 << 8 | _Byte122 << 16;
                if (index == 62) return _Byte574 | _Byte125 << 8 | _Byte124 << 16;
                if (index == 63) return _Byte575 | _Byte127 << 8 | _Byte126 << 16;
                if (index == 64) return _Byte576 | _Byte129 << 8 | _Byte128 << 16;
                if (index == 65) return _Byte577 | _Byte131 << 8 | _Byte130 << 16;
                if (index == 66) return _Byte578 | _Byte133 << 8 | _Byte132 << 16;
                if (index == 67) return _Byte579 | _Byte135 << 8 | _Byte134 << 16;
                if (index == 68) return _Byte580 | _Byte137 << 8 | _Byte136 << 16;
                if (index == 69) return _Byte581 | _Byte139 << 8 | _Byte138 << 16;
                if (index == 70) return _Byte582 | _Byte141 << 8 | _Byte140 << 16;
                if (index == 71) return _Byte583 | _Byte143 << 8 | _Byte142 << 16;
                if (index == 72) return _Byte584 | _Byte145 << 8 | _Byte144 << 16;
                if (index == 73) return _Byte585 | _Byte147 << 8 | _Byte146 << 16;
                if (index == 74) return _Byte586 | _Byte149 << 8 | _Byte148 << 16;
                if (index == 75) return _Byte587 | _Byte151 << 8 | _Byte150 << 16;
                if (index == 76) return _Byte588 | _Byte153 << 8 | _Byte152 << 16;
                if (index == 77) return _Byte589 | _Byte155 << 8 | _Byte154 << 16;
                if (index == 78) return _Byte590 | _Byte157 << 8 | _Byte156 << 16;
                if (index == 79) return _Byte591 | _Byte159 << 8 | _Byte158 << 16;
                if (index == 80) return _Byte592 | _Byte161 << 8 | _Byte160 << 16;
                if (index == 81) return _Byte593 | _Byte163 << 8 | _Byte162 << 16;
                if (index == 82) return _Byte594 | _Byte165 << 8 | _Byte164 << 16;
                if (index == 83) return _Byte595 | _Byte167 << 8 | _Byte166 << 16;
                if (index == 84) return _Byte596 | _Byte169 << 8 | _Byte168 << 16;
                if (index == 85) return _Byte597 | _Byte171 << 8 | _Byte170 << 16;
                if (index == 86) return _Byte598 | _Byte173 << 8 | _Byte172 << 16;
                if (index == 87) return _Byte599 | _Byte175 << 8 | _Byte174 << 16;
                if (index == 88) return _Byte600 | _Byte177 << 8 | _Byte176 << 16;
                if (index == 89) return _Byte601 | _Byte179 << 8 | _Byte178 << 16;
                if (index == 90) return _Byte602 | _Byte181 << 8 | _Byte180 << 16;
                if (index == 91) return _Byte603 | _Byte183 << 8 | _Byte182 << 16;
                if (index == 92) return _Byte604 | _Byte185 << 8 | _Byte184 << 16;
                if (index == 93) return _Byte605 | _Byte187 << 8 | _Byte186 << 16;
                if (index == 94) return _Byte606 | _Byte189 << 8 | _Byte188 << 16;
                if (index == 95) return _Byte607 | _Byte191 << 8 | _Byte190 << 16;
                if (index == 96) return _Byte608 | _Byte193 << 8 | _Byte192 << 16;
                if (index == 97) return _Byte609 | _Byte195 << 8 | _Byte194 << 16;
                if (index == 98) return _Byte610 | _Byte197 << 8 | _Byte196 << 16;
                if (index == 99) return _Byte611 | _Byte199 << 8 | _Byte198 << 16;
                if (index == 100) return _Byte612 | _Byte201 << 8 | _Byte200 << 16;
                if (index == 101) return _Byte613 | _Byte203 << 8 | _Byte202 << 16;
                if (index == 102) return _Byte614 | _Byte205 << 8 | _Byte204 << 16;
                if (index == 103) return _Byte615 | _Byte207 << 8 | _Byte206 << 16;
                if (index == 104) return _Byte616 | _Byte209 << 8 | _Byte208 << 16;
                if (index == 105) return _Byte617 | _Byte211 << 8 | _Byte210 << 16;
                if (index == 106) return _Byte618 | _Byte213 << 8 | _Byte212 << 16;
                if (index == 107) return _Byte619 | _Byte215 << 8 | _Byte214 << 16;
                if (index == 108) return _Byte620 | _Byte217 << 8 | _Byte216 << 16;
                if (index == 109) return _Byte621 | _Byte219 << 8 | _Byte218 << 16;
                if (index == 110) return _Byte622 | _Byte221 << 8 | _Byte220 << 16;
                if (index == 111) return _Byte623 | _Byte223 << 8 | _Byte222 << 16;
                if (index == 112) return _Byte624 | _Byte225 << 8 | _Byte224 << 16;
                if (index == 113) return _Byte625 | _Byte227 << 8 | _Byte226 << 16;
                if (index == 114) return _Byte626 | _Byte229 << 8 | _Byte228 << 16;
                if (index == 115) return _Byte627 | _Byte231 << 8 | _Byte230 << 16;
                if (index == 116) return _Byte628 | _Byte233 << 8 | _Byte232 << 16;
                if (index == 117) return _Byte629 | _Byte235 << 8 | _Byte234 << 16;
                if (index == 118) return _Byte630 | _Byte237 << 8 | _Byte236 << 16;
                if (index == 119) return _Byte631 | _Byte239 << 8 | _Byte238 << 16;
                if (index == 120) return _Byte632 | _Byte241 << 8 | _Byte240 << 16;
                if (index == 121) return _Byte633 | _Byte243 << 8 | _Byte242 << 16;
                if (index == 122) return _Byte634 | _Byte245 << 8 | _Byte244 << 16;
                if (index == 123) return _Byte635 | _Byte247 << 8 | _Byte246 << 16;
                if (index == 124) return _Byte636 | _Byte249 << 8 | _Byte248 << 16;
                if (index == 125) return _Byte637 | _Byte251 << 8 | _Byte250 << 16;
                if (index == 126) return _Byte638 | _Byte253 << 8 | _Byte252 << 16;
                if (index == 127) return _Byte639 | _Byte255 << 8 | _Byte254 << 16;
                if (index == 128) return _Byte640 | _Byte257 << 8 | _Byte256 << 16;
                if (index == 129) return _Byte641 | _Byte259 << 8 | _Byte258 << 16;
                if (index == 130) return _Byte642 | _Byte261 << 8 | _Byte260 << 16;
                if (index == 131) return _Byte643 | _Byte263 << 8 | _Byte262 << 16;
                if (index == 132) return _Byte644 | _Byte265 << 8 | _Byte264 << 16;
                if (index == 133) return _Byte645 | _Byte267 << 8 | _Byte266 << 16;
                if (index == 134) return _Byte646 | _Byte269 << 8 | _Byte268 << 16;
                if (index == 135) return _Byte647 | _Byte271 << 8 | _Byte270 << 16;
                if (index == 136) return _Byte648 | _Byte273 << 8 | _Byte272 << 16;
                if (index == 137) return _Byte649 | _Byte275 << 8 | _Byte274 << 16;
                if (index == 138) return _Byte650 | _Byte277 << 8 | _Byte276 << 16;
                if (index == 139) return _Byte651 | _Byte279 << 8 | _Byte278 << 16;
                if (index == 140) return _Byte652 | _Byte281 << 8 | _Byte280 << 16;
                if (index == 141) return _Byte653 | _Byte283 << 8 | _Byte282 << 16;
                if (index == 142) return _Byte654 | _Byte285 << 8 | _Byte284 << 16;
                if (index == 143) return _Byte655 | _Byte287 << 8 | _Byte286 << 16;
                if (index == 144) return _Byte656 | _Byte289 << 8 | _Byte288 << 16;
                if (index == 145) return _Byte657 | _Byte291 << 8 | _Byte290 << 16;
                if (index == 146) return _Byte658 | _Byte293 << 8 | _Byte292 << 16;
                if (index == 147) return _Byte659 | _Byte295 << 8 | _Byte294 << 16;
                if (index == 148) return _Byte660 | _Byte297 << 8 | _Byte296 << 16;
                if (index == 149) return _Byte661 | _Byte299 << 8 | _Byte298 << 16;
                if (index == 150) return _Byte662 | _Byte301 << 8 | _Byte300 << 16;
                if (index == 151) return _Byte663 | _Byte303 << 8 | _Byte302 << 16;
                if (index == 152) return _Byte664 | _Byte305 << 8 | _Byte304 << 16;
                if (index == 153) return _Byte665 | _Byte307 << 8 | _Byte306 << 16;
                if (index == 154) return _Byte666 | _Byte309 << 8 | _Byte308 << 16;
                if (index == 155) return _Byte667 | _Byte311 << 8 | _Byte310 << 16;
                if (index == 156) return _Byte668 | _Byte313 << 8 | _Byte312 << 16;
                if (index == 157) return _Byte669 | _Byte315 << 8 | _Byte314 << 16;
                if (index == 158) return _Byte670 | _Byte317 << 8 | _Byte316 << 16;
                if (index == 159) return _Byte671 | _Byte319 << 8 | _Byte318 << 16;
                if (index == 160) return _Byte672 | _Byte321 << 8 | _Byte320 << 16;
                if (index == 161) return _Byte673 | _Byte323 << 8 | _Byte322 << 16;
                if (index == 162) return _Byte674 | _Byte325 << 8 | _Byte324 << 16;
                if (index == 163) return _Byte675 | _Byte327 << 8 | _Byte326 << 16;
                if (index == 164) return _Byte676 | _Byte329 << 8 | _Byte328 << 16;
                if (index == 165) return _Byte677 | _Byte331 << 8 | _Byte330 << 16;
                if (index == 166) return _Byte678 | _Byte333 << 8 | _Byte332 << 16;
                if (index == 167) return _Byte679 | _Byte335 << 8 | _Byte334 << 16;
                if (index == 168) return _Byte680 | _Byte337 << 8 | _Byte336 << 16;
                if (index == 169) return _Byte681 | _Byte339 << 8 | _Byte338 << 16;
                if (index == 170) return _Byte682 | _Byte341 << 8 | _Byte340 << 16;
                if (index == 171) return _Byte683 | _Byte343 << 8 | _Byte342 << 16;
                if (index == 172) return _Byte684 | _Byte345 << 8 | _Byte344 << 16;
                if (index == 173) return _Byte685 | _Byte347 << 8 | _Byte346 << 16;
                if (index == 174) return _Byte686 | _Byte349 << 8 | _Byte348 << 16;
                if (index == 175) return _Byte687 | _Byte351 << 8 | _Byte350 << 16;
                if (index == 176) return _Byte688 | _Byte353 << 8 | _Byte352 << 16;
                if (index == 177) return _Byte689 | _Byte355 << 8 | _Byte354 << 16;
                if (index == 178) return _Byte690 | _Byte357 << 8 | _Byte356 << 16;
                if (index == 179) return _Byte691 | _Byte359 << 8 | _Byte358 << 16;
                if (index == 180) return _Byte692 | _Byte361 << 8 | _Byte360 << 16;
                if (index == 181) return _Byte693 | _Byte363 << 8 | _Byte362 << 16;
                if (index == 182) return _Byte694 | _Byte365 << 8 | _Byte364 << 16;
                if (index == 183) return _Byte695 | _Byte367 << 8 | _Byte366 << 16;
                if (index == 184) return _Byte696 | _Byte369 << 8 | _Byte368 << 16;
                if (index == 185) return _Byte697 | _Byte371 << 8 | _Byte370 << 16;
                if (index == 186) return _Byte698 | _Byte373 << 8 | _Byte372 << 16;
                if (index == 187) return _Byte699 | _Byte375 << 8 | _Byte374 << 16;
                if (index == 188) return _Byte700 | _Byte377 << 8 | _Byte376 << 16;
                if (index == 189) return _Byte701 | _Byte379 << 8 | _Byte378 << 16;
                if (index == 190) return _Byte702 | _Byte381 << 8 | _Byte380 << 16;
                if (index == 191) return _Byte703 | _Byte383 << 8 | _Byte382 << 16;
                if (index == 192) return _Byte704 | _Byte385 << 8 | _Byte384 << 16;
                if (index == 193) return _Byte705 | _Byte387 << 8 | _Byte386 << 16;
                if (index == 194) return _Byte706 | _Byte389 << 8 | _Byte388 << 16;
                if (index == 195) return _Byte707 | _Byte391 << 8 | _Byte390 << 16;
                if (index == 196) return _Byte708 | _Byte393 << 8 | _Byte392 << 16;
                if (index == 197) return _Byte709 | _Byte395 << 8 | _Byte394 << 16;
                if (index == 198) return _Byte710 | _Byte397 << 8 | _Byte396 << 16;
                if (index == 199) return _Byte711 | _Byte399 << 8 | _Byte398 << 16;
                if (index == 200) return _Byte712 | _Byte401 << 8 | _Byte400 << 16;
                if (index == 201) return _Byte713 | _Byte403 << 8 | _Byte402 << 16;
                if (index == 202) return _Byte714 | _Byte405 << 8 | _Byte404 << 16;
                if (index == 203) return _Byte715 | _Byte407 << 8 | _Byte406 << 16;
                if (index == 204) return _Byte716 | _Byte409 << 8 | _Byte408 << 16;
                if (index == 205) return _Byte717 | _Byte411 << 8 | _Byte410 << 16;
                if (index == 206) return _Byte718 | _Byte413 << 8 | _Byte412 << 16;
                if (index == 207) return _Byte719 | _Byte415 << 8 | _Byte414 << 16;
                if (index == 208) return _Byte720 | _Byte417 << 8 | _Byte416 << 16;
                if (index == 209) return _Byte721 | _Byte419 << 8 | _Byte418 << 16;
                if (index == 210) return _Byte722 | _Byte421 << 8 | _Byte420 << 16;
                if (index == 211) return _Byte723 | _Byte423 << 8 | _Byte422 << 16;
                if (index == 212) return _Byte724 | _Byte425 << 8 | _Byte424 << 16;
                if (index == 213) return _Byte725 | _Byte427 << 8 | _Byte426 << 16;
                if (index == 214) return _Byte726 | _Byte429 << 8 | _Byte428 << 16;
                if (index == 215) return _Byte727 | _Byte431 << 8 | _Byte430 << 16;
                if (index == 216) return _Byte728 | _Byte433 << 8 | _Byte432 << 16;
                if (index == 217) return _Byte729 | _Byte435 << 8 | _Byte434 << 16;
                if (index == 218) return _Byte730 | _Byte437 << 8 | _Byte436 << 16;
                if (index == 219) return _Byte731 | _Byte439 << 8 | _Byte438 << 16;
                if (index == 220) return _Byte732 | _Byte441 << 8 | _Byte440 << 16;
                if (index == 221) return _Byte733 | _Byte443 << 8 | _Byte442 << 16;
                if (index == 222) return _Byte734 | _Byte445 << 8 | _Byte444 << 16;
                if (index == 223) return _Byte735 | _Byte447 << 8 | _Byte446 << 16;
                if (index == 224) return _Byte736 | _Byte449 << 8 | _Byte448 << 16;
                if (index == 225) return _Byte737 | _Byte451 << 8 | _Byte450 << 16;
                if (index == 226) return _Byte738 | _Byte453 << 8 | _Byte452 << 16;
                if (index == 227) return _Byte739 | _Byte455 << 8 | _Byte454 << 16;
                if (index == 228) return _Byte740 | _Byte457 << 8 | _Byte456 << 16;
                if (index == 229) return _Byte741 | _Byte459 << 8 | _Byte458 << 16;
                if (index == 230) return _Byte742 | _Byte461 << 8 | _Byte460 << 16;
                if (index == 231) return _Byte743 | _Byte463 << 8 | _Byte462 << 16;
                if (index == 232) return _Byte744 | _Byte465 << 8 | _Byte464 << 16;
                if (index == 233) return _Byte745 | _Byte467 << 8 | _Byte466 << 16;
                if (index == 234) return _Byte746 | _Byte469 << 8 | _Byte468 << 16;
                if (index == 235) return _Byte747 | _Byte471 << 8 | _Byte470 << 16;
                if (index == 236) return _Byte748 | _Byte473 << 8 | _Byte472 << 16;
                if (index == 237) return _Byte749 | _Byte475 << 8 | _Byte474 << 16;
                if (index == 238) return _Byte750 | _Byte477 << 8 | _Byte476 << 16;
                if (index == 239) return _Byte751 | _Byte479 << 8 | _Byte478 << 16;
                if (index == 240) return _Byte752 | _Byte481 << 8 | _Byte480 << 16;
                if (index == 241) return _Byte753 | _Byte483 << 8 | _Byte482 << 16;
                if (index == 242) return _Byte754 | _Byte485 << 8 | _Byte484 << 16;
                if (index == 243) return _Byte755 | _Byte487 << 8 | _Byte486 << 16;
                if (index == 244) return _Byte756 | _Byte489 << 8 | _Byte488 << 16;
                if (index == 245) return _Byte757 | _Byte491 << 8 | _Byte490 << 16;
                if (index == 246) return _Byte758 | _Byte493 << 8 | _Byte492 << 16;
                if (index == 247) return _Byte759 | _Byte495 << 8 | _Byte494 << 16;
                if (index == 248) return _Byte760 | _Byte497 << 8 | _Byte496 << 16;
                if (index == 249) return _Byte761 | _Byte499 << 8 | _Byte498 << 16;
                if (index == 250) return _Byte762 | _Byte501 << 8 | _Byte500 << 16;
                if (index == 251) return _Byte763 | _Byte503 << 8 | _Byte502 << 16;
                if (index == 252) return _Byte764 | _Byte505 << 8 | _Byte504 << 16;
                if (index == 253) return _Byte765 | _Byte507 << 8 | _Byte506 << 16;
                if (index == 254) return _Byte766 | _Byte509 << 8 | _Byte508 << 16;
                if (index == 255) return _Byte767 | _Byte511 << 8 | _Byte510 << 16;
                return 0;
            }

            fixed4 frag(v2f i) : SV_Target
            {
                int2 size = int2(floor(_Size.x), floor(_Size.y));
                // 计算当前UV对应的字符行列
                int row = floor((1 - i.uv.y) * size.y); // 反转Y轴
                int col = floor(i.uv.x * size.x); // 32列
                int index = row * size.x + col; // 字符索引 (0-127)
                int codePointWithColor = GetCodePoint(index);
                int codePoint = (codePointWithColor >> 8) & 0xffff;
                int codeColor = codePointWithColor & 0xff;

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

                // 获取字体颜色所在行列
                int fontColorCol = (codeColor >> 4) & 0xf;
                int fontColorRow = codeColor & 0xf;

                // 混合颜色
                fixed4 rs = _BackgroundColor;
                float2 fontColorUV = float2(fontColorCol / 16.0, fontColorRow / 16.0);

                if (fontColorCol >= 13) // 13列以后是绘制背景而不是绘制字体
                {
                    if (showMain) rs = _ShadowColor;
                    else {
                        rs = tex2D(_FontColor, fontColorUV);
                    }
                }
                else
                {
                    if (showShadow) rs = _ShadowColor;
                    if (showMain)
                    {
                        rs = tex2D(_FontColor, fontColorUV);
                    }
                }

                return rs;
            }
            ENDCG
        }
    }
}