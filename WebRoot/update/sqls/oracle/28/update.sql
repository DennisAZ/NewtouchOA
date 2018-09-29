CREATE TABLE message_body (
  SEQ_ID NUMBER  primary key,
  FROM_ID NUMBER default NULL,
  MESSAGE_TYPE varchar2(200) default NULL,
  CONTENT CLOB,
  SEND_TIME date default NULL,
  REMIND_URL CLOB
) ;
CREATE TABLE  message (
  SEQ_ID NUMBER  primary key,
  TO_ID NUMBER default NULL,
  REMIND_FLAG VARCHAR2(1) default NULL,
  DELETE_FLAG VARCHAR2(1) default '0',
  BODY_SEQ_ID NUMBER default NULL,
  REMIND_TIME date default NULL
);
CREATE TABLE  im_group (
  GROUP_ID NUMBER  primary key,
  GROUP_NAME varchar2(100) default NULL,
  GROUP_UID varchar2(4000) default NULL,
  GROUP_ACTIVE varchar2(20) default '1',
  ORDER_NO varchar2(20) default NULL,
  REMARK varchar2(300) default NULL,
  GROUP_CREATOR NUMBER  default NULL,
  GROUP_CREATE_TIME varchar2(45) default NULL,
  GROUP_INTRODUCTION varchar2(3000) default NULL,
  GROUP_SUBJECT varchar2(1000) default NULL
);

CREATE TABLE  im_group_maxmsgid (
  MSG_ID NUMBER  primary key,
  GROUP_MEMBER_UID varchar2(200) default NULL,
  GROUP_ID varchar2(45) default NULL,
  MAX_MSG_ID varchar2(45) default NULL
) ;

CREATE TABLE  im_group_msg (
  MSG_ID NUMBER  primary key,
  MSG_UID NUMBER default NULL,
  MSG_TIME varchar2(45) default NULL,
  MSG_GROUP_ID NUMBER default NULL,
  ATTACHMENT_ID varchar2(100) default NULL,
  ATTACHMENT_NAME varchar2(100) default NULL,
  MSG_USER_NAME varchar2(100) default NULL,
  READER_UID varchar2(3000) default NULL,
  MSG_CONTENT varchar2(3000) default NULL,
  MSG_CONTENT_SIMPLE varchar2(3000) default NULL,
  ATTACHMENT_ID2 varchar2(300) default NULL,
  ATTACHMENT_NAME2 varchar2(300) default NULL
) ;
CREATE TABLE  weixun_share (
  SEQ_ID NUMBER  primary key,
  USER_ID NUMBER default '0',
  CONTENT CLOB,
  ADD_TIME varchar2(45) default NULL,
  TOPICS varchar2(255) default NULL,
  MENTIONED_IDS varchar2(200) default NULL,
  BROADCAST_IDS varchar2(500) default NULL
);
CREATE TABLE  im_offline_file (
  ID NUMBER  primary key,
  TIME date default NULL,
  SRC_UID NUMBER default NULL,
  DEST_UID NUMBER default NULL,
  FILE_NAME varchar2(1000) default NULL,
  FILE_SIZE NUMBER  default NULL,
  FLAG NUMBER default '0'
);
CREATE TABLE  weixun_share_topic (
  SEQ_ID NUMBER  primary key,
  TOPIC_NAME varchar2(255) NOT NULL
);

DELETE FROM DS_FIELD WHERE TABLE_NO = '90505';
delete from ds_table where TABLE_NO = '90505';

DELETE FROM DS_FIELD WHERE TABLE_NO = '90506';
delete from ds_table where TABLE_NO = '90506';
Insert into ds_table (TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values('90506', 'MESSAGE_BODY', 'sss', '1', '', 'T9MessageBody');
Insert into ds_table (TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values('90505', 'MESSAGE', 's', '1', '', 'T9Message');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505006', 'REMIND_TIME', 'remindTime', 'ss', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505005', 'BODY_SEQ_ID', 'bodySeqId', 'ss', '90506', '', '90506001', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505004', 'DELETE_FLAG', 'deleteFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505003', 'REMIND_FLAG', 'remindFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ('90505', '90505002', 'TO_ID', 'toId', 'ss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '0', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90505', '90505001', 'SEQ_ID', 'seqId', 'sssss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506006', 'REMIND_URL', 'remindUrl', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ('90506', '90506005', 'SEND_TIME', 'sendTime', 's', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506004', 'CONTENT', 'content', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ( '90506', '90506003', 'MESSAGE_TYPE', 'messageType', 's', '', '', '', '', '', '', '', 'text', '', '', 200, 0, 12, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ('90506', '90506002', 'FROM_ID', 'fromId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field (TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values ('90506', '90506001', 'SEQ_ID', 'seqId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');

exec pr_CreateIdentityColumn('message_body','SEQ_ID');
exec pr_CreateIdentityColumn('weixun_share_topic','SEQ_ID');
exec pr_CreateIdentityColumn('weixun_share','SEQ_ID');
exec pr_CreateIdentityColumn('im_offline_file','ID');
exec pr_CreateIdentityColumn('im_group_msg','MSG_ID');
exec pr_CreateIdentityColumn('im_group_maxmsgid','MSG_ID');
exec pr_CreateIdentityColumn('im_group','GROUP_ID');
exec pr_CreateIdentityColumn('message','SEQ_ID');


delete from  sys_function  where menu_id = '020802';
update sys_function set func_name='手机短信管理' , func_code='/core/funcs/mobilesms/index.jsp' ,menu_id='0207' where menu_id = '020804';
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('0209','精灵群管理','/core/frame/ispirit/n12/group/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('2001','微讯分享','/core/funcs/message/jpanel/weixun_share/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('6051','性能优化','/core/funcs/youhua/index.jsp','sys.gif','0');
 
 ALTER TABLE email_body ADD (TO_WEBMAIL_COPY CLOB);
 ALTER TABLE email_body ADD (TO_WEBMAIL_SECRET CLOB);
 ALTER TABLE webmail_body ADD (TO_MAIL_COPY CLOB);
 ALTER TABLE webmail_body ADD (TO_MAIL_SECRET CLOB);
 ALTER TABLE webmail_body ADD (DELETE_FLAG VARCHAR2(45));
 
 update sys_function set func_name='消息管理' , func_code='/core/funcs/sms/index11.jsp' , menu_id='0206' where menu_id = '0208';