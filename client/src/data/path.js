import Membership from "@/views/customerViews/membership.vue";

const baseUrl='http://localhost:8080'

const path = {
    baseUrl:baseUrl,
    //登录账号
    loggingUrl:baseUrl+'/login',
    //注册账号
    registerUrl:baseUrl+'/register',
    resetUrl:baseUrl+'/reset',
    
    //用户操作路径
    userUrl:baseUrl+"/user",
    //顾客信息操作路径
    customerUrl: baseUrl+"/customer",

    //教练信息操作路径
    coachUrl: baseUrl+"/coach",

    //会员信息操作路径
    membershipUrl: baseUrl+"/membership",

    //会员卡信息操作路径
    membershipCardUrl: baseUrl+"/membershipCard",

    //交易信息操作路径
    transactionUrl: baseUrl+"/transaction",

    //练习区域信息操作路径
    practiceAreaUrl: baseUrl+"/practiceArea",

    //私教课程信息操作路径
    privateCoachUrl: baseUrl+"/privateCoach",
    //团课信息操作路径
    groupClassUrl: baseUrl+"/groupClass",
    //团课选课操作路径
    customerCourseUrl: baseUrl+"/customerCourse",
    //日程信息操作路径
    scheduleUrl: baseUrl+"/schedule"
}

export default path;