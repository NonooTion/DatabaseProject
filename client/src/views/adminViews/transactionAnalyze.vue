<template>
  <el-container class="container">
    <el-header style="padding:0px;">
        <el-form class="queryForm" ref="form" :model="queryForm" label-width="80px" style="margin-top: 15px;" inline>
            <el-form-item label="开始日期:" >
                <el-date-picker 
                v-model="queryForm.startDate" 
                type="date" placeholder="请选择开始日期"
                format="YYYY/MM/DD"  
                value-format="YYYY-MM-DD"  
                ></el-date-picker>
            </el-form-item>
            <el-form-item label="结束日期:">
                <el-date-picker v-model="queryForm.endDate" 
                type="date"
                 placeholder="请选择结束日期"
                 format="YYYY/MM/DD"  
                 value-format="YYYY-MM-DD" 
                 ></el-date-picker>
            </el-form-item>
            <el-form-item >
                <el-button type="primary" @click="handleQuery">查询</el-button>
            </el-form-item>
        </el-form>
    </el-header>
    <el-main style="padding: 0px;">
        <Echarts class="chart" ref="chart" :option="option"></Echarts>
    </el-main>
  </el-container>    
</template>

<script setup>
import {ref,computed,onBeforeMount} from 'vue'
import path from '@/data/path'
import axios from 'axios'
const transactionUrl= path.transactionUrl
const queryForm= ref(
    {
        startDate:'',
        endDate:''
    }
)
let data= ref([])
const option=computed(()=>{
    return {
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      data: data.value.map(d=>d.type),
      axisTick: {
        alignWithLabel: true
      }
    }
  ],
  yAxis: [
    {
      type: 'value'
    }
  ],
  series: [
    {
      name: '金额',
      type: 'bar',
      barWidth: '60%',
      data: data.value.map(d=>d.data),
    }
  ]
}
})
async function find(){
    try {  
    let params=queryForm.value
    const response = await axios.get(transactionUrl+"/getByDate",{params:params});  
    data.value=response.data.data
  } catch (error) {  
    console.error('There was an error!', error);  
  } 
}
function handleQuery(){
    find();
}
onBeforeMount(()=>{
    find();
})
</script>

<style scoped>
.chart{
    height: 400px;
}
.container{
    background-color: white;
}
.queryForm{
     width: 100%;
     height: 100%;  
     padding: 0px;
     margin: 0;
         background: #fff;    
 }
</style>