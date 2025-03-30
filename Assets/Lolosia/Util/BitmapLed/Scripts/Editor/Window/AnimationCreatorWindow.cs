using UnityEditor;
using UnityEngine;

namespace Lolosia.Util.BitmapLed.Scripts.Editor.Window
{
    public class AnimationCreatorWindow : EditorWindow
    {
        [MenuItem("Lolosia/生成动画器")]
        public static void ShowWindow()
        { 
            GetWindow<AnimationCreatorWindow>("生成动画器");
        }
        
        private GameObject mRoot;
        private GameObject mToggleItem;
        private string mCharsInput = "256";

        private void OnGUI()
        {
            mRoot = (GameObject)EditorGUILayout.ObjectField("动画根", mRoot, typeof(GameObject), true);
            mToggleItem = (GameObject)EditorGUILayout.ObjectField("要控制的目标", mToggleItem, typeof(GameObject), true);

            mCharsInput = GUILayout.TextField(mCharsInput, EditorStyles.numberField);
            if (GUILayout.Button("点击生成控制器"))
            {
                var path = EditorUtility.SaveFilePanelInProject("资产保存路径", "AAC Demo", "asset", "AAC Demo");
                int.TryParse(mCharsInput, out var size);
                new AnimationCreator(mRoot, mToggleItem).Create(path, size);
            }
        }
    }
}