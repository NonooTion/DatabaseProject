<template>
    <!-- 添加对话框 -->
<el-dialog v-model="addDialogVisiable" style="width: 50%; height: 60%;" title="请输入有关信息">
     <el-form labelPosition="top" ref="addForm" :model="addForm" :rules="rules">
       <el-form-item label="顾客:" prop="userId">
         <el-select v-model="addForm.userId" placeholder="请选择顾客">
             <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
             </el-option>
         </el-select>
       </el-form-item>
       <el-form-item label="交易类型:" prop="transactionType" >
         <el-select v-model="addForm.transactionType" placeholder="请选择交易类型">
            <el-option label="请选择交易类型" value=""/>
            <el-option label="私教" value="私教"/>
            <el-option label="团课" value="团课"/>
            <el-option label="自主训练" value="自主训练"/>
            <el-option label="会员卡升级" value="会员卡升级"/>
            <el-option label="会员卡充值" value="会员卡充值"/>
         </el-select>
       </el-form-item>
       <el-form-item label="金额:" prop="amount">
        <el-input v-model="addForm.amount" placeholder="请输入金额"></el-input>
       </el-form-item>
       <el-form-item label="描述:" prop="description">
        <el-input v-model="addForm.description" placeholder="请输入描述"></el-input>
       </el-form-item>
       <el-form-item label="日期:" prop="transactionDate">
         <el-date-picker v-model="addForm.transactionDate" value-format="YYYY-MM-DDTHH:mm:ss"  placeholder="请选择日期" type="datetime"></el-date-picker>
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
        <el-form-item label="顾客:" prop="userId">
         <el-select v-model="editForm.userId" placeholder="请选择顾客">
             <el-option v-for="item in customers" :key="item.userId" :label="item.userId + '-' + item.name" :value="item.userId">
             </el-option>
         </el-select>
       </el-form-item>
       <el-form-item label="交易类型:" prop="transactionType" >
         <el-select v-model="editForm.transactionType" placeholder="请选择交易类型">
            <el-option label="请选择交易类型" value=""/>
            <el-option label="私教" value="私教"/>
            <el-option label="团课" value="团课"/>
            <el-option label="自主训练" value="自主训练"/>
            <el-option label="会员卡升级" value="会员卡升级"/>
            <el-option label="会员卡充值" value="会员卡充值"/>
         </el-select>
       </el-form-item>
       <el-form-item label="金额:" prop="amount">
        <el-input v-model="editForm.amount" placeholder="请输入金额"></el-input>
       </el-form-item>
       <el-form-item label="描述:" prop="description">
        <el-input v-model="editForm.description" placeholder="请输入描述"></el-input>
       </el-form-item>
       <el-form-item label="日期:" prop="transactionDate">
         <el-date-picker v-model="editForm.transactionDate" value-format="YYYY-MM-DDTHH:mm:ss"  placeholder="请选择日期" type="datetime"></el-date-picker>
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
          <el-form-item label="顾客名称:" style="width: 300px;">
            <el-select v-model="queryData.userId" placeholder="请选择顾客">
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
     <el-table :data="transactionData" stripe border fit highlight-current-row class="table" ref="tableRef">
       <el-table-column prop="transactionId" label="交易编号"/>
       <el-table-column prop="userId" label="客户编号"/>
       <el-table-column prop="userName" label="客户名称"/>
       <el-table-column prop="transactionType" label="交易类型"/>
       <el-table-column prop="amount" label="金额"/>
       <el-table-column prop="transactionDate" label="交易日期">
        <template v-slot:default="scope">  
                <span>{{this.$moment(scope.row.transactionDate).format("YYYY-MM-DD HH:mm:ss")}}</span>  
            </template> 
        </el-table-column>
       <el-table-column prop="description" label="交易描述" />

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
         transactionUrl:this.$path.transactionUrl,
         userUrl:this.$path.userUrl,
         addDialogVisiable:false,
         editDialogVisiable:false,
         transactionData:[],
         customers:[],
         queryData:{
            userId:''
         },
         addForm:{
             userId:'',
             transactionType:'',
             amount:'',
             transactionDate:'',
             description:''
         },
         editForm:{
            transactionId:'',
            userId:'',
            transactionType:'',
            amount:'',
            transactionDate:'',
            description:''
         },
         rules:{
            userId:[{required:true,message:"请选择顾客",trigger:"blur"}],
            transactionType:[{required:true,message:"请选择交易类型",trigger:"blur"}],
            amount:[{required:true,message:"请填写交易金额",trigger:"blur"}],
            transactionDate:[{required:true,message:"请选择交易时间",trigger:"blur"}],
            description:[{required:true,message:"请填写交易描述",trigger:"blur"}],
         },
      }
  },
  components:{
    Edit,
    Delete,
    Message,
  },
  methods:{
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
     let url=this.transactionUrl
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
     this.editForm.transactionDate =this.$moment(row.transactionData).format("YYYY-MM-DDTHH:mm:ss")
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
     let url=this.transactionUrl
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
         let url=this.transactionUrl
         let params=this.queryData
         console.log(this.queryData);
         this.$axios.get(url,{params:params}).then(
             (res)=>{
                 if(res.data.code===this.$code.SELECT_SUCCESS){
                     this.transactionData=res.data.data
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
       '该交易信息将被永久删除！',
       '注意',
       {
         confirmButtonText: '确认',
         cancelButtonText: '取消',
         type: 'warning',
       }
     ).then(()=>{
       let url=this.transactionUrl;
       let params={transactionId:row.transactionId};
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
         let url=this.transactionUrl
         this.$axios.get(url).then(
             (res)=>{
                 if(res.data.code===this.$code.SELECT_SUCCESS){
                     this.transactionData=res.data.data
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

