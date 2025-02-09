<template>
  <el-drawer v-model="drawer" :title="title" :before-close="close">
    <div v-loading="loading" class="drawer__content">
      <el-tabs
        v-model="activeName"
        style="flex: 1; overflow: scroll"
        :stretch="true"
      >
        <el-tab-pane label="组" :name="0">
          <el-form
            ref="menu"
            :model="form"
            :rules="menuRules"
            label-width="80px"
          >
            <el-form-item label="组名称" prop="title">
              <el-input
                v-model.trim="form.title"
                autocomplete="off"
                placeholder="请输入组名称"
              />
            </el-form-item>
            <el-form-item label="组路由" prop="path">
              <el-input
                v-model.trim="form.path"
                autocomplete="off"
                placeholder="请输入组路由"
              />
            </el-form-item>
            <el-form-item label="组图标" prop="icon">
              <icon-selector v-model="form.icon"/>
            </el-form-item>
            <el-form-item label="组排序" prop="order">
              <el-input-number
                v-model="form.order"
                :min="0"
                label="序号"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="菜单" :name="1">
          <el-form
            ref="submenu"
            :model="form"
            :rules="submenuRules"
            label-width="80px"
          >
            <el-form-item label="菜单名称" prop="title">
              <el-input
                v-model.trim="form.title"
                autocomplete="off"
                placeholder="请输入菜单名称"
              />
            </el-form-item>
            <el-form-item label="菜单路由" prop="path">
              <el-input
                v-model.trim="form.path"
                autocomplete="off"
                placeholder="请输入菜单路由"
              />
            </el-form-item>
            <el-form-item label="上级菜单" prop="parentId">
              <el-select
                ref="singleTree"
                v-model="form.parentTitle"
                placeholder="请选择上级菜单"
                class="vab-tree-select"
                clearable
                popper-class="select-tree-popper"
                value-key="id"
                @clear="selectTreeClearHandle()"
              >
                <el-option :value="form.parentId">
                  <el-tree
                    id="singleSelectTree"
                    ref="singleSelectTree"
                    :current-node-key="form.parentId"
                    :data="menuData"
                    :default-expanded-keys="selectTreeDefaultSelectedKeys"
                    :highlight-current="true"
                    :props="selectTreeDefaultProps"
                    node-key="id"
                    @node-click="selectTreeNodeClick"
                  >
                    <template #defalut="{ node }">
                      <span class="vab-tree-item">
                        <i :class="'bi bi-' + node.icon" />
                        {{ node.label }}</span>
                    </template>
                  </el-tree>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="组件路径" prop="component">
              <el-input
                v-model.trim="form.component"
                autocomplete="off"
                placeholder="请输入组件路径"
              />
            </el-form-item>
            <el-form-item label="菜单图标" prop="icon">
              <icon-selector v-model="form.icon"/>
            </el-form-item>
            <el-form-item label="菜单排序" prop="order">
              <el-input-number
                v-model="form.order"
                :min="0"
                label="序号"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <div class="drawer__footer">
        <el-button size="default" @click="close(true)">取 消</el-button>
        <el-button size="default" type="primary" @click="save">确 定</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script>
import { doAdd, doUpdate } from "@/api/menuManagement";
import { allIcons } from "@/utils/common-util";

export default {
  name: "MenuManagementEdit",
  components: {},
  props: {
    // 单选树的值
    menuData: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  data() {
    return {
      activeName: 0,
      iconList: [],
      /* 单选树---------开始 */
      selectTreeDefaultSelectedKeys: [], //单选树默认展开的key值数组
      selectTreeDefaultProps: {
        children: "children",
        label: "title"
      },
      /* 单选树---------结束 */

      form: {
        title: "",
        icon: "",
        path: "",
        parentId: "",
        component: "",

        parentTitle: "",
        // 排序
        order: 0,
        type: ""
      },
      menuRules: {
        path: [{ required: true, trigger: "blur", message: "请输入路由" }],
        title: [{ required: true, trigger: "blur", message: "请输入标题" }],
        icon: [{ required: true, trigger: "blur", message: "请选择图标" }]
      },
      submenuRules: {
        path: [{ required: true, trigger: "blur", message: "请输入路由" }],
        title: [{ required: true, trigger: "blur", message: "请输入标题" }],
        component: [
          { required: true, trigger: "blur", message: "请输入组件路径" }
        ],
        parentId: [
          { required: true, trigger: "blur", message: "请选择上级菜单" }
        ]
      },
      formBackup: {
        title: "",
        icon: "",
        path: "",
        parentId: "",
        component: "",

        parentTitle: "",
        // 排序
        order: 0,
        type: ""
      },
      title: "",
      loading: false,
      drawer: false
    };
  },
  created() {
    this.getIconList();
  },
  methods: {
    getIconList() {
      this.iconList = allIcons().map(it => ({ value: it, label: it }));
    },
    showEdit(row) {
      if (!row) {
        this.title = "添加";
      } else {
        this.title = "编辑";
        this.activeName = row.type;
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
    // 初始化单选树的值
    async initSingleTree() {
      if (this.form.parentId) {
        this.$nextTick(() => {
          this.selectTreeDefaultSelectedKeys = [this.form.parentId]; // 设置默认展开
          //单选树
          this.$refs.singleSelectTree.setCurrentKey(this.form.parentId); // 设置默认选中
        });
      }
    },
    // 清除单选树选中
    selectTreeClearHandle() {
      this.selectTreeDefaultSelectedKeys = [];
      this.clearSelected();
      this.form.parentTitle = "";
      this.form.parentId = "";
      this.$refs.singleSelectTree.setCurrentKey(""); // 设置默认选中
    },
    // 清空选中样式
    clearSelected() {
      const allNode = document.querySelectorAll(
        "#singleSelectTree .el-tree-node"
      );
      allNode.forEach((element) => element.classList.remove("is-current"));
    },
    // 点击叶子节点
    selectTreeNodeClick(data, node, el) {
      this.form.parentTitle = data.title;
      this.form.parentId = data.id;
      this.$refs.singleTree.blur();
    },
    async close(confirm) {
      if (this.loading) return;
      const compare =
        JSON.stringify(this.form) !== JSON.stringify(this.formBackup);
      if (confirm && compare) {
        const rs = await elConfirmNoCancelBtn(null, "编辑数据将会清空，确认退出？");
        if (rs !== "confirm") return;
        this.resetDrawer();
      } else {
        this.resetDrawer();
      }
    },
    resetDrawer() {
      this.$refs["menu"].resetFields();
      this.$refs["submenu"].resetFields();
      this.form = this.$options.data().form;
      this.formBackup = this.$options.data().formBackup;
      this.drawer = false;
    },
    save() {
      const whichForm = this.activeName === "0" ? "menu" : "submenu";
      this.$refs[whichForm].validate(async (valid) => {
        if (valid) {
          let msg;
          this.form.type = Number(this.activeName);
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
