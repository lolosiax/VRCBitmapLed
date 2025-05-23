using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AnimatorAsCode.V1;
using AnimatorAsCode.V1.VRC;
using JetBrains.Annotations;
using UnityEditor;
using UnityEditor.Animations;
using UnityEngine;
using Object = UnityEngine.Object;

namespace Lolosia.Util.BitmapLed.Scripts.Editor
{
  internal class AnimationCreator
  {
    private class DummyObject : ScriptableObject
    {
    }

    private readonly GameObject mRoot;
    private readonly GameObject mToggleItem;
    private AacFlBase mAac;
    private AacFlController mController;
    private int mChars;
    private int mMemorySize;

    internal AacFlController Controller => mController;
    internal AacFlBase Aac => mAac;

    internal AnimationCreator(GameObject root) : this(root, root)
    {
    }

    internal AnimationCreator(GameObject root, GameObject toggleItem)
    {
      mRoot = root;
      mToggleItem = toggleItem;
    }

    public async Task Create(string path, int size)
    {
      Debug.Log($"开始创建动画器，长度{size}，位于{path}");
      mChars = size;
      mMemorySize = size * 5;
      var systemName = "BitmapLed";
      // Supply your animator root located in the world
      var animatorRoot = mRoot.transform;

      // Supply the world animator controller asset
      // mController = new AnimatorController();
      // AssetDatabase.CreateAsset(mController, path);

      // Supply a project asset where generated assets will be added to
      Object assetContainer = ScriptableObject.CreateInstance<DummyObject>();
      var assetContainerPath = path.Substring(0, path.Length - 6) + ".asset";
      AssetDatabase.CreateAsset(assetContainer, assetContainerPath);

      var assetKey = GUID.Generate().ToString();
      mAac = AacV1.Create(new AacConfiguration
      {
        SystemName = systemName,
        AnimatorRoot = animatorRoot,
        DefaultValueRoot = animatorRoot,
        AssetKey = assetKey,
        AssetContainer = assetContainer,
        ContainerMode = AacConfiguration.Container.Everything,
        DefaultsProvider = new AacDefaultsProvider(true)
      });
      
      await Task.Yield();
      
      mController = mAac.NewAnimatorController();

      await CreateMaterialLayer();
      await Task.Yield();
      
      await CreateUnicodeLayer();
      await Task.Yield();
      // CreateUnicodeParamCopyLayer();

      // CreateGb2312Layer();
      // CreateGb2312ParamCopyLayer();

      CreateColorLayer();

      AssetDatabase.SaveAssets();
    }

    private async Task CreateMaterialLayer()
    {
      Debug.Log("开始创建材质层");
      var layer = mController.NewLayer("Blend");

      for (var i = 0; i < 64; i++)
      {
        layer.BoolParameter($"BitmapLed/Sync/Bit{i}");
      }

      await Task.Yield();
      
      for (var i = 0; i < mMemorySize; i++)
      {
        layer.FloatParameter($"BitmapLed/Memory/Data{i}");
      }

      await Task.Yield();
      var mark4 = layer.BoolParameter("BitmapLed/Sync/Bit63");
      var mark3 = layer.BoolParameter("BitmapLed/Sync/Bit62");
      var mark2 = layer.BoolParameter("BitmapLed/Sync/Bit61");
      var mark1 = layer.BoolParameter("BitmapLed/Sync/Bit60");
      var mark0 = layer.BoolParameter("BitmapLed/Sync/Bit59");

      var clip = mAac.NewClip().Animating(it =>
      {
        it.Animates(mToggleItem.transform, typeof(MeshRenderer), "m_Enabled")
          .WithUnit(AacFlUnit.Frames, f => f.Linear(0, 0));
      });

      await Task.Yield();
      var idle = layer.NewState("Idle")
        .WithAnimation(clip);

      var directBlendTree = mAac.NewBlendTree().Direct();
      var blendParam = layer.FloatParameter("Blend");
      for (var i = 0; i < mMemorySize; i++)
      {
        var d1 = mAac.NewBlendTree()
          .Simple1D(layer.FloatParameter($"BitmapLed/Memory/Data{i}"))
          .WithAnimation(CreateByteClip(i, 0), 0f)
          .WithAnimation(CreateByteClip(i, 255), 255f);
        directBlendTree.WithAnimation(d1, blendParam);
      }
      await Task.Yield();

      var bitmap = layer.NewState("BlendBitmapDisplay")
        .WithAnimation(directBlendTree);

      bitmap.TransitionsTo(idle).When(it =>
      {
        it.And(mark4.IsTrue());
        it.And(mark3.IsTrue());
        it.And(mark2.IsTrue());
        it.And(mark1.IsTrue());
        it.And(mark0.IsTrue());
      });

      await Task.Yield();
      idle.TransitionsTo(bitmap)
        .When(mark4.IsFalse()).Or()
        .When(mark3.IsFalse()).Or()
        .When(mark2.IsFalse()).Or()
        .When(mark1.IsFalse()).Or()
        .When(mark0.IsFalse());

      layer.WithDefaultState(bitmap);

      await Task.Yield();
      return;

      AacFlClip CreateByteClip(int index, float value)
      {
        return mAac.NewClip().Animating(it =>
        {
          it.Animates(mToggleItem.transform, typeof(MeshRenderer), $"material._Data{index}")
            .WithUnit(AacFlUnit.Frames, f => f.Linear(0.0f, value));
        });
      }
    }

