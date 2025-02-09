<template>
  <app-page class="menuManagement-container">
<!--    <vab-query-form>-->
<!--      <vab-query-form-left-panel :span="4">-->
<!--        <last-breadcrumb class="page-breadcrumb"/>-->
<!--      </vab-query-form-left-panel>-->
<!--      <vab-query-form-right-panel :span="20">-->
<!--      </vab-query-form-right-panel>-->
<!--    </vab-query-form>-->
    <template #right>
      <el-button type="primary" @click="handleEdit()">添加</el-button>
    </template>
    <div class="container-inner">
      <el-table
        v-loading="listLoading"
        :data="list"
        :element-loading-text="elementLoadingText"
        row-key="path"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :header-cell-style="{ background: '#F7F9FB', textAlign: 'center' }"
      >
        <el-table-column
          show-overflow-tooltip
          prop="meta.title"
          label="标题"
          style="text-align: center"
        />
        <el-table-column show-overflow-tooltip label="类型" width="100">
          <template #default="{ row }">
            <div style="text-align: center">
              <el-tag v-if="row.type === '0'" effect="dark">目录</el-tag>
              <el-tag v-else type="success" effect="dark">菜单</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="图标" width="150">
          <template #default="{ row }">
            <div style="text-align: center">
              <span v-if="row.meta">
                <i :class="'icon bi bi-' + row.meta.icon"/>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          show-overflow-tooltip
          prop="name"
          label="name"
        />
        <el-table-column
          show-overflow-tooltip
          prop="path"
          label="路径"
        />
        <el-table-column
          show-overflow-tooltip
          prop="component"
          label="vue文件路径"
        />
        <el-table-column
          show-overflow-tooltip
          prop="redirect"
          label="重定向"
        />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <div style="display: flex; justify-content: center">
              <el-button class="icon" type="primary" link @click="handleEdit(row)">
                <i class="bi bi-pencil-square"/>
              </el-button>
              <el-button class="icon" type="primary" link @click="handleDelete(row)">
                <i class="bi bi-trash"/>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <edit ref="edit" :menu-data="menuData" @fetch-data="fetchData"/>
    </div>
  </app-page>
</template>

<script>
  import { doDelete, getTree } from '@/api/menuManagement'
  import Edit from './components/MenuManagementEdit.vue'
  import AppPage from "@/components/AppPage.vue";
  export default {
    name: 'MenuManagement',
    components: { Edit, AppPage },
    data() {
      return {
        data: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        list: [],
        listLoading: true,
        elementLoadingText: '正在加载...',
      }
    },
    computed: {
      menuData() {
        return this.list
      },
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleEdit(row) {
        if (row) {
          this.$refs['edit'].showEdit(row)
        } else {
          this.$refs['edit'].showEdit()
        }
      },
      async handleDelete(row) {
        if (row.id) {
          const rs = await elConfirmNoCancelBtn(null, '你确定要删除当前项吗')
          if (rs !== 'confirm') return;
          const { msg } = await doDelete(row)
          elMessage(msg, 'success')
          await this.fetchData()
        }
      },
      async fetchData() {
        this.listLoading = true
        this.list = await getTree()
        setTimeout(() => {
          this.listLoading = false
        }, 300)
      },
    },
  }
</script>
<style scoped lang="scss">
.icon {
  font-size: 1.5em !important;
  padding: 5px !important;
}
</style>
