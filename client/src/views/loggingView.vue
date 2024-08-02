<template>
	<div class="login">
		<el-form class="login-container">
			<el-text type="info" size="large">欢迎使用健身房管理系统</el-text>
			<h1 style="color: black;">用户登录</h1>
			<el-form-item label="账号:">
				<el-input type="text" v-model="userName" placeholder="登录账号" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="密码:" >
				<el-input type="password" v-model="password" placeholder="登录密码" autocomplete="off" show-password></el-input>
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
       userName:'',
       password:''
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
        let  url=this.$path.loggingUrl
		let params={
			userName:this.userName,
			password:this.password
		}
		this.$axios.post(url,params).then(res=>{
			if(res.data.code==this.$code.LOGIN_SUCCESS){
			//登录成功消息
			this.$message({
				message:res.data.message,
				type: 'success'
			})
			//跳转到页面
			this.$router.push("/admin")
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