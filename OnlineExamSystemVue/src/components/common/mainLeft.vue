<template>
  <div id="left">
    <el-menu
      active-text-color="lightgrey" 
      text-color="#000" 
      :default-active="this.$route.path"
      class="el-menu-vertical-demo" 
      @open="handleOpen" 
      @close="handleClose" 
      :collapse="flag"
      background-color="#304156"
      menu-trigger="click" 
      router>
      <el-submenu v-for="(item,index) in menu" :index='item.index' :key="index">
        <template slot="title">
          <div class="el-item-menu left-width">
            <i class="iconfont" :class="item.icon"></i>
            <span slot="title" class="title">{{item.title}}</span>
          </div>
        </template>
        <el-menu-item-group v-for="(list,index1) in item.content" :key="index1">
          <el-menu-item
            @click="handleTitle(item.index)" 
            :index="list.path" 
            v-if="list.item1!= null">
            <i :class="list.icon"></i>
            <span>{{list.item1}}</span>
          </el-menu-item>
          <el-menu-item 
            @click="handleTitle(item.index)" 
            :index="list.path" 
            v-if="list.item2 != null">
            <i :class="list.icon"></i>
            <span>{{list.item2}}</span>
          </el-menu-item>
          <el-menu-item 
            @click="handleTitle(item.index)" 
            :index="list.path" 
            v-if="list.item3 != null">
            <i :class="list.icon"></i>
            <span>{{list.item3}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: "mainLeft",
  computed: mapState(["flag", "menu"]),
  created() {
    this.addData();
  },
  methods: {
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleTitle(index) {
      this.bus.$emit('sendIndex', index);
    },
    addData() {
      let role = this.$cookies.get("role");
      if (role == 0) {
        this.menu.push({
          index: '5',
          title: '教师管理',
          icon: 'icon-r-user3',
          content: [
            {
              item1: '教师管理',
              path: '/teacherManage',
              icon: "iconfont icon-r-user3"
            },
            {
              item2: '添加教师',
              path: '/addTeacher',
              icon: "iconfont icon-r-add"
            }
          ]
        });
      }
    }
  }
}
</script>

<style scoped>
.el-menu-vertical-demo .el-submenu__title {
  overflow: hidden;
}

.left-width .iconfont {
  font-size: 18px;
  color: #fff;
}

.left-width {
  width: 200px; /* 与顶部栏宽度一致 */
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  min-height: 900px;
}

#left {
  width: 200px; /* 与顶部栏left值一致 */
  height: 100vh;
  background-color: #304156;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

#left .el-menu-vertical-demo .title {
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  margin-left: 14px;
}

.el-submenu {
  border-bottom: 1px solid rgba(238, 238, 238, 0.06);
}

.el-submenu__title:hover {
  background-color: #263445 !important;
}

.el-submenu__title i {
  color: #fbfbfc !important;
}

.el-menu-item {
  color: #bfcbd9 !important;
}

.el-menu-item:hover {
  background-color: #263445 !important;
}

.el-menu-item.is-active {
  background-color: #263445 !important;
  color: #409EFF !important;
}

.el-menu-item i {
  color: #bfcbd9;
  font-size: 12px;
  margin-right: 10px;
}

.el-menu-item span {
  font-size: 12px;
}
</style>
