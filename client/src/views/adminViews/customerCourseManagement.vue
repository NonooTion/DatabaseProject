<template>
    <!-- 添加对话框 -->
    <el-dialog v-model="addDialogVisiable" style="width: 50%; height: 30%; margin-top: 15%;" title="请输入有关信息">
   <el-form labelPosition="top" ref="addForm" :model="addForm" :rules="rules">
    <el-form-item label="顾客:" prop="customerId" >
       <el-select v-model="addForm.customerId" placeholder="请选择顾客">
           <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
           </el-option>
       </el-select>
     </el-form-item>
     <el-form-item label="课程:" prop="classId" >
       <el-select v-model="addForm.classId" placeholder="请选择课程">
           <el-option v-for="item in groupClasses" :key="item.classId" :label="item.classId + '-' + item.className" :value="item.classId">
           </el-option>
       </el-select>
     </el-form-item>
      <el-form-item>
       <el-button style="margin-left: 80%;" type="success" @click="handleSubmit()">提交</el-button>
       <el-button type="danger" @click="handleAddCancel()">取消</el-button>
     </el-form-item>
   </el-form>
 </el-dialog>

     <!-- 修改对话框 -->
   <el-dialog v-model="editDialogVisiable" style="width: 50%; height: 80%; margin-top: 5%;" title="请输入有关信息">
   <el-form labelPosition="top" ref="editForm" :model="editForm" :rules="rules">
    <el-form-item label="顾客:" prop="customerId" >
       <el-select v-model="editForm.customerId" placeholder="请选择顾客">
           <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
           </el-option>
       </el-select>
     </el-form-item>
     <el-form-item label="课程:" prop="classId" >
       <el-select v-model="editForm.classId" placeholder="请选择课程">
           <el-option v-for="item in groupClasses" :key="item.classId" :label="item.classId + '-' + item.className" :value="item.classId">
           </el-option>
       </el-select>
     </el-form-item>
     <el-form-item>
       <el-button style="margin-left: 80%;" type="success" @click="editSubmit()">提交</el-button>
       <el-button type="danger" @click="handleEditCancel()">取消</el-button>
     </el-form-item>
   </el-form>
 </el-dialog>
<el-container>
<el-header style="padding:0px;">
   <el-form class="queryForm" :inline="true">
     <el-form-item label="课程名称:"  style="width: 300px;">
      <el-select v-model="queryData.classId" placeholder="请选择课程">
            <el-option label="请选择课程" value=""></el-option>
           <el-option v-for="item in groupClasses" :key="item.classId" :label="item.classId + '-' + item.className" :value="item.classId">
           </el-option>
       </el-select>
     </el-form-item>
     <el-form-item label="顾客名称:" style="width: 300px;">
       <el-select v-model="queryData.customerId" placeholder="请选择顾客">
            <el-option label="请选择顾客" value=""></el-option>
           <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
           </el-option>
       </el-select>
     </el-form-item>
     <el-form-item>
       <el-button type="primary" @click="handleQuery()"> 查询</el-button>
       <el-button type="success" @click="handleAdd()" > 添加</el-button>
     </el-form-item>
   </el-form>
</el-header>

<el-main style="padding: 0px;">
<el-table :data="customerCourseData" stripe border fit highlight-current-row class="table" ref="tableRef">
  <el-table-column prop="customerCourseId" label="编号"/>
  <el-table-column prop="classId" label="课程编号"/>
  <el-table-column prop="className" label="课程名称"/>
  <el-table-column prop="coachId" label="教练编号"/>
  <el-table-column prop="coachName" label="教练姓名"/>
  <el-table-column prop="customerId" label="顾客编号"/>
  <el-table-column prop="customerName" label="顾客姓名"/>
  <el-table-column prop="endDate" label="开课日期" width="200px"> 
              <template v-slot:default="scope">  
                <span>{{ 
                    this.$moment({
                    year:scope.row.startDate[0],
                    month:scope.row.startDate[1]-1,
                    day:scope.row.startDate[2],
                   }).format('YYYY-MM-DD')
                   +'~'+
                  this.$moment({
                    year:scope.row.endDate[0],
                    month:scope.row.endDate[1]-1,
                    day:scope.row.endDate[2],
                   }).format('YYYY-MM-DD')
                  }}</span>  
            </template>  
          </el-table-column>
          <el-table-column prop="startTime" label="上课时间">
            <template v-slot:default="scope">  
                <span>{{this.formatTimeArray(scope.row.startTime) +"~"+this.formatTimeArray(scope.row.endTime)}}</span>  
            </template>  
          </el-table-column>
  <el-table-column label="操作" width="200px">
    <template v-slot:default="scope">  
    <el-button-group>
      <el-button type="primary" @click="handleEdit(scope.row)"><el-icon><Edit/></el-icon>修改</el-button>
      <el-button type="danger"  @click="handleDelete(scope.row)"><el-icon><Delete/></el-icon>删除</el-button>
    </el-button-group>
  </template>
  </el-table-column>
</el-table>
</el-main>
</el-container>    
</template>

