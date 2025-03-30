using System;
using AnimatorAsCode.V1;
using AnimatorAsCode.V1.ModularAvatar;
using UnityEditor;
using UnityEngine;
using VRC.SDK3.Avatars.Components;

namespace Lolosia.Util.BitmapLed.Scripts.Editor
{
    internal class VrcBitmapCreator : ScriptableObject
    {
        private static readonly int FontAtlas = Shader.PropertyToID("_FontAtlas");
        private static readonly int FontAtlasGb2312 = Shader.PropertyToID("_FontAtlasGB2312");
        private static readonly int Size = Shader.PropertyToID("_Size");
        public Mesh mesh;
        public Texture2DArray fontAtlas;
        public Texture2DArray fontAtlasGb2312;
        private int mChars;
        private AacFlController mController;
        private AacFlBase mAac;

        internal void Create(string path, string prefix, int chars)
        {
            // 1. 生成 着色器
            // 2. 生成 GameObject
            // 3. 生成 AAC动画器
            // 4. 附加 MeshFilter、MeshRenderer
            // 5. 生成 材质球
            // 6. 附加 材质球 到 MeshRenderer
            // 7. 使用 AAC 生成MA参数表
            // 8. 丢入 Scene
            var gameObject = new GameObject(prefix);
            try
            {
                var shaderFile = $"{path}/{prefix}_Shader.shader";
                CreateInstance<ShaderCreator>().Create(shaderFile, chars);

                var animatorFile = $"{path}/{prefix}_Animator.asset";
                var animatorCreator = new AnimationCreator(gameObject);
                animatorCreator.Create(animatorFile, chars);
                mController = animatorCreator.Controller;
                mAac = animatorCreator.Aac;

                var meshFilter = gameObject.AddComponent<MeshFilter>();
                meshFilter.mesh = mesh;
                var meshRenderer = gameObject.AddComponent<MeshRenderer>();

                Shader newShader = AssetDatabase.LoadAssetAtPath<Shader>(shaderFile);
                Material newMat = new Material(newShader);
                newMat.SetTexture(FontAtlas, fontAtlas);
                newMat.SetTexture(FontAtlasGb2312, fontAtlasGb2312);
                newMat.SetVector(Size, new Vector4(16, 16, 0, 0));

                AssetDatabase.CreateAsset(newMat, $"{path}/{prefix}_Mat.mat");
                AssetDatabase.SaveAssets();

                meshRenderer.sharedMaterial = newMat;

                Selection.activeObject = gameObject;
                SceneView.FrameLastActiveSceneView();

                CreateMaParams(gameObject, chars);
            }
            catch
            {
                Destroy(gameObject);
                throw;
            }
        }

        private void CreateMaParams(GameObject gameObject, int chars)
        {
            var nop = mAac.NoAnimator();
            {
                var aacMa = MaAc.Create(gameObject);
                aacMa.NewMergeAnimator(mController, VRCAvatarDescriptor.AnimLayerType.FX).Relative();
            }
            {
                var obj = new GameObject("SyncParams");
                obj.transform.parent = gameObject.transform;
                var aacMa = MaAc.Create(obj);
                aacMa.NewParameter(nop.FloatParameter("Blend")).NotSaved().NotSynced().WithDefaultValue(1);
                aacMa.NewParameter(nop.BoolParameter("True")).NotSaved().NotSynced().WithDefaultValue(true);
                for (var i = 0; i < 64; i++)
                {
                    aacMa.NewParameter(nop.BoolParameter($"BitmapLed/Sync/Bit{i}")).NotSaved().WithDefaultValue(true);
                }
            }
            {
                var obj = new GameObject("DisplayParams");
                obj.transform.parent = gameObject.transform;
                var aacMa = MaAc.Create(obj);
                for (int i = 0; i < chars; i++)
                {
                    var param0 = nop.IntParameter($"BitmapLed/Memory/Data{i * 5 + 3}");
                    var param1 = nop.IntParameter($"BitmapLed/Memory/Data{i * 5 + 4}");
                    aacMa.NewParameter(param0).NotSaved().NotSynced().WithDefaultValue(0x7F);
                    aacMa.NewParameter(param1).NotSaved().NotSynced().WithDefaultValue(0xFF);
                }
            }
        }
    }
}