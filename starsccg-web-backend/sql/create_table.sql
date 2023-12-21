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
values (1, 'stars', 'a0dd3697a192885d7c055db46155b26c', 'Stars', 'stars-userAvatar', '超级管理员 Stars', 'admin'),
       (2, 'jack', 'b0dd3697a192885d7c055db46155b26c', 'Jack', 'jack-userAvatar', '超级大用户 Jack', 'user');


-- 代码生成器表
create table if not exists generator
(
    id                   bigint auto_increment comment '代码生成器主键' primary key,
    generatorName        varchar(255)                       null comment '代码生成器名称',
    generatorDescription text                               null comment '代码生成器描述',
    generatorAuthor      varchar(255)                       null comment '代码生成器作者',
    generatorVersion     varchar(255)                       null comment '代码生成器版本',
    generatorBasePackage varchar(255)                       null comment '代码生成器基础包',
    generatorPicture     varchar(255)                       null comment '代码生成器图片',
    generatorTags        varchar(1023)                      null comment '代码生成器标签列表(JSON数组)',
    generatorFileConfig  text                               null comment '代码生成器文件配置(JSON字符串)',
    generatorModelConfig text                               null comment '代码生成器模型配置(JSON字符串)',
    generatorDistPath    text                               null comment '代码生成器产物路径',
    generatorStatus      int      default 0                 not null comment '代码生成器状态',
    generatorUserId      bigint                             not null comment '代码生成器添加人的用户主键',
    createTime           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime           datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete             tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    index idx_userId (generatorUserId)
) comment '代码生成器表' collate = utf8mb4_0900_ai_ci;

-- 插入数据 代码生成器表
insert into generator (id, generatorName, generatorDescription, generatorAuthor, generatorVersion,
                       generatorBasePackage, generatorPicture, generatorTags, generatorFileConfig,
                       generatorModelConfig, generatorDistPath, generatorStatus, generatorUserId)
values (1, 'ACM 测试项目', 'ACM 测试项目生成器', 'Stars', '1.0.0',
        'com.stars', 'ACM 测试项目-generatorPicture', '["Java"]', '{}',
        '{}', null, 0, 1),
       (2, 'Spring Boot 初始化项目', 'Spring Boot 初始化项目生成器', 'Stars', '1.0.0',
        'com.stars', 'Spring Boot 初始化项目-generatorPicture', '["Java"]', '{}',
        '{}', null, 0, 1),
       (3, '美团外卖前端项目', '美团外卖前端项目生成器', 'Jack', '1.0.0',
        'com.jack', '美团外卖前端项目-generatorPicture', '["Java", "前端"]', '{}',
        '{}', null, 0, 1),
       (4, '天猫商城后端项目', '天猫商城后端项目生成器', 'Jack', '1.0.0',
        'com.jack', '天猫商城后端项目-generatorPicture', '["Java", "后端"]', '{}',
        '{}', null, 0, 1);
