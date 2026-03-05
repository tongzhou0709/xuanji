<template>
  <div class="app-container user-manage">
    <el-row :gutter="20">
      <el-col :span="5">
        <el-card shadow="never" class="org-tree-card">
          <div slot="header" class="card-header">
            <span>机构列表</span>
          </div>
          <div class="org-tree">
            <el-tree
              :data="orgTreeData"
              :props="orgTreeProps"
              node-key="id"
              :expand-on-click-node="false"
              :default-expand-all="true"
              @node-click="handleOrgNodeClick"
            >
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
              </span>
            </el-tree>
          </div>
        </el-card>
      </el-col>
      <el-col :span="19">
        <el-card shadow="never" class="search-wrapper">
          <el-form :inline="true" :model="queryParams" ref="queryForm">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="queryParams.realName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-wrapper">
          <div slot="header" class="table-header">
            <div class="left">
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
              <el-button type="success" icon="el-icon-upload2" @click="handleImport">批量导入</el-button>
            </div>
            <div class="right" v-if="currentOrgName">
              <el-tag>当前机构：{{ currentOrgName }}</el-tag>
            </div>
          </div>

          <el-table v-loading="loading" :data="userList" @row-click="handleRowClick">
            <el-table-column label="EOA ID" align="center" prop="eoaId" width="120" />
            <el-table-column label="姓名" align="center" prop="realName" />
            <el-table-column label="所属机构" align="center" prop="orgName" />
            <el-table-column label="状态" align="center" prop="status" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="mini">
                  {{ scope.row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="60">
            </el-table-column>
          </el-table>

          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.current"
            :limit.sync="queryParams.size"
            @pagination="getList"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" :disabled="form.id != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属机构" prop="orgId">
              <el-cascader
                v-model="form.orgId"
                :options="orgOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'orgName', emitPath: false }"
                placeholder="请选择所属机构"
                clearable
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="EOA ID" prop="eoaId">
              <el-input v-model="form.eoaId" placeholder="请输入EOA ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="USAP账号" prop="usapAccount">
              <el-input v-model="form.usapAccount" placeholder="请输入USAP账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
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
import { listUser, saveUser, updateUser, deleteUser, getUser } from "@/api/user";
import { listOrg } from "@/api/org";
import { getToken } from "@/utils/auth";
import store from "@/store";

export default {
  name: "User",
  data() {
    const validateEmail = (rule, value, callback) => {
      if (value) {
        if (!value.endsWith("@abchina.com.cn")) {
          callback(new Error("邮箱必须以@abchina.com.cn结尾"));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      loading: true,
      userList: [],
      isAdmin: store.state.user.roles.includes('SUPER_ADMIN'),
      orgOptions: [],
      orgTreeData: [],
      orgTreeProps: {
        children: "children",
        label: "orgName"
      },
      currentOrgId: undefined,
      currentOrgName: '',
      total: 0,
      title: "",
      open: false,
      queryParams: {
        current: 1,
        size: 10,
        username: undefined,
        realName: undefined,
        orgId: undefined
      },
      upload: {
        open: false,
        title: "用户导入",
        isUploading: false,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/user/import"
      },
      form: {},
      rules: {
        username: [{ required: true, message: "用户名不能为空", trigger: "blur" }],
        realName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        orgId: [{ required: true, message: "请选择所属机构", trigger: "change" }],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] },
          { validator: validateEmail, trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getOrgTree();
    this.getList();
  },
  methods: {
    formatDateTime(dateStr) {
      if (!dateStr) return '';
      let date;
      if (typeof dateStr === 'string') {
        const s = dateStr.replace('T', ' ').replace(/\.\d{3}/, '');
        date = new Date(s);
      } else {
        date = new Date(dateStr);
      }
      if (isNaN(date.getTime())) return dateStr;
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    getOrgTree() {
      listOrg().then(response => {
        const treeData = this.handleTree(response.data, "id", "parentId");
        this.orgTreeData = treeData;
        this.orgOptions = treeData;
      });
    },
    handleOrgNodeClick(data) {
      this.currentOrgId = data.id;
      this.currentOrgName = data.orgName;
      this.queryParams.orgId = data.id;
      this.getList();
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
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.currentOrgId = undefined;
      this.currentOrgName = '';
      this.queryParams.orgId = undefined;
      this.handleQuery();
    },
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$message.success(response.message);
      this.getList();
    },
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleAdd() {
      this.reset();
      if (this.currentOrgId) {
        this.form.orgId = this.currentOrgId;
      }
      this.open = true;
      this.title = "添加用户";
    },
    handleUpdate(row) {
      this.reset();
      getUser(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户";
      });
    },
    handleRowClick(row) {
      this.$router.push({ path: '/system/user/detail', query: { id: row.id }});
    },
    handleDelete(row) {
      this.$confirm('是否确认删除用户名为"' + row.username + '"的数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteUser(row.id);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      });
    },
    reset() {
      this.form = {
        id: undefined,
        username: undefined,
        realName: undefined,
        orgId: undefined,
        phone: undefined,
        email: undefined,
        eoaId: undefined,
        usapAccount: undefined,
        status: 1
      };
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateUser(this.form).then(response => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            saveUser(this.form).then(response => {
              this.$message.success("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.user-manage {
  height: 100%;
}
.org-tree-card {
  height: calc(100vh - 180px);
  overflow: auto;
}
.org-tree-card .card-header {
  font-weight: bold;
}
.org-tree {
  min-height: 300px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.search-wrapper {
  margin-bottom: 20px;
}
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
