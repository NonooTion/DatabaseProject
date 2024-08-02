<template>
	<div class="register">
		<el-form :model="userForm" class="register-container" :rules="rules" ref="userForm">
			<h2>请输入信息</h2>
			<el-form-item label="账号:" prop="userName">
				<el-input type="text" v-model="userForm.userName" placeholder="登录账号" ></el-input>
			</el-form-item>
			<el-form-item label="密码:" prop="password">
				<el-input type="password" v-model="userForm.password" placeholder="登录密码" ></el-input>
			</el-form-item>
            <el-form-item label="电话号码:" prop="tel">
				<el-input type="text" v-model="userForm.tel" placeholder="电话号码" ></el-input>
			</el-form-item>
            <el-form-item label="用户类型:" prop="userType">
                <el-radio-group v-model="userForm.userType">
                    <el-radio value="客户">客户</el-radio>
                    <el-radio value="教练">教练</el-radio>
                </el-radio-group>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" style="width:100%;" @click="submitForm()">注册</el-button>
			</el-form-item>
            <el-row style="text-align: center;margin-top:-10px">
				<el-link type="primary" @click="returnToLogin()">返回</el-link>
			</el-row>
		</el-form>
	</div>
</template>
 
<script >

export default{
    data(){
        return{
            userForm:{
            userName: '',
            password: '',
            userType: '',
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
                ],
                userType:[
                { required: true, message: '请选择一个用户类型', trigger: 'blur' }
                ]

            }
        }
    },
    methods:
    {
        //验证表单
        submitForm(){
        this.$refs.userForm.validate((valid) => {
          if (valid) {
            this.doRegister();
          } else {
            this.$message({
                type:'error',
                message:'请根据要求输入完整的注册信息'
            })
          }
        });
      },
        // 注册账号
        doRegister(){
            let url=this.$path.registerUrl
            let params={
                userType:this.userForm.userType,
                userName:this.userForm.userName,
                password:this.userForm.password,
                tel:this.userForm.tel
            }
            this.$axios.post(url,params).then(
                res=>{
                    if(res.data.code==this.$code.REGISTER_SUCCESS)
                    {
                        this.$message({
                            type:'success',
                            message: res.data.message
                        })
                        this.$router.push('/login');
                    }
                    else{
                        this.$message({
                            type:'error',
                            message: res.data.message
                        })
                    }
                }
            )
        },
        //返回登录界面
        returnToLogin(){
            this.$router.push('/login')
        }
    }
}

  
 
</script>
 
 
<style scoped>
	.register{
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
	.register-container{
		border-radius: 10px;  
    	width: 350px;  
    	padding: 30px 35px;  
		background: #fff;  
		border: 1px solid #eaeaea;  
		text-align: center;  
		box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);  
	}

</style>