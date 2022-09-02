-- 要求：根据业务可以独立完成所有表的创建

create database java19_tingshu charset utf8mb4;

use java19_tingshu;

create table users (
    uid int primary key auto_increment,
    username varchar(20) not null unique,
    password char(60) not null
);

create table tracks (
    tid int primary key auto_increment,
    uid int not null,
    title varchar(10) not null,
    type char(40) not null,
    content longblob not null comment '音频的二进制数据'
);

create table albums (
    aid int primary key auto_increment,
    uid int not null,
    title varchar(10) not null,
    cover varchar(600) not null,
    state int not null comment '0: 已下线 / 1: 未发布 / 2: 已发布'
);

create table relations (
    rid int primary key auto_increment,
    aid int not null,
    tid int not null
) comment '维护专辑和音频之间的多对多关系';