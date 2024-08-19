<template>
    
    <el-container style="background-color: #e3e0f3; height: fit-content; min-height: 100%;">
        <div style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%; width: 100%;">
            <div style="">
                <el-form ref="coachInfoform" :model="coachInfo" label-width="80px" labelPosition="top" style="margin-left: 5%;">
                    <h1 style="margin-top: 5%;  font-family: KAITI;">教练信息</h1>
                    <img :src="coachInfo.imageUrl" alt="请上传图片" width="200" height="250"> 
                    <el-form-item label="个人简介:" prop="introduce" >
                        <el-input  v-model="coachInfo.introduce" placeholder="请输入个人简介" style="width:45%;" type="textarea"></el-input>
                    </el-form-item>
                    <el-form-item label="私教价格(每小时):" prop="ratePerHour">
                        <el-input v-model="coachInfo.ratePerHour" placeholder="请输入私教时薪" style="width:30%;" ></el-input>
                    </el-form-item>
                    <el-form-item >
                        <el-upload :http-request="upload"  :limit="1" :auto-upload="true" style="margin-left: 1%;">
                            <el-button type="warning">上传图片</el-button>
                        </el-upload>  
                        <el-button type="primary" style="margin-left: 5%; margin-bottom: 1%;" @click="handleCoachInfoSave()">保存</el-button>
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
                coachInfo:{
                    coachId:-1,
                    userId:-1,
                    ratePerHour:null,
                    introduce:'',
                    imageUrl:''
                },
                coachUrl:this.$path.coachUrl,
            }
        },
        methods:{
            //上传照片
            upload(file)
            {
                let url=this.coachUrl+"/upload"
                let formData = new FormData();  
                formData.append('file', file.file); 
                formData.append('coachId',this.coachInfo.coachId),
                this.$axios.post(url,formData).then(
                (res)=>{
                    if(res.data.code===this.$code.UPDATE_SUCCESS){
                    this.$message({type:'success',message:'上传成功'})
                    
                    window.location.reload();
                    }
                    else{
                    this.$message({type:'error',message:'上传失败，请稍后再试'})
                    }
                })
            },
            handleCoachInfoSave(){
                let url=this.coachUrl
                let params=this.coachInfo
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
            },
        },
        beforeMount(){
            let params={
                userId: sessionStorage.getItem('userId')
            }
            let url=this.coachUrl+"/findById"
            this.$axios.get(url,{params:params}).then(
                (res)=>
                {
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        console.log(res.data.data);
                        this.coachInfo=res.data.data
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