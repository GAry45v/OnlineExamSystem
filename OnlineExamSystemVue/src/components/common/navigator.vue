<template>
  <div id="nav">
    <!-- 面包屑已移除 -->
    <!-- 保留父容器，用于后续布局扩展 -->
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  data() {
    return {
      active: [], // 保留变量，防止其他地方可能用到
      index1: null, // 保留变量，防止影响逻辑
    };
  },
  computed: mapState(["menu"]), // 保留 Vuex 状态绑定
  methods: {
    getIndex() {
      // 保留事件监听，防止与其他组件交互逻辑被破坏
      this.bus.$on('sendIndex', (data) => {
        this.index1 = data; // 更新当前索引
        this.active = this.menu[data - 1]; // 根据索引更新 active 数据
      });
    },
  },
  created() {
    this.getIndex(); // 组件加载时注册事件监听
  },
  beforeDestroy() {
    this.bus.$off('sendIndex'); // 组件销毁时注销事件，防止内存泄漏
  },
};
</script>

<style scoped>
/* 清理面包屑相关样式，保留父容器样式 */
#nav {
  height: 56.95px; /* 固定高度占位，原面包屑高度 */
  padding-left: 20px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08); /* 保留整体外观样式 */
}

/* 面包屑相关样式已删除 */
</style>
