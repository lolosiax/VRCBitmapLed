<template>
  <app-page class="userManagement-container">
    <template #right>
      <right-nav-search-input
        style="margin-right: 1em" @keyup.enter="queryData"
      />
      <el-button type="primary" @click="handleEdit">新增</el-button>
    </template>

    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="elementLoadingText"
      :header-cell-style="{ background: '#F7F9FB', textAlign: 'center' }"
      :cell-style="{ textAlign: 'center' }"
      @selection-change="setSelectRows"
    >
      <!-- <el-table-column show-overflow-tooltip type="selection"></el-table-column> -->
      <el-table-column label="序号" type="index" />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="userName"
        label="账号"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="realName"
        label="姓名"
      />
      <el-table-column :show-overflow-tooltip="true" label="账号状态">
        <template #default="{ row }">
          <el-tag v-if="row.isUse === 1" effect="dark">正常</el-tag>
          <el-tag v-else type="danger" effect="dark">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="所属角色">
        <template #default="{ row }">
          <el-tag effect="dark" :style="getRoleTagStyle(row)">
            {{ row.roleName || "未知" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="updatedAt"
        label="修改时间"
      />

      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <div class="table-btn-list">
            <el-button class="detail" link type="primary" @click="handleEdit(row, true)">
              <menu-icon icon="file-earmark-richtext-fill" />
            </el-button>
            <el-button class="edit" link type="primary" @click="handleEdit(row, false)">
              <menu-icon icon="pencil-square" />
            </el-button>
            <el-button class="del" link type="primary" @click="handleDelete(row)">
              <menu-icon icon="trash-fill" />
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :page-size="queryForm.pageSize"
      :layout="layout"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <edit ref="edit" @fetch-data="fetchData" />
  </app-page>
</template>

<script>
import { doDelete, getList } from "@/api/userManagement";
import Edit from "./components/UserManagementEdit.vue";
import { sha1 } from "hash.js";
import MenuIcon from "@/components/MenuIcon.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import RightNavSearchInput from "@/views/system/userManagement/components/RightNavSearchInput.vue";
import { useSystemStore } from "@/store/system";

// 注释
export default {
  name: "UserManagement",
  components: { RightNavSearchInput, MenuIcon, Edit },
  data() {
    return {
      list: null,
      listLoading: true,
      layout: "total, sizes, prev, pager, next, jumper",
      total: 0,
      selectRows: "",
      elementLoadingText: "正在加载...",
      queryForm: {
        pageNo: 1,
        pageSize: 10,
        keys: ""
      }
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    setSelectRows(val) {
      this.selectRows = val;
    },
    handleEdit(row, detail) {
      if (row.id) {
        this.$refs["edit"].showEdit(row, detail);
      } else {
        this.$refs["edit"].showEdit();
      }
    },
    async handleDelete(row) {
      if (row.id) {
        const rs = await ElMessageBox.confirm("你确定要删除当前项吗?");
        if (rs !== "confirm") return;
        const { msg } = await doDelete({ ids: row.id });
        ElMessage.success(msg);
        await this.fetchData();
      } else {
        if (this.selectRows.length > 0) {
          const ids = this.selectRows.map((item) => item.id).join();
          const rs = await ElMessageBox.confirm("你确定要删除当前项吗?");
          if (rs !== "confirm") return;
          const { msg } = await doDelete({ ids });
          ElMessage.success(msg);
          await this.fetchData();
        } else {
          ElMessage.error("未选中任何行");
          return false;
        }
      }
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val;
      this.fetchData();
    },
    queryData() {
      this.queryForm.pageNo = 1;
      this.fetchData();
    },
    switchChange(row) {
    },
    async fetchData() {
      this.listLoading = true;
      this.queryForm.keys = useSystemStore().userMng.searchKey;
      const { data } = await getList(this.queryForm);
      this.list = data.data;
      this.total = data.total;
      setTimeout(() => {
        this.listLoading = false;
      }, 300);
    },
    /**
     * 获取标签的颜色
     * @param row 角色对象
     * @return {{backgroundColor: string, borderColor: string, color?: string}}
     */
    getRoleTagStyle(row) {
      const style = {
        backgroundColor: undefined,
        borderColor: undefined,
        color: undefined
      };
      const type = row.roleType || "unknown";
      if (type.includes("teacher")) style.backgroundColor = "red";
      else if (type.includes("admin")) style.backgroundColor = "darkorange";
      else if (type.includes("student")) style.backgroundColor = "#62CAD7";
      else if (!row.roleName) style.backgroundColor = "darkgray";
      else {
        // 根据哈希码计算一个颜色
        const [r, g, b] = sha1().update(type).digest();
        const gray = r * 0.299 + g * 0.587 + b * 0.114;
        style.backgroundColor = `rgb(${r}, ${g}, ${b})`;
        if (gray < 128) {
          style.color = "black";
          style.borderColor = "gray";
        }
      }
      if (!style.borderColor) style.borderColor = style.backgroundColor;
      return style;
    }
  }
};
</script>
<style lang="scss" scoped>
.table-btn-list {
  display: flex;

  :deep(.el-button) {
    padding: 0;
  }
}
</style>
