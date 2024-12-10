<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
<!-- 添加教师 -->
<template>
  <section class="add">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="姓名">
            <el-input v-model="form.teacherName"></el-input>
          </el-form-item>
          <el-form-item label="学院">
            <el-input v-model="form.institute"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-input v-model="form.sex"></el-input>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="form.tel"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.pwd"></el-input>
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="form.cardId"></el-input>
          </el-form-item>
          <el-form-item label="职称">
            <el-input v-model="form.type"></el-input>
          </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()" > 立即创建</el-button>
        <el-button type="danger" @click="cancel()" > 取消</el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script>
export default {
  data() {
    return {
      form: { //表单数据初始化
        studentName: "",
        grade: "",
        major: "",
        clazz: "",
        institute: "",
        tel: "",
        email: "",
        pwd: "",
        cardId: "",
        sex: "",
        role: 2
      }
    };
  },
  methods: {
    onSubmit() { //数据提交
      // 校验
      if (this.form.teacherName == "") {
        this.$message({
          message: '请输入教师姓名',
          type: 'error'
        })
        return
      }
      if (this.form.institute == "") {
        this.$message({
          message: '请输入学院',
          type: 'error'
        })
        return
      }
      if (this.form.sex == "") {
        this.$message({
          message: '请输入性别',
          type: 'error'
        })
        return
      }
      if (this.form.tel == "") {
        this.$message({
          message: '请输入电话号码',
          type: 'error'
        })
        return
      }
      if(this.form.tel.length > 11) {
        this.$message({
          message: '请输入正确的电话号码',
          type: 'error'
        })
        return
      }
      if (this.form.pwd == "") {
        this.$message({
          message: '请输入密码',
          type: 'error'
        })
        return
      }
      if (this.form.cardId == "") {
        this.$message({
          message: '请输入身份证号码',
          type: 'error'
        })
        return
      }
      if(this.form.cardId.length > 18) {
        this.$message({
          message: '请输入正确的身份证号码',
          type: 'error'
        })
        return
      }
      if (this.form.type == "") {
        this.$message({
          message: '请输入职称',
          type: 'error'
        })
        return
      }
      this.$axios({
        url: '/api/teacher',
        method: 'post',
        data: {
          ...this.form
        }
      }).then(res => {
        if(res.data.code == 200) {
          this.$message({
            message: '数据添加成功',
            type: 'success'
          })
          this.$router.push({path: '/teacherManage'})
        }
      })
    },
    cancel() { //取消按钮
      this.form = {}
    },
    
  }
};
</script>
<style lang="less" scoped>
.add {
  padding: 0px 40px;
  width: 400px;
}
</style>

