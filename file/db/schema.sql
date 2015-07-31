/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/7/28 11:07:03                           */
/*==============================================================*/


drop table if exists t_account;

drop table if exists t_account_device;

drop table if exists t_admin_log;

drop table if exists t_character;

drop table if exists t_exchange_data_format;

drop table if exists t_game;

drop table if exists t_game_account;

drop table if exists t_game_complete_condition;

drop table if exists t_game_daily_recharge;

drop table if exists t_game_item_source;

drop table if exists t_game_mail;

drop table if exists t_game_notice;

drop table if exists t_game_operating;

drop table if exists t_game_player_keep;

drop table if exists t_game_register_statistics;

drop table if exists t_game_server;

drop table if exists t_gift_card;

drop table if exists t_gold_cost_log;

drop table if exists t_login_log;

drop table if exists t_menu;

drop table if exists t_recharge_log;

drop table if exists t_recharge_statistics;

drop table if exists t_request_command;

drop table if exists t_server_access_ip;

drop table if exists t_synchronized_time;

drop table if exists t_system_code;

drop table if exists t_user;

drop table if exists t_user_group;

drop table if exists t_user_group_menu;

drop table if exists t_user_permission;

/*==============================================================*/
/* Table: t_account                                             */
/*==============================================================*/
create table t_account
(
   id                   bigint not null auto_increment,
   userName             varchar(30) not null,
   nickName             varchar(30),
   password             varchar(255),
   gender               tinyint(2),
   email                varchar(30),
   platform             varchar(30),
   deviceId             varchar(150),
   autoCreate           tinyint(1),
   lastUpdateTime       bigint,
   lastLoginTime        bigint,
   createTime           bigint,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_account_device                                      */
/*==============================================================*/
create table t_account_device
(
   id                   bigint not null auto_increment,
   deviceId             varchar(150),
   accountId            bigint,
   createTime           bigint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_admin_log                                           */
/*==============================================================*/
create table t_admin_log
(
   id                   bigint not null,
   userId               bigint,
   requestCommandId     bigint,
   params               text,
   createTime           bigint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_character                                           */
/*==============================================================*/
create table t_character
(
   id                   bigint not null auto_increment,
   accountId            bigint,
   gameServerId         int,
   gameCharacterId      varchar(100),
   nickName             varchar(15),
   role                 varchar(15),
   deviceId             varchar(50),
   deviceType           varchar(50),
   level                int,
   vipLevel             int,
   lastLoginTime        bigint,
   lastLogoutTime       bigint,
   ip                   varchar(30),
   gameVersion          varchar(30),
   vsRank               int comment '��������',
   vsRankMax            int comment '�����������',
   rechargeTotal        int comment '��ֵ�ܽ���ʯ/Ԫ����',
   rechargeStage        int comment '��ǰ��Ӫ�׶γ�ֵ����ʯ/Ԫ����',
   createTime           bigint,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_exchange_data_format                                */
/*==============================================================*/
create table t_exchange_data_format
(
   id                   bigint not null,
   gameId               int default 0 comment '0:����ϵͳ',
   name                 varchar(255),
   content              longtext,
   description          longtext,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_exchange_data_format comment '���ݴ���ͨ�Ÿ�ʽ����';

/*==============================================================*/
/* Table: t_game                                                */
/*==============================================================*/
create table t_game
(
   id                   int not null auto_increment,
   name                 varchar(30),
   developpedBy         varchar(30),
   gameType             varchar(20),
   onlineTime           bigint,
   offlineTime          bigint,
   income               bigint,
   encryptKey           text,
   configKey            varchar(255) comment '���ùؼ���',
   description          varchar(255),
   enabled              tinyint(1),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_account                                        */
/*==============================================================*/
create table t_game_account
(
   id                   bigint not null auto_increment,
   accessKey            varchar(150),
   token                varchar(150),
   accountId            bigint,
   gameId               int,
   updateTime           bigint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_complete_condition                             */
/*==============================================================*/
create table t_game_complete_condition
(
   id                   bigint not null,
   gameId               int comment '0:����',
   completionKey        varchar(50),
   description          varchar(255),
   createTime           bigint,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_daily_recharge                                 */
/*==============================================================*/
create table t_game_daily_recharge
(
   id                   bigint not null auto_increment,
   gameServerId         int,
   playerAmount         int,
   goldAmount           int,
   moneyAmount          int,
   time                 bigint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_item_source                                    */
/*==============================================================*/
create table t_game_item_source
(
   id                   bigint not null auto_increment,
   gameId               int,
   sourceType           varchar(64),
   sourceId             varchar(64),
   sourceName           varchar(50),
   description          varchar(255),
   createTime           bigint,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_mail                                           */
/*==============================================================*/
create table t_game_mail
(
   id                   bigint not null auto_increment,
   createUserId         int,
   approveUserId        int,
   gameId               int,
   gameServerIds        text comment '��1_2_3����Ϊȫ��',
   characterIds         text comment 'gameServerId1=cid1_cid2..|gameServerId2=.. cidΪ��Ϸ�������еĽ�ɫid',
   guildIds             varchar(255) comment 'gameServerId1=gid1_..|gameServerId2=.. gidΪ��Ϸ�������еĹ���id',
   title                varchar(80),
   content              text,
   items                text,
   createTime           bigint,
   exipreTime           bigint,
   startTime            bigint,
   approveTime          bigint,
   state                int comment '״̬(0Ϊδ���,1Ϊ���ͨ��,2Ϊ�ѷ���,3Ϊ�ܾ�)',
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_notice                                         */
/*==============================================================*/
create table t_game_notice
(
   id                   bigint not null auto_increment,
   createUserId         int,
   approveUserId        int,
   gameId               int,
   gameServerIds        text comment '��1|2|3����Ϊȫ��',
   content              text,
   beginTime            bigint,
   endTime              bigint,
   timeInterval         int comment '��λ��',
   createTime           bigint,
   approveTime          bigint,
   state                int comment '״̬(0Ϊδ���,1Ϊ���ͨ��,2Ϊ�ѷ���,3Ϊ�ܾ�)',
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_operating                                      */
/*==============================================================*/
create table t_game_operating
(
   id                   bigint not null auto_increment,
   createUserId         int,
   approveUserId        int,
   gameId               int,
   gameServerIds        text comment '��1_2_3����Ϊȫ��',
   configId             varchar(30) comment 'gameServerId1=cid1_cid2..|gameServerId2=.. cidΪ��Ϸ�������еĽ�ɫid',
   title                varchar(80),
   content              text,
   items                text,
   conditions           varchar(255),
   createTime           bigint,
   exipreTime           bigint,
   startTime            bigint,
   approveTime          bigint,
   state                int comment '״̬(0Ϊδ���,1Ϊ���ͨ��,2Ϊ�ѷ���,3Ϊ�ܾ�)',
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_player_keep                                    */
/*==============================================================*/
create table t_game_player_keep
(
   id                   bigint not null auto_increment,
   gameServerId         int,
   newAmount            int,
   day2                 int,
   day3                 int,
   day4                 int,
   day5                 int,
   day6                 int,
   day7                 int,
   day14                int,
   day30                int,
   time                 bigint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_game_register_statistics                            */
/*==============================================================*/
create table t_game_register_statistics
(
   id                   bigint not null auto_increment,
   time                 bigint,
   gameServerId         int,
   requestAmount        int,
   enterAmount          int,
   lossAmount           int,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_game_register_statistics comment '��Ϸ������ÿ��ͳ��һ���ϴ���������ʱ��ȡͳ�Ƶĵ�ǰһ��';

/*==============================================================*/
/* Table: t_game_server                                         */
/*==============================================================*/
create table t_game_server
(
   id                   int not null auto_increment,
   gameId               int,
   gameZoneId           int,
   gameZoneName         varchar(30),
   serverType           tinyint,
   deployId             int,
   name                 varchar(30),
   onlineTime           bigint,
   offlineTime          bigint,
   serverInfos          varchar(255),
   dbConfig             varchar(255),
   income               bigint,
   dataSyncTime         bigint,
   enabled              tinyint(1),
   description          varchar(255),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_gift_card                                           */
/*==============================================================*/
create table t_gift_card
(
   id                   bigint not null auto_increment,
   serialKey            varchar(64),
   md5Key               varchar(255),
   createTime           bigint,
   expireTime           bigint,
   exchangedTime        bigint,
   gameId               int,
   characterId          bigint,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_gold_cost_log                                       */
/*==============================================================*/
create table t_gold_cost_log
(
   id                   bigint not null auto_increment,
   characterId          bigint,
   opType               int,
   level                int,
   vipExp               int,
   time                 bigint,
   marketType           varchar(30),
   itemName             varchar(255),
   itemType             varchar(255),
   itemPrice            int,
   itemAmount           int,
   goldExpend           int,
   goldLeft             int,
   platform             varchar(20),
   deviceType           varchar(50),
   expendReason         varchar(50),
   detail               varchar(255),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_login_log                                           */
/*==============================================================*/
create table t_login_log
(
   id                   bigint not null auto_increment,
   characterId          bigint,
   opType               int,
   level                int,
   vipLevel             int,
   time                 bigint,
   onlineTime           int,
   ip                   varchar(20),
   gameVersion          varchar(30),
   deviceId             varchar(50),
   deviceType           varchar(50),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_menu                                                */
/*==============================================================*/
create table t_menu
(
   id                   int not null,
   name                 varchar(100),
   url                  varchar(255),
   parentId             int default 0,
   displayIndex         int default 0 comment '��ʾ˳��',
   enabled              tinyint default 0,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_recharge_log                                        */
/*==============================================================*/
create table t_recharge_log
(
   id                   bigint not null auto_increment,
   characterId          bigint,
   gameServerId         int,
   gameCharacterId      varchar(100),
   opType               int,
   level                int,
   lastVipExp           int,
   vipExp               int,
   gold                 int comment 'ʣ����ʯ',
   goldGain             int comment '��ֵ��ʯ',
   goldGainExtra        int comment '��������ʯ',
   rechargeAmount       int comment '��ֵ��ʯ',
   rechargeFor          varchar(20) comment '��ֵ���ͣ��㿨���¿����ɳ������',
   isFirstRecharge      tinyint,
   rechargePlatform     varchar(20),
   platform             varchar(20),
   time                 bigint,
   gainReason           varchar(100),
   updateTime           bigint,
   money                int comment '�����ֽ�',
   orderId              varchar(200),
   detail               varchar(255),
   status               tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_recharge_statistics                                 */
/*==============================================================*/
create table t_recharge_statistics
(
   id                   bigint not null auto_increment,
   time                 bigint,
   gameServerId         int,
   rechargeAmount       int,
   rechargeCharacters   int,
   newRechargeAmount    int,
   newRechargeCharacters int,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_request_command                                     */
/*==============================================================*/
create table t_request_command
(
   id                   bigint not null,
   gameId               int default 0 comment '0:ϵͳ',
   code                 int,
   name                 varchar(100),
   content              varchar(255),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_server_access_ip                                    */
/*==============================================================*/
create table t_server_access_ip
(
   id                   bigint not null,
   serverId             int,
   ip                   varchar(30),
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_synchronized_time                                   */
/*==============================================================*/
create table t_synchronized_time
(
   id                   bigint not null auto_increment,
   gameServerId         int,
   lastUpdateTime       bigint,
   dbConfig             varchar(255),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_system_code                                         */
/*==============================================================*/
create table t_system_code
(
   id                   bigint not null,
   gameId               int default 0 comment '0:ϵͳ',
   code                 int,
   displayIndex         int,
   name                 varchar(100),
   content              varchar(255),
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_system_code comment 'ҵ��code�������';

/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user
(
   id                   int not null auto_increment,
   userName             varchar(30),
   nickName             varchar(30),
   passWord             varchar(30),
   userGroupId          int,
   gender               int,
   enabled              tinyint,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_user_group                                          */
/*==============================================================*/
create table t_user_group
(
   id                   int not null,
   name                 varchar(30),
   description          varchar(255),
   enabled              int,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_user_group_menu                                     */
/*==============================================================*/
create table t_user_group_menu
(
   id                   int not null auto_increment,
   menuId               int,
   userGroupId          int,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_user_permission                                     */
/*==============================================================*/
create table t_user_permission
(
   id                   bigint not null auto_increment,
   requestCommandId     bigint,
   userGroupId          int,
   primary key (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

