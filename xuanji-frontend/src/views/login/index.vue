<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="brand">
          <h1 class="brand-title">璇玑</h1>
          <p class="brand-desc">专业·高效·智能的事务管理平台</p>
        </div>
      </div>
      <div class="login-right">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
          <div class="title-container">
            <h3 class="title">欢迎登录</h3>
            <p class="sub-title">请使用您的账号和密码进行登录</p>
          </div>

          <el-form-item prop="username">
            <el-input
              ref="username"
              v-model="loginForm.username"
              placeholder="请输入用户名"
              name="username"
              type="text"
              tabindex="1"
              auto-complete="on"
              prefix-icon="el-icon-user"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="请输入密码"
              name="password"
              tabindex="2"
              auto-complete="on"
              prefix-icon="el-icon-lock"
              @keyup.enter.native="handleLogin"
            >
              <i slot="suffix" :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'" class="show-pwd" @click="showPwd" />
            </el-input>
          </el-form-item>

          <el-button :loading="loading" type="primary" class="login-btn" @click.native.prevent="handleLogin">登 录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123456Qq!'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$nextTick(() => {
              this.$router.push('/dashboard')
            })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/styles/variables.scss";

.login-container {
  height: 100vh;
  width: 100%;
  background-color: $bgColor;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: radial-gradient(circle at 20% 20%, rgba(47, 84, 235, 0.05) 0%, transparent 40%),
                    radial-gradient(circle at 80% 80%, rgba(47, 84, 235, 0.05) 0%, transparent 40%);

  .login-box {
    width: 900px;
    height: 500px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
    display: flex;
    overflow: hidden;
  }

  .login-left {
    flex: 1;
    background: $menuBg;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40px;
    color: #fff;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image: linear-gradient(135deg, rgba(24, 144, 255, 0.2) 0%, transparent 100%);
    }

    .brand {
      position: relative;
      z-index: 1;
      text-align: center;

      .brand-title {
        font-size: 48px;
        margin-bottom: 20px;
        letter-spacing: 8px;
        font-weight: 600;
      }

      .brand-desc {
        font-size: 16px;
        opacity: 0.8;
        letter-spacing: 2px;
      }
    }
  }

  .login-right {
    width: 450px;
    padding: 60px 50px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .title-container {
      margin-bottom: 40px;

      .title {
        font-size: 28px;
        color: $titleColor;
        margin-bottom: 12px;
        font-weight: 600;
      }

      .sub-title {
        font-size: 14px;
        color: $subTextColor;
      }
    }

    .login-form {
      .el-form-item {
        margin-bottom: 25px;

        ::v-deep .el-input__inner {
          height: 45px;
          border-radius: 4px;
          border: 1px solid $borderColor;

          &:focus {
            border-color: $primaryColor;
          }
        }

        .show-pwd {
          cursor: pointer;
          font-size: 16px;
          color: $subTextColor;
          margin-right: 5px;

          &:hover {
            color: $primaryColor;
          }
        }
      }

      .login-btn {
        width: 100%;
        height: 45px;
        font-size: 16px;
        font-weight: 500;
        background-color: $primaryColor;
        border-color: $primaryColor;
        margin-top: 10px;
        transition: all 0.3s;

        &:hover {
          opacity: 0.9;
          box-shadow: 0 4px 12px rgba(47, 84, 235, 0.3);
        }
      }
    }
  }
}
</style>
