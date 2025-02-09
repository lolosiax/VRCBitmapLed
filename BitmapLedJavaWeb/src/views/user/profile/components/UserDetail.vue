<template>
  <el-card class="user-detail el-descriptions">
    <template #header>
      <div class="user-detail-header">
        <span class="user-detail-title">个人信息</span>
        <el-button v-if="!editing" type="primary" @click="editing=true">修改</el-button>
        <template v-else>
          <el-button type="warning" @click="cancel">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </template>
      </div>
    </template>
    <table class="user-table" :class="{'edit-mode': editing}">
      <user-detail-body ref="body">
        <user-detail-avatar-item />
        <user-detail-item label="用户名" prop="realName" />
        <user-detail-item label="账号" prop="userName" :editable="false" />
        <user-detail-item label="手机号" prop="phone" />
        <user-detail-password-item />
        <user-detail-item label="UID" prop="id" :editable="false" />
        <user-detail-item label="注册日期" prop="createdAt" :editable="false" />
        <user-detail-item label="上次更新" prop="updatedAt" :editable="false" />
      </user-detail-body>
    </table>
  </el-card>
</template>

<script lang="ts">
import UserDetailItem from "@/views/user/profile/components/UserDetailItem.vue";
import UserDetailAvatarItem from "@/views/user/profile/components/UserDetailAvatarItem.vue";
import UserDetailPasswordItem from "@/views/user/profile/components/UserDetailPasswordItem.vue";
import { editUser } from "@/api/user";
import { ElMessage } from "element-plus";
import type { VNode } from "vue";

export default defineComponent({
  components: {
    UserDetailBody: {
      render() {
        return h("tbody", this.$slots.default());
      }
    },
    UserDetailItem,
    UserDetailAvatarItem,
    UserDetailPasswordItem
  },
  setup() {
    const userInfo = toRef(useBasicStore(), "userInfo");
    const editing = ref(false);
    const copy = ref(JSON.parse(JSON.stringify(userInfo.value)));

    provide("userDetail:editing", editing);
    provide("userDetail:user", copy);

    const body = ref();

    async function save() {
      try {
        const vNodes = body.value.$.subTree.children as VNode[];
        const component = vNodes.map(it => it.component!);
        for (const c of component) {
          const rs = c.exposed?.ok?.();
          if (rs === false) {
            ElMessage.error("请检查输入是否正确");
            return;
          }
        }
      } catch (e: Error | any) {
        ElMessage.error(e.message);
      }

      await editUser({
        ...copy.value,
        avatar: undefined
      });
      ElMessage.success("操作成功");
      userInfo.value = copy.value;
      delete copy.password;
      cancel();
    }

    function cancel() {
      copy.value = JSON.parse(JSON.stringify(userInfo.value));
      editing.value = false;
    }

    return {
      cancel, save, editing, body
    };
  }
});

</script>


<style scoped lang="scss">
.user-detail {
  .user-detail-header {
    display: flex;
    align-items: center;
  }

  .user-detail-title {
    flex-grow: 1;
  }

  .user-table {
    border-collapse: collapse;
    width: 100%;

    &.edit-mode :deep(.item-content) {
      color: lightgray;
    }

    &.edit-mode :deep(tr:not(.editing) .item-title) {
      color: lightgray;
    }

    &.edit-mode :deep(tr.editing .item-content) {
      box-shadow: none;
      transition-property: box-shadow;
      transition-duration: 0.5s;
      border-radius: 7px;

      &:has(input) {
        cursor: text;
      }

      &:not(:focus-within):hover {
        box-shadow: 0 0 3px var(--el-color-primary) inset;
      }
    }

    :deep(.item-title) {
      width: 128px;
      background: var(--el-descriptions-item-bordered-label-background);
      font-weight: 700;
      color: var(--el-text-color-regular);
    }

    :deep(td) {
      padding: 0.8em;
      border: var(--el-descriptions-table-border);
    }

    :deep(.item-input) {
      width: 100%;
      position: relative;

      &::after {
        content: '';
        display: block;
        /*开始时候下划线的宽度为100%*/
        width: 100%;
        height: 2px;
        position: absolute;
        bottom: -3px;
        background-color: var(--el-color-primary);
        transition: all 0.3s ease-in-out;
        /*通过transform的缩放scale来让初始时x轴为0*/
        transform: scale3d(0, 1, 1);
        /*将坐标原点移到元素的中间，以原点为中心进行缩放*/
        transform-origin: 50% 0;
      }

      &:focus-within::after {
        /*内部取得焦点时显示下划线*/
        transform: scale3d(1, 1, 1);
      }

      input {
        border: none;
        width: 100%;
        outline: none;
      }
    }
  }

}
</style>
