<!-- 用户绑定界面 -->
<template>
    <div id="bind">
      <div class="bg"></div>
      <el-row class="main-container">
        <el-col :lg="8" :xs="16" :md="10" :span="10">
          <div class="top" style="color: black;font-size: 32px">
            <i class="iconfont icon-r-team title-icon"></i> 账号绑定
            <span class="title"> </span>
          </div>
          <br />
          <div class="bottom">
            <div class="container">
              <p class="title">绑定信息</p>
              <el-form :label-position="labelPosition" label-width="100px" :model="bindForm">
                <el-form-item label="学号/工号">
                  <el-input v-model="bindForm.user.userNumber" placeholder="请输入学号或工号"></el-input>
                </el-form-item>
                <el-form-item label="学校">
                  <el-input v-model="bindForm.schoolname" placeholder="请输入学校名称"></el-input>
                </el-form-item>
                <el-form-item label="身份">
                  <el-radio-group v-model="bindForm.user.roleId">
                    <el-radio :label="2">学生</el-radio>
                    <el-radio :label="1">教师</el-radio>
                    <el-radio :label="3">管理员</el-radio>
                  </el-radio-group>
                </el-form-item>
                <div class="submit">
                  <el-button type="primary" class="row-bind" @click="bindUser()">绑定</el-button>
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
    name: "bind",
    data() {
      return {
        labelPosition: "left",
        bindForm: {
          user: {
            userNumber: "",
            roleId: 2,  // 默认选中学生
            phoneNumber: this.$route.query.phoneNumber || ''  // 
          },
          schoolname: ""
        }
      };
    },
    methods: {
      bindUser() {
        // 表单验证
        if (!this.bindForm.user.userNumber) {
          this.$message.error("请输入学号/工号");
          return;
        }
        if (!this.bindForm.schoolname) {
          this.$message.error("请输入学校名称");
          return;
        }
        // 发送绑定请求
        this.$axios({
          url: '/api/user/bind',
          method: 'post',
          data: this.bindForm
        }).then(res => {
          if (res.data.code === 200) {
            this.$message.success("绑定成功");
            // 根据角色跳转到不同页面
            const routeMap = {
              1: '/index',    // 教师
              2: '/student',  // 学生
              3: '/index'     // 管理员
            };
            this.$router.push(routeMap[this.bindForm.user.roleId]);
          }
        }).catch(error => {
          this.$message.error(error.response.data.message || "绑定失败");
        });
      }
    }
  };
  </script>
  
  <style lang="less" scoped>
  #bind {
    font-size: 14px;
    color: #000;
    background-color: #fff;
    
    .bg {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: url("../../assets/img/login.jpg") center top / cover no-repeat;
      background-color: #b6bccdd1 !important;
    }
  
    .main-container {
      display: flex;
      justify-content: center;
      align-items: center;
    }
  
    .bottom {
      display: flex;
      justify-content: center;
      background-color: #fff;
      border-radius: 5px;
      box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
      padding: 20px;
      
      .container {
        width: 100%;
        
        .title {
          text-align: center;
          font-size: 24px;
          margin: 20px 0;
        }
      }
    }
  
    .submit {
      display: flex;
      justify-content: center;
      margin-top: 20px;
      
      .row-bind {
        width: 100%;
        padding: 15px 20px;
        background-color: #409EFF;
        border-color: #409EFF;
        color: white;
      }
    }
  }
  
  .title-icon {
    font-size: 44px;
    margin-right: 10px;
  }
  
  .top {
    margin-top: 100px;
    text-align: center;
    margin-bottom: 30px;
  }
  </style>
  