    private void CreateUnicodeParamCopyLayer()
    {
      var layer = mController.NewLayer("Unicode");

      var mark3 = layer.BoolParameter("BitmapLed/Sync/Bit63");
      var mark2 = layer.BoolParameter("BitmapLed/Sync/Bit62");
      var mark1 = layer.BoolParameter("BitmapLed/Sync/Bit61");
      var mark0 = layer.BoolParameter("BitmapLed/Sync/Bit60");
      var @true = layer.BoolParameter("True").IsTrue();

      var pointer = layer.IntParameter("BitmapLed/Unicode/Pointer");
      var data = new AacFlIntParameter[6];
      for (var i = 0; i < data.Length; i++)
      {
        data[i] = layer.IntParameter($"BitmapLed/Unicode/Data{i}");
      }

      var @params = new AacFlFloatParameter[mMemorySize];
      for (var i = 0; i < @params.Length; i++)
      {
        @params[i] = layer.FloatParameter($"BitmapLed/Memory/Data{i}");
      }

      var endStates = new List<AacFlState>();

      var idle = layer.NewState("Idle");
      var beginState = layer.NewState("Begin");

      // 是Unicode
      idle.TransitionsTo(beginState).When(it =>
      {
        it.And(mark3.IsFalse());
        it.And(mark2.IsFalse());
        it.And(mark1.IsFalse());
        it.And(mark0.IsFalse());
      });

      for (var i = 0; i < mChars; i++)
      {
        var state = layer.NewState($"Accept{i}");
        state.DrivingCopies(data[0], @params[i * 5]);
        state.DrivingCopies(data[1], @params[i * 5 + 1]);
        state.DrivingCopies(data[2], @params[i * 5 + 2]);
        if ((i + 1) * 5 + 2 < @params.Length)
        {
          state.DrivingCopies(data[3], @params[(i + 1) * 5]);
          state.DrivingCopies(data[4], @params[(i + 1) * 5 + 1]);
          state.DrivingCopies(data[5], @params[(i + 1) * 5 + 2]);
        }

        endStates.Add(state);
        beginState.TransitionsTo(state).When(pointer.IsEqualTo(i));
      }

      foreach (var state in endStates)
      {
        state.TransitionsTo(idle).When(@true);
      }

      layer.WithDefaultState(idle);
    }

    private async Task CreateUnicodeLayer()
    {
      // CreateByteLayer(52, 8, "BitmapLed/Unicode/Pointer", "Unicode");
      // 
      // CreateByteLayer(0, 8, "BitmapLed/Unicode/Data5", "Unicode");
      // CreateByteLayer(8, 8, "BitmapLed/Unicode/Data4", "Unicode");
      // CreateByteLayer(16, 8, "BitmapLed/Unicode/Data3", "Unicode");
      // CreateByteLayer(24, 8, "BitmapLed/Unicode/Data2", "Unicode");
      // CreateByteLayer(32, 8, "BitmapLed/Unicode/Data1", "Unicode");
      // CreateByteLayer(40, 8, "BitmapLed/Unicode/Data0", "Unicode");
      for (var i = 0; i < 6; i++)
      {
        await CreateUnicodeByteLayer(i);
        await Task.Yield();
      }
    }

