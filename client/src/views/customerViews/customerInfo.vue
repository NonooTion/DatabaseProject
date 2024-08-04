<template>
    
    <el-container style="background-color: #e3e0f3; height: 100%;">
        <div style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%; width: 100%;">
            <div style="">
                <h1 style="margin-top: 5%; margin-left: 5%; font-family: KAITI;">个人信息</h1>
                <el-form ref="form" :model="userInfo" label-width="80px" labelPosition="top" style="margin-left: 5%;">
                    <el-form-item label="用户名:" prop="userName">
                        <el-input v-model="userInfo.userName" placeholder="请输入用户名" style="width:30%;" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="姓名:" prop="name">
                        <el-input v-model="userInfo.name" placeholder="请输入姓名" style="width:30%;" ></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-select v-model="userInfo.gender" placeholder="请选择性别" style="width:30%;">
                        <el-option value="男">男</el-option>
                        <el-option value="女">女</el-option>
                        <el-option value="其他">其他</el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="联系方式:" prop="tel">
                        <el-input v-model="userInfo.tel" placeholder="请输入联系方式" style="width:30%;"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱:" prop="email">
                        <el-input v-model="userInfo.email" placeholder="请输入邮箱" style="width:30%;"></el-input>
                    </el-form-item>
                    
                    <el-form-item >
                        <el-button type="primary" style="margin-left: 25%;" @click="handleSave()">保存</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </el-container>
        

</template>

<script>
    export default{
        data(){
            return{
                userInfo:{
                    userId:-1,
                    userName:'',
                    name:'',
                    gender:'',
                    tel:'',
                    userType:'客户',
                    email:''
                },
                customerUrl:this.$path.customerUrl,
            }
        },
        methods:{
            handleSave()
            {
            let url=this.customerUrl;
            let params=this.userInfo;
            this.$axios.put(url,params).then(
            (res)=>{
            if(res.data.code=this.$code.UPDATE_SUCCESS)
            this.$message({type:"success",message:res.data.message})
            else if(res.data.code=this.$code.UPDATE_FAILURE)
            this.$message({type:"error",message:res.data.message})
            }
            ).catch(
            (error)=>{
            this.$alert(error)
            this.$message({type:"error",message:"系统错误，请联系管理员"})
            })
            }
        },
        beforeMount(){
            let url=this.customerUrl+"/findById"
            let params={
                userId: sessionStorage.getItem('userId')
            }
            this.$axios.get(url,{params:params}).then(
                (res)=>
                {
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.userInfo=res.data.data
                    }
                    else if(res.data.code===this.$code.SELECT_FAILURE){
                        this.$alert("系统异常，请稍后再试")
                    }
                }
            ).catch(
                    (error)=>{
                        this.$alert("服务器异常，请稍后再试")
                    }
                )
        }
    }
</script>

<style>

</style>