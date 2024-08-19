<template>
    <el-container class="coachContainer">
        <el-header class="coachHeader" >
        <el-menu
        active-text-color="#cc7f2b"
        background-color="#31323f"
        text-color="#bdc7cc"
        :collapse="false"
        mode="horizontal"
        router
        >
        <div index="0" style="padding:0px " >
          <img
            style="padding-left: 2px; padding-top: 1px; height: 58px;"
            src="../assets/icon.jpeg"
            alt="Element logo"
          />
        </div>
          <el-sub-menu index="1">
          <template #title>个人中心</template>
          <el-menu-item index="/personalInfo">个人信息</el-menu-item>
          <el-menu-item index="/coachInfo">教练信息</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/coachSchedule">今日日程</el-menu-item>
         
        <el-menu-item index="/login" style="width:100px">退出登录</el-menu-item>
      </el-menu>
        </el-header>
        <el-main class="coachMain">
          <router-view></router-view>
        </el-main>
    </el-container>
    
    </template>
    
    <script>
    
    </script>
    
    <style>
    .coachContainer{
        background-image: url("../assets/customerBackgroud.jpg");
        background-size: 100% 100%;
        padding: 0px;
        margin: 0px;
    }
    .coachHeader{
        padding:0px;
        margin:0px
    
    }
    .coachMain{
        height: 93vh;
        padding:0px;
    }
    </style>