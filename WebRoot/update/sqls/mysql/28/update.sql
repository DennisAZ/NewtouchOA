DROP TABLE IF EXISTS message_body;
CREATE TABLE  message_body (
  SEQ_ID int(11) NOT NULL auto_increment,
  FROM_ID int(11) default NULL,
  MESSAGE_TYPE varchar(200) default NULL,
  CONTENT text,
  SEND_TIME datetime default NULL,
  REMIND_URL text,
  PRIMARY KEY  (SEQ_ID),
  UNIQUE KEY SEQ_ID (SEQ_ID)
) ENGINE=MyISAM AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS message;
CREATE TABLE  message (
  SEQ_ID int(11) NOT NULL auto_increment,
  TO_ID int(11) default NULL,
  REMIND_FLAG char(1) default NULL,
  DELETE_FLAG char(1) default '0',
  BODY_SEQ_ID int(11) default NULL,
  REMIND_TIME datetime default NULL,
  PRIMARY KEY  (SEQ_ID),
  UNIQUE KEY SEQ_ID (SEQ_ID)
) ENGINE=MyISAM AUTO_INCREMENT=1527 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS im_group;
CREATE TABLE  im_group (
  GROUP_ID int(10) unsigned NOT NULL auto_increment,
  GROUP_NAME varchar(100) default NULL,
  GROUP_UID varchar(6000) default NULL,
  GROUP_ACTIVE varchar(20) default '1',
  ORDER_NO varchar(20) default NULL,
  REMARK varchar(300) default NULL,
  GROUP_CREATOR int(10) unsigned default NULL,
  GROUP_CREATE_TIME varchar(45) default NULL,
  GROUP_INTRODUCTION varchar(3000) default NULL,
  GROUP_SUBJECT varchar(1000) default NULL,
  PRIMARY KEY  USING BTREE (GROUP_ID),
  KEY ORDER_NO (ORDER_NO)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS im_group_maxmsgid;
CREATE TABLE  im_group_maxmsgid (
  MSG_ID int(10) unsigned NOT NULL auto_increment,
  GROUP_MEMBER_UID varchar(200) default NULL,
  GROUP_ID varchar(45) default NULL,
  MAX_MSG_ID varchar(45) default NULL,
  PRIMARY KEY  USING BTREE (MSG_ID)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS im_group_msg;
CREATE TABLE  im_group_msg (
  MSG_ID int(10) NOT NULL auto_increment,
  MSG_UID int(10) default NULL,
  MSG_TIME varchar(45) default NULL,
  MSG_GROUP_ID int(10) default NULL,
  ATTACHMENT_ID varchar(100) default NULL,
  ATTACHMENT_NAME varchar(100) default NULL,
  MSG_USER_NAME varchar(100) default NULL,
  READER_UID varchar(3000) default NULL,
  MSG_CONTENT varchar(3000) default NULL,
  MSG_CONTENT_SIMPLE varchar(3000) default NULL,
  ATTACHMENT_ID2 varchar(300) default NULL,
  ATTACHMENT_NAME2 varchar(300) default NULL,
  PRIMARY KEY  USING BTREE (MSG_ID),
  KEY MSG_GROUP_ID (MSG_GROUP_ID),
  KEY MSG_TIME (MSG_TIME)
) ENGINE=MyISAM AUTO_INCREMENT=77 DEFAULT CHARSET=gbk;


DROP TABLE IF EXISTS im_offline_file;
CREATE TABLE  im_offline_file (
  ID int(11) NOT NULL auto_increment,
  TIME datetime default NULL,
  SRC_UID int(11) default NULL,
  DEST_UID int(11) default NULL,
  FILE_NAME varchar(1000) default NULL,
  FILE_SIZE bigint(20) unsigned default NULL,
  FLAG tinyint(1) default '0',
  PRIMARY KEY  (ID)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS weixun_share;
CREATE TABLE  weixun_share (
  SEQ_ID int(11) NOT NULL auto_increment,
  USER_ID int(11) default '0',
  CONTENT text,
  ADD_TIME varchar(45) default NULL,
  TOPICS varchar(255) default NULL,
  MENTIONED_IDS varchar(200) default NULL,
  BROADCAST_IDS varchar(500) default NULL,
  PRIMARY KEY  USING BTREE (SEQ_ID)
) ENGINE=MyISAM AUTO_INCREMENT=110 DEFAULT CHARSET=gbk;

DROP TABLE IF EXISTS weixun_share_topic;
CREATE TABLE  weixun_share_topic (
  SEQ_ID int(10) unsigned NOT NULL auto_increment,
  TOPIC_NAME varchar(255) NOT NULL,
  PRIMARY KEY  USING BTREE (SEQ_ID)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=gbk;
Insert into ds_table (SEQ_ID, TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values(946, '90506', 'MESSAGE_BODY', 'sss', '1', '', 'T9MessageBody');
Insert into ds_table (SEQ_ID, TABLE_NO, TABLE_NAME, TABLE_DESC, CATEGORY_NO, DB_NO, CLASS_NAME) values(945, '90505', 'MESSAGE', 's', '1', '', 'T9Message');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4622, '90505', '90505006', 'REMIND_TIME', 'remindTime', 'ss', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4621, '90505', '90505005', 'BODY_SEQ_ID', 'bodySeqId', 'ss', '90506', '', '90506001', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4620, '90505', '90505004', 'DELETE_FLAG', 'deleteFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4619, '90505', '90505003', 'REMIND_FLAG', 'remindFlag', 'ss', '', '', '', '', '', '', '', 'text', '', '', 1, 0, 1, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4618, '90505', '90505002', 'TO_ID', 'toId', 'ss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '0', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4617, '90505', '90505001', 'SEQ_ID', 'seqId', 'sssss', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4616, '90506', '90506006', 'REMIND_URL', 'remindUrl', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4615, '90506', '90506005', 'SEND_TIME', 'sendTime', 's', '', '', '', '', '', '', '', 'text', '', '', 19, 0, 93, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4614, '90506', '90506004', 'CONTENT', 'content', 's', '', '', '', '', '', '', '', 'text', '', '', 21845, 0, -1, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4613, '90506', '90506003', 'MESSAGE_TYPE', 'messageType', 's', '', '', '', '', '', '', '', 'text', '', '', 200, 0, 12, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4612, '90506', '90506002', 'FROM_ID', 'fromId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '0', 0, '1', '0', 'null');
Insert into ds_field (SEQ_ID, TABLE_NO, FIELD_NO, FIELD_NAME, PROP_NAME, FIELD_DESC, FK_TABLE_NO, FK_TABLE_NO2, FK_RELA_FIELD_NO, FK_NAME_FIELD_NO, FK_FILTER, CODE_CLASS, DEFAULT_VALUE, FORMAT_MODE, FORMAT_RULE, ERROR_MSRG, FIELD_PRECISION, FIELD_SCALE, DATA_TYPE, IS_IDENTITY, DISPLAY_LEN, IS_MUST_FILL, IS_PRIMARY_KEY, FK_NAME_FIELD_NO2) values (4611, '90506', '90506001', 'SEQ_ID', 'seqId', 's', '', '', '', '', '', '', '', 'number', '', '', 11, 0, 4, '1', 0, '1', '1', 'null');

update sys_function set func_name='消息管理' , func_code='/core/funcs/sms/index11.jsp' , menu_id='0206' where menu_id = '0208';
delete from  sys_function  where menu_id = '020802';
update sys_function set func_name='手机短信管理' , func_code='/core/funcs/mobilesms/index.jsp' ,menu_id='0207' where menu_id = '020804';
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('0209','精灵群管理','/core/frame/ispirit/n12/group/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('2001','微讯分享','/core/funcs/message/jpanel/weixun_share/index.jsp','sys.gif','0');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('6051','性能优化','/core/funcs/youhua/index.jsp','sys.gif','0');

 
 ALTER TABLE email_body ADD COLUMN TO_WEBMAIL_COPY TEXT AFTER IS_WEBMAIL,
 ADD COLUMN TO_WEBMAIL_SECRET TEXT AFTER TO_WEBMAIL_COPY;
 ALTER TABLE webmail_body ADD COLUMN TO_MAIL_COPY TEXT AFTER FROM_MAIL,
 ADD COLUMN TO_MAIL_SECRET TEXT AFTER TO_MAIL_COPY;
ALTER TABLE webmail_body ADD COLUMN DELETE_FLAG VARCHAR(45);