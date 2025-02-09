<template>
  <el-drawer v-model="drawer" :title="title" :before-close="close">
    <div v-loading="loading" class="drawer__content">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
        :disabled="detail"
        size="default"
      >
        <el-form-item label="姓名" prop="realName">
          <el-popover
            v-model="searchShow"
            :trigger="title === '新增' ? 'hover' : ''"
            :width="300"
          >
            <template #reference>
              <el-input
                v-model.trim="form.realName"
                autocomplete="off"
              />
            </template>
            <el-table
              :data="searchTable"
              size="small"
              :show-header="false"
              @row-click="selectItem"
            >
              <el-table-column
                property="realName"
                label="姓名"
                :width="100"
              />
              <el-table-column
                property="userName"
                label="账号"
                :width="200"
              />
            </el-table>
          </el-popover>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model.trim="form.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="用户账号" prop="userName">
          <el-input
            :value="form.userName"
            autocomplete="off"
            readonly
            placeholder="与手机号相同"
          />
        </el-form-item>
        <!--<el-form-item label="密码" prop="password">-->
        <!--          <el-input-->
        <!--            v-model.trim="form.password"-->
        <!--            type="password"-->
        <!--            autocomplete="off"-->
        <!--          ></el-input>-->
        <!--        </el-form-item>-->
        <el-form-item label="角色" prop="roleId">
          <el-radio-group v-model="form.roleId">
            <el-radio v-for="item in roleList" :key="item.id" :label="item.id">
              {{ item.roleName }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="isUse">
          <el-radio-group v-model="form.isUse">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div v-show="!detail" class="drawer__footer">
        <el-button size="default" @click="close">取 消</el-button>
        <el-button size="default" type="primary" @click="save">确 定</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script>
  import { doCreate, doEdit } from '@/api/userManagement'
  import { getRoleByUserId, getRoleList } from '@/api/roleManagement'
  import { isPhone } from '@/utils/validate'
  import { userSearching } from '@/api/user'
  import { ElMessage, ElMessageBox } from "element-plus";
  export default {
    name: 'UserManagementEdit',
    data() {
      return {
        form: {
          password: '',
          phone: '',
          realName: '',
          userName: undefined,
          roleId: '',
          isUse: 1,
        },
        editForm: {},
        rules: {
          password: [
            { required: false, trigger: 'blur', message: '请输入密码' },
          ],
          phone: [
            {
              required: true,
              validator: (rule, value, callback) => {
                if (!isPhone(value)) {
                  callback(new Error('请输入正确的手机号'))
                } else {
                  callback()
                }
              },
              trigger: 'blur',
            },
          ],
          realName: [
            { required: true, trigger: 'blur', message: '请输入姓名' },
          ],
          roleId: [{ required: true, trigger: 'blur', message: '请选择角色' }],
        },
        title: '',
        loading: false,
        drawer: false,
        detail: false,
        roleList: [],
        searchTable: [],
        searchWaiter: 0,
        searchShow: false,
        formChanging: false,
      }
    },
    watch: {
      'form.realName': function(value) {
        const key = Math.random()
        this.searchWaiter = key
        setTimeout(async () => {
          if (this.searchWaiter !== key) return
          try {
            this.searchTable = await userSearching(value)
          } catch {}
        }, 1000)
      },
      'form.phone': function() {
        if (this.formChanging) return
        this.form.userName = undefined
      },
    },
    created() {
      this.fetchData()
    },
    methods: {
      showEdit(row, detail) {
        this.loading = true
        this.formChanging = true
        if (!row) {
          this.title = '新增'
        } else {
          this.title = detail ? '查看' : '编辑'
          this.detail = detail
          this.form = Object.assign({}, row)
          // 记录有没有编辑过的form
          this.editForm = JSON.parse(JSON.stringify(this.form))
          this.getRole()
        }
        this.drawer = true
        setTimeout(() => {
          this.loading = false
          this.formChanging = false
        }, 1000)
      },
      close(confirm) {
        let formChanged = false
        // 判断抽屉是否被编辑过
        if (this.title === '新增') {
          formChanged = !(
            JSON.stringify(this.form) ===
            JSON.stringify(this.$options.data().form)
          )
        } else {
          formChanged = !(
            JSON.stringify(this.form) === JSON.stringify(this.editForm)
          )
        }
        if (confirm && formChanged && this.title !== '查看') {
          ElMessageBox.confirm(
            '编辑数据将会清空，确认退出？',
            '请仔细确认'
          ).then(() => this.closeEdit(), () => {})
        } else {
          this.closeEdit()
        }
      },
      selectItem(item) {
        this.formChanging = true
        this.form.phone = item.phone
        this.form.userName = item.userName
        this.form.realName = item.realName
        this.searchShow = false
        this.$nextTick(() => (this.formChanging = false))
      },
      closeEdit() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.detail = false
        this.drawer = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.form.id) {
              const { msg } = await doEdit(this.form)
              ElMessage.success(msg)
            } else {
              const { msg } = await doCreate(this.form)
              ElMessage.success(msg)
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      async fetchData() {
        this.listLoading = true
        // 角色
        const req5 = await getRoleList()
        this.roleList = req5.data.filter((item) => !item.type.includes('admin'))

        setTimeout(() => {
          this.listLoading = false
        }, 300)
      },
      // 角色
      async getRole() {
        const { data } = await getRoleByUserId(this.form.id)
        this.form.roleId = ref(data.roleId)
      },
    },
  }
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
