<template>
  <div>
  <el-container style="height: 100px;">
  <el-header class="header" style="background-color: #22232f;" >
        <img src="../assets/icon.jpeg" alt="" style="width: 50px; top: 5px; position:relative">
        <span class="title" >健身房管理系统</span>
  </el-header>
    <el-container>
    <el-aside class="menu" style="background-color: #31323f; min-height: 93vh;">
      <el-menu
        active-text-color="#cc7f2b"
        background-color="#31323f"
        text-color="#bdc7cc"
        :collapse="false"
        router
      >
            <el-sub-menu index="1">
                <template #title>  
                    <i class="el-icon-menu"></i>
                    <span>人员管理</span>  
                </template>  
                <el-menu-item index="/customerManagement">客户管理</el-menu-item>
                <el-menu-item index="/coachManagement">教练管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="2">
                <template #title>  
                    <i class="el-icon-menu"></i>
                    <span>会员管理</span>  
                </template>  
                <el-menu-item index="/membershipManagement">会员卡信息管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <span>健身房信息管理</span>
              </template>
              <el-menu-item index="/practiceAreaManagement">练习区域管理</el-menu-item>
              <el-sub-menu index="3-1">
                <template #title>
                <span>课程管理</span>
              </template>
              <el-menu-item index="/privateCoachManagement">私教管理</el-menu-item>
                <el-menu-item index="/groupClassManagement">团课管理</el-menu-item>
                <el-menu-item index="/customerCourseManagement">团课选课管理</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            
            <el-sub-menu index="4">
              <template #title>
                <span>财务管理</span>
              </template>
                <el-menu-item index="/transaction">交易信息管理</el-menu-item>
                <el-menu-item index="/transactionAnalyze">交易信息分析</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/">登出</el-menu-item>
        </el-menu>
    </el-aside>
    <el-main class="adminMain">
        <router-view/>
    </el-main>
  </el-container>
</el-container>
</div>
</template>


<style>
.title{
  font-size: 20px;
  margin-left: 20px;
  color: whitesmoke;
}
.headerTitle{
    font-size: 40px;
    display: flex;
    align-content: center;
    justify-content: center;
}
.adminMain{
  height: calc(100vh - 4rem);
  background-image: url(../assets/adminBackgroud.jpg);
  padding: 0px;
  display: flex;
  background-size: 100% 100%;
  width: 100%;
}
</style>