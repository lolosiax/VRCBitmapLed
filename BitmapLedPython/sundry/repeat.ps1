foreach ($i in 512..767) {
    "[IntRange] _Byte$i(""Byte$i"", Range(0, 255)) = 0"
}

$line = ""
foreach ($i in 512..767) {
    $line += " _Byte$i"
    if (($i + 1) % 8 -eq 0 -or$i -eq 255) {
        $line = "int" +$line + ";"
        Write-Output $line
        $line = ""
    } else {
        $line += ","
    }
}

foreach ($i in 127..255) {
    $byte0 =$i * 2
    $byte1 =$i * 2 + 1
    "if (index == $i) return _Byte$byte1 | _Byte$byte0 << 8;"
}

foreach ($i in 0..255) {
    $byte0 = $i * 2
    $byte1 = $i * 2 + 1
    $byte2 = 512 + $i
    "if (index == $i) return _Byte$byte2 | _Byte$byte1 << 8 | _Byte$byte0 << 16;"
}

foreach ($i in 512..768) {
"  - nameOrPrefix: BitmapLed/Data$i"
"    remapTo: "
"    internalParameter: 0"
"    isPrefix: 0"
"    syncType: 1"
"    localOnly: 1"
"    defaultValue: 0"
"    saved: 0"
"    hasExplicitDefaultValue: 1"
"    m_overrideAnimatorDefaults: 1"
}

# ParamCopy
foreach ($i in 512..767) {
"  - m_Name: BitmapLed/Data$i"
"    m_Type: 3"
"    m_DefaultFloat: 0"
"    m_DefaultInt: 0"
"    m_DefaultBool: 0"
"    m_Controller: {fileID: 9100000}"
}

# Display
foreach ($i in 512..767) {
"  - m_Name: BitmapLed/Data$i"
"    m_Type: 1"
"    m_DefaultFloat: 0"
"    m_DefaultInt: 0"
"    m_DefaultBool: 0"
"    m_Controller: {fileID: 9100000}"
}

foreach ($i in 256..767) {
"  - type: 0"
"    name: BitmapLed/Data$i"
"    source: "
"    value: 0"
"    valueMin: 0"
"    valueMax: 0"
"    chance: 0"
"    convertRange: 0"
"    sourceMin: 0"
"    sourceMax: 0"
"    destMin: 0"
"    destMax: 0"
}