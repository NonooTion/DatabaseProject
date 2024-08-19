<template>
    <el-container style="background-color: #e3e0f3; height: fit-content;">
        <div style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%; width: 100%;">
            <div v-for="area in areas" :key="area.id" class="gym-area">  
            <h1>{{ area.areaName }}</h1>    
            <img :src="area.imageUrl" alt="Gym Area Image"> 
            <p>{{ area.description }}</p>
            </div>
        </div>
    </el-container>
</template>

<script>
    export default{
        data(){
            return{
                areas:[
                    {
                        areaName:'',
                        description:'',
                        imageUrl:''
                    }
                ],
                practiceUrl:this.$path.practiceAreaUrl,
            }
        },
        methods:{
        },
        beforeMount(){
           let url=this.practiceUrl;
           console.log(url);
           
           this.$axios.get(url).then(
            (res)=>{
                if(res.data.code===this.$code.SELECT_SUCCESS){ 
                    this.areas=res.data.data
                    console.log(this.areas);
                    
                }
                else{
                    this.$message({type:'error',message:'服务器异常，请稍后再试'})
                }
            }
           )
    }
}
</script>

<style>
.gym-area-container {  
  display: flex;  
  flex-wrap: wrap;  
  gap: 20px;  
  padding: 20px;  
}  
  
.gym-area img { 
  margin-left: 25%;
  width: 50%;  
  height: auto;  
  margin-bottom: 10px;  
}  
.gym-area h1 { 
background: #2B6695;
border-radius: 6px 6px 6px 6px;
box-shadow: 0 0 0 1px #5F5A4B, 1px 1px 6px 1px rgba(10, 10, 0, 0.5);
color: #FFFFFF;
font-family:"微软雅黑", "宋体", "黑体", Arial;
font-size: 30px;
font-weight: bold;
height: 25px;
line-height: 25px;
margin: 18px 0 !important;
padding: 8px 0 5px 5px;
text-shadow: 2px 2px 3px #222222;
}
.gym-area p { 
    margin-left: 5%;
    margin-right: 5%;
    font-family:"微软雅黑", "宋体", "黑体", Arial;
    font-size: 30px;
}  
</style>