<template>
  <div class="app-container role-container">
    <el-row :gutter="20">
      <el-col :span="selectedRole ? 10 : 24">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>角色列表</span>
            <el-button type="text" @click="handleAdd" style="float: right; padding: 3px 0">
              <i class="el-icon-plus"></i> 新增角色
            </el-button>
          </div>
          
          <el-table v-loading="loading" :data="roleList" highlight-current-row @row-click="handleRowClick">
            <el-table-column label="角色名称" align="center" prop="roleName" />
            <el-table-column label="角色编码" align="center" prop="roleCode" />
            <el-table-column label="描述" align="center" prop="description" show-overflow-tooltip />
            <el-table-column label="状态" align="center" prop="status" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="mini">
                  {{ scope.row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="权限" align="center" min-width="200">
              <template slot-scope="scope">
                <span class="menu-tags">
                  <el-tag v-for="menu in (scope.row.menuNames || []).slice(0, 3)" :key="menu" size="mini" type="info" style="margin-right: 2px; margin-bottom: 2px;">
                    {{ menu }}
                  </el-tag>
                  <el-button type="text" size="small" @click.stop="handleRowClick(scope.row)">
                    配置
                  </el-button>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="120" v-if="isAdmin">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click.stop="handleEdit(scope.row)">编辑</el-button>
                <el-button type="text" size="small" @click.stop="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="14" v-if="selectedRole">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>权限配置 - {{ selectedRole.roleName }}</span>
            <el-button type="text" @click="selectedRole = null" style="float: right; padding: 3px 0">
              <i class="el-icon-close"></i> 关闭
            </el-button>
          </div>
          
          <div class="menu-tree-container">
            <el-tree
              ref="menuTree"
              :data="menuList"
              :props="menuProps"
              show-checkbox
              node-key="id"
              :default-expand-all="true"
              :check-strictly="false"
              :default-checked-keys="checkedMenus"
            />
          </div>
          
          <div class="action-buttons" v-if="isAdmin">
            <el-button @click="selectedRole = null">取消</el-button>
            <el-button type="primary" @click="handleSaveMenus" :loading="menuLoading">保存权限</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form :model="roleForm" :rules="roleRules" ref="roleForm" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" :disabled="roleForm.id ? true : false" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRole, saveRole, updateRole, deleteRole, getRoleMenus, assignMenus, listMenu } from "@/api/role";
import { getInfo } from "@/api/user";

export default {
  name: "Role",
  data() {
    return {
      loading: false,
      menuLoading: false,
      roleList: [],
      menuList: [],
      checkedMenus: [],
      selectedRole: null,
      dialogVisible: false,
      dialogTitle: "",
      isAdmin: false,
      roleForm: {
        id: null,
        roleName: "",
        roleCode: "",
        description: "",
        status: 1
      },
      roleRules: {
        roleName: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
        roleCode: [{ required: true, message: "请输入角色编码", trigger: "blur" }]
      },
      menuProps: {
        children: "children",
        label: "menuName"
      }
    };
  },
  created() {
    this.getRoleList();
    this.getMenuList();
    this.checkAdmin();
  },
  methods: {
    getRoleList() {
      this.loading = true;
      listRole().then(res => {
        this.roleList = res.data || [];
        this.loading = false;
      });
    },
    getMenuList() {
      listMenu().then(res => {
        const menus = res.data || [];
        this.menuList = this.buildTree(menus);
      });
    },
    buildTree(menus) {
      const map = {};
      const roots = [];
      menus.forEach(menu => {
        map[menu.id] = { ...menu, children: [] };
      });
      menus.forEach(menu => {
        if (menu.parentId === 0) {
          roots.push(map[menu.id]);
        } else if (map[menu.parentId]) {
          map[menu.parentId].children.push(map[menu.id]);
        }
      });
      return roots;
    },
    handleRowClick(row) {
      this.selectedRole = row;
      this.getRoleMenuIds(row.id);
    },
    getRoleMenuIds(roleId) {
      getRoleMenus(roleId).then(res => {
        const menus = res.data || [];
        this.checkedMenus = menus.map(m => m.id);
      });
    },
    handleAdd() {
      this.dialogTitle = "新增角色";
      this.dialogVisible = true;
    },
    handleEdit(row) {
      this.dialogTitle = "编辑角色";
      this.roleForm = { ...row };
      this.dialogVisible = true;
    },
    handleDelete(row) {
      this.$confirm(`确认删除角色"${row.roleName}"？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteRole(row.id).then(() => {
          this.$message.success("删除成功");
          this.getRoleList();
        });
      });
    },
    handleSubmit() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          const action = this.roleForm.id ? updateRole : saveRole;
          action(this.roleForm).then(() => {
            this.$message.success("保存成功");
            this.dialogVisible = false;
            this.getRoleList();
          });
        }
      });
    },
    handleSaveMenus() {
      const checkedNodes = this.$refs.menuTree.getCheckedNodes(false, true);
      const menuIds = checkedNodes.map(node => node.id);
      
      this.menuLoading = true;
      assignMenus(this.selectedRole.id, menuIds).then(() => {
        this.$message.success("权限保存成功");
        this.menuLoading = false;
      }).catch(() => {
        this.menuLoading = false;
      });
    },
    checkAdmin() {
      getInfo().then(res => {
        const roles = res.data.roles || [];
        this.isAdmin = roles.includes("SUPER_ADMIN");
      });
    },
    resetForm() {
      this.$refs.roleForm.resetFields();
      this.roleForm = {
        id: null,
        roleName: "",
        roleCode: "",
        description: "",
        status: 1
      };
    }
  }
};
</script>

<style scoped>
.role-container {
  padding: 20px;
}
.card-header {
  font-size: 16px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.menu-tree-container {
  max-height: 500px;
  overflow-y: auto;
  padding: 10px 0;
}
.action-buttons {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
