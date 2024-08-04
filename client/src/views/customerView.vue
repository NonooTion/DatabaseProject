<template>
<el-container class="customerContainer">
    <el-header class="customerHeader" >
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
      <el-menu-item index="/customerInfo">个人信息</el-menu-item>
      <el-menu-item index="/membership">会员信息</el-menu-item>
    </el-sub-menu>

      <el-sub-menu index="2">
      <template #title>Workspace</template>
      <el-menu-item>item one</el-menu-item>
      <el-menu-item>item two</el-menu-item>
      <el-menu-item>item three</el-menu-item>
      <el-sub-menu index="2-1">
        <template #title>item four</template>
        <el-menu-item>item one</el-menu-item>
        <el-menu-item>item two</el-menu-item>
        <el-menu-item>item three</el-menu-item>
      
        </el-sub-menu>
    </el-sub-menu>
     
    <el-menu-item index="/login" style="width:100px">退出登录</el-menu-item>
  </el-menu>
    </el-header>
    <el-main class="customerMain">
      <router-view></router-view>
    </el-main>
</el-container>

</template>

<script>

</script>

<style>
.customerContainer{
    background-image: url("../assets/customerBackgroud.jpg");
    background-size: 100% 100%;
    padding: 0px;
    margin: 0px;
}
.customerHeader{
    padding:0px;
    margin:0px

}
.customerMain{
    height: 93vh;
    padding:0px;
}
</style>