<!-- 用户登录界面 -->

<template>
    <div id="login">
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
                        <p class="title">账号登录</p>
                        <el-form :label-position="labelPosition" label-width="80px" :model="formLabelAlign">
                            <el-form-item label="手机号">
                                <el-input v-model.number="formLabelAlign.phoneNumber" placeholder="请输入手机号"></el-input>
                            </el-form-item>
                            <el-form-item label="密码">
                                <el-input v-model="formLabelAlign.password" placeholder="请输入密码"
                                    type="password"></el-input>
                            </el-form-item>
                            <div class="submit">
                                <el-button type="primary" class="row-login" @click="login()">登录</el-button>
                            </div>
                            <div class="register-link">
                                <span>还没有账号？</span>
                                <router-link to="/register" class="register-text">立即注册</router-link>
                            </div>
                        </el-form>
                    </div>
                </div>
            </el-col>
        </el-row>
        <el-row class="footer">
            <el-col>

            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mapState } from "vuex";
export default {
    name: "login",
    data() {
        return {
            role: 2,
            labelPosition: "left",
            formLabelAlign: {
                phoneNumber: "",
                password: "",
            },
        };
    },
    methods: {
        //用户登录请求后台处理
        login() {
            if (
                this.formLabelAlign.phoneNumber == undefined ||
                this.formLabelAlign.phoneNumber == ""
            ) {
                this.$message("请输入手机号");
                return;
            }
            if (!/^1[3-9]\d{9}$/.test(this.formLabelAlign.phoneNumber)){
                this.$message("手机号有误");
                return;
            }
            if (this.formLabelAlign.password == "") {
                this.$message("请输入密码");
                return;
            }
            this.$axios({
                url: `/api/user/login`,
                method: "post",
                data: {
                    ...this.formLabelAlign,
                },
            })
            .then((res) => {
                let resData = res.data.data;
                if (resData != null) {
                    const token = resData.token;
                    localStorage.setItem('token', token);
                    // 设置axios默认headers
                    this.$axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                    // 判断是否绑定 (roleId为0表示未绑定)
                    if (resData.role === "0") {
                    this.$message({
                        type: 'warning',
                        message: '请先完成账号绑定'
                    });
                    // 跳转到绑定页面，并传递必要的参数
                    this.$router.push({
                        path: '/bindUserInfo',
                        query: {
                        phoneNumber: this.formLabelAlign.phoneNumber 
                        }
                    });
                    return;
                    }

                    // 已绑定，根据角色跳转
                    switch (resData.role) {
                    case "1": //教师
                        localStorage.set("roleId", resData.role);
                        this.$router.push({ path: "/index" }); 
                        break;
                    case "2": //学生
                        localStorage.set("roleId", resData.role);
                        this.$router.push({ path: "/student" }); 
                        break;
                    case "3": //管理员
                        localStorage.set("roleId", resData.role);
                        this.$router.push({ path: "/index" }); 
                        break;}
                }
                if (resData == null) {
                    this.$message({
                    showClose: true,
                    type: "error",
                    message: "手机号或者密码错误",
                    });
                }
                })
                .catch((e) => {
                    console.log(e);
                    if (
                        e.response == undefined ||
                        e.response.data == undefined
                    ) {
                        this.$message({
                            showClose: true,
                            message: e,
                            type: "error",
                            duration: 5000,
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: e.response.data,
                            type: "error",
                            duration: 5000,
                        });
                    }
                });
        },
        clickTag(key) {
            this.role = key;
        },
    },
    computed: mapState(["userInfo"]),
    mounted() { },
};
</script>

<style lang="less" scoped>
.remind {
    border-radius: 4px;
    padding: 10px 20px;
    display: flex;
    position: fixed;
    right: 20px;
    bottom: 50%;
    flex-direction: column;
    color: #606266;
    background-color: #fff;
    border-left: 4px solid #409eff;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

.container {
    margin-bottom: 32px;
}

.title-icon {
    font-size: 44px;
    margin-right: 10px;
}

.container .el-radio-group {
    margin: 30px 0px;
}

.register-link {
  text-align: right;
  margin-top: 10px;
  padding-right: 10px;
  span {
    color: #606266;
    font-size: 14px;
  }
}
  
  .register-text {
    color: #409EFF;
    margin-left: 5px;
    cursor: pointer;
    text-decoration: none;
    &:hover {
      color: #66b1ff;
    }
  }

a:link {
    color: #ff962a;
    text-decoration: none;
}

#login {
    font-size: 14px;
    color: #000;
    background-color: #fff;
}

#login .bg {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    overflow-y: auto;
    height: 100%;
    background: url("../../assets/img/login.jpg") center top / cover no-repeat;
    background-color: #b6bccdd1 !important;
}

#login .main-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

#login .main-container .top {
    margin-top: 100px;
    font-size: 30px;
    color: #ff962a;
    display: flex;
    justify-content: center;
}

#login .top .icon-kaoshi {
    font-size: 80px;
}

#login .top .title {
    margin-top: 20px;
}

#login .bottom {
    display: flex;
    justify-content: center;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

#login .bottom .title {
    text-align: center;
    font-size: 30px;
}

.bottom .container .title {
    margin: 30px 0px;
}

.bottom .submit .row-login {
    width: 100%;
    margin: 20px 0px 10px 0px;
    padding: 15px 20px;
    background-color: #409EFF;
    border-color: #409EFF;
    color: white
}

.bottom .submit {
    display: flex;
    justify-content: center;
}

.footer {
    margin-top: 50px;
    text-align: center;
}

.footer .msg1 {
    font-size: 18px;
    color: #fff;
    margin-bottom: 15px;
}

.footer .msg2 {
    font-size: 14px;
    color: #e3e3e3;
    margin-top: 70px;
}

.bottom .options {
    margin-bottom: 40px;
    color: #ff962a;
    display: flex;
    justify-content: space-between;
}

.bottom .options>a {
    color: #ff962a;
}

.bottom .options .register span:nth-child(1) {
    color: #8c8c8c;
}

</style>

