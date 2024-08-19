<template>
    <el-container>
      <!-- 添加对话框 -->
      <el-dialog v-model="addDialogVisiable" style="width: 50%; height: 70%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="addForm" :model="userForm" :rules="rules">
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="userForm.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
          
          <el-form-item label="用户名:" prop="userName">
            <el-input v-model="userForm.userName" placeholder="请输入用户名"></el-input>
          </el-form-item>

          <el-form-item label="密码:" prop="password">
            <el-input v-model="userForm.password" placeholder="请输入密码"></el-input>
          </el-form-item>

          <el-form-item label="性别" prop="gender">
            <el-select v-model="userForm.gender" placeholder="请选择性别">
              <el-option value="男">男</el-option>
              <el-option value="女">女</el-option>
              <el-option value="其他">其他</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="联系方式:" prop="tel">
            <el-input v-model="userForm.tel" placeholder="请输入联系方式"></el-input>
          </el-form-item>

          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button style="margin-left: 80%;" type="success" @click="handleSubmit()">提交</el-button>
            <el-button type="danger" @click="handleAddCancel()">取消</el-button>
          </el-form-item>

        </el-form>
      </el-dialog>

      <!-- 修改对话框 -->
      <el-dialog v-model="editDialogVisiable" style="width: 50%; height: 50%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="editForm" :model="editForm" :rules="rules">
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="editForm.name" placeholder="请输入姓名" ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="editForm.gender" placeholder="请选择性别">
              <el-option value="男">男</el-option>
              <el-option value="女">女</el-option>
              <el-option value="其他">其他</el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="联系方式:" prop="tel">
            <el-input v-model="editForm.tel" placeholder="请输入联系方式"></el-input>
          </el-form-item>

          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="editForm.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button style="margin-left: 80%;" type="success" @click="editSubmit()">修改</el-button>
            <el-button type="danger" @click="handleEditCancel()">取消</el-button>
          </el-form-item>

        </el-form>
      </el-dialog>

      <el-header style="padding:0">
          <el-form class="queryForm" :inline="true">
            <el-form-item label="姓名:">
                <el-input placeholder="请输入姓名" v-model="queryData.name"> </el-input>
            </el-form-item>

            <el-form-item label="电话号码:">
                <el-input placeholder="请输入电话号码" v-model="queryData.tel"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleQuery()"> 查询</el-button>
              <el-button type="success" @click="handleAdd()" > 添加</el-button>
            </el-form-item>
          </el-form>
      </el-header>
      <el-main style="padding: 0px;">
          <el-table :data="userData" stripe border fit highlight-current-row class="table" ref="tableRef">
          <el-table-column prop="userId" label="用户编号"/>
          <el-table-column prop="name" label="姓名"/>
          <el-table-column prop="gender" label="性别" />
          <el-table-column prop="tel" label="联系方式"  />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column label="操作">
            <template v-slot:default="scope">  
            <el-button-group>
              <el-button type="primary" @click="handleEdit(scope.row)"><el-icon><Edit/></el-icon>修改</el-button>
              <el-button type="danger" @click="handleDelete(scope.row)"><el-icon><Delete/></el-icon>删除</el-button>
            </el-button-group>
          </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>    
  </template>

<script >
import {
  Check,
  Delete,
  Edit,
  Message,
  Search,
  Star,
} from '@element-plus/icons-vue'
import { SCOPE } from 'element-plus';
  export default{
    data(){
      return{
        userData:[],
        queryData:{
          tel:'',
          name:'',
          userType:'客户'
        },
        userForm:{
          userId:-1,
          name:'',
          gender:'',
          userName:'',
          tel:'',
          email:'',
          password:'',
          userType:'客户'
        },
        editForm:{
          userId:-1,
          name:'',
          gender:'',
          tel:'',
          email:'',
          userType:''
        },
        rules:{
          name:[{required:true,message:"请输入姓名",trigger:"blur"}],
          gender:[{required:true,message:"请选择性别",trigger:"blur"}],
          userName:[{required:true,message:"请输入用户名",trigger:"blur"}],
          password:[{required:true,message:"请输入密码",trigger:"blur"}],
          tel:[{required:true,message:"请输入联系方式",trigger:"blur"}],
          email:[{required:true,message:"请输入邮箱",trigger:"blur"}]
        }
        ,
        addDialogVisiable:false,
        editDialogVisiable:false
      }
    },
    components:{
      Edit,
      Delete
    },
    methods:{

      //获取所有用户的信息
      findAll()
      {
        let url=this.$path.userUrl
        let params=this.queryData
      this.$axios.get(url,{params:params}).then((res)=>{
        if(res.data.code==this.$code.SELECT_SUCCESS){
          this.userData=res.data.data
        }
        else{
          this.$message({
            type:'error',
            message: res.data.message
          })
        }
      })
      },

      //修改某一行的数据
      handleEdit(row)
      {
        this.editDialogVisiable=true;
        //深拷贝选中行的信息
        this.editForm = JSON.parse(JSON.stringify(row));
      },
      editSubmit()
      {
        let url=this.$path.userUrl
        let params=this.editForm
        this.$axios.put(url,params).then(
          (res)=>{
            if(res.data.code=this.$code.UPDATE_SUCCESS)
            this.$message({type:"success",message:res.data.message})
            else if(res.data.code=this.$code.UPDATE_FAILURE)
            this.$message({type:"error",message:res.data.message})
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
      handleEditCancel(){
        this.editDialogVisiable=false
      },
      handleDelete(row){
        this.$confirm(
          '该用户信息将被永久删除！',
          '注意',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(()=>{
          let url=this.$path.userUrl
          let params=row;
          console.log(params)
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
                message:res.data.message
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
      }
      ,
      //条件查询
      handleQuery()
      {
        console.log(this.queryData)
        let url=this.$path.userUrl
        let params=this.queryData
      this.$axios.get(url,{params:params}).then((res)=>{
        if(res.data.code==this.$code.SELECT_SUCCESS){
          this.userData=res.data.data
          this.$message({
            type:'success',
            message: res.data.message
          })
        }
        else{
          this.$message({
            type:'error',
            message: res.data.message
          })
        }
      })
      },
      handleAdd()
      {
        this.addDialogVisiable=true
      },
      handleAddCancel(){
        this.addDialogVisiable=false
        this.$refs.addForm.resetFields()
      },
      handleSubmit(){
        this.$refs.addForm.validate((valid)=>{
          if(valid){
            let url=this.$path.userUrl;
        let params=this.userForm;
        this.$axios.post(url,params).then((res)=>{
          if(res.data.code==this.$code.ADD_FAILURE){
            this.$message({
              type:"error",
              message:res.data.message
            })
          }
          else{
            this.$message({
              type:"success",
              message:res.data.message
            })
            this.addDialogVisiable=false
            this.$refs.addForm.resetFields()
            this.findAll()
          }
        })
        }
        else{
          this.$message({
            type:"error",
            message:"请输入完整信息"
          })
        }
        })
      },
    },
    beforeMount()
    {
      this.findAll();
    }
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

