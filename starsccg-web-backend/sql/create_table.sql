# 数据库初始化
# @author stars

-- StarsCCG基础数据库
create database if not exists starsccg
    character set = utf8mb4
    collate = utf8mb4_0900_ai_ci;


-- 切换库
use starsccg;


-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment '用户主键' primary key,
    userAccount  varchar(255)                           not null comment '用户账号',
    userPassword varchar(511)                           not null comment '用户密码(加密)',
    userName     varchar(255)                           null comment '用户名称',
    userAvatar   varchar(1023)                          null comment '用户头像',
    userProfile  varchar(255)                           null comment '用户简介',
    userRole     varchar(255) default 'user'            not null comment '用户角色(user/admin/ban)',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除(0-未删, 1-已删)',
    index idx_userAccount (userAccount)
) comment '用户表' collate = utf8mb4_0900_ai_ci;

-- 插入数据 用户表
insert into user (id, userAccount, userPassword, userName, userAvatar, userProfile, userRole)
values (1, 'stars', 'b44a6ffad48da7ff92b7de7838a73148', 'Stars',
        'https://th.bing.com/th/id/OIP.7OME75wD5I8guEbiD6oyCAHaHa?w=564&h=564&rs=1&pid=ImgDetMain',
        '超级管理员 Stars', 'admin'),
       (2, 'jack', 'b44a6ffad48da7ff92b7de7838a73148', 'Jack',
        'https://th.bing.com/th/id/OIP.7OME75wD5I8guEbiD6oyCAHaHa?w=564&h=564&rs=1&pid=ImgDetMain',
        '超级大用户 Jack', 'user');


-- 代码生成器表
create table if not exists generator
(
    id          bigint auto_increment comment '代码生成器主键' primary key,
    name        varchar(255)                       null comment '代码生成器名称',
    description text                               null comment '代码生成器描述',
    author      varchar(255)                       null comment '代码生成器作者',
    version     varchar(255)                       null comment '代码生成器版本',
    basePackage varchar(255)                       null comment '代码生成器基础包',
    picture     varchar(255)                       null comment '代码生成器图片',
    tags        varchar(1023)                      null comment '代码生成器标签列表(JSON数组)',
    fileConfig  text                               null comment '代码生成器文件配置(JSON字符串)',
    modelConfig text                               null comment '代码生成器模型配置(JSON字符串)',
    distPath    text                               null comment '代码生成器产物路径',
    status      int      default 0                 not null comment '代码生成器状态',
    userId      bigint                             not null comment '代码生成器添加人的用户主键',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    index idx_userId (userId)
) comment '代码生成器表' collate = utf8mb4_0900_ai_ci;

-- 插入数据 代码生成器表
insert into generator (id, name, description, author, version, basePackage,
                       picture, tags, fileConfig, modelConfig, distPath, status, userId)
values (1, 'ACM测试用例1', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://pic.52112.com/180308/180308_123/OOQhvZI5Lm_small.jpg',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (2, 'ACM测试用例2', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://img0.baidu.com/it/u=2540484512,2651961397&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (3, 'ACM测试用例3', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://pic.52112.com/180830/EPS-180830_33/8kNujAll5t_small.jpg',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (4, 'ACM测试用例4', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://img2.baidu.com/it/u=1670935071,187739686&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (5, 'ACM测试用例5', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://img1.baidu.com/it/u=1228841328,4269940623&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (6, 'ACM测试用例6', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://photo.16pic.com/00/78/81/16pic_7881857_b.jpg',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (7, 'ACM测试用例7', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://img1.baidu.com/it/u=4022928163,2469725446&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400',
        '["Java", "ACM"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1),
       (8, 'SpringBoot项目初始化模板', 'ACM测试用例生成器', 'Stars', '1.0.0', 'com.stars',
        'https://pic.rmb.bdstatic.com/bjh/events/bdc298989f46c00989eb2ca9d582063e.png@h_1280',
        '["Java", "SpringBoot"]',
        '{"files":[{"inputPath":"src/com/stars/acm/MainTemplate.java.ftl","outputPath":"src/com/stars/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"type":"group","condition":"needGit","groupKey":"git","groupName":"开源","files":[{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}]}',
        '{"models":[{"fieldName":"needGit","type":"boolean","description":"是否生成 .gitignore 文件","defaultValue":true},{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":true,"abbr":"l"},{"type":"MainTemplate","description":"用于生成核心模板文件","groupKey":"mainTemplate","groupName":"核心模板","models":[{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"stars","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}],"condition":"loop"}]}',
        '/generator_dist/1/acm-template-pro-generator-dist.zip', 0, 1);
