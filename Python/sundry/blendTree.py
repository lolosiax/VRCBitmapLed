from pydoc import replace
from typing import List

text = """%YAML 1.1
%TAG !u! tag:unity3d.com,2011:
--- !u!206 &20600000
BlendTree:
  m_ObjectHideFlags: 0
  m_CorrespondingSourceObject: {fileID: 0}
  m_PrefabInstance: {fileID: 0}
  m_PrefabAsset: {fileID: 0}
  m_Name: Data-$index
  m_Childs:
  - serializedVersion: 2
    m_Motion: {fileID: 7400000, guid: $sid, type: 2}
    m_Threshold: 0
    m_Position: {x: 0, y: 0}
    m_TimeScale: 1
    m_CycleOffset: 0
    m_Mirror: 0
  - serializedVersion: 2
    m_Motion: {fileID: 7400000, guid: $eid, type: 2}
    m_Threshold: 255
    m_Position: {x: 0, y: 0}
    m_TimeScale: 1
    m_CycleOffset: 0
    m_Mirror: 0
  m_BlendParameter: BitmapLed/Data$index
  m_BlendParameterY: Blend
  m_MinThreshold: 0
  m_MaxThreshold: 255
  m_UseAutomaticThresholds: 0
  m_NormalizedBlendValues: 0
  m_BlendType: 0
"""


def get_guid(line: List[str]) -> str:
    for l in line:
        if l.startswith("guid:"):
            return l.split(" ")[1].replace("\r", "").replace("\n", "")
    raise Exception("No guid found")


for i in range(640, 768):
    guid1: str
    guid2: str
    with open(f"anim/Data-{i}-A.anim.meta", "r") as meta:
        guid1 = get_guid(meta.readlines())
    with open(f"anim/Data-{i}-B.anim.meta", "r") as meta:
        guid2 = get_guid(meta.readlines())

    with open(f"anim/Data-{i}.asset", "w") as f:
        text1 = text.replace("$sid", guid1) \
            .replace("$eid", guid2) \
            .replace("$index", str(i))
        f.write(text1)
