CREATE TABLE  DEPT_GROUP(
  SEQ_ID NUMBER  primary key,
  GROUP_NAME VARCHAR2(200),
  ORDER_NO VARCHAR2(45),
  DEPT_STR CLOB
);
exec pr_CreateIdentityColumn('DEPT_GROUP','SEQ_ID');
CREATE TABLE  seclog (
  SEQ_ID NUMBER primary key,
  USER_SEQ_ID VARCHAR2(200),
  OP_TIME DATE,
  CLIENT_IP VARCHAR2(20),
  OP_TYPE VARCHAR2(10),
  OP_OBJECT CLOB,
  OP_DESC CLOB,
  user_name VARCHAR2(200),
  op_result VARCHAR2(45)
);
exec pr_CreateIdentityColumn('seclog','SEQ_ID');
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('221749','部门组管理','/core/funcs/doc/group/index.jsp','sys.gif','0');

 
