<template>
    
<el-container style="background-color: #e3e0f3;  height: fit-content; min-height: 100%;">
<div style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%;  ">
    <el-header style="display: flex; align-items: center; height: 100px; width: 1500px; position:fixed; background-color:#faf7ff ;">  
    <el-input  
      v-model="queryData.className"  
      placeholder="请输入您感兴趣的课程"  
      clearable  
      style="flex: 1; height: 50%;"  
      
    >  
      <template #prepend>搜索</template>  
    </el-input>  
    <el-button type="primary" @click="handleSearch" style="margin-left: 10px; margin-top: 0%;">搜索</el-button>  
  </el-header>  
    
    <el-main style="margin-top: 5%; width:1500px;">
        <div class="purchase-container" v-for="item in courses" >
            <h1>{{ item.className }}</h1>
            <img :src="item.imageUrl" class="image" alt="课程图片">  
            <h2>{{ item.title }}</h2>
            <h3>开课时间:{{formatDate(item.startDate)}} 至 {{formatDate(item.endDate)}}</h3>
            <h3>每天:{{ formatTime(item.startTime) }} 至 {{ formatTime(item.endTime) }}</h3>
            <h3>授课教练: {{ item.coachId }}-{{ item.coachName }} </h3>
            <h2 style="color: red;">价格:{{ item.price }}(元)</h2>
            <p style="margin-left: 5%; margin-right: 5%;">{{ item.description }}</p>
            <button @click="purchaseCourse(item)">购买</button>
        </div>
    </el-main>
</div>
</el-container>
  </template>
  <script>
   
  export default {
    data(){
        return{
            customerCourseUrl:this.$path.customerCourseUrl,
            groupClassUrl:this.$path.groupClassUrl,
            courses:[],
            queryData:{
                className:'',
                date: this.$moment(new Date()).format("YYYY-MM-DD")
            }
        }
    },
    methods:{
        formatDate(dateArray)
        {
            if (!Array.isArray(dateArray) || dateArray.length < 3) {  
            return 'Invalid date array';  
            }   
            return this.$moment({
                    year:dateArray[0],
                    month:dateArray[1]-1,
                    day:dateArray[2],
                   }).format('YYYY-MM-DD')
        },
        formatTime(timeArray) {
        if (!Array.isArray(timeArray) || timeArray.length < 2) {  
        return 'Invalid time array';  
         }   
        const [hours, minutes] = timeArray;
        return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;  
        },
        handleSearch(){
         let url=this.groupClassUrl
         let params=this.queryData
         this.$axios.get(url,{params:params}).then(
             (res)=>{
                 if(res.data.code===this.$code.SELECT_SUCCESS){
                     this.courses=res.data.data
                     this.$notify({message:'查询成功', type: 'success' })
                 }
                 else{
                     this.$notify({message:'查询失败，请稍后再试', type: 'error' })
                 }
                 this.queryData.coachId=''
             }
         ).catch(error=>{
             this.$notify({message:'服务器异常，请稍后再试', type: 'warning' })
         })
        },
        purchaseCourse(item)
        {
            this.$confirm(
       '是否确认购买该课程',
       '确认',
       {
         confirmButtonText: '购买',
         cancelButtonText: '取消',
         type: 'info',
       }
     ).then(()=>{
       let url=this.customerCourseUrl+"/payment";
       let params={ customerId: sessionStorage.userId, classId:item.classId};
       
       this.$axios.post(url,params).then((res)=>{
         if(res.data.code==this.$code.ADD_SUCCESS){
           this.$message({
             type:'success',
             message:res.data.message
           })
         }
         else if(res.data.code==this.$code.ADD_FAILURE){
           this.$message({
             type:'error',
             message: res.data.message
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
        },
        findAll(){
         let url=this.groupClassUrl
         let params={date: this.$moment(new Date()).format("YYYY-MM-DD")}
         
         this.$axios.get(url,{params:params}).then(
             (res)=>{
                 if(res.data.code===this.$code.SELECT_SUCCESS){
                     this.courses=res.data.data 
                 }
             }
         )} ,
    },
    beforeMount(){
        this.findAll();
    }
  };
  </script>
   
  <style scoped>
.purchase-container {  
    max-width: 80%;  
    margin-left: 10%;
    margin-bottom: 3%;
    background-color: #ffffff;  
    color: #333333; 
    border: 1px solid #e0d6f9; 
    border-radius: 8px;  
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);  
    font-family: Arial, sans-serif;  
    text-align: center; 
}  
  
.purchase-container h2 {  
    font-size: 24px; /* 增大标题字体大小 */  
    margin-bottom: 15px; /* 增加标题与描述之间的间距 */  
}  
  
.purchase-container p {  
    font-size: 16px; /* 设定描述文字的字体大小 */  
    line-height: 1.5; /* 设定行高，使文本更易读 */  
}  
  
.purchase-container button {  
    background-color: #337ab7; /* 蓝色背景，与橙色形成对比 */  
    border: none; /* 移除默认边框 */  
    color: white; /* 白色字体 */  
    padding: 10px 20px; /* 设定内边距 */  
    text-align: center; /* 文本居中 */  
    text-decoration: none; /* 移除文本装饰 */  
    display: inline-block; /* 设置为内联块级元素 */  
    font-size: 16px; /* 字体大小 */  
    margin: 20px 0; /* 上下边距 */  
    cursor: pointer; /* 鼠标悬停时显示指针 */  
    border-radius: 5px; /* 圆角边框 */  
    transition: background-color 0.3s; /* 背景色过渡效果 */  
}  
  
.purchase-container button:hover {  
    background-color: #286090; /* 鼠标悬停时改变背景色 */  
}
  button {
    cursor: pointer;
    background-color: #007bff;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    margin-top: 10px;
    font-size: 16px;
  }
  </style>