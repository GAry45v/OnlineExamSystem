<template>
  <section class="course-list">
    <!-- 当有课程数据时，显示课程表 -->
    <el-table v-if="courses.length > 0" :data="courses" border style="width: 100%">
      <!-- 课程ID -->
      <el-table-column prop="courseId" label="课程ID" width="120"></el-table-column>
      <!-- 课程名称 -->
      <el-table-column prop="courseName" label="课程名称" width="200"></el-table-column>
      <!-- 学期 -->
      <el-table-column prop="semester" label="学期" width="180"></el-table-column>
      <!-- 创建者工号 -->
      <el-table-column prop="createdByEmployeeNumber" label="教师工号" width="200"></el-table-column>
      <!-- 操作列 -->
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button
            type="danger"
            plain
            size="small"
            @click="confirmDelete(scope.row.courseId)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 当没有课程数据时，显示空状态提示 -->
    <div v-else class="empty-message">
      <el-empty description="暂无创建的课程"></el-empty>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      courses: [], // 存储课程列表
      mockData: [ // 模拟数据
        {
          courseId: "1001",
          courseName: "高等数学",
          semester: "2023-2024学年第一学期",
          createdByEmployeeNumber: "T2001"
        },
        {
          courseId: "1002",
          courseName: "线性代数",
          semester: "2023-2024学年第一学期",
          createdByEmployeeNumber: "T2001"
        },
        {
          courseId: "1003",
          courseName: "概率论",
          semester: "2023-2024学年第二学期",
          createdByEmployeeNumber: "T2002"
        }
      ],
      useMockData: true // 控制是否使用模拟数据
    };
  },
  created() {
    this.fetchCourses(); // 页面加载时调用
  },
  methods: {
    fetchCourses() {
      if (this.useMockData) {
        // 使用模拟数据
        this.courses = this.mockData;
        return;
      }

      // 实际的API调用
      this.$axios
        .get("/course/teacher/search_course")
        .then((response) => {
          const { code, data, message } = response.data;
          if (code === 200) {
            this.courses = data;
            if (data.length === 0) {
              this.$message.info("该教师未创建任何课程");
            }
          } else {
            this.$message.error("获取课程失败：" + message);
          }
        })
        .catch((error) => {
          this.$message.error("获取课程失败：" + error.message);
        });
    },

    confirmDelete(courseId) {
      // 删除前确认
      this.$confirm(`确定要删除课程 ID 为 ${courseId} 的课程吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteCourse(courseId);
        })
        .catch(() => {
          this.$message.info("取消删除");
        });
    },

    deleteCourse(courseId) {
      // 实际的删除逻辑
      this.$axios
        .delete(`/course/teacher/delete_course/courseId`, { params: { courseId } })
        .then((response) => {
          const { code, message } = response.data;
          if (code === 200) {
            this.$message.success("课程删除成功");
            this.courses = this.courses.filter((course) => course.courseId !== courseId);
          } else {
            this.$message.error("课程删除失败：" + message);
          }
        })
        .catch((error) => {
          this.$message.error("课程删除失败：" + error.message);
        });
    },
  },
};
</script>

<style lang="less" scoped>
.course-list {
  padding: 20px 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.empty-message {
  margin-top: 20px;
  text-align: center;
}

// 样式优化
.el-table {
  margin-top: 20px;
}

.el-table th {
  background-color: #f5f7fa;
}

.el-table td, .el-table th {
  padding: 12px 0;
}
</style>
