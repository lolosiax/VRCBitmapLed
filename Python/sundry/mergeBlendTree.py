from typing import List

before = """%YAML 1.1
%TAG !u! tag:unity3d.com,2011:
--- !u!206 &20600000
BlendTree:
  m_ObjectHideFlags: 0
  m_CorrespondingSourceObject: {fileID: 0}
  m_PrefabInstance: {fileID: 0}
  m_PrefabAsset: {fileID: 0}
  m_Name: New BlendTree
  m_Childs:"""

template = """
  - serializedVersion: 2
    m_Motion: {fileID: 20600000, guid: $fid, type: 2}
    m_Threshold: 0
    m_Position: {x: 0, y: 0}
    m_TimeScale: 1
    m_CycleOffset: 0
    m_DirectBlendParameter: Blend
    m_Mirror: 0"""

after = """  m_BlendParameter: Blend
  m_BlendParameterY: Blend
  m_MinThreshold: 0
  m_MaxThreshold: 1
  m_UseAutomaticThresholds: 1
  m_NormalizedBlendValues: 0
  m_BlendType: 4
"""

text = before


def get_guid(line: List[str]) -> str:
    for l in line:
        if l.startswith("guid:"):
            return l.split(" ")[1].replace("\r", "").replace("\n", "")
    raise Exception("No guid found")


for i in range(768):
    print(f"生成聚合混合树进度：{i}")
    guid: str
    with open(f"anim/Data-{i}.asset.meta", "r") as meta:
        guid = get_guid(meta.readlines())

    text += template.replace("$fid", guid)

text += "\n"
text += after

with open(f"BitmapLedMergedBlendTree768.yaml", "w") as f:
    f.write(text)
