<template>
    <!-- 充值对话框 -->
    <el-dialog title="充值" v-model="rechargeDialogVisiable" style="width: 30%; height: 30%;">
        <div style="margin-top: 5%;">
            <h2>请选择支付金额：</h2>
            <el-form label-width="80px" >
                <el-select v-model="money" placeholder="请选择支付金额" >
                <el-option value="50">50元</el-option>
                <el-option value="100">100元</el-option>
                <el-option value="200">200元</el-option>
                <el-option value="500">500元</el-option>
                <el-input  v-model="money" placeholder="请输入金额(元)" style="margin-left: 4%; width: 40%;"></el-input>
            </el-select>
            </el-form>
        </div>
        <el-container style="margin-left: 70%; margin-top: 15%;">
            <el-button type="primary" @click="rechargeConfirm()">确 定</el-button>
            <el-button type="danger" @click="rechargeCancel()">取 消</el-button>
        </el-container>
    </el-dialog>

    <!-- 卡片升级对话框 -->

    <el-container style="background-color: #e3e0f3; height: 100%;">
        <el-container style="background-color: #faf7ff; margin-left: 10%; margin-right: 10%;">
            <el-header style="width: 100%; padding: 0%;"><h1 style="text-align: center; font-family:cursive; color:#31323f;" >尊敬的{{ userInfo.name }},欢迎回来!</h1>
            </el-header>
            <el-container style="height: 90%;" >
                <el-aside style=" width: 20%;margin-left: 1%; padding: 0px;" class="aside">
                <h3 style="text-align: center;">会员权益</h3>
                <h4>1.年卡与月卡用户免费自主练习区域</h4>
                <h4>2.次卡用户每次入场使用自主练习区域将从卡中扣除响应余额</h4>
                <h4>3.年卡用户报名团课享受7折优惠</h4>
                <h4>4.月卡用户报名团课享受9折优惠</h4>
                <br>
                <h3 style="text-align: center;">操作</h3>
                <h4>1.点击充值按钮为会员卡充值</h4>
                <h4>2.点击升级按钮，可将次卡升级为年卡或月卡</h4>
                <h4>3.点击停卡按钮，可停用会员卡</h4>
                <h4>4.点击激活按钮，重新激活会员卡</h4>
            </el-aside>
            <el-main class="gym-membership-card">
            <div class="card-body" style="margin-left: 35%;">  
                <div style="margin-top: 1%;">  
                    <div class="info-item" style="margin-top: 0%;">  
                    <label>会员卡号:</label>  
                    <span>{{ membershipInfo.membershipNum }}</span>  
                    </div> 
                    <div class="info-item">  
                    <label>会员卡类型:</label>  
                    <span>{{ membershipInfo.membershipType }}</span>  
                    </div>
                    <div class="info-item">  
                    <label>余额:</label>  
                    <span> {{ membershipInfo.balance }} (元)</span>  
                    </div> 
                    <div class="info-item">  
                    <label>会员卡状态:</label>  
                    <span> {{ membershipInfo.status }}</span>  
                    </div> 
                    <div class="info-item">  
                    <label>有效期:</label>  
                    <span>{{ validity }}</span>  
                </div>
                <div style="margin-top: 20%; margin-left: 49%;">
                    <el-button type="primary" @click="recharge()">充值</el-button>
                    <el-button type="warning">升级</el-button>
                    <el-button type="success" @click="activation()">激活</el-button>
                    <el-button type="danger"@click="pause()">暂停</el-button>
                </div>
                </div>  
            </div> 
            </el-main>
            </el-container>
        </el-container>
    </el-container>
        

</template>

