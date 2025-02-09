<template>
  <el-dialog
    :model-value="modelValue"
    title="编辑头像"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    width="600px"
    @update:model-value="v => $emit('update:modelValue', v)"
  >
    <div style="display: flex" class="avatar">
      <div class="avatar-left">
        <div v-show="!options.img">
          <el-upload
            ref="upload"
            action=""
            style="text-align: center;margin-bottom: 24px"
            :on-change="uploads"
            accept="image/png, image/jpeg, image/jpg"
            :show-file-list="false"
            :auto-upload="false">
            <template #trigger>
              <el-button ref="uploadBtn" size="small" type="primary">选择图片</el-button>
            </template>
          </el-upload>
          <div>支持jpg、png格式的图片，大小不超过3M</div>
        </div>
        <div v-show="options.img" class="avatar-left-crop">
          <VueCropper
            ref="cropper"
            class="crop-box"
            :img="options.img"
            :auto-crop="options.autoCrop"
            :fixed-box="options.fixedBox"
            :can-move-box="options.canMoveBox"
            :auto-crop-width="options.autoCropWidth"
            :auto-crop-height="options.autoCropHeight"
            :center-box="options.centerBox"
            mode="contain"
            @real-time="realTime" />
          <p class="avatar-left-p">鼠标滚轮缩放控制图片显示大小，鼠标拖拽调整显示位置</p>
        </div>
      </div>
      <div class="avatar-right">
        <div v-for="(item, i) in previewsDiv" :key="i" class="avatar-right-div" :style="item.style">
          <div v-show="options.img" :class="previews.div" class="avatar-right-previews" :style="item.zoomStyle">
            <img :src="previews.url" :style="previews.img" alt="img" />
          </div>
        </div>
        <div class="avatar-right-text">
          <el-button v-if="options.img" link type="primary" @click="uploadPreviews">重新上传</el-button>
          <span v-else>预览</span>
        </div>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="getCrop">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { VueCropper } from "vue-cropper";
import { getAvatar, setAvatar } from "@/api/user";
import { ElMessage } from "element-plus";

export default {
  name: "AvatarEdit",
  components: {
    VueCropper
  },
  props: {
    modelValue: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      //vueCropper组件 裁剪配置信息
      options: {
        img: "",  //原图文件
        autoCrop: true,  //默认生成截图框
        fixedBox: true,  //固定截图框大小
        canMoveBox: false,    //截图框不能拖动
        autoCropWidth: 200,  //截图框宽度
        autoCropHeight: 200, //截图框高度
        centerBox: false    //截图框被限制在图片里面
      },
      //实时预览图数据
      previews: {},
      //实时预览图样式
      previewsDiv: [
        //128px 预览样式
        {
          style: {
            width: "108px",
            height: "108px",
            margin: "0 auto"
          },
          zoomStyle: {
            zoom: 0.54
          }
        },
        //68px 预览样式
        {
          style: {
            width: "68px",
            height: "68px",
            margin: "27px auto"
          },
          zoomStyle: {
            zoom: 0.34
          }
        },
        //48px 预览样式
        {
          style: {
            width: "48px",
            height: "48px",
            margin: "0 auto"
          },
          zoomStyle: {
            zoom: 0.24
          }
        }
      ]
    };
  },

  methods: {
    //读取原图
    uploads(file) {
      const isIMAGE = file.raw.type === "image/jpeg" || file.raw.type === "image/png";
      const isLt3M = file.raw.size / 1024 / 1024 < 3;
      if (!isIMAGE) {
        this.$message({
          showClose: true,
          message: "请选择 jpg、png 格式的图片！",
          type: "error"  //提示类型
        });
        return false;
      }
      if (!isLt3M) {
        this.$message({
          showClose: true,
          message: "上传图片大小不能超过 3MB",
          type: "error"  //提示类型
        });
        return false;
      }
      const reader = new FileReader();
      reader.readAsDataURL(file.raw);
      reader.onload = e => {
        this.options.img = e.target.result; //base64
      };
    },
    //实时预览数据
    realTime(data) {
      this.previews = data;
    },
    //重新上传
    uploadPreviews() {
      this.$refs.uploadBtn.$el.click();
    },
    //获取截图信息
    async getCrop() {
      /** @type {Blob} */
      const blob = await new Promise(r => {
        this.$refs.cropper.getCropBlob((data) => {
          r(data);
        });
      });
      const userInfo = useBasicStore().userInfo;
      await setAvatar(userInfo.id, blob);
      // 获取截图的 base64 数据
      const data = await getAvatar(userInfo.id);
      const fr = new FileReader();
      fr.onload = () => userInfo.avatar = fr.result;
      fr.readAsDataURL(data);
      ElMessage.success("操作成功！");
      this.closeDialog();
    },
    //关闭弹框
    closeDialog() {
      //调用父组件关闭弹框方法 closeAvatarEdits()
      this.$emit("update:modelValue", false);
      //重置 data 数据。(Object.assign是对象深复制  this.$data是组件内的数据对象 this.$options.data()是原始的数据)
      Object.assign(this.$data, this.$options.data());
    }
  }
};
</script>

<style lang="scss" scoped>

:deep(.el-dialog__header) {
  padding: 24px 0 11px 28px;
}

:deep(.el-dialog__title) {
  color: #333333;
}

:deep(.el-dialog__body) {
  padding: 0 28px;
}

:deep(.el-dialog__footer) {
  padding: 20px 28px;

  .el-button {
    width: 145px;
  }
}

.avatar {
  display: flex;

  .avatar-left {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 400px;
    height: 400px;
    background-color: #F0F2F5;
    margin-right: 10px;
    border-radius: 4px;

    .avatar-left-crop {
      width: 400px;
      height: 400px;
      position: relative;

      .crop-box {
        width: 100%;
        height: 100%;
        border-radius: 4px;
        overflow: hidden
      }

    }

    .avatar-left-p {
      text-align: center;
      width: 100%;
      position: absolute;
      bottom: 20px;
      color: #ffffff;
      font-size: 14px;
      pointer-events: none;
    }
  }

  .avatar-right {
    width: 150px;
    height: 400px;
    background-color: #F0F2F5;
    border-radius: 4px;
    padding: 16px 0;
    box-sizing: border-box;

    .avatar-right-div {
      border: 3px solid #ffffff;
      border-radius: 24%;
    }

    .avatar-right-previews {
      width: 200px;
      height: 200px;
      overflow: hidden;
      border-radius: 24%;
    }

    .avatar-right-text {
      text-align: center;
      margin-top: 50px;
      font-size: 14px;

      :deep(.el-button) {
        padding: 0;
      }

      span {
        color: #666666;
      }
    }
  }
}

</style>

