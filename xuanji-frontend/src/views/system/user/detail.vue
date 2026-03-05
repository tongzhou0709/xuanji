<template>
  <div class="app-container user-detail">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span class="card-title">用户详情</span>
      </div>
      
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="账号信息" name="account">
          <el-form :model="userDetail" :rules="rules" ref="accountForm" label-width="100px" class="detail-form">
            <div class="info-section">
              <div class="section-title">基本信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="用户名">
                    <span class="value" v-if="!editMode">{{ userDetail.username }}</span>
                    <el-input v-else v-model="userDetail.username" disabled />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="姓名">
                    <span class="value" v-if="!editMode">{{ userDetail.realName }}</span>
                    <el-input v-else v-model="userDetail.realName" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="邮箱">
                    <span class="value highlight" v-if="!editMode">{{ userDetail.email }}</span>
                    <el-input v-else v-model="userDetail.email" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="手机号">
                    <span class="value" v-if="!editMode">{{ userDetail.phone }}</span>
                    <el-input v-else v-model="userDetail.phone" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div class="info-section">
              <div class="section-title">账号状态</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="状态">
                    <el-tag v-if="!editMode" :type="userDetail.status === 1 ? 'success' : 'danger'" size="medium">
                      {{ userDetail.status === 1 ? '启用' : '禁用' }}
                    </el-tag>
                    <el-radio-group v-else v-model="userDetail.status">
                      <el-radio :label="1">启用</el-radio>
                      <el-radio :label="0">禁用</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建时间">
                    <span class="value">{{ formatDateTime(userDetail.createTime) }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="人员信息" name="person">
          <el-form :model="userDetail" :rules="rules" ref="personForm" label-width="100px" class="detail-form">
            <div class="info-section">
              <div class="section-title">组织信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="所属机构">
                    <span class="value" v-if="!editMode">{{ userDetail.orgName }}</span>
                    <el-cascader
                      v-else
                      v-model="userDetail.orgId"
                      :options="orgOptions"
                      :props="{ checkStrictly: true, value: 'id', label: 'orgName', emitPath: false }"
                      placeholder="请选择所属机构"
                      clearable
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="岗位">
                    <span class="value" v-if="!editMode">{{ userDetail.postName }}</span>
                    <el-input v-else v-model="userDetail.postName" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div class="info-section">
              <div class="section-title">标识信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="EOA ID">
                    <span class="value highlight" v-if="!editMode">{{ userDetail.eoaId }}</span>
                    <el-input v-else v-model="userDetail.eoaId" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="USAP账号">
                    <span class="value" v-if="!editMode">{{ userDetail.usapAccount }}</span>
                    <el-input v-else v-model="userDetail.usapAccount" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="角色权限" name="role">
          <div v-if="!editMode">
            <el-tag v-for="role in userRoles" :key="role.id" type="success" style="margin-right: 8px; margin-bottom: 8px;">
              {{ role.roleName }}
            </el-tag>
            <div v-if="userRoles.length === 0" style="color: #909399;">暂无绑定角色</div>
          </div>
          <div v-else>
            <el-checkbox-group v-model="selectedRoles">
              <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id" style="display: block; margin: 10px 0;">
                {{ role.roleName }} ({{ role.roleCode }})
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-tab-pane>
      </el-tabs>
      
      <div class="action-buttons">
        <el-button @click="goBack">返回</el-button>
        <template v-if="isAdmin && !editMode">
          <el-button type="primary" @click="handleEdit">编辑</el-button>
        </template>
        <template v-else-if="editMode">
          <el-button @click="cancelEdit">取消</el-button>
          <el-button type="primary" @click="saveEdit">保存</el-button>
        </template>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getUser, updateUser, getUserRoles, assignUserRoles } from "@/api/user";
import { listOrg } from "@/api/org";
import { listRole } from "@/api/role";
import store from "@/store";

export default {
  name: "UserDetail",
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
      userDetail: {},
      activeTab: "account",
      editMode: false,
      isAdmin: false,
      orgOptions: [],
      userRoles: [],
      allRoles: [],
      selectedRoles: [],
      rules: {
        realName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] },
          { validator: validateEmail, trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.isAdmin = store.state.user.roles.includes('SUPER_ADMIN');
    const userId = this.$route.query.id;
    if (userId) {
      this.getUserDetail(userId);
      this.getOrgList();
      this.getUserRoles(userId);
      this.getAllRoles();
    }
  },
  methods: {
    getUserDetail(id) {
      getUser(id).then(response => {
        this.userDetail = response.data;
      });
    },
    getUserRoles(userId) {
      getUserRoles(userId).then(response => {
        this.userRoles = response.data || [];
        this.selectedRoles = this.userRoles.map(r => r.id);
      });
    },
    getAllRoles() {
      listRole().then(response => {
        this.allRoles = response.data || [];
      });
    },
    getOrgList() {
      listOrg().then(response => {
        this.orgOptions = this.handleTree(response.data, "id", "parentId");
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
    handleEdit() {
      this.editMode = true;
    },
    cancelEdit() {
      const userId = this.$route.query.id;
      this.getUserDetail(userId);
      this.editMode = false;
    },
    saveEdit() {
      this.$refs.accountForm && this.$refs.accountForm.validate(valid => {
        if (valid) {
          this.doSave();
        }
      });
      this.$refs.personForm && this.$refs.personForm.validate(valid => {
        if (valid) {
          this.doSave();
        }
      });
    },
    doSave() {
      updateUser(this.userDetail).then(() => {
        assignUserRoles(this.userDetail.id, this.selectedRoles).then(() => {
          this.$message.success("保存成功");
          this.editMode = false;
          this.getUserRoles(this.userDetail.id);
        });
      });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.user-detail {
  padding: 20px;
}
.card-title {
  font-size: 18px;
  font-weight: bold;
}
.detail-form {
  padding: 20px 0;
}
.info-section {
  margin-bottom: 24px;
}
.info-section:last-child {
  margin-bottom: 0;
}
.section-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 16px;
  padding-left: 10px;
  border-left: 3px solid #409EFF;
}
.detail-form .el-form-item {
  margin-bottom: 14px;
}
.detail-form .el-form-item span.value {
  color: #606266;
  font-size: 14px;
}
.detail-form .highlight {
  color: #409EFF !important;
  font-weight: 500;
}
.action-buttons {
  margin-top: 20px;
  text-align: center;
}
</style>
