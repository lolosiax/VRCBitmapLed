using UnityEditor;
using UnityEngine;

namespace Lolosia.Util.BitmapLed.Scripts.Editor.B3.Window
{
    public class ShaderCreatorWindow : EditorWindow
    {
        [MenuItem("Lolosia/生成着色器（B3）")]
        public static void ShowWindow()
        {
            GetWindow<ShaderCreatorWindow>("生成着色器");
        }
        
        private string mCharsInput = "256";
        

        private void OnGUI()
        {
            mCharsInput = GUILayout.TextField(mCharsInput, EditorStyles.numberField);
            if (GUILayout.Button("点击生成Shader"))
            {
                int.TryParse(mCharsInput, out var chars);
                var path = EditorUtility.SaveFilePanelInProject(
                    "资产保存路径",
                    "DisplayShaderRGB(B3)" + chars * 3,
                    "shader",
                    "DisplayShader"
                );
                _ = CreateInstance<ShaderCreator>().Create(path, chars);
            }
        }
    }
}