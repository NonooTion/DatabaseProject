const baseUrl='http://localhost:8080'

const path = {
    //登录账号
    loggingUrl:baseUrl+'/login',
    //注册账号
    registerUrl:baseUrl+'/register',
    resetUrl:baseUrl+'/reset',

    //顾客信息操作路径
    customerUrl: baseUrl+"/customer",

    //教练信息操作路径
    coachUrl: baseUrl+"/coach"
}

export default path;