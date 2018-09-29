INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
('600411','外部机构管理','/core/esb/client/org/index.jsp','sys.gif','0');
CREATE TABLE ext_dept (
   SEQ_ID NUMBER  primary key,
  DEPT_ID VARCHAR2(100) ,
  DEPT_NAME VARCHAR2(50)  DEFAULT '',
  ESB_USER VARCHAR2(50)  DEFAULT '',
  DEPT_NO VARCHAR2(200) DEFAULT '0',
  DEPT_PARENT VARCHAR2(100) DEFAULT '0',
  DEPT_DESC VARCHAR2(200) DEFAULT '0',
  SYNC_STATE VARCHAR2(1) DEFAULT '0'
);

CREATE TABLE ESB_REC_PERSON (
  SEQ_ID NUMBER  primary key,
  USER_ID CLOB,
  DEPT_ID CLOB,
  USER_PRIV CLOB
);
exec pr_CreateIdentityColumn('ext_dept','SEQ_ID');
exec pr_CreateIdentityColumn('ESB_REC_PERSON','SEQ_ID');