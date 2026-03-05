<template>
  <div class="app-container profile-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span class="card-title">个人信息</span>
      </div>
      
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="账号信息" name="account">
          <el-form :model="userInfo" label-width="100px" class="detail-form">
            <div class="info-section">
              <div class="section-title">基本信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="用户名">
                    <span class="value">{{ userInfo.username }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="姓名">
                    <span class="value">{{ userInfo.realName }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="邮箱">
                    <span class="value highlight">{{ userInfo.email }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="手机号">
                    <span class="value">{{ userInfo.phone }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div class="info-section">
              <div class="section-title">账号状态</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="状态">
                    <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'" size="medium">
                      {{ userInfo.status === 1 ? '启用' : '禁用' }}
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建时间">
                    <span class="value">{{ formatDateTime(userInfo.createTime) }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="人员信息" name="person">
          <el-form :model="userInfo" label-width="100px" class="detail-form">
            <div class="info-section">
              <div class="section-title">组织信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="所属机构">
                    <span class="value">{{ userInfo.orgName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="岗位">
                    <span class="value">{{ userInfo.postName }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div class="info-section">
              <div class="section-title">标识信息</div>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="EOA ID">
                    <span class="value highlight">{{ userInfo.eoaId }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="USAP账号">
                    <span class="value">{{ userInfo.usapAccount }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px" class="password-form">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdatePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getUser, updateUser, updatePassword } from "@/api/user";
import { getInfo } from "@/api/user";
import store from "@/store";

export default {
  name: "Profile",
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      userInfo: {},
      activeTab: "account",
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: "请输入原密码", trigger: "blur" }],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, message: "密码长度至少6位", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请再次输入新密码", trigger: "blur" },
          { validator: validateConfirmPassword, trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      getInfo().then(response => {
        const userId = response.data.id;
        getUser(userId).then(res => {
          this.userInfo = res.data;
        });
      });
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
    handleUpdatePassword() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.$confirm('确认修改密码？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            updatePassword({
              userId: this.userInfo.id,
              oldPassword: this.passwordForm.oldPassword,
              newPassword: this.passwordForm.newPassword
            }).then(() => {
              this.$message.success('密码修改成功，请重新登录');
              this.passwordForm = {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
              };
              setTimeout(() => {
                this.$store.dispatch('user/logout');
                this.$router.push('/login');
              }, 1500);
            });
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.profile-container {
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
.password-form {
  max-width: 500px;
  padding: 20px 0;
}
</style>
