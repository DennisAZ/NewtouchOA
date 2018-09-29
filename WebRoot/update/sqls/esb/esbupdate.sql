INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES 
 ('8021','修改密码','/core/funcs/setdescktop/pass/index.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES 
 ('8001','用户管理','/core/esb/server/user/index.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES 
 ('8005','任务状态查询','/core/esb/server/taskstatus/index.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('8010','系统配置与分发','/core/esb/server/sysConfig/sysClientConfig.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('8015','本地配置','/core/esb/server/clientConfig/clientConfig.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('8020','收发文件','/core/esb/server/demo/index.jsp','sys.gif','0');
 INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('8025','系统信息','/core/esb/server/sysinfo/','sys.gif','0');

INSERT INTO sys_menu ( MENU_ID, MENU_NAME, IMAGE) VALUES ('80','数据交换平台','edit.gif');
CREATE TABLE esb_sys_msg (
  seq_id int(11) unsigned NOT NULL auto_increment,
  guid varchar(45) NOT NULL,
  to_id varchar(200) default NULL,
  content text,
  time datetime default NULL,
  status varchar(1) NOT NULL default '0',
  PRIMARY KEY  (seq_id)
);
CREATE TABLE esb_transfer (
  seq_id int(11) unsigned NOT NULL auto_increment,
  from_id int(11) unsigned NOT NULL,
  file_path varchar(1000) NOT NULL,
  content text,
  status varchar(1) NOT NULL,
  guid varchar(45) default NULL,
  type varchar(1) NOT NULL default '0',
  create_time datetime default NULL,
  to_id varchar(200) NOT NULL,
  complete_time datetime default NULL,
  failed_message varchar(200) default NULL,
  PRIMARY KEY  (seq_id)
) ;
CREATE TABLE esb_transfer_status (
  seq_id int(10) unsigned NOT NULL auto_increment,
  trans_id varchar(45) NOT NULL,
  status varchar(1) NOT NULL default '0',
  to_id int(11) unsigned NOT NULL,
  create_time datetime default NULL,
  complete_time datetime default NULL,
  failed_message varchar(200) default NULL,
  PRIMARY KEY  (seq_id)
);
CREATE TABLE td_user (
  seq_id int(10) unsigned NOT NULL auto_increment,
  user_code varchar(30) default NULL,
  user_name varchar(50) default NULL,
  password varchar(50) default NULL,
  description varchar(100) default NULL,
  app_id int(10) unsigned default NULL,
  user_type int(10) unsigned default NULL,
  status int(10) unsigned default NULL,
  is_online int(10) unsigned default NULL,
  online_Ip varchar(50) default NULL,
  PRIMARY KEY  USING BTREE (seq_id)
);
CREATE TABLE ESB_UPLOAD_TASK (
  SEQ_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  FILE_NAME VARCHAR(1000),
  GUID VARCHAR(200),
  STATUS INTEGER,
  TO_ID text,
  OPT_GUID VARCHAR(200),
  PRIMARY KEY (SEQ_ID)
)
ENGINE = InnoDB;
CREATE TABLE ESB_DOWN_TASK (
  SEQ_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  FILE_NAME VARCHAR(1000),
  GUID VARCHAR(200),
  STATUS INTEGER,
  FROM_ID VARCHAR(200),
  PRIMARY KEY (SEQ_ID)
)
ENGINE = InnoDB;

ALTER TABLE esb_upload_task ADD COLUMN MESSAGE TEXT;
ALTER TABLE esb_down_task ADD COLUMN MESSAGE TEXT;
ALTER TABLE esb_transfer ADD COLUMN MESSAGE TEXT;
ALTER TABLE esb_down_task ADD COLUMN OPT_GUID  VARCHAR(200);
ALTER TABLE esb_transfer ADD COLUMN OPT_GUID  VARCHAR(200);

