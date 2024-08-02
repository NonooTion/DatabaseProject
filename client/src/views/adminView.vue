<template>
  <div>
  <el-container>
  <el-header class="header" style="background-color: #22232f;">
        <img src="../assets/icon.jpeg" alt="" style="width: 50px; top: 5px; position:relative">
        <span class="title" >健身房管理系统</span>
  </el-header>
    <el-container >
    <el-aside style="background-color: #31323f; min-height: 93vh;">
      <el-menu
        active-text-color="#7fa4ff"
        background-color="#31323f"
        text-color="#fff"
        router
      >
            <el-sub-menu index="1">
                <template #title>  
                    <i class="el-icon-menu"></i>
                    <span>人员管理</span>  
                </template>  
                <el-menu-item index="/customerManagement">客户管理</el-menu-item>
                <el-menu-item index="/coachManagement">教练管理</el-menu-item>
                <el-menu-item index="/">选项2</el-menu-item>
            </el-sub-menu>
        </el-menu>
    </el-aside>
    <el-main class="main">
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
.main{
  padding: 0px;
  display: flex;
  background-image: url(../assets/Backgroud.jpg);
  background-size: cover;
  width: 100%;
  position: relative;
  overflow: hidden;
}
</style>