    private async Task CreateUnicodeByteLayer(int index)
    {
      Debug.Log($"正在创建Unicode第{index}个动画层");
      var layer = mController.NewLayer($"Unicode_Data_{index}");

      var memory = Enumerable.Range(0, mMemorySize)
        .Select(i => layer.IntParameter($"BitmapLed/Memory/Data{i}"))
        .ToArray();

      await Task.Yield();
      
      var mark3 = layer.BoolParameter("BitmapLed/Sync/Bit63");
      var mark2 = layer.BoolParameter("BitmapLed/Sync/Bit62");
      var mark1 = layer.BoolParameter("BitmapLed/Sync/Bit61");
      var mark0 = layer.BoolParameter("BitmapLed/Sync/Bit60");
      var @true = layer.BoolParameter("True").IsTrue();

      var pointerBits = Enumerable.Range(52, 8)
        .Select(i => layer.BoolParameter($"BitmapLed/Sync/Bit{i}"))
        .ToArray();
      
      var pointerCacheBits = Enumerable.Range(0, 8)
        .Select(i => layer.BoolParameter($"BitmapLed/Unicode_Cache/PointerBits_{index}_{i}"))
        .ToArray();
      
      var dataBits = Enumerable.Range(index * 8, 8)
        .Select(i => layer.BoolParameter($"BitmapLed/Sync/Bit{i}"))
        .ToArray();

      var dataCacheBits = Enumerable.Range(0, 8)
        .Select(i => layer.BoolParameter($"BitmapLed/Unicode_Cache/DataBits_{index}_{i}"))
        .ToArray();
      await Task.Yield();
      
      var idle = layer.NewState("Idle");
      var begin = layer.NewState("Begin");
      for (var i = 0; i < 8; i++)
      {
        begin.DrivingCopies(pointerBits[i], pointerCacheBits[i]);
        begin.DrivingCopies(dataBits[i], dataCacheBits[i]);
      }

      idle.TransitionsTo(begin).When(it =>
      {
        it.And(mark3.IsFalse());
        it.And(mark2.IsFalse());
        it.And(mark1.IsFalse());
        it.And(mark0.IsFalse());
      });

      AacFlState[][] pointerStates = {null};
      var pointer = layer.IntParameter($"BitmapLed/Unicode_Cache/Pointer_{index}");
      var data = layer.IntParameter($"BitmapLed/Unicode_Cache/Data_{index}");

      // 创建指针状态
      Debug.Log($"为Unicode第{index}个动画层中等待 -> 指针的建立连接");
      await CreateStates(layer, begin, pointerStates, "Pointer_", pointerBits, pointer);
      await Task.Yield();
      
      Debug.Log($"为Unicode第{index}个动画层中指针 -> 数据的建立连接");
      AacFlState[][] dataStates = {null};
      foreach (var it in pointerStates[0]!)
      {
        // 创建数据状态，只创建1次，第2次开始只是单纯的连接
        await CreateStates(layer, it, dataStates, "Data_", dataBits, data);
      }
      await Task.Yield();

      // 创建拷贝状态
      Debug.Log($"为Unicode第{index}个动画层中数据 -> 拷贝的建立连接");
      var copyStates = Enumerable.Range(0, mChars)
        .Select(i => layer.NewState($"Copy_{i}"))
        .ToArray();
      
      for (var i = 0; i < copyStates.Length; i++)
      {
        Debug.Log($"初始化Unicode第{index}个动画层中第{i}个状态的状态拷贝和过渡的连接");
        var it = copyStates[i];
        var memoryIndex = index < 3 ? i * 5 + index : (i + 1) * 5 + (index - 3);
        if (memoryIndex >= mMemorySize) continue;
        
        it.DrivingCopies(data, memory[memoryIndex]);
        foreach (var dataState in dataStates[0]!)
        {
          dataState.TransitionsTo(it).When(pointer.IsEqualTo(i));
          await Task.Yield();
        }

        it.TransitionsTo(idle).When(@true);
        await Task.Yield();
      }
      
      layer.WithDefaultState(idle);
      Debug.Log($"正在Unicode第{index}个动画层创建完成");
    }

    private void CreateGb2312ParamCopyLayer()
    {
      var layer = mController.NewLayer("GB2312");

      var mark3 = layer.BoolParameter("BitmapLed/Sync/Bit63");
      var mark2 = layer.BoolParameter("BitmapLed/Sync/Bit62");
      var mark1 = layer.BoolParameter("BitmapLed/Sync/Bit61");
      var mark0 = layer.BoolParameter("BitmapLed/Sync/Bit60");
      var @true = layer.BoolParameter("True").IsTrue();

      var pointer = layer.IntParameter("BitmapLed/GB2312/Pointer");
      var data = new AacFlIntParameter[8];
      for (var i = 0; i < data.Length; i++)
      {
        data[i] = layer.IntParameter($"BitmapLed/GB2312/Data{i}");
      }

      var @params = new AacFlFloatParameter[mMemorySize];
      for (var i = 0; i < @params.Length; i++)
      {
        @params[i] = layer.FloatParameter($"BitmapLed/Memory/Data{i}");
      }

      var endStates = new List<AacFlState>();

      var idle = layer.NewState("Idle");
      var beginState = layer.NewState("Begin");

      // 是GB2312
      idle.TransitionsTo(beginState).When(it =>
      {
        it.And(mark3.IsFalse());
        it.And(mark2.IsFalse());
        it.And(mark1.IsFalse());
        it.And(mark0.IsTrue());
      });

      for (var i = 0; i < mChars; i++)
      {
        var state = layer.NewState($"Accept{i}");
        state.DrivingCopies(data[0], @params[i * 5 + 1]);
        state.DrivingCopies(data[1], @params[i * 5 + 2]);
        state.Drives(@params[i * 5], 254);
        if ((i + 1) * 5 + 2 < @params.Length)
        {
          state.Drives(@params[(i + 1) * 5], 254);
          state.DrivingCopies(data[2], @params[(i + 1) * 5 + 1]);
          state.DrivingCopies(data[3], @params[(i + 1) * 5 + 2]);
        }

        if ((i + 2) * 5 + 2 < @params.Length)
        {
          state.Drives(@params[(i + 2) * 5], 254);
          state.DrivingCopies(data[4], @params[(i + 2) * 5 + 1]);
          state.DrivingCopies(data[5], @params[(i + 2) * 5 + 2]);
        }

        if ((i + 3) * 5 + 2 < @params.Length)
        {
          state.Drives(@params[(i + 3) * 5], 254);
          state.DrivingCopies(data[6], @params[(i + 3) * 5 + 1]);
          state.DrivingCopies(data[7], @params[(i + 3) * 5 + 2]);
        }

        endStates.Add(state);
        beginState.TransitionsTo(state).When(pointer.IsEqualTo(i));
      }

      foreach (var state in endStates)
      {
        state.TransitionsTo(idle).When(@true);
      }

      layer.WithDefaultState(idle);
    }

    private void CreateGb2312Layer()
    {
      CreateByteLayer(52, 8, "BitmapLed/GB2312/Pointer", "GB2312");

      CreateByteLayer(0, 8, "BitmapLed/GB2312/Data7", "GB2312");
      CreateByteLayer(8, 5, "BitmapLed/GB2312/Data6", "GB2312");
      CreateByteLayer(13, 8, "BitmapLed/GB2312/Data5", "GB2312");
      CreateByteLayer(21, 5, "BitmapLed/GB2312/Data4", "GB2312");
      CreateByteLayer(26, 8, "BitmapLed/GB2312/Data3", "GB2312");
      CreateByteLayer(34, 5, "BitmapLed/GB2312/Data2", "GB2312");
      CreateByteLayer(39, 8, "BitmapLed/GB2312/Data1", "GB2312");
      CreateByteLayer(47, 5, "BitmapLed/GB2312/Data0", "GB2312");
    }

    private void CreateColorLayer()
    {
      Debug.Log("正在创建颜色层");
      CreateByteLayer(52, 8, "BitmapLed/Color/Pointer", "Color");

      CreateByteLayer(0, 8, "BitmapLed/Color/Low", "Color");
      CreateByteLayer(8, 8, "BitmapLed/Color/High", "Color");
    }

    private void CreateByteLayer(int index, int length, string paramName, string layoutMark)
    {
      var layer = mController.NewLayer($"Byte_{layoutMark}_{index}_{length}");
      var @true = layer.BoolParameter("True").IsTrue();


      var mark4 = layer.BoolParameter("BitmapLed/Sync/Bit63");
      var mark3 = layer.BoolParameter("BitmapLed/Sync/Bit62");
      var mark2 = layer.BoolParameter("BitmapLed/Sync/Bit61");
      var mark1 = layer.BoolParameter("BitmapLed/Sync/Bit60");
      var mark0 = layer.BoolParameter("BitmapLed/Sync/Bit59");

      var data = layer.IntParameter(paramName);
      var idle = layer.NewState("Idle");
      var begin = layer.NewState("Begin");

      begin.TransitionsTo(idle).When(it =>
      {
        it.And(mark4.IsTrue());
        it.And(mark3.IsTrue());
        it.And(mark2.IsTrue());
        it.And(mark1.IsTrue());
        it.And(mark0.IsTrue());
      });

      idle.TransitionsTo(begin)
        .When(mark4.IsFalse()).Or()
        .When(mark3.IsFalse()).Or()
        .When(mark2.IsFalse()).Or()
        .When(mark1.IsFalse()).Or()
        .When(mark0.IsFalse());

      var paramBits = new AacFlBoolParameter[length];
      for (var i = 0; i < length; i++)
      {
        paramBits[i] = layer.BoolParameter($"BitmapLed/Sync/Bit{index + i}");
      }

      for (var i = 0; i < Math.Pow(2, length); i++)
      {
        var bits = new bool[length];
        for (var j = 0; j < length; j++)
        {
          bits[j] = ((i >> j) & 1) == 1;
        }

        var state = layer.NewState($"Accept{i}");
        state.Drives(data, i);
        begin.TransitionsTo(state).When(it =>
        {
          for (var j = 0; j < length; j++)
          {
            it.And(paramBits[j].IsEqualTo(bits[j]));
          }
        });
        state.TransitionsTo(begin).When(@true);
      }

      layer.WithDefaultState(idle);
    }

    /// <summary>
    /// 创建 255 个状态，具体多少个取决于二进制序列的长度。
    /// <br />
    /// 若 states 传入了非null的值，将不会重复创建
    /// </summary>
    /// <param name="layer">动画层</param>
    /// <param name="begin">起始状态</param>
    /// <param name="states">全部 255 个状态</param>
    /// <param name="prefix">状态前缀</param>
    /// <param name="bitParams">二进制序列</param>
    /// <param name="outParam">输出到的int参数</param>
    private async Task CreateStates(
      AacFlLayer layer,
      AacFlState begin,
      AacFlState[][] states,
      string prefix,
      AacFlBoolParameter[] bitParams,
      AacFlIntParameter outParam
    )
    {
      var length = bitParams.Length;
      // states为空时创建
      if (states[0] == null)
      {
        Debug.Log($"正在为{prefix}创建{1 << bitParams.Length}个动画状态");
        states[0] = Enumerable.Range(0, 1 << length)
          .Select(i => layer.NewState($"{prefix}{i}"))
          .ToArray();
        
        await Task.Yield();
        // 需要拷贝参数
        for (var i = 0; i < Math.Pow(2, length); i++)
        {
          states[0][i].Drives(outParam, i);
        }
        Debug.Log($"{prefix}的动画状态创建完成");
      }

      await Task.Yield();
      
      for (var i = 0; i < states.Length; i++)
      {
        var bits = new bool[length];
        for (var j = 0; j < length; j++)
        {
          bits[j] = ((i >> j) & 1) == 1;
        }

        begin.TransitionsTo(states[0][i]).When(it =>
        {
          for (var j = 0; j < length; j++)
          {
            it.And(bitParams[j].IsEqualTo(bits[j]));
          }
        });
        
        await Task.Yield();
      }
    }
  }
}