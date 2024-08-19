<template>
    <!-- 预约信息对话框 -->
   <el-dialog v-model="addDialogVisiable" style="width: 45%; height: 40%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="addForm" :model="addForm" :rules="rules">
          <el-form-item label="日期:" prop="date"  >
            <el-date-picker v-model="addForm.date" value-format="YYYY-MM-DD"  placeholder="请选择日期" :disabled-date="disabledDate"></el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-select v-model="addForm.startTime"  start="07:00" end="22:00"step='01:00' placeholder="请选择开始时间" style="width: 50%;"/>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-select v-model="addForm.endTime" :min-time="addForm.startTime" start="07:00"end="22:00"step='01:00' placeholder="请选择结束时间" style="width: 50%;"/>
          </el-form-item>
          <el-form-item>
            <el-button style="margin-left: 80%;" type="success" @click="handleSubmit()">提交</el-button>
            <el-button type="danger" @click="handleAddCancel()">取消</el-button>
          </el-form-item>
        </el-form>
    </el-dialog>

    <el-container style="background-color: #e3e0f3;  height: fit-content; min-height: 100%;">
        <div style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%; width: 100%; ">
        <el-card class="box-card" v-for="coach in coaches" :key="coach.id"  >  
        <img :src="coach.imageUrl" class="image" alt="教练图片">  
        <div class="content">  
          <h3>{{ coach.name }}</h3>
          <p>价格:{{ coach.ratePerHour }}(元/小时)</p>
          <p>{{ coach.introduce }}</p>  
          <el-button type="primary" @click="handleAdd(coach.coachId)">预约</el-button>  
        </div>  
      </el-card> 
        </div>
    </el-container>
        

</template>

<script>
import { extractDateFormat } from 'element-plus';

  
 
  
export default {  

  data() {  
    return {  
      coachUrl:this.$path.coachUrl,
      privateCoachUrl:this.$path.privateCoachUrl,
      coaches:[],
      addDialogVisiable:false,
      addForm:{
                coachId:'',
                customerId: sessionStorage.getItem("userId"),
                date:'',
                startTime:'',
                endTime:'',
            },
            rules:{
                date:[{required:true,message:"请选择日期",trigger:"blur"}],
                startTime:[{required:true,message:"请选择开始时间",trigger:"blur"}],
                endTime:[{required:true,message:"请选择结束时间",trigger:"blur"}],
            },
    }  
  },  
  methods: { 
    disabledDate(time){
        const now = new Date();
        return time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)).getTime();
    },
    handleAdd(coachId){
        this.addForm.coachId=coachId;
        this.addDialogVisiable=true;
      },
      //添加取消
      handleAddCancel()
      {
        console.log(this.addForm);
        this.addDialogVisiable=false;
        this.$refs.addForm.resetFields();
      }
      ,
      //添加提交
      handleSubmit()
      {
        this.$refs.addForm.validate((valid)=>{
            if(valid){
        let url=this.privateCoachUrl+"/schedule"
        let params=this.addForm
        this.$axios.post(url,params).then(
          (res)=>{
            if(res.data.code===this.$code.ADD_SUCCESS)
            this.$message({type:"success",message:res.data.message})
            else if(res.data.code===this.$code.ADD_FAILURE)
            this.$message({type:"error",message:res.data.message})
            this.addDialogVisiable=false,
            this.$refs.addForm.resetFields();
          }
        ).catch(
          (error)=>{
            this.$alert(error)
            this.$message({type:"error",message:"系统错误，请联系管理员"})
          }
        )
            }
        })
      },
  },

  beforeMount(){
    let url=this.coachUrl;;
           this.$axios.get(url).then(
            (res)=>{
                if(res.data.code===this.$code.SELECT_SUCCESS){ 
                    this.coaches=res.data.data
                }
                else{
                    this.$message({type:'error',message:'服务器异常，请稍后再试'})
                }
            }
           )
  }
};  
</script>  
  
<style scoped>  
.box-card {  
  width: 50%; 
  margin-top: 10px; 
  margin-left: 25%;
  margin-right: 25%;
  margin-bottom: 10px;
  min-height:500px; 
  height: fit-content; 
  text-align: center;  
}  
  
.image {  
  width: auto;
  height: 300px;
  object-fit: cover;  
  border-radius: 4px 4px 0 0;  
}  
  
.content {  
  padding: 20px;  
}  
</style>