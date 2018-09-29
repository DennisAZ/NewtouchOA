CREATE TABLE  message_body (
  SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  FROM_ID int default NULL,
  MESSAGE_TYPE varchar(200) default NULL,
  CONTENT text,
  SEND_TIME datetime default NULL,
  REMIND_URL text
) ;
CREATE TABLE  message (
  SEQ_ID int  NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  TO_ID int default NULL,
  REMIND_FLAG char(1) default NULL,
  DELETE_FLAG char(1) default '0',
  BODY_SEQ_ID int default NULL,
  REMIND_TIME datetime default NULL
);
CREATE TABLE  im_group (
  GROUP_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  GROUP_NAME varchar(100) default NULL,
  GROUP_UID varchar(6000) default NULL,
  GROUP_ACTIVE varchar(20) default '1',
  ORDER_NO varchar(20) default NULL,
  REMARK varchar(300) default NULL,
  GROUP_CREATOR int  default NULL,
  GROUP_CREATE_TIME varchar(45) default NULL,
  GROUP_INTRODUCTION varchar(3000) default NULL,
  GROUP_SUBJECT varchar(1000) default NULL
);

CREATE TABLE  im_group_maxmsgid (
  MSG_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  GROUP_MEMBER_UID varchar(200) default NULL,
  GROUP_ID varchar(45) default NULL,
  MAX_MSG_ID varchar(45) default NULL
) ;

CREATE TABLE  im_group_msg (
  MSG_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  MSG_UID int default NULL,
  MSG_TIME varchar(45) default NULL,
  MSG_GROUP_ID int default NULL,
  ATTACHMENT_ID varchar(100) default NULL,
  ATTACHMENT_NAME varchar(100) default NULL,
  MSG_USER_NAME varchar(100) default NULL,
  READER_UID varchar(3000) default NULL,
  MSG_CONTENT varchar(3000) default NULL,
  MSG_CONTENT_SIMPLE varchar(3000) default NULL,
  ATTACHMENT_ID2 varchar(300) default NULL,
  ATTACHMENT_NAME2 varchar(300) default NULL
);

CREATE TABLE  weixun_share (
  SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  USER_ID int default '0',
  CONTENT text,
  ADD_TIME varchar(45) default NULL,
  TOPICS varchar(255) default NULL,
  MENTIONED_IDS varchar(200) default NULL,
  BROADCAST_IDS varchar(500) default NULL
);


CREATE TABLE  weixun_share_topic (
  SEQ_ID int NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  TOPIC_NAME varchar(255) NOT NULL
);
Insert into ds_table ( TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values('90506', 'MESSAGE_BODY', 'sss', '1', '', 'T9MessageBody');
Insert into ds_table ( TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values('90505', 'MESSAGE', 's', '1', '', 'T9Message');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505006', 'REMIND_TIME', 'remindTime', 'ss', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505005', 'BODY_SEQ_ID', 'bodySeqId', 'ss', '90506', '', '90506001', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505004', 'DELETE_FLAG', 'deleteFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505003', 'REMIND_FLAG', 'remindFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505002', 'TO_ID', 'toId', 'ss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '0', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505001', 'SEQ_ID', 'seqId', 'sssss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506006', 'REMIND_URL', 'remindUrl', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ('90506', '90506005', 'SEND_TIME', 'sendTime', 's', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506004', 'CONTENT', 'content', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506003', 'MESSAGE_TYPE', 'messageType', 's', '', '', '', '', '', '', '', 'text', '', '', 200, 0, 12, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506002', 'FROM_ID', 'fromId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field ( TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506001', 'SEQ_ID', 'seqId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');


delete from  sys_function  where menu_id = '020802';
update sys_function set func_name='手机短信管理' , func_code='/core/funcs/mobilesms/index.jsp' ,menu_id='0207' where menu_id = '020804';
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('0209','精灵群管理','/core/frame/ispirit/n12/group/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('2001','微讯分享','/core/funcs/message/jpanel/weixun_share/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('6051','性能优化','/core/funcs/youhua/index.jsp','sys.gif','0');
update sys_function set func_name='消息管理' , func_code='/core/funcs/sms/index11.jsp' , menu_id='0206' where menu_id = '0208';
 
alter table email_body add TO_WEBMAIL_COPY text;
alter table email_body add TO_WEBMAIL_SECRET text;
alter table webmail_body add TO_MAIL_COPY  text;
alter table webmail_body add TO_MAIL_SECRET text;
alter table webmail_body add DELETE_FLAG varchar(45);

CREATE TABLE im_offline_file (
  ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  TIME datetime default NULL,
  SRC_UID int default NULL,
  DEST_UID int default NULL,
  FILE_NAME varchar(1000) default NULL,
  FILE_SIZE int  default NULL,
  FLAG int default '0'
);