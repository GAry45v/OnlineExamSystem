<template>
    <section class="create">
      <el-form ref="form" :model="form" label-width="120px">
        <!-- 课程名称 -->
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName"></el-input>
        </el-form-item>
        <!-- 学期 -->
        <el-form-item label="学期">
          <el-select v-model="form.semester" placeholder="请选择学期">
            <el-option label="2025/2026-2" value="2025/2026-2"></el-option>
            <el-option label="2025/2026-1" value="2025/2026-1"></el-option>
            <el-option label="2024/2025-2" value="2024/2025-2"></el-option>
            <el-option label="2024/2025-1" value="2024/2025-1"></el-option>
          </el-select>
        </el-form-item>
        <!-- 按钮 -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button type="danger" @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </section>
  </template>
  
  <script>
  export default {
    data() {
      return {
        form: {
          courseName: null, // 课程名称
          semester: null, // 学期
        },
      };
    },
    methods: {
      validateForm() {
        // 表单验证
        if (!this.form.courseName || this.form.courseName.trim() === "") {
          this.$message.error("课程名称不能为空");
          return false;
        }
        if (!this.form.semester) {
          this.$message.error("请选择学期");
          return false;
        }
        return true;
      },
      onSubmit() {
        // 提交表单
        if (!this.validateForm()) return;
  
        this.$axios({
          url: "/course/teacher/create_course",
          method: "post",
          data: {
            courseName: this.form.courseName,
            semester: this.form.semester,
          },
        }).then((res) => {
          if (res.data.code === 200) {
            this.$message.success("课程创建成功");
            this.$router.push({ path: "/courseList" }); // 跳转到课程列表页面
          } else {
            this.$message.error("课程创建失败：" + res.data.message);
          }
        });
      },
      cancel() {
        // 重置表单
        this.form = {
          courseName: null,
          semester: null,
        };
      },
    },
  };
  </script>
  
  <style lang="less" scoped>
  .create {
    padding: 0px 40px;
    width: 400px;
  }
  </style>
  