//查询所有考试
<template>
  <div class="exam">
    <el-table :data="pagination.records" border>
      <el-table-column
        fixed="left"
        prop="source"
        label="试卷名称"
        width="180"
      ></el-table-column>
      <el-table-column
        prop="description"
        label="介绍"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="institute"
        label="所属学院"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="major"
        label="所属专业"
        width="200"
      ></el-table-column>
      <el-table-column prop="grade" label="年级" width="100"></el-table-column>
      <el-table-column
        prop="examDate"
        label="考试日期"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="totalTime"
        label="持续时间"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="totalScore"
        label="总分"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="type"
        label="试卷类型"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="tips"
        label="考生提示"
        width="400"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="420">
        <template slot-scope="scope">
          <el-button type="success"  @click="getExamDetail(scope.row.examCode, scope.row.paperId)"
            >
            试题详情</el-button
          >
          <el-button
            @click="edit(scope.row.examCode)"
            type="primary"
            
            > 编辑</el-button
          >
          <el-button
            @click="deleteRecord(scope.row.examCode)"
            type="danger"
            
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
      :page-sizes="[4, 8, 10]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      class="page"
    >
    </el-pagination>
    <!-- 编辑对话框-->
    <el-dialog
      title="编辑试卷信息"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <section class="update">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="试卷名称">
            <el-input v-model="form.source"></el-input>
          </el-form-item>
          <el-form-item label="介绍">
            <el-input v-model="form.description"></el-input>
          </el-form-item>
          <el-form-item label="所属学院">
            <el-input v-model="form.institute"></el-input>
          </el-form-item>
          <el-form-item label="所属专业">
            <el-input v-model="form.major"></el-input>
          </el-form-item>
          <el-form-item label="年级">
            <el-input v-model="form.grade"></el-input>
          </el-form-item>
          <el-form-item label="考试日期">
            <el-col :span="11">
              <el-date-picker
                type="date"
                placeholder="选择日期"
                v-model="form.examDate"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              ></el-date-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="持续时间（分钟）">
            <el-input v-model="form.totalTime" type="number" oninput="if(value<=0)value=''" :min="1"></el-input>
          </el-form-item>
          <!-- <el-form-item label="总分">
            <el-input v-model="form.totalScore"></el-input>
          </el-form-item> -->
          <el-form-item label="试卷类型">
            <el-input v-model="form.type"></el-input>
          </el-form-item>
          <el-form-item label="考生提示">
            <el-input type="textarea" v-model="form.tips"></el-input>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" style="font-size: 20px;"> 取 消</el-button>
        <el-button type="primary" @click="submit()"> 确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查询试卷对话框 -->
    <el-dialog title="试卷内容" :visible.sync="showExamDetail" width="60%">
      <span>
        <div class="content">
          <el-collapse v-model="activeName">
            <el-collapse-item class="header" name="0">
              <template slot="title">
                <div class="title">
                  <span>{{ examData.source }}</span
                  ><i class="header-icon el-icon-info"></i>
                  <span class="time"
                    >{{ score[0] + score[1] + score[2] }}分 /
                    {{ examData.totalTime }}分钟</span
                  >
                </div>
              </template>
              <el-collapse class="inner">
                <el-collapse-item>
                  <template slot="title">
                    <div class="titlei">
                      选择题 (共{{ topicCount[0] }}题 共计{{ score[0] }}分)
                    </div>
                  </template>
                  <div class="contenti">
                    <ul
                      class="question"
                      v-for="(list, index) in topic[1]"
                      :key="index"
                    >
                      <li>
                        {{ index + 1 }}. {{ list.question }} {{ list.score }}分
                        <a style="color: red" href="#" @click.prevent="deleteQuestion(1, list.questionId)">删除</a>
                      </li>
                    </ul>
                  </div>
                </el-collapse-item>
                <el-collapse-item>
                  <template slot="title">
                    <div class="titlei">
                      填空题 (共{{ topicCount[1] }}题 共计{{ score[1] }}分)
                    </div>
                  </template>
                  <div class="contenti">
                    <ul
                      class="question"
                      v-for="(list, index) in topic[2]"
                      :key="index"
                    >
                      <li>
                        {{ topicCount[0] + index + 1 }}.{{ list.question }}
                        {{ list.score }}分
                        <a style="color: red" href="#" @click.prevent="deleteQuestion(2, list.questionId)">删除</a>
                      </li>
                    </ul>
                  </div>
                </el-collapse-item>
                <el-collapse-item>
                  <template slot="title">
                    <div class="titlei">
                      判断题 (共{{ topicCount[2] }}题 共计{{ score[2] }}分)
                    </div>
                  </template>
                  <div class="contenti">
                    <ul
                      class="question"
                      v-for="(list, index) in topic[3]"
                      :key="index"
                    >
                      <li>
                        {{ topicCount[0] + topicCount[1] + index + 1 }}.
                        {{ list.question }} {{ list.score }}分
                        <a style="color: red" href="#" @click.prevent="deleteQuestion(3, list.questionId)">删除</a>
                      </li>
                    </ul>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </el-collapse-item>
          </el-collapse>
        </div>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showExamDetail = false" style="font-size: 20px;"> 取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {}, //保存点击以后当前试卷的信息
      pagination: {
        //分页后的考试信息
        current: 1, //当前页
        total: null, //记录条数
        size: 4, //每页条数
      },
      dialogVisible: false,
      showExamDetail: false, //是否展示试卷
      activeName: '0',  //默认打开序号
      topicCount: [],//每种类型题目的总数
      score: [],  //每种类型分数的总数
      examData: { //考试信息
        // source: null,
        // totalScore: null,
      },
      topic: {  //试卷信息

      },
      examDetailCode: "",   // 当前查看试题详情中，考试编码code
      examDetailPaperId: "",    // 当前查看的试题详情中，试卷id
    };
  },
  created() {
    this.getExamInfo();
    
  },
  methods: {
    deleteQuestion(type, questionId) {
      // type: 1选择 2填空 3判断
      var paperId = this.examDetailPaperId;
      this.$confirm("确认从试卷中移除该试题？")
        .then((_) => {
          this.$axios(`/api/paper/delete/${paperId}/${type}/${questionId}`).then(res => {  //通过examCode请求试卷详细信息
            this.getExamDetail(this.examDetailCode, paperId);
          })  
        })
        .catch((_) => {});
    },
    getExamDetail(examCode, paperId) {
      this.examDetailCode = examCode;
      this.examDetailPaperId = paperId;
      this.topicCount = [];
      this.score = [];
      this.examData = {},
      this.topic = {},

      this.showExamDetail = true;

      this.$axios(`/api/exam/${examCode}`).then(res => {  //通过examCode请求试卷详细信息
        res.data.data.examDate = res.data.data.examDate.substr(0,10)
        this.examData = { ...res.data.data}
        let paperId = this.examData.paperId
        this.$axios(`/api/paper/${paperId}`).then(res => {  //通过paperId获取试题题目信息
          this.topic = {...res.data}
          let keys = Object.keys(this.topic) //对象转数组
          keys.forEach(e => {
            let data = this.topic[e]
            this.topicCount.push(data.length)
            let currentScore = 0
            for(let i = 0; i< data.length; i++) { //循环每种题型,计算出总分
              currentScore += data[i].score
            }
            this.score.push(currentScore) //把每种题型总分存入score
          })
        })
      })
    },
    edit(examCode) {
      //编辑试卷
      this.dialogVisible = true;
      this.$axios(`/api/exam/${examCode}`).then((res) => {
        //根据试卷id请求后台
        if (res.data.code == 200) {
          this.form = res.data.data;
        }
      });
    },
    handleClose(done) {
      //关闭提醒
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    submit() {
      //提交修改后的试卷信息
      if (this.form.source == null || this.form.source == "") {
                this.$message({
                    message: "试卷名称不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.description == null || this.form.description == "") {
                this.$message({
                    message: "介绍不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.institute == null || this.form.institute == "") {
                this.$message({
                    message: "所属学院不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.major == null || this.form.major == "") {
                this.$message({
                    message: "所属专业不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.grade == null || this.form.grade == "") {
                this.$message({
                    message: "年级不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.examDate == null || this.form.examDate == "") {
                this.$message({
                    message: "考试日期不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.totalTime == null || this.form.totalTime == "") {
                this.$message({
                    message: "持续时间不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.type == null || this.form.type == "") {
                this.$message({
                    message: "考试类型不能为空",
                    type: "error",
                });
                return;
            }
            if (this.form.tips == null || this.form.tips == "") {
                this.$message({
                    message: "考生提示不能为空",
                    type: "error",
                });
                return;
            }
      this.dialogVisible = false;
      
      this.$axios({
        url: "/api/exam",
        method: "put",
        data: {
          ...this.form,
        },
      }).then((res) => {
        if (res.data.code == 200) {
          this.$message({
            //成功修改提示
            message: "更新成功",
            type: "success",
          });
        }
        this.getExamInfo();
      });
    },
    deleteRecord(examCode) {
      this.$confirm("确定删除该记录吗,该操作不可逆！！！", "删除提示", {
        confirmButtonText: "确定删除",
        cancelButtonText: "取消",
        type: "danger",
      })
        .then(() => {
          //确认删除
          this.$axios({
            url: `/api/exam/${examCode}`,
            method: "delete",
          }).then((res) => {
            this.getExamInfo();
          });
        })
        .catch(() => {});
    },
    getExamInfo() {
      //分页查询所有试卷信息
      this.$axios(
        `/api/exams/${this.pagination.current}/${this.pagination.size}`
      )
        .then((res) => {
          this.pagination = res.data.data;
        })
        .catch((error) => {});
    },
    //改变当前记录条数
    handleSizeChange(val) {
      this.pagination.size = val;
      this.getExamInfo();
    },
    //改变当前页码，重新发送请求
    handleCurrentChange(val) {
      this.pagination.current = val;
      this.getExamInfo();
    },
  },
};
</script>
<style lang="less" scoped>
.exam {
  padding: 0px 40px;
  .page {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .edit {
    margin-left: 20px;
  }
}
</style>