<script>
import CustomerInfo from './customerInfo.vue';

    export default{
        data()
        {
            return{
                // Url路径
                transactionUrl:this.$path.transactionUrl+"/recharge",
                membershipUrl:this.$path.membershipUrl,
                //数据
                userInfo:{
                    userId:'',
                    name:''
                },
                membershipInfo:{
                    membershipId:'',
                    userId:'',
                    membershipNum:'',
                    membershipType:'',
                    balance:0.00,
                    status:'',
                    startDate:'',
                    endDate:''
                },
                rechargeDialogVisiable:false,
                money:0.00,
                validity:""
            }
        },
        methods:{
            //刷新页面
            reload()
            {
                setTimeout(() => {window.location.reload();}, 2000); // 2000毫秒后刷新页面
            },
            //注册会员卡
            register()
            {
            this.$confirm(
                '检测到您没有会员卡，是否成为会员?',
                {
                    confirmButtonText: '是',
                    cancelButtonText: '返回',
                    type: 'info',
                }
                ).then(
                    (res)=>{
                        let url=this.membershipUrl;
                        let params={
                            userId:sessionStorage.getItem("userId")
                        }
                        this.$axios.post(url,params).then(
                            (res)=>{
                                if(res.data.code===this.$code.ADD_SUCCESS){
                                    this.$message({type:'success',message:'恭喜你称为会员'})
                                }
                                else{
                                    this.$message({type:'error',message:'系统异常，请稍后再试'})
                                }
                            }
                        )
                    }
                ).catch(
                    ()=>{
                        this.$router.push('/customer')
                    }
                )
            },
            //打开充值界面
            recharge()
            {
                this.money=''
                this.rechargeDialogVisiable=true
            },
            //取消充值
            rechargeCancel()
            {
                this.rechargeDialogVisiable=false
                this.$message({type:'success',message:'充值已取消'})
            },
            //充值
            rechargeConfirm(){
                if(isNaN(this.money) || this.money<=0){
                    this.$message({type:"error",message:"充值金额必须为大于0的数字"})
                }
                else{
                let url=this.transactionUrl;
                let params={
                    userId: sessionStorage.getItem("userId"),
                    transactionType: '其他',
                    amount: this.money,
                    description: '会员卡充值'
                }
                this.$axios.post(url,params).then(
                    (res)=>{
                        if(res.data.code===this.$code.ADD_SUCCESS){
                            this.$message({type:'success',message:res.data.message})
                            this.reload();
                        }
                        else if(res.data.code===this.$code.ADD_FAILURE){
                            this.$message({type:'error',message:res.data.message})
                        }
                    }
                )
                }
            },
            //升级卡

            //暂停操作
            pause(){
                //检查卡状态，是否已经暂停
                if(this.membershipInfo.status==='暂停'){
                    this.$notify({ title: '暂停失败', message: '您的健身卡已经是暂停状态', type: 'warning' });
                }
                else{
                let url=this.membershipUrl+"/pause";
                let params=this.membershipInfo;
                this.$axios.put(url,params).then(
                    (res)=>{
                        if(res.data.code===this.$code.UPDATE_SUCCESS){
                            this.$notify({ title: '暂停成功', message: '您的健身卡已暂停', type: 'success' })
                            this.reload();
                            }
                        else 
                        {
                        this.$notify({ title: '警告', message: '操作异常，请稍后再试', type: 'error' })
                        }
                    }
                ).catch((error)=>{
                    // this.$notify({ title: '警告', message: '系统异常，请联系系统管理员', type: 'error' });
                })}
            },
            //激活操作
            activation(){
                    //检查卡状态，是否已经激活
                if(this.membershipInfo.status==='激活'){
                    this.$notify({ title: '激活失败', message: '您的健身卡已经是激活状态', type: 'warning' });
                }
                else{
                let url=this.membershipUrl+"/activation";
                let params=this.membershipInfo;
                this.$axios.put(url,params).then(
                    (res)=>{
                        if(res.data.code===this.$code.UPDATE_SUCCESS){
                            this.$notify({ title: '激活成功', message: '您的健身卡已激活', type: 'success' })
                            this.reload();
                            }
                        else 
                        this.$notify({ title: '警告', message: '操作异常，请稍后再试', type: 'error' })
                    }
                ).catch((error)=>{
                    this.$notify({ title: '警告', message: '系统异常，请联系系统管理员', type: 'error' });
                })}
            },
            //根据卡类型设计有效日期
            setValidity()
            {
                let type=this.membershipInfo.membershipType
                        let status=this.membershipInfo.status
                        if(status==='暂停'){
                            this.validity='健身卡已暂停'
                        }
                        else if(type==='次卡'){
                            this.validity='次卡无有效期'
                        }
                        else{
                            this.validity=this.$moment(this.membershipInfo.startDate).format("YYYY-MM-DD")
                            +" - "+this.$moment(this.membershipInfo.endDate).format("YYYY-MM-DD")
                        }
            },
            //检测会员卡是否过期
            testExpire()
            {
                let currentTime=new Date().getTime();
                if(this.membershipInfo.endDate-currentTime<0){
                    let url=this.membershipUrl+"/expire"
                    let params=this.membershipInfo;
                    this.$axios.put(url,params).then(
                        (res)=>{
                            if(res.data.code===this.$code.UPDATE_SUCCESS){
                                this.$notify({ title: '通知', message:'您的会员卡已过期，即将降为次卡', type: 'warning' })
                                this.reload();
                            }
                        }
                    ).catch(error=>{this.$message({type:'error',message:'网络异常，请稍后再试'})})
                }
                
                
            }
        },
        beforeMount()
        {
            let url=this.membershipUrl+'/findByUserId'
            let params={ userId:sessionStorage.getItem('userId')}
            this.$axios.get(url,{params:params}).then(
                (res)=>{
                    if(res.data.code==this.$code.SELECT_SUCCESS){
                        this.membershipInfo=res.data.data
                        //检测会员卡是否过期，过期则设置为次卡
                        if(this.membershipInfo.membershipType!='次卡')
                        {this.testExpire()}
                        //根据卡类型设置有效日期
                        this.setValidity()
                    }
                    else if(res.data.code==this.$code.SELECT_FAILURE){
                        this.register()
                    }
                }
            )
            url=this.$path.customerUrl+"/findById"
            this.$axios.get(url,{params:params}).then(
                (res)=>
                {
                    if(res.data.code===this.$code.SELECT_SUCCESS){
                        this.userInfo=res.data.data
                        
                    }
                    else if(res.data.code===this.$code.SELECT_FAILURE){
                        this.$alert("系统异常，请稍后再试")
                    }
                }
            )
        }
    }
</script>


<style>
.gym-membership-card {
  background-image: url(@/assets/vipCard.jpg);
  background-size: 100% 100%;
  margin-right: 10%;
  margin-left: 5%;
  margin-top: 5%;
  margin-bottom: 25%;
  width: 50%;  
  height: 60%;
  border: 1px solid #ccc;  
  border-radius: 8px;  
  padding: 20px;  
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);  
} 
.info-item{
    color:#e3e0f3;
    font-size:25px;
    margin-top: 5%;
    margin-left: 1%;
}
.info-item span{
    margin-left: 1%;
}
.action{
    margin-top: 28%;
    margin-left:60%;
}
.aside h4{
    color: #6399a6 ;
}
.aside h3{
    color: #3f8181;
}
</style>