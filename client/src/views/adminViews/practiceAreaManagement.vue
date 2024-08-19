<template>
   <!-- 添加对话框 -->
   <el-dialog v-model="addDialogVisiable" style="width: 50%; height: 40%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="addForm" :model="addForm" :rules="rules">
          <el-form-item label="名称:" prop="areaName">
            <el-input v-model="addForm.areaName" placeholder="请输入区域名称"></el-input>
          </el-form-item>
          <el-form-item label="简介:" prop="description" >
            <el-input type="textarea" v-model="addForm.description" placeholder="请输入简介"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="margin-left: 80%;" type="success" @click="handleSubmit()">提交</el-button>
            <el-button type="danger" @click="handleAddCancel()">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <!-- 修改对话框 -->
      <el-dialog v-model="editDialogVisiable" style="width: 50%; height: 40%;" title="请输入有关信息">
        <el-form labelPosition="top" ref="editForm" :model="editForm" :rules="rules">
          <el-form-item label="名称:" prop="areaName">
            <el-input v-model="editForm.areaName" placeholder="请输入区域名称"></el-input>
          </el-form-item>
          <el-form-item label="简介:" prop="description" >
            <el-input type="textarea" v-model="editForm.description" placeholder="请输入简介"></el-input>
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
            <el-form-item label="区域名称:">
                <el-input placeholder="请输入区域名称" v-model="queryData.areaName"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery()"> 查询</el-button>
              <el-button type="success" @click="handleAdd()" > 添加</el-button>
            </el-form-item>
          </el-form>
      </el-header>
      <el-main style="padding: 0px;">
          <el-table :data="practiceAreaData" stripe border fit highlight-current-row class="table" ref="tableRef">
          <el-table-column prop="areaId" label="区域编号"/>
          <el-table-column prop="areaName" label="区域名" />
          <el-table-column prop="description" label="描述" >  
                <template v-slot:default="scope">  
                <div @click="toggleFullDescription(scope.$index, scope.row)"  >  
                    <div v-if="!scope.row.showFullDescription" class="ellipsis">{{ scope.row.description }}</div>  
                    <div v-else>{{ scope.row.description }}</div>  
                </div>  
                </template>  
          </el-table-column>
            <el-table-column label="图片" width="180px">  
            <template  #default="{ row }">    
              <a v-if="row.imageUrl!=null" :href="row.imageUrl ? row.imageUrl : 'src/image/placeholder.jpg'" target="_blank">  
                <img :src="row.imageUrl ? row.imageUrl : 'src/image/placeholder.jpg'" alt="图片加载失败" style="width: 100%; height: auto; ">  
              </a>
              <el-upload :http-request="upload" :data="row" :limit="1" :auto-upload="true">
                  <el-button  type="warning" >上传图片</el-button>
              </el-upload>
            </template>  
          </el-table-column>  
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
  Delete,
  Edit,
  Message,
} from '@element-plus/icons-vue'
import { SCOPE } from 'element-plus';
  export default{
    data(){
        return{
            practiceAreaUrl:this.$path.practiceAreaUrl,
            baseUrl:this.$path.baseUrl,
            uploadUrl:this.$path.practiceAreaUrl+"/upload",
            addDialogVisiable:false,
            editDialogVisiable:false,
            practiceAreaData:[
            {
                areaId:'',
                areaName:'',
                description:'',
                imageUrl:'',
                showFullDescription:false,
            },
        ],
        addForm:{
          areaName:'',
          description:''
        },
        editForm:{
          areaId:-1,
          areaName:'',
          description:''
        },
        rules:{
          areaName:[{required:true,message:"请输入区域名称",trigger:"blur"}],
          description:[{required:true,message:"请输入简介",trigger:"blur"}],
        },
        queryData:{
          areaName:''
        }
        }
    },
    components:{
      Edit,
      Delete,
      Message,
    },
    methods:{
      //查询全部
        findAll(){
            let url=this.practiceAreaUrl
            console.log(url);
            this.$axios.get(url).then(
                (res)=>{
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.practiceAreaData=res.data.data
                    }
                    else{
                        this.$message({type:"error",message:"系统异常，请稍后再试"})
                    }
                }
            )
        },
      //条件查询
      handleQuery(){
        let url=this.practiceAreaUrl
        let params=this.queryData
            this.$axios.get(url,{params:params}).then(
                (res)=>{
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.practiceAreaData=res.data.data
                        this.$message({type:"success",message:res.data.message})
                    }
                    else{
                        this.$message({type:"error",message:"系统异常，请稍后再试"})
                    }
                }
            )
      },
      //修改按钮
      handleEdit(row){
        this.editDialogVisiable=true;
        //深拷贝选中行的信息
        this.editForm = JSON.parse(JSON.stringify(row));
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
        let url=this.practiceAreaUrl
        let params=this.editForm
        this.$axios.put(url,params).then(
          (res)=>{
            if(res.data.code===this.$code.ADD_SUCCESS)
            this.$message({type:"success",message:res.data.message})
            else if(res.data.code===this.$code.ADD_FAILURE)
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
      //添加按钮
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
        this.$refs.addForm.validate( valid=>{
          if(valid){
        let url=this.practiceAreaUrl
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
        toggleFullDescription(index, row) { 
          this.practiceAreaData[index] = { ...this.practiceAreaData[index], showFullDescription: !row.showFullDescription }
        },    
        //上传图片
      upload(file)
      {
        console.log(file);
        let url=this.practiceAreaUrl+"/upload"
        let formData = new FormData();  
        formData.append('file', file.file); 
        formData.append('areaId',file.data.areaId),
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
      //删除所选行
      handleDelete(row){
        this.$confirm(
          '该区域将被永久删除！',
          '注意',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(()=>{
          let url=this.practiceAreaUrl;
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