<script >
import {
Delete,
Edit,
Message,
} from '@element-plus/icons-vue'
import { SCOPE } from 'element-plus';
import PracticeArea from '../customerViews/practiceArea.vue';
export default{
data(){
 return{
    customerCourseUrl:this.$path.customerCourseUrl,
    groupClassUrl:this.$path.groupClassUrl,
    userUrl:this.$path.userUrl,
    addDialogVisiable:false,
    editDialogVisiable:false,
    customerCourseData:[],
    customers:[],
    groupClasses:[],
    queryData:{
        customerId:'',
        classId:''
    },
    addForm:{
        customerId:'',
        classId:''
    },
    editForm:{
        customerCourseId:'',
        customerId:'',
        classId:''
    },
    rules:{
        classId:[{required:true,message:"请选择课程",trigger:"blur"}],
        customerId:[{required:true,message:"请选择顾客",trigger:"blur"}],
    },
 }
},
components:{
Edit,
Delete,
Message,
},
methods:{
  formatTimeArray(timeArray) {
    if (!Array.isArray(timeArray) || timeArray.length !== 2) {  
    return 'Invalid time array';  
    }   
  const [hours, minutes] = timeArray;   
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;  
  },
  disabledDate(time){
    const now = new Date();
    return time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)).getTime();
  },
  //添加
  handleAdd(){
   this.addDialogVisiable=true;
  },
  //添加取消
  handleAddCancel()
  {
    this.addDialogVisiable=false;
    this.$refs.addForm.resetFields();
  }
  ,
  //添加提交
  handleSubmit()
  {
  this.$refs.addForm.validate((valid)=>{
  if(valid){
  let url=this.customerCourseUrl
  let params=this.addForm
  this.$axios.post(url,params).then(
  (res)=>{
    if(res.data.code===this.$code.ADD_SUCCESS)
    this.$message({type:"success",message:res.data.message})
    else if(res.data.code===this.$code.ADD_FAILURE)
    this.$message({type:"error",message:res.data.message})
    this.addDialogVisiable=false,
    this.findAll()
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
  handleEdit(row){
  this.editDialogVisiable=true;
  //深拷贝选中行的信息
  this.editForm = JSON.parse(JSON.stringify(row));
  },
  //修改取消
  handleEditCancel()
  {
  this.editDialogVisiable=false;
  },
  //修改提交
  editSubmit()
  {
  let url=this.customerCourseUrl
  let params=this.editForm
  this.$axios.put(url,params).then(
  (res)=>{
    if(res.data.code===this.$code.UPDATE_SUCCESS)
    this.$message({type:"success",message:res.data.message})
    else {
    this.$message({type:"error",message:res.data.message})
    }
    this.editDialogVisiable=false
    this.findAll()
  }
  ).catch(
  (error)=>{
    this.$alert(error)
    this.$message({type:"error",message:"系统错误，请联系管理员"})
  }
  )
  },
  //查询
  handleQuery()
  {
    let url=this.customerCourseUrl
    let params=this.queryData
    this.$axios.get(url,{params:params}).then(
        (res)=>{
            if(res.data.code===this.$code.SELECT_SUCCESS){
                this.customerCourseData=res.data.data
                this.$notify({message:'查询成功', type: 'success' })
            }
            else{
                this.$notify({message:'查询失败，请稍后再试', type: 'error' })
            }
        }
    ).catch(error=>{
        this.$notify({message:'服务器异常，请稍后再试', type: 'warning' })
    })
  },
  handleDelete(row){
  this.$confirm(
  '该信息将被永久删除！',
  '注意',
  {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }
  ).then(()=>{
  let url=this.customerCourseUrl;
  let params={customerCourseId: row.customerCourseId};
  this.$axios.delete(url,{params:params}).then((res)=>{
    if(res.data.code==this.$code.DELETE_SUCCESS){
      this.$message({
        type:'success',
        message:res.data.message
      })
    }
    else if(res.data.code==this.$code.DELETE_FAILURE){
      this.$message({
        type:'error',
        message:'删除失败，请稍后再试'
      })
    }
    this.findAll();
  }).catch((error)=>{
    console.error(error)
    this.$message({
        type:'error',
        message:'系统异常,请联系系统管理员'
      })
  })
  })
  } ,
  findAll(){
    let url=this.customerCourseUrl
    this.$axios.get(url).then(
        (res)=>{
            if(res.data.code===this.$code.SELECT_SUCCESS){
                this.customerCourseData=res.data.data 
            }
        }
    )
    url=this.userUrl
    let params={userType:'客户'}
    this.$axios.get(url,{params:params}).then(
        (res)=>{
            if(res.data.code===this.$code.SELECT_SUCCESS){
                this.customers=res.data.data
            }
        }
    )
    url=this.groupClassUrl
    this.$axios.get(url,{params:params}).then(
        (res)=>{
            if(res.data.code===this.$code.SELECT_SUCCESS){
                this.groupClasses=res.data.data
            }
        }
    )
  } ,
  },
  beforeMount()
  {      
  this.findAll();
  },
}

</script>

<style>
.table{
height: 100%;
}
.table .el-table__body-wrapper tbody tr td {
text-align: center;
}
.table .el-table__header-wrapper th {
text-align: center;
}
.queryForm{
width: 100%;
height: 100%;  
padding: 0px;
margin: 0;
 background: #fff;    
}
.el-form-item{
margin-left: 10px;
margin-top: 15px;
padding: 0px;
}
.el-table__body tr.current-row>td{
background-color: #c9dff7 !important;
color: #000000;
}
.ellipsis {  
overflow: hidden;  
text-overflow: ellipsis;  
white-space: nowrap;  
max-width: 100%; 
cursor: pointer; 
} 
</style> 

