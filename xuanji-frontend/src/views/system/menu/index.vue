<template>
  <div class="app-container">
    <el-card shadow="never" class="table-wrapper">
      <div slot="header" class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增菜单</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="menuList"
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160" />
        <el-table-column prop="icon" label="图标" align="center" width="100">
          <template slot-scope="scope">
            <i :class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="60" />
        <el-table-column prop="permission" label="权限标识" :show-overflow-tooltip="true" />
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)">新增</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-cascader
                v-model="form.parentId"
                :options="menuOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'menuName', emitPath: false }"
                placeholder="请选择上级菜单"
                clearable
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio :label="0">目录</el-radio>
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单图标">
              <el-input v-model="form.icon" placeholder="请输入图标类名 (el-icon-xxx)" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 2">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 1">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限标识">
              <el-input v-model="form.permission" placeholder="请输入权限标识" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMenu, saveMenu, updateMenu, deleteMenu } from "@/api/menu";

export default {
  name: "Menu",
  data() {
    return {
      loading: true,
      menuList: [],
      menuOptions: [],
      title: "",
      open: false,
      form: {},
      rules: {
        menuName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
        sort: [{ required: true, message: "显示排序不能为空", trigger: "blur" }],
        path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listMenu().then(response => {
        this.menuList = this.handleTree(response.data, "id");
        this.menuOptions = [{ id: 0, menuName: '主类目', children: this.menuList }];
        this.loading = false;
      });
    },
    handleTree(data, id, parentId, children) {
      const config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
      }
      const childrenListMap = {}
      const nodeIds = {}
      const tree = []

      for (const d of data) {
        const pId = d[config.parentId]
        if (childrenListMap[pId] == null) {
          childrenListMap[pId] = []
        }
        nodeIds[d[config.id]] = d
        childrenListMap[pId].push(d)
      }

      for (const d of data) {
        const pId = d[config.parentId]
        if (nodeIds[pId] == null) {
          tree.push(d)
        }
      }

      for (const t of tree) {
        adaptToChildrenList(t)
      }

      function adaptToChildrenList(o) {
        if (childrenListMap[o[config.id]] !== null) {
          o[config.childrenList] = childrenListMap[o[config.id]]
        }
        if (o[config.childrenList]) {
          for (const c of o[config.childrenList]) {
            adaptToChildrenList(c)
          }
        }
      }
      return tree
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 1,
        sort: 0,
        path: undefined,
        component: undefined,
        permission: undefined,
        status: 1
      };
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    handleAdd(row) {
      this.reset();
      if (row != null && row.id) {
        this.form.parentId = row.id;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加菜单";
    },
    handleUpdate(row) {
      this.reset();
      this.form = { ...row };
      this.open = true;
      this.title = "修改菜单";
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateMenu(this.form).then(response => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            saveMenu(this.form).then(response => {
              this.$message.success("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.menuName + '"的数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteMenu(row.id);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      });
    }
  }
};
</script>

<style scoped>
.table-header {
  margin-bottom: 10px;
}
</style>
