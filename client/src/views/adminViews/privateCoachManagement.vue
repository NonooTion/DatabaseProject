<template>
       <!-- 添加对话框 -->
   <el-dialog v-model="addDialogVisiable" style="width: 50%; height: 60%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="addForm" :model="addForm" :rules="rules">
          <el-form-item label="顾客:" prop="customerId">
            <el-select v-model="addForm.customerId" placeholder="请选择顾客">
                <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
                </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="教练:" prop="coachId" >
            <el-select v-model="addForm.coachId" placeholder="请选择教练">
                <el-option v-for="item in coaches" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
                </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期:" prop="date">
            <el-date-picker v-model="addForm.date" value-format="YYYY-MM-DD"  placeholder="请选择日期" :disabled-date="disabledDate"></el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-select v-model="addForm.startTime"  start="07:00" end="22:00"step='01:00' placeholder="请选择开始时间" style="width: 50%;"/>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-select v-model="addForm.endTime"  start="07:00"end="22:00"step='01:00' placeholder="请选择结束时间" style="width: 50%;"/>
          </el-form-item>
          <el-form-item>
            <el-button style="margin-left: 80%;" type="success" @click="handleSubmit()">提交</el-button>
            <el-button type="danger" @click="handleAddCancel()">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <!-- 修改对话框 -->
      <el-dialog v-model="editDialogVisiable" style="width: 50%; height: 60%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="editForm" :model="editForm" :rules="rules">
          <el-form-item label="顾客:" prop="customerId">
            <el-select v-model="editForm.customerId" placeholder="请选择顾客">
                <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
                </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="教练:" prop="coachId" >
            <el-select v-model="editForm.coachId" placeholder="请选择教练">
                <el-option v-for="item in coaches" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
                </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期:" prop="date"  >
            <el-date-picker v-model="editForm.date" value-format="YYYY-MM-DD"  placeholder="请选择日期" :disabled-date="disabledDate"></el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-select v-model="editForm.startTime"   start="07:00" end="22:00"step='01:00' placeholder="请选择开始时间" style="width: 50%;"/>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-select v-model="editForm.endTime" :min-time="addForm.startTime"  start="07:00"end="22:00"step='01:00' placeholder="请选择结束时间" style="width: 50%;"/>
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
             <el-form-item label="教练名称:" >
                 <el-input placeholder="请输入教练名称" v-model="queryData.coachName" ></el-input>
             </el-form-item>
             <el-form-item label="顾客名称:">
                 <el-input placeholder="请输入顾客名称" v-model="queryData.customerName" ></el-input>
             </el-form-item>
             <el-form-item>
               <el-button type="primary" @click="handleQuery()"> 查询</el-button>
               <el-button type="success" @click="handleAdd()" > 添加</el-button>
             </el-form-item>
           </el-form>
       </el-header>

       <el-main style="padding: 0px;">
        <el-table :data="privateCoachData" stripe border fit highlight-current-row class="table" ref="tableRef">
          <el-table-column prop="privateCoachId" label="私教编号"/>
          <el-table-column prop="coachId" label="教练编号"/>
          <el-table-column prop="coachName" label="教练姓名"/>
          <el-table-column prop="customerId" label="顾客编号"/>
          <el-table-column prop="customerName" label="顾客姓名"/>
          <el-table-column prop="date" label="日期"  >
            <template v-slot:default="scope">  
                <span>{{ 
                  this.$moment({
                    year:scope.row.date[0],
                    month: scope.row.date[1]-1,
                    day:scope.row.date[2],
                   }).format('YYYY-MM-DD')
                  }}</span>  
            </template>  
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间">
            <template v-slot:default="scope">  
                <span>{{this.formatTimeArray(scope.row.startTime)}}</span>  
            </template>  
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间">
            <template v-slot:default="scope">  
                <span>{{this.formatTimeArray(scope.row.endTime)}}</span>  
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
            privateCoachUrl:this.$path.privateCoachUrl,
            userUrl:this.$path.userUrl,
            addDialogVisiable:false,
            editDialogVisiable:false,
            privateCoachData:[],
            customers:[],
            coaches:[],
            queryData:{
                coachName:'',
                customerName:''
            },
            addForm:{
                date:'',
                startTime:'',
                endTime:'',
                coachId:'',
                customerId:'',
            },
            editForm:{
                date:'',
                startTime:'',
                endTime:'',
                coachId:'',
                customerId:'',
            },
            rules:{
                coachId:[{required:true,message:"请选择教练",trigger:"blur"}],
                customerId:[{required:true,message:"请选择顾客",trigger:"blur"}],
                date:[{required:true,message:"请选择日期",trigger:"blur"}],
                startTime:[{required:true,message:"请选择开始时间",trigger:"blur"}],
                endTime:[{required:true,message:"请选择结束时间",trigger:"blur"}],
            },
         }
     },
     components:{
       Edit,
       Delete,
       Message,
     },
     methods:{
      disabledDate(time){
        const now = new Date();
        return time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)).getTime();
      },
      formatTimeArray(timeArray) {  
      const [hours, minutes] = timeArray;   
      return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;  
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
        let url=this.privateCoachUrl
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
        this.editForm.date=this.$moment(new Date(row.date)).format('YYYY-MM-DD')
        this.editForm.startTime=this.formatTimeArray(row.startTime)
        this.editForm.endTime=this.formatTimeArray(row.endTime)
      },
      //修改取消
      handleEditCancel()
      {
        this.editDialogVisiable=false;
        this.$refs.editForm.resetFields();
      },
      //修改提交
      editSubmit()
      {
        let url=this.privateCoachUrl
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
            this.$refs.editForm.resetFields();
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
            let url=this.privateCoachUrl
            let params=this.queryData
            this.$axios.get(url,{params:params}).then(
                (res)=>{
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.privateCoachData=res.data.data
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
          '该私教信息将被永久删除！',
          '注意',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(()=>{
          let url=this.privateCoachUrl;
          let params={privateCoachId:row.privateCoachId};
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
            let url=this.privateCoachUrl
            this.$axios.get(url).then(
                (res)=>{
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.privateCoachData=res.data.data
                        console.log(this.privateCoachData);
                        
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
            url=this.userUrl
            params={userType:'教练'}
            this.$axios.get(url,{params:params}).then(
                (res)=>{
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.coaches=res.data.data
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
 </style> 
 
 