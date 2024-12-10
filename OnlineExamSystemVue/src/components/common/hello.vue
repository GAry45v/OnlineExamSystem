<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-03-08 20:38:49
-->
<template>
    <div class="index">
        <div class="hello">
            <i
                class="iconfont icon-r-user2"
                style="font-size: 34px;"
            ></i>
            <span> 很高兴遇见你, {{ user.userName }}。</span>
        </div>
        <div style="margin-top: 20px; font-size: 24px">
            <h1>欢迎登录在线考试系统后台</h1>
            <br />
            <img
                :src="require('@/assets/img/admin_bg.jpg')"
                style="margin-top: 10px; width: 60vw"
            />
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            user: {
                //用户信息
                userName: null,
                userId: null,
            },
        };
    },
    created() {
        this.getUserInfo();
    },
    methods: {
        getUserInfo() {
            //获取用户信息
            let userName = this.$cookies.get("cname");
            let userId = this.$cookies.get("cid");
            this.user.userName = userName;
            this.user.userId = userId;
        },
    },
};
</script>


<style lang="less" scoped>
.index {
    margin-left: 70px;
    .hello {
        font-size: 28px;
        color: #726f70;
    }
    .msg {
        .title {
            font-size: 16px;
            color: #000;
            margin-top: 20px;
            margin-left: 10px;
        }
        ul {
            display: flex;
            flex-direction: column;
            width: 200px;
            overflow: hidden;
        }
        li {
            margin-top: 10px;
            font-size: 14px;
            color: lightcoral;
            cursor: pointer;
            display: inline-block;
        }
    }
}
</style>

