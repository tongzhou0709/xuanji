<template>
  <div class="app-container">
    <el-card shadow="never" class="search-wrapper">
      <el-form :inline="true" :model="queryParams" ref="queryForm">
        <el-form-item label="机构名称" prop="orgName">
          <el-input v-model="queryParams.orgName" placeholder="请输入机构名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="机构状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-wrapper">
      <div slot="header" class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增机构</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="orgList"
        row-key="id"
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column prop="orgName" label="机构名称" width="260" />
        <el-table-column prop="orgCode" label="机构代码" align="center" />
        <el-table-column prop="orgLevel" label="级别" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orgLevel === 1">分行级</el-tag>
            <el-tag v-else-if="scope.row.orgLevel === 2" type="success">处级</el-tag>
            <el-tag v-else-if="scope.row.orgLevel === 3" type="warning">科级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" align="center" width="80" />
        <el-table-column prop="status" label="状态" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)">新增</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改机构对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级机构" prop="parentId">
              <el-cascader
                v-model="form.parentId"
                :options="orgOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'orgName', emitPath: false }"
                placeholder="选择上级机构"
                clearable
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构名称" prop="orgName">
              <el-input v-model="form.orgName" placeholder="请输入机构名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构代码" prop="orgCode">
              <el-input v-model="form.orgCode" placeholder="请输入机构代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">停用</el-radio>
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
import { listOrg, saveOrg, updateOrg, deleteOrg } from "@/api/org";

export default {
  name: "Org",
  data() {
    return {
      loading: true,
      orgList: [],
      orgOptions: [],
      title: "",
      open: false,
      queryParams: {
        orgName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        parentId: [{ required: true, message: "上级机构不能为空", trigger: "blur" }],
        orgName: [{ required: true, message: "机构名称不能为空", trigger: "blur" }],
        orgCode: [{ required: true, message: "机构代码不能为空", trigger: "blur" }],
        sort: [{ required: true, message: "显示排序不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOrg(this.queryParams).then(response => {
        this.orgList = this.handleTree(response.data, "id", "parentId");
        this.orgOptions = [{ id: 0, orgName: '主目录', children: this.orgList }];
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
        orgName: undefined,
        orgCode: undefined,
        sort: 0,
        status: 1
      };
    },
    handleQuery() {
      this.getList();
    },
    resetQuery() {
      this.queryParams = {
        orgName: undefined,
        status: undefined
      };
      this.handleQuery();
    },
    handleAdd(row) {
      this.reset();
      if (row != null && row.id) {
        this.form.parentId = row.id;
      }
      this.open = true;
      this.title = "添加机构";
    },
    handleUpdate(row) {
      this.reset();
      this.form = { ...row };
      this.open = true;
      this.title = "修改机构";
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateOrg(this.form).then(response => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            saveOrg(this.form).then(response => {
              this.$message.success("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.orgName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteOrg(row.id);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.table-header {
  display: flex;
  justify-content: flex-start;
}
</style>
