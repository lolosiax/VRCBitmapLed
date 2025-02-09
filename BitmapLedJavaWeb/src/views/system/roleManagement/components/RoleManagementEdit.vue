<template>
  <el-drawer v-model="drawer" :title="title" :before-close="close">
    <div v-loading="loading" class="drawer__content">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称：" prop="roleName">
          <el-input
            v-model.trim="form.roleName"
            autocomplete="off"
            placeholder="请输入角色名称"
          />
        </el-form-item>
        <el-form-item label="角色key：" prop="type">
          <el-input
            v-model.trim="form.type"
            autocomplete="off"
            placeholder="请输入角色key"
          />
        </el-form-item>
        <el-form-item label="菜单列表：" prop="menuId">
          <el-select
            v-model="form.menuTitle"
            class="vab-tree-select"
            clearable
            collapse-tags
            multiple
            popper-class="select-tree-popper"
            @clear="selectTreeClearHandle()"
            @remove-tag="removeSelectTreeTag"
          >
            <!--          <el-select-->
            <!--            v-model="form.menuTitle"-->
            <!--            class="vab-tree-select"-->
            <!--            clearable-->
            <!--            multiple-->
            <!--            popper-class="select-tree-popper"-->
            <!--            @clear="selectTreeClearHandle()"-->
            <!--            @remove-tag="removeSelectTreeTag"-->
            <!--          >-->
            <el-option :value="form.menuId" style="height: 250px; overflow: auto; background-color: white">
              <el-tree
                id="multipleSelectTree"
                ref="multipleSelectTree"
                :data="menu"
                :default-expanded-keys="form.menuId"
                :highlight-current="true"
                :props="selectTreeDefaultProps"
                node-key="id"
                show-checkbox
                @check="multipleSelectTreeCheckNode"
              />
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="drawer__footer">
        <el-button size="default" @click="close(true)">取 消</el-button>
        <el-button size="default" type="primary" @click="save">确 定</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script>
import { doAdd, doUpdate } from "@/api/roleManagement";
import { getTree } from "@/api/menuManagement";
import { delay } from "@/utils/common-util";

export default {
  name: "RoleManagementEdit",
  data() {
    return {
      form: {
        roleName: "",
        type: "",
        menuId: [],
        menuTitle: []
      },
      rules: {
        roleName: [
          { required: true, trigger: "blur", message: "请输入角色名称" }
        ],
        menuId: [{ required: true, trigger: "blur", message: "请选择菜单" }]
      },
      formBackup: {
        roleName: "",
        type: "",
        menuId: [],
        menuTitle: []
      },
      title: "",
      loading: false,
      drawer: false,

      /* 单选树-多选树---------开始 */
      menu: [],
      selectTreeDefaultProps: {
        children: "children",
        label: "title"
      }
      /* 单选树-多选树---------结束 */
    };
  },
  created() {
  },
  methods: {
    /* 单选/多选树方法-------------------开始 */
    // 初始化单选树的值
    async initSingleTree() {
      this.menu = await getTree();
      this.$nextTick(() => {
        // 多选树
        this.form.menuId.forEach((item) => {
          const node = this.$refs.multipleSelectTree.getNode(item);
          if (node && node.isLeaf) {
            this.$refs.multipleSelectTree.setChecked(node, true);
          }
        });
      });
    },
    // 清除单选树选中
    selectTreeClearHandle() {
      this.form.menuTitle = [];
      this.form.menuId = [];
      this.$refs.multipleSelectTree.setCheckedKeys([]);
    },
    // select多选时移除某项操作
    removeSelectTreeTag(val) {
      const stack = JSON.parse(JSON.stringify(this.menu));
      while (stack.length) {
        const curr = stack.shift();
        if (curr.title == val) {
          this.form.menuId.splice(this.form.menuId.indexOf(curr.id), 1);
          return this.$refs.multipleSelectTree.setChecked(curr.id, false);
        }
        if (curr.children && curr.children.length) {
          stack.unshift(...curr.children);
        }
      }
    },
    // 节点选中操作
    multipleSelectTreeCheckNode(data, node, el) {
      const checkedNodes = this.$refs.multipleSelectTree.getCheckedNodes();
      const halfCheckedNodes =
        this.$refs.multipleSelectTree.getHalfCheckedNodes();
      const allNodes = checkedNodes.concat(halfCheckedNodes);
      const keyArr = [];
      const valueArr = [];
      allNodes.forEach((item) => {
        keyArr.push(item.id);
        valueArr.push(item.title);
      });
      this.form.menuTitle = valueArr;
      this.form.menuId = keyArr;
    },
    /* 单选/多选树方法-------------------结束 */

    showEdit(row) {
      if (!row) {
        this.title = "添加";
      } else {
        this.title = "编辑";
        this.form = Object.assign({}, row);
      }
      this.loading = true;
      this.drawer = true;
      this.$nextTick(() => {
        this.formBackup = Object.assign({}, this.form);
        this.initSingleTree();
      });
      setTimeout(() => {
        this.loading = false;
      }, 1500);
    },
    async close(confirm) {
      if (this.loading) return;
      const compare =
        JSON.stringify(this.form) !== JSON.stringify(this.formBackup);
      if (confirm && compare) {
        const rs = await elConfirmNoCancelBtn(null,
          "编辑数据将会清空，确认退出？"
        );
        if (rs !== "confirm") return;
        this.resetDrawer();
      } else {
        this.resetDrawer();
      }
    },
    resetDrawer() {
      this.$refs["form"].resetFields();
      this.form = this.$options.data().form;
      this.formBackup = this.$options.data().formBackup;
      this.drawer = false;
    },
    save() {
      this.$refs["form"].validate(async (valid) => {
        if (valid) {
          let msg;
          if (this.title === "添加") {
            const resp = await doAdd(this.form);
            msg = resp.msg;
          } else {
            const resp = await doUpdate(this.form);
            msg = resp.msg;
          }
          elMessage(msg, "success");
          this.$emit("fetch-data");
          this.close(false);
        } else {
          return false;
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;

  form {
    flex: 1;
  }

  .drawer__footer {
    display: flex;

    button {
      flex: 1;
    }
  }
}
</style>
