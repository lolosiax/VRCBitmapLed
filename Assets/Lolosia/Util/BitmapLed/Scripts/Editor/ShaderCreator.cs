using System.IO;
using System.Linq;
using UnityEditor;
using UnityEngine;

namespace Lolosia.Util.BitmapLed.Scripts.Editor
{
    internal class ShaderCreator : ScriptableObject
    {
        [Tooltip("拖拽你的Shader模板文件到这里")] [SerializeField]
        public Shader shaderTemplate;

        public void Create(string path, int chars)
        {
            var templatePath = AssetDatabase.GetAssetPath(shaderTemplate);
            var template = File.ReadAllText(templatePath);

            var bytes = chars * 5;

            var lines = template.Split("\n").Select(it =>
            {
                if (it.Contains("/*%_Line_Shader_Name_%*/"))
                {
                    return $"Shader \"Unlit/DisplayShaderRGB{bytes}\"";
                }

                if (it.Contains("/*%_Line_Int_Memory_Length_%*/"))
                {
                    var size = Mathf.CeilToInt(bytes / 4f);
                    return $"            #define INT_MEMORY_LENGTH {size}";
                }

                if (it.Contains("/*%_Line_Int_Array_Content_%*/"))
                {
                    return GenerateIntArrayDef(bytes);
                }

                return it;
            });
            template = string.Join("\n", lines);

            template = template.Replace("/*%_Data_Prop_Def_%*/", GeneratePropDef(bytes));
            template = template.Replace("/*%_Data_Field_Def_%*/", GenerateFieldDef(bytes));
            template = template.Replace("\r", "").Replace("\n", "\r\n");

            File.WriteAllText(path, template);
            AssetDatabase.Refresh();
        }

        private string GeneratePropDef(int bytes)
        {
            var lines = new string[bytes];
            for (var i = 0; i < bytes; i++)
            {
                lines[i] = $"        [IntRange] _Data{i}(\"Data{i}\", Range(0, 255)) = 0";
            }

            return "\n" + string.Join("\n", lines);
        }

        private string GenerateFieldDef(int bytes)
        {
            var lineCount = Mathf.CeilToInt(bytes / 8.0f);
            var lines = new string[lineCount];
            for (var i = 0; i < lineCount; i++)
            {
                var vars = new string[8];
                for (var j = 0; j < 8; j++)
                {
                    var it = i * 8 + j;
                    vars[j] = "_Data" + it;
                }

                lines[i] = "            uint " + string.Join(", ", vars) + ";";
            }

            return "\n" + string.Join("\n", lines);
        }

        private string GenerateIntArrayDef(int bytes)
        {
            var lineCount = Mathf.CeilToInt(bytes / 4.0f);
            var lines = new string[lineCount];
            for (var i = 0; i < lineCount; i++)
            {
                var vars = new string[4];
                for (var j = 0; j < 4; j++)
                {
                    var it = i * 4 + j;
                    vars[j] = "_Data" + it;
                }

                lines[i] = "                    " +
                           $"(({vars[0]} & 0xFF) << 24) | (({vars[1]} & 0xFF) << 16) | " +
                           $"(({vars[2]} & 0xFF) << 8) | ({vars[3]} & 0xFF),";
            }

            return "\n" + string.Join("\n", lines);
        }
    }
}