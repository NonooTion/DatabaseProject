<template>
    
    <el-container style="background-color: #e3e0f3; height: 100%;">
        <div style="background-color: #f8f9fa; margin-left: 10%; margin-right: 10%; width: 100%;">
            <div class="calendar-container">  
                <h1 style="text-align: center;">今日日程</h1>
            <ul class="events-list">  
            <li v-for="event in events"  class="event-item">  
                <div class="event-date">
                  {{ formatDate(event.date) }} {{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}
                </div>  
                <div class="event-title">{{ event.title }}</div>  
                <div class="event-details">{{ event.details }}</div>  
            </li>  
            </ul>  
        </div>  
        </div>
    </el-container>
        

</template>

<script >   
    export default{
        data(){
            return{
                events :[]
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
        },
        beforeMount(){
        let url=this.$path.scheduleUrl+"/coach"
        console.log(url);
        
          let params={userId:sessionStorage.getItem("userId")}
            this.$axios.get(url,{params:params}).then((res)=>{
        if(res.data.code==this.$code.SELECT_SUCCESS){
          this.events=res.data.data
        }
        else{
          this.$message({
            type:'error',
            message: res.data.message
          })
        }
      }).catch(error=>{this.$message({
            type:'error',
            message: "系统异常,请稍后再试"
          })})
        }
    } 
</script>  
  
<style scoped>  
.calendar-container {  
  padding: 20px;  
  max-width: 100%;  
  margin: 40px auto;  
  border-radius: 8px;  
  background-color: #f8f9fa;  
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);  
}  
  
.events-list {  

  list-style: none;  
  padding: 0;  
  counter-reset: event-counter;  
}  
  
.event-item {  
  position: relative;  
  padding: 20px;  
  margin-bottom: 10px;  
  background-color: #fff;  
  border-radius: 4px;  
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);  
  transition: box-shadow 0.3s ease;  
}  
  
.event-item:hover {  
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);  
}  
  
.event-item::before {  
  counter-increment: event-counter;  
  content: counter(event-counter) ".";  
  position: absolute;  
  top: 10px;  
  left: -30px;  
  width: 20px;  
  height: 20px;  
  line-height: 20px;  
  text-align: center;  
  font-size: 14px;  
  font-weight: bold;  
  color: #fff;  
  background-color: #42b983;  
  border-radius: 50%;  
}  
  
.event-date {  
  font-size: 14px;  
  color: #6c757d;  
  margin-bottom: 5px;  
}  
.event-content {  
  display: flex;  
  flex-direction: column;  
}  
  
.event-title {  
  font-size: 1.2em;  
  margin-bottom: 5px;  
  color: #343a40;  
}  
  
.event-details {  
  font-size: 0.9em;  
  color: #6c757d;  
}  
</style>