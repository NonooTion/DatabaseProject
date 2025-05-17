# 健身房管理系统 Gym Management System

## 项目简介 | Project Introduction

本项目是一个基于前后端分离架构的健身房管理系统，支持会员、教练和管理员三类角色，涵盖会员管理、课程预约、教练排班、场地管理、数据分析等功能。适用于健身房日常运营的信息化管理。

This project is a full-stack Gym Management System supporting members, coaches, and administrators. It covers member management, course booking, coach scheduling, venue management, and data analytics, suitable for digitalizing gym operations.

---

## 技术栈 | Tech Stack

- **前端 Frontend**: [Vue 3](https://vuejs.org/), [Vite](https://vitejs.dev/), [Element Plus](https://element-plus.org/), [ECharts](https://echarts.apache.org/)
- **后端 Backend**: [Spring Boot 3](https://spring.io/projects/spring-boot), [MyBatis-Plus](https://baomidou.com/), [MySQL](https://www.mysql.com/)
- **其他 Others**: Axios, Moment.js, RESTful API

---

## 目录结构 | Directory Structure

```
├── client/           # 前端源码 Frontend source code
│   ├── src/
│   │   ├── views/    # 页面组件 Views (admin, coach, customer)
│   │   ├── router/   # 路由配置 Router
│   │   ├── data/     # 静态数据 Static data
│   │   └── ...
│   ├── public/
│   └── ...
├── server/           # 后端源码 Backend source code
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gym/   # Java代码 Java code
│   │   │   └── resources/      # 配置文件 Config files
│   │   └── test/
│   └── pom.xml       # Maven配置 Maven config
└── README.md         # 项目说明 Project readme
```

---

## 主要功能 | Main Features

- 会员注册、登录、信息管理
- 课程预约与管理（团课、私教）
- 教练信息展示与排班管理
- 场地管理与预约
- 会员卡管理与交易记录
- 数据统计与可视化分析
- 管理员后台多维度管理

---

## 安装与运行 | Installation & Run

### 1. 克隆项目 Clone the repo
```bash
git clone <your-repo-url>
cd <project-root>
```

### 2. 数据库配置 Database Setup
- 安装MySQL，创建名为`gym`的数据库。
- 修改 `server/src/main/resources/application.yaml` 中的数据库连接信息（用户名、密码）。
- 导入数据库表结构（如有SQL文件请补充）。

### 3. 启动后端 Start Backend
```bash
cd server
mvn clean install
mvn spring-boot:run
```
默认端口：`8080`

### 4. 启动前端 Start Frontend
```bash
cd client
npm install
npm run dev
```
默认端口：`5173`

前端会自动代理API请求到后端。

---

## 常见问题 | FAQ

- **图片上传路径如何配置？**
  - 请根据 `application.yaml` 中的路径，将图片资源目录放置在对应位置。
- **端口冲突怎么办？**
  - 可在配置文件中修改端口。
- **数据库连接失败？**
  - 检查MySQL服务、用户名、密码、端口及数据库名。

---

## 开源协议 | License

本项目采用 MIT License 开源。

This project is licensed under the MIT License.

---

## 联系方式 | Contact

如有问题或建议，欢迎提issue或联系开发者。

Feel free to open issues or contact the maintainer for questions or suggestions. 