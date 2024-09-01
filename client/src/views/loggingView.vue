<template>
	<div class="login">
		<el-form class="login-container" :model="loginInfo" ref="loginForm" :rules="rules">
			<el-text type="info" size="large">欢迎使用健身房管理系统</el-text>
			<h1 style="color: black;">用户登录</h1>
			<el-form-item label="账号:" prop="userName">
				<el-input type="text" v-model="loginInfo.userName" placeholder="登录账号"></el-input>
			</el-form-item>
			<el-form-item label="密码:" prop="password">
				<el-input type="password" v-model="loginInfo.password" placeholder="登录密码" show-password></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" style="width:100%;" @click="doSubmit()">登录</el-button>
			</el-form-item>
			<el-row style="text-align: center;margin-top:-10px">
				<el-link type="primary" @click="gotoRegister()">用户注册</el-link>
				<el-link type="primary" @click="gotoReseter()" style="margin-left: 10px; ">忘记密码</el-link>
			</el-row>
		</el-form>
	</div>
</template>
 
<script>
  export default {
    name: 'Login',
    data(){
      return {
		loginInfo:{
			userName:'',
			password:''
		},
		rules:{
			userName:[
				{required:true ,message:'请输入用户名',trigger:'blur'}
			],
			password:[
				{required:true ,message:'请输入密码',trigger:'blur'}
			]
		}
      }
    },

    //页面跳转
     methods:{
       gotoRegister(){
       this.$router.push('/register');
       },
	   gotoReseter(){
		this.$router.push('/reset')
	   },
       doSubmit(){
		this.$refs.loginForm.validate(
			(valid)=>{
			if(valid){
				let  url=this.$path.loggingUrl
				let params={
					userName:this.loginInfo.userName,
					password:this.loginInfo.password
				}
				this.$axios.post(url,params).then(res=>{
					if(res.data.code==this.$code.LOGIN_SUCCESS){
					//存储登录用户的信息
					sessionStorage.setItem('userId',res.data.data.userId)
					sessionStorage.setItem('userType',res.data.data.userType)
					sessionStorage.setItem('isLoggedIn',true)
					let userType=sessionStorage.getItem('userType')
					//根据用户类型跳转页面
					if(userType==="管理员") this.$router.push("/admin")
					else if(userType==="客户") this.$router.push("/customer")
					else if(userType==="教练") this.$router.push("/coach")
					else this.$alert("账户类型异常!请联系管理员")
					//登录成功消息
					this.$message({
						message:res.data.message,
						type: 'success'
					})
					
				}
					else
					{
						this.$message({
						message:res.data.message,
						type: 'error'
					})
					}
				})
			}
			else{
				this.$message({type:'error', message:'请输入账号和密码'})
			}
		}
	)
       }
     },
	 mounted()
	 {
		sessionStorage.setItem("isLoggedIn",false)
	 }
  }
</script>
 
 
<style scoped>
	.login{
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
	.login-container{
		border-radius: 10px;  
    	width: 350px;  
    	padding: 30px 35px;  
		background: #fff;  
		border: 1px solid #eaeaea;  
		text-align: center;  
		box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);  
	}

</style>