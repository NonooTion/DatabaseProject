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
            //人员管理
            {
                path:"/customerManagement",
                component: ()=>import("@/views/adminViews/customerManagement.vue")
            },
            {
                path:"/coachManagement",
                component: ()=>import("@/views/adminViews/coachManagement.vue")
            },
            //会员管理
            {
                path:"/membershipManagement",
                component: ()=>import("@/views/adminViews/membershipManagement.vue")
            },
            //练习区域管理
            {
                path:"/practiceAreaManagement",
                component: ()=>import("@/views/adminViews/practiceAreaManagement.vue")
            },
            //私教管理
            {
                path:"/privateCoachManagement",
                component: ()=>import("@/views/adminViews/privateCoachManagement.vue")
            },
            //团课管理
            {
                path:"/groupClassManagement",
                component: ()=>import("@/views/adminViews/groupClassManagement.vue")
            },
            //团课选课管理
            {
                path:"/customerCourseManagement",
                component: ()=>import("@/views/adminViews/customerCourseManagement.vue")
            },
            //交易信息管理
            {
                path:'/transaction',
                component: ()=>import("@/views/adminViews/transactionManagement.vue")
            },
        ]
    },
    {
        path:'/customer',
        component: ()=>import('@/views/customerView.vue'),
        children:[
            {
                path:"/customerInfo",
                component: ()=>import("@/views/customerViews/customerInfo.vue")
            },
            {
                path:'/membership',
                component: ()=>import("@/views/customerViews/membership.vue")
            },
            {
                path:'/practiceArea',
                component: ()=>import("@/views/customerViews/practiceArea.vue")
            },
            {
                path:'/privateCoach',
                component: ()=>import("@/views/customerViews/privateCoach.vue")
            },
            {
                path:'/groupClass',
                component: ()=>import("@/views/customerViews/groupClass.vue")
            },
            {
                path:'/schedule',
                component: ()=>import("@/views/customerViews/schedule.vue")
            },
        ]
    },
    {
        path:'/coach',
        component: ()=>import('@/views/coachView.vue'),
        children:[
            {
                path:'/personalInfo',
                component: ()=>import("@/views/coachViews/personalInfo.vue")
            },
            {
                path:'/coachInfo',
                component: ()=>import("@/views/coachViews/coachInfo.vue")
            },
            {
                path:'/coachSchedule',
                component: ()=>import("@/views/coachViews/schedule.vue")
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router