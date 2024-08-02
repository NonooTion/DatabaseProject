<template>
    <div class="reset">
		<el-form :model="userForm" class="reset-container" :rules="rules" ref="userForm">
			<h2>请输入信息</h2>
			<el-form-item label="账号:" prop="userName">
				<el-input type="text" v-model="userForm.userName" placeholder="登录账号" ></el-input>
			</el-form-item>
            <el-form-item label="电话号码:" prop="tel">
				<el-input type="text" v-model="userForm.tel" placeholder="电话号码" ></el-input>
			</el-form-item>
            <el-form-item label="新密码:" prop="password">
				<el-input type="password" v-model="userForm.password" placeholder="请输入新的密码" ></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" style="width:100%;" @click="submitForm()">重置密码</el-button>
			</el-form-item>
            <el-row style="text-align: center;margin-top:-10px">
				<el-link type="primary" @click="returnToLogin()">返回</el-link>
			</el-row>
		</el-form>
	</div>
</template>

<script>
    export default{
        data()
        {
            return{
                userForm:{
                    userName:'',
                    password:'',
                    tel:''
                },
                rules:{
                userName:[
                { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password:[
                { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                tel:[
                { required: true, message: '请输入手机号', trigger: 'blur' }
                ]
            }
            }
            
        },
        methods:{
            doReset(){
                let url=this.$path.resetUrl
                let param=this.userForm
                this.$axios.put(url,param).then(res=>{
                    if(res.data.code==this.$code.RESET_SUCCESS){
                        this.$message({
                            type:'success',
                            message:res.data.message
                        });
                        this.returnToLogin();
                    }
                    else
                    {
                        this.$message({
                        type:'error',
                        message:res.data.message
                    })
                    }
                })
            },
            submitForm(){
                this.$refs.userForm.validate((valid)=>
            {
                if(valid) {
                    this.doReset()
                }
                else{
                    this.$message({type:"error",message:"请根据要求填写所需信息"})
                }
            })
            },
            returnToLogin()
            {
                this.$router.push("/login")
            }
        }
    }
</script>

<style>
    .reset{
        background-image: url("../assets/loggingBackgroud.jpg");   
    	background-position: center;
		background-size: cover;
    	display: flex;  
    	justify-content: center;  
    	align-items: center;
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
    }
    .reset-container{
		border-radius: 10px;  
    	width: 350px;  
    	padding: 30px 35px;  
		background: #fff;  
		border: 1px solid #eaeaea;  
		text-align: center;  
		box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);  
    }
</style>