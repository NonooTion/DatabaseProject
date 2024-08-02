import path from '@/data/path'
import {createRouter, createWebHistory} from 'vue-router'

//routes数组用于定义路由
const routes =[
    {
        path:"/",
        alias:"/login",
        component: ()=>import("@/views/loggingView.vue")
    },
    {
        path:"/register",
        component: ()=>import("@/views/register.vue")
    },
    {
        path:"/reset",
        component: ()=>import("@/views/resetPasswordView.vue")
    },
    {
        path:"/admin",
        component: ()=>import("@/views/adminView.vue"),
        children:[
            {
                //人员管理
                path:"/customerManagement",
                component: ()=>import("@/views/PME/customerManagement.vue")
            },
            {
                path:"/coachManagement",
                component: ()=>import("@/views/PME/coachManagement.vue")
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router