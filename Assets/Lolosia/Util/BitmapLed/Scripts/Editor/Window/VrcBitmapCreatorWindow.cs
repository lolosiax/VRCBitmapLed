using System;
using System.Threading.Tasks;
using UnityEditor;
using UnityEngine;

namespace Lolosia.Util.BitmapLed.Scripts.Editor.Window
{
  public class VrcBitmapCreatorWindow : EditorWindow
  {
    [MenuItem("Lolosia/生成点阵屏")]
    public static void ShowWindow()
    {
      GetWindow<VrcBitmapCreatorWindow>("生成点阵屏");
    }

    private string mCharsInput = "256";
    private string mFileName = "";
    private bool mProcessing = false;

    private void OnGUI()
    {
      if (mProcessing)
      {
        GUILayout.Label("正在处理……");
        return;
      }
      mCharsInput = GUILayout.TextField(mCharsInput, EditorStyles.numberField);
      mFileName = GUILayout.TextField(mFileName, EditorStyles.textField);

      var prefix = string.IsNullOrEmpty(mFileName) ? "BitmapLed" : mFileName;

      GUILayout.Label($"最终生成的资源名称将以“{prefix}_”开头");

      if (!GUILayout.Button("创建点阵屏")) return;

      int.TryParse(mCharsInput, out var chars);

      var path = EditorUtility.OpenFolderPanel("选择资源保存路径", Application.dataPath, "");
      if (path.Length == 0) return;
      if (!path.StartsWith(Application.dataPath))
      {
        EditorUtility.DisplayDialog("错误", "不能选择Assets以外的文件夹！", "确定");
        return;
      }

      path = "Assets" + path[Application.dataPath.Length..];

      _ = Process(path, prefix, chars);
    }

    private async Task Process(string path, string prefix, int chars)
    {
      mProcessing = true;
      try
      {
        await CreateInstance<VrcBitmapCreator>().Create(path, prefix, chars);
      }
      catch (Exception e)
      {
        Debug.LogException(e);
      }
      finally
      {
        mProcessing = false;
      }
    }
  }
}