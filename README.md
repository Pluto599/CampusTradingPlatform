# 校园二手交易平台

基于命令行的校园二手交易平台，使用Java开发。

## 项目结构

```
校园交易平台/
├── UML/                    # UML设计图
│   ├── class.puml         # 类图
│   ├── usecase.puml       # 用例图
│   ├── sequence_publish_product.puml  # 时序图
│   └── component.puml     # 组件图
├── src/
│   ├── model/             # 实体类
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── Tag.java
│   │   ├── Favorite.java
│   │   ├── BrowseHistory.java
│   │   ├── Conversation.java
│   │   └── Message.java
│   ├── repository/        # 数据存储层
│   │   └── Database.java
│   ├── service/           # 业务逻辑层
│   │   ├── AuthService.java
│   │   ├── ProductService.java
│   │   ├── SearchService.java
│   │   ├── ChatService.java
│   │   └── UserService.java
│   ├── util/              # 工具类
│   │   ├── PasswordUtil.java
│   │   └── ValidationUtil.java
│   └── CampusMarketApp.java  # 主程序
└── README.md
```

## 功能特性

### 游客功能
- ✅ 注册账号
- ✅ 用户登录
- ✅ 浏览商品列表
- ✅ 搜索商品
- ✅ 查看商品详情

### 注册用户功能
- ✅ 发布商品（标题、价格、描述、分类）
- ✅ 编辑/下架/删除自己发布的商品
- ✅ 收藏商品
- ✅ 查看浏览历史
- ✅ 查看我的发布
- ✅ 查看我的收藏
- ✅ 站内消息（与卖家沟通）
- ✅ 查看/编辑个人资料
- ✅ 查看卖家联系方式

### 系统特性
- 用户认证与授权
- 密码SHA-256加密
- 输入验证（用户名、邮箱、手机号、密码等）
- 商品分类管理
- 搜索与筛选
- 用户信用分系统
- 浏览历史记录
- 站内即时通讯

## 技术实现

### 架构设计
- **MVC架构**: Model-View-Controller分层设计
- **单例模式**: Database使用单例模式管理数据
- **内存存储**: 使用HashMap存储所有数据（模拟数据库）

### 核心类说明

#### Model层（实体类）
- `User`: 用户实体
- `Product`: 商品实体
- `Category`: 分类实体
- `Tag`: 标签实体
- `Favorite`: 收藏关系
- `BrowseHistory`: 浏览历史
- `Conversation`: 会话
- `Message`: 消息

#### Repository层（数据层）
- `Database`: 单例模式，提供内存数据存储和CRUD操作

#### Service层（业务逻辑层）
- `AuthService`: 用户认证服务（注册、登录、密码重置）
- `ProductService`: 商品服务（发布、编辑、删除、查询）
- `SearchService`: 搜索服务（关键词搜索、分类筛选）
- `ChatService`: 聊天服务（创建会话、发送消息）
- `UserService`: 用户服务（收藏、浏览历史、个人资料）

#### Util层（工具类）
- `PasswordUtil`: 密码加密与验证（SHA-256）
- `ValidationUtil`: 输入验证（邮箱、手机号、用户名等）

## 编译与运行

### 方法一：使用脚本（推荐）

**Windows系统：**
```powershell
# 编译
.\compile.bat

# 运行
.\run.bat
```

### 方法二：手动编译运行

```powershell
# 进入src目录
cd src

# 编译所有Java文件
javac -encoding UTF-8 CampusMarketApp.java model/*.java service/*.java repository/*.java util/*.java

# 运行主程序
java CampusMarketApp
```

## 使用说明

### 1. 注册账号
- 选择菜单"1. 注册"
- 输入用户名（3-20位字母数字下划线）
- 输入邮箱（需符合邮箱格式）
- 输入手机号（11位数字，1开头）
- 设置密码（至少6位）

### 2. 登录系统
- 选择菜单"2. 登录"
- 输入用户名和密码

### 3. 浏览商品
- 主菜单选择"1. 浏览商品"
- 可以选择分类筛选
- 查看商品列表

### 4. 搜索商品
- 主菜单选择"2. 搜索商品"
- 输入关键词进行搜索

### 5. 发布商品
- 登录后选择"4. 发布商品"
- 输入标题、价格、描述
- 选择商品分类

### 6. 联系卖家
- 查看商品详情时选择"2. 联系卖家"
- 可以发送站内消息与卖家沟通

## 默认数据

系统启动时会自动创建以下默认数据：

**分类：**
1. 电子产品
2. 书籍教材
3. 生活用品
4. 运动器材
5. 其他

**标签：**
- 九成新
- 全新
- 急售
- 可议价
- 包邮

## 系统特点

1. **安全性**
   - 密码SHA-256加密存储
   - 用户权限验证
   - 输入格式验证

2. **易用性**
   - 清晰的菜单导航
   - 友好的提示信息
   - 简单的操作流程

3. **功能完整**
   - 覆盖用例图中所有用例
   - 实现类图中所有类和方法
   - 符合时序图的业务流程

4. **代码质量**
   - 分层架构清晰
   - 面向对象设计
   - 代码注释完整

## 开发团队

基于UML设计图开发的校园二手交易平台

## 版本信息

- 版本: 1.0
- 开发语言: Java
- 开发日期: 2025年11月

## 许可证

本项目仅供学习交流使用。
