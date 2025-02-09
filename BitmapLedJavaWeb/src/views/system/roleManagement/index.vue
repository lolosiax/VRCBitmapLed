<template>
  <app-page class="roleManagement-container">
    <template #right>
      <el-form :inline="true" :model="queryForm" class="nav" @submit.prevent>
        <el-form-item>
          <el-input
            v-model.trim="queryForm.roleName"
            placeholder="请输入查询条件"
            @keyup.enter="queryData"
          >
            <template #suffix>
              <i
                class="el-icon-search el-input__icon i-search"
                @click="queryData"
              />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleEdit">添加</el-button>
        </el-form-item>
        <!--          <el-form-item>-->
        <!--            <el-button-->
        <!--              icon="el-icon-delete"-->
        <!--              type="danger"-->
        <!--              @click="handleDestroy"-->
        <!--            >-->
        <!--              批量删除-->
        <!--            </el-button>-->
        <!--          </el-form-item>-->
      </el-form>
    </template>
    <div class="container-inner">
      <el-table
        v-loading="listLoading"
        :data="list"
        :element-loading-text="elementLoadingText"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :header-cell-style="{ background: '#F7F9FB', textAlign: 'center' }"
        :cell-style="{ textAlign: 'center' }"
        @selection-change="setSelectRows"
      >
        <!--      <el-table-column show-overflow-tooltip type="selection"></el-table-column>-->
        <el-table-column
          show-overflow-tooltip
          width="120"
          prop="id"
          label="id"
        />
        <el-table-column
          show-overflow-tooltip
          width="200"
          prop="roleName"
          label="角色名称"
        />
        <el-table-column
          show-overflow-tooltip
          width="200"
          prop="type"
          label="角色类型"
        />
        <el-table-column show-overflow-tooltip label="菜单列表">
          <template #default="{ row }">
            <span>{{ row.menuTitle.toString() }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button class="icon" type="primary" link @click="handleEdit(row)">
              <i class="bi bi-pencil-square" />
            </el-button>
            <el-button class="icon" type="primary" link @click="handleDestroy(row)">
              <i class="bi bi-trash" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNo"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        style="justify-content: center"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
      <edit ref="edit" @fetch-data="fetchData" />
    </div>
  </app-page>
</template>

<script>
import { doDelete, getList } from "@/api/roleManagement";
import Edit from "./components/RoleManagementEdit.vue";
import AppPage from "@/components/AppPage.vue";

export default {
  name: "RoleManagement",
  components: { Edit, AppPage },
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
        roleName: ""
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
    handleEdit(row) {
      if (row.id) {
        this.$refs["edit"].showEdit(row);
      } else {
        this.$refs["edit"].showEdit();
      }
    },
    async handleDestroy(row) {
      // if (row.id) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg } = await doDelete({ ids: row.id })
      //     this.$baseMessage(msg, 'success')
      //     this.fetchData()
      //   })
      // } else {
      //   if (this.selectRows.length > 0) {
      //     const ids = this.selectRows.map((item) => item.id).join()
      //     this.$baseConfirm('你确定要删除选中项吗', null, async () => {
      //       const { msg } = await doDelete({ ids })
      //       this.$baseMessage(msg, 'success')
      //       this.fetchData()
      //     })
      //   } else {
      //     this.$baseMessage('未选中任何行', 'error')
      //     return false
      //   }
      // }
      const rs = await elConfirmNoCancelBtn(null, "你确定要删除当前项吗");
      if (rs !== "confirm") return;
      const { msg } = await doDelete({
        id: row.id
      });
      elMessage(msg, "success");
      this.fetchData();
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
    async fetchData() {
      this.listLoading = true;
      const { data } = await getList(this.queryForm);
      this.list = data.rows;
      this.total = data.total;
      setTimeout(() => {
        this.listLoading = false;
      }, 300);
    }
  }
};
</script>

<style scoped lang="scss">
.nav > * {
  margin-bottom: 0;
  margin-right: 1em;

  &:last-child {
    margin-right: 0;
  }
}

.icon {
  font-size: 1.5em !important;
  padding: 5px !important;
}
</style>
