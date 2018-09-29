INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('600411','外部机构管理','/core/esb/client/org/index.jsp','sys.gif','0');
CREATE TABLE ext_dept (
 SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  DEPT_ID varchar(100) ,
  DEPT_NAME varchar(50)  DEFAULT '',
  ESB_USER varchar(50)  DEFAULT '',
  DEPT_NO varchar(200)  DEFAULT '0',
  DEPT_PARENT varchar(100)  DEFAULT '0',
  DEPT_DESC varchar(200)  DEFAULT '0',
  SYNC_STATE char(1)  DEFAULT '0'
) ;

CREATE TABLE ESB_REC_PERSON (
  SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  USER_ID TEXT,
  DEPT_ID TEXT,
  USER_PRIV TEXT
);