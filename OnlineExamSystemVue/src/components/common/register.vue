<!-- 用户注册界面 -->
<template>
    <div id="register">
      <div class="bg"></div>
      <el-row class="main-container">
        <el-col :lg="8" :xs="16" :md="10" :span="10">
          <div class="top" style="color: black;font-size: 32px">
            <i class="iconfont icon-r-team title-icon"></i> 类学习通的在线考试系统
            <span class="title"> </span>
          </div>
          <br />
          <div class="bottom">
            <div class="container">
              <p class="title">用户注册</p>
              <el-form :label-position="labelPosition" label-width="80px" :model="formLabelAlign">
                <el-form-item label="手机号">
                  <el-input v-model="formLabelAlign.phoneNumber" placeholder="请输入手机号"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                  <el-input v-model="formLabelAlign.password" placeholder="请输入密码" type="password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                  <el-input v-model="formLabelAlign.confirmPassword" placeholder="请确认密码" type="password"></el-input>
                </el-form-item>
                <div class="submit">
                  <el-button type="primary" class="row-register" @click="register()">注册</el-button>
                </div>
                <div class="login-link">
                  <span>已有账号？</span>
                  <router-link to="/" class="login-text">立即登录</router-link>
                </div>
              </el-form>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  export default {
    name: "register",
    data() {
      return {
        labelPosition: "left",
        formLabelAlign: {
          phoneNumber: "",
          password: "",
          confirmPassword: ""
        }
      };
    },
    methods: {
      register() {
        // 手机号验证
        if (!this.formLabelAlign.phoneNumber) {
          this.$message("请输入手机号");
          return;
        }
        // 手机号格式验证
        if (!/^1[3-9]\d{9}$/.test(this.formLabelAlign.phoneNumber)) {
          this.$message("手机号格式不正确");
          return;
        }
        // 密码验证
        if (!this.formLabelAlign.password) {
          this.$message("请输入密码");
          return;
        }
        // 确认密码验证
        if (!this.formLabelAlign.confirmPassword) {
          this.$message("请确认密码");
          return;
        }
        // 两次密码是否一致
        if (this.formLabelAlign.password !== this.formLabelAlign.confirmPassword) {
          this.$message("两次输入的密码不一致");
          return;
        }
  
        // 发送注册请求
        this.$axios({
          url: `/api/user/register`,
          method: "post",
          data: {
            phoneNumber: this.formLabelAlign.phoneNumber,
            password: this.formLabelAlign.password
          }
        })
          .then(res => {
            if (res.data.code === 200) {
              this.$message({
                type: "success",
                message: "注册成功"
              });
              // 注册成功后跳转到登录页
              this.$router.push("/");
            } else {
              this.$message({
                type: "error",
                message: res.data.message
              });
            }
          })
          .catch(e => {
            console.log(e);
            this.$message({
              showClose: true,
              message: "注册失败，请稍后重试",
              type: "error",
              duration: 5000
            });
          });
      }
    }
  };
  </script>
  
  <style lang="less" scoped>
  #register {
    font-size: 14px;
    color: #000;
    background-color: #fff;
  
    .bg {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      overflow-y: auto;
      height: 100%;
      background: url("../../assets/img/login.jpg") center top / cover no-repeat;
      background-color: #b6bccdd1!important;
    }
  
    .main-container {
      display: flex;
      justify-content: center;
      align-items: center;
  
      .top {
        margin-top: 100px;
        font-size: 30px;
        color: #ff962a;
        display: flex;
        justify-content: center;
      }}
  
    .bottom {
      display: flex;
      justify-content: center;
      background-color: #fff;
      border-radius: 5px;
      box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  
      .container {
        margin-bottom: 32px;
        .title {
          text-align: center;
          font-size: 30px;
          margin: 30px 0px;
        }
      }
    }
  
    .submit {
      display: flex;
      justify-content: center;
  
      .row-register {
        width: 100%;
        margin: 20px 0px 10px 0px;
        padding: 15px 20px;
        background-color: rgb(133, 174, 191);
        border-color: rgb(133, 174, 191);
        color: white;}
    }
  
    .login-link {
      text-align: right;
      margin-top: 10px;
      padding-right: 10px;
  
      span {
        color: #606266;
        font-size: 14px;
      }
    }
  
    .login-text {
      color: #409EFF;
      margin-left: 5px;
      cursor: pointer;
      text-decoration: none;
  
      &:hover {
        color: #66b1ff;
      }
    }
  }
  
  .title-icon {
    font-size: 44px;
    margin-right: 10px;
  }
  </style>
  