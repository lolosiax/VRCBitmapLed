from typing import List

text = """%YAML 1.1
%TAG !u! tag:unity3d.com,2011:
"""

textA = """--- !u!1102 &-3159569900958847$index
AnimatorState:
  serializedVersion: 1
  m_ObjectHideFlags: 1
  m_CorrespondingSourceObject: {fileID: 0}
  m_PrefabInstance: {fileID: 0}
  m_PrefabAsset: {fileID: 0}
  m_Name: Data-$index
  m_Speed: 1
  m_CycleOffset: 0
  m_Transitions: []
  m_StateMachineBehaviours: []
  m_Position: {x: 50, y: 50, z: 0}
  m_IKOnFeet: 0
  m_WriteDefaultValues: 1
  m_Mirror: 0
  m_SpeedParameterActive: 0
  m_MirrorParameterActive: 0
  m_CycleOffsetParameterActive: 0
  m_TimeParameterActive: 0
  m_Motion: {fileID: 20600000, guid: $fid, type: 2}
  m_Tag: 
  m_SpeedParameter: 
  m_MirrorParameter: 
  m_CycleOffsetParameter: 
  m_TimeParameter: 
"""

textB = """--- !u!1107 &-2264954049791841$index
AnimatorStateMachine:
  serializedVersion: 1
  m_ObjectHideFlags: 1
  m_CorrespondingSourceObject: {fileID: 0}
  m_PrefabInstance: {fileID: 0}
  m_PrefabAsset: {fileID: 0}
  m_Name: Data-$index
  m_ChildStates:
  - serializedVersion: 1
    m_State: {fileID: -3159569900958847$index}
    m_Position: {x: 370, y: 120, z: 0}
  m_ChildStateMachines: []
  m_AnyStateTransitions: []
  m_EntryTransitions: []
  m_StateMachineTransitions: {}
  m_StateMachineBehaviours: []
  m_AnyStatePosition: {x: 50, y: 20, z: 0}
  m_EntryPosition: {x: 50, y: 120, z: 0}
  m_ExitPosition: {x: 800, y: 120, z: 0}
  m_ParentStateMachinePosition: {x: 800, y: 20, z: 0}
  m_DefaultState: {fileID: -3159569900958847$index}
"""

textC = """  - serializedVersion: 5
    m_Name: Data$index
    m_StateMachine: {fileID: -2264954049791841$index}
    m_Mask: {fileID: 0}
    m_Motions: []
    m_Behaviours: []
    m_BlendingMode: 0
    m_SyncedLayerIndex: -1
    m_DefaultWeight: 0
    m_IKPass: 0
    m_SyncedLayerAffectsTiming: 0
    m_Controller: {fileID: 9100000}
"""

def get_guid(line: List[str]) -> str:
    for l in line:
        if l.startswith("guid:"):
            return l.split(" ")[1].replace("\r", "").replace("\n", "")
    raise Exception("No guid found")


for i in range(512):
    print(f"生成动画状态进度：{i}")
    guid: str
    with open(f"anim/Data-{i}.asset.meta", "r") as meta:
        guid = get_guid(meta.readlines())

    textA1 = textA.replace("$index", str(i)).replace("$fid", guid)
    textB1 = textB.replace("$index", str(i))
    text += textA1
    text += textB1

text += """
# Other
latest:
  items:
"""

for i in range(512):
    print(f"生成动画层进度：{i}")
    textC1 = textC.replace("$index", str(i))
    text += textC1

with open(f"BitmapLedDisplayAnimator512.yaml", "w") as f:
    f.write(text)
