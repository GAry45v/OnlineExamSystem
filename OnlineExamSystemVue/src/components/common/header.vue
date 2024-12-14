<template>
    <header id="topbar">
      <el-dialog
        :append-to-body="true"
        title="修改密码"
        :visible.sync="dialogVisible"
        width="30%"
      >
        <el-form status-icon ref="ruleForm2" label-width="100px" class="demo-ruleForm">
          <el-form-item label="旧密码" prop="pass">
            <el-input type="password" v-model="oldPsw" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="pass">
            <el-input type="password" v-model="newPsw" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="checkPass">
            <el-input type="password" v-model="confirmNewPsw" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="resetPsw">确 定</el-button>
        </span>
      </el-dialog>
  
      <div class="topbar-container">
        <div class="breadcrumb-container">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
              {{ item }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="user">
          <span>{{ user.userName }}</span>
          <img
            src="@/assets/img/userimg.png"
            class="user-img"
            ref="img"
            @click="showSetting()"
          />
          <transition name="fade">
            <div class="out" ref="out" v-show="login_flag">
              <ul>
                <li><a @click="dialogVisible = true" href="javascript:;">修改密码</a></li>
                <li class="exit" @click="exit()">
                  <a href="javascript:;">退出登录</a>
                </li>
              </ul>
            </div>
          </transition>
        </div>
      </div>
    </header>
  </template>
  
  <script>
  import { mapState, mapMutations } from "vuex";
  
  export default {
    data() {
      return {
        login_flag: false,
        user: {
          userName: null,
          userId: null,
        },
        dialogVisible: false,
        oldPsw: "",
        newPsw: "",
        confirmNewPsw: "",
        role: 0,
        breadcrumbList: [],
      };
    },
    created() {
      this.getUserInfo();
      this.role = this.$cookies.get("role");
    },
    computed: mapState(["flag", "menu"]),
    watch: {
      $route: {
        handler: function (to) {
          this.updateBreadcrumb(to);
        },
        immediate: true,
      },
    },
    methods: {
      resetPsw() {
        if (this.oldPsw == "") {
          this.$message("请输入旧密码");
          return;
        }
        if (this.newPsw == "") {
          this.$message("请输入新密码");
          return;
        }
        if (this.confirmNewPsw != this.newPsw) {
          this.$message("两次新密码不一致");
          return;
        }
        this.$axios(
          `/api/admin/resetPsw/${this.user.userId}/${this.oldPsw}/${this.newPsw}`
        ).then((res) => {
          let status = res.data.code;
          if (status == 200) {
            if (res.data.data != true) {
              this.$message(res.data.data);
            } else {
              this.$message("修改成功");
              this.dialogVisible = false;
              this.oldPsw = "";
              this.newPsw = "";
              this.confirmNewPsw = "";
            }
          }
        });
      },
      showSetting() {
        this.login_flag = !this.login_flag;
      },
      ...mapMutations(["toggle"]),
      getUserInfo() {
        let userName = this.$cookies.get("cname");
        let userId = this.$cookies.get("cid");
        this.user.userName = userName;
        this.user.userId = userId;
      },
      exit() {
        let role = this.$cookies.get("role");
        this.$router.push({ path: "/" });
        this.$cookies.remove("cname");
        this.$cookies.remove("cid");
        this.$cookies.remove("role");
        this.$cookies.remove("rb_token");
        this.$cookies.remove("rb_role");
        if (role == 0) {
          this.menu.pop();
        }
      },
      updateBreadcrumb(route) {
        let currentPath = [];
        this.menu.forEach((menuItem) => {
          if (menuItem.content) {
            menuItem.content.forEach((subItem) => {
              if (subItem.path === route.path) {
                currentPath = [menuItem.title];
                if (subItem.item1) currentPath.push(subItem.item1);
                if (subItem.item2) currentPath.push(subItem.item2);
                if (subItem.item3) currentPath.push(subItem.item3);
              }
            });
          }
        });
        this.breadcrumbList = currentPath;
      },
    },
  };
  </script>
  
  <style scoped>
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s;
  }
  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
  
  #topbar {
    position: fixed;
    top: 0;
    left: 200px; /* 配合左侧导航栏宽度 */
    width: calc(100% - 200px); /* 避免覆盖左侧导航栏 */
    z-index: 1000;
    background-color: #fff;
    height: 56.95px;
    line-height: 56.95px;
    border-bottom: 1px solid #e6e6e6;
  }
  
  .topbar-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 100%;
    padding: 0 20px;
  }
  
  .breadcrumb-container {
    flex: 1;
  }
  
  .el-breadcrumb {
    line-height: 50px;
    font-size: 14px;
  }
  
  .user {
  position: relative;
  display: flex;
  align-items: center;
  z-index: 2000; /* 确保头像层级 */
}

.user-img {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  cursor: pointer;
  transition: transform 0.3s;
}

.user-img:hover {
  transform: scale(1.1);
}

.out {
  font-size: 14px;
  position: absolute;
  top: 50px;
  right: 0;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  padding: 8px 0;
  z-index: 2100; /* 弹出菜单层级 */
  width: 120px; /* 设置足够的宽度 */
}

.out ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.out ul > li {
  padding: 0 16px;
  height: 36px; /* 每一项的高度 */
  line-height: 36px; /* 文字垂直居中 */
  cursor: pointer;
  white-space: nowrap; /* 禁止文字换行 */
}

.out ul > li:hover {
  background-color: #f5f7fa; /* 悬停背景色 */
}

.out a {
  text-decoration: none;
  color: #606266;
  display: block;
  white-space: nowrap; /* 防止换行 */
  font-size: 14px;
}

.el-dialog {
  z-index: 2200 !important;
}

.el-dialog__wrapper {
  z-index: 2200 !important;
}
</style>
  