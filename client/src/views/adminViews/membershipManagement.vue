<template>
    <el-container>
      <el-header style="padding:0">
          <el-form class="queryForm" :inline="true">
            <el-form-item label="会员卡号:">
                <el-input placeholder="请输入会员卡号" v-model="queryData.membershipNum"> </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery()"> 查询</el-button>
            </el-form-item>
          </el-form>
      </el-header>
      <el-main style="padding: 0px;">
          <el-table :data="membershipData" stripe border fit highlight-current-row class="table" ref="tableRef">
          <el-table-column prop="membershipId" label="会员卡编号"/>
          <el-table-column prop="membershipNum" label="会员卡号" />
          <el-table-column prop="membershipType" label="会员卡类型"  />
          <el-table-column prop="startDate" label="开始日期"  :formatter="dateFormatter"/>
          <el-table-column prop="endDate" label="结束日期"  :formatter="dateFormatter"/>
          <el-table-column prop="status" label="会员卡状态" />
          <el-table-column prop="balance" label="会员卡余额" />
          <el-table-column label="操作">
            <template v-slot:default="scope">  
            <el-button-group>
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
} from '@element-plus/icons-vue'
import { SCOPE } from 'element-plus';
  export default{
    data(){
      return{
        membershipData:[
            {
                membershipId:'',
                userId:'',
                membershipNum:'',
                startDate: new Date(),
                endDate: new Date(),
                status:'',
                balance:0
            }
        ],
        queryData:{
            membershipNum:''
        },
        membershipUrl:this.$path.membershipUrl,
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
        let url=this.membershipUrl
        let params=this.queryData
      this.$axios.get(url,{params:params}).then((res)=>{
        if(res.data.code==this.$code.SELECT_SUCCESS){
          this.membershipData=res.data.data
        }
        else{
          this.$message({
            type:'error',
            message: res.data.message
          })
        }
      })
      },
      handleDelete(row){
        this.$confirm(
          '该会员卡信息将被永久删除！',
          '注意',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(()=>{
          let url=this.membershipUrl;
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
        let url=this.membershipUrl
        let params=this.queryData
        console.log(params);
        
      this.$axios.get(url,{params:params}).then((res)=>{
        if(res.data.code==this.$code.SELECT_SUCCESS){
          this.membershipData=res.data.data
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
      dateFormatter(row, column, cellValue){
        const date = cellValue;
        if (date) {
        return this.$moment(date).format('YYYY-MM-DD');
      }
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
</style> 

