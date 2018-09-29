INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('600411','外部机构管理','/core/esb/client/org/index.jsp','sys.gif','0');
CREATE TABLE ext_dept (
  SEQ_ID int(11) NOT NULL AUTO_INCREMENT,
  DEPT_ID varchar(100) not null,
  DEPT_NAME varchar(50) NOT NULL DEFAULT '',
  ESB_USER varchar(50) NOT NULL DEFAULT '',
  DEPT_NO varchar(200) NOT NULL DEFAULT '0',
  DEPT_PARENT varchar(100) NOT NULL DEFAULT '0',
  DEPT_DESC varchar(200) NOT NULL DEFAULT '0',
  SYNC_STATE char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (SEQ_ID)
);
CREATE TABLE ESB_REC_PERSON (
  SEQ_ID INTEGER UNSIGNED AUTO_INCREMENT,
  USER_ID TEXT,
  DEPT_ID TEXT,
  USER_PRIV TEXT,
  PRIMARY KEY (SEQ_ID)
);