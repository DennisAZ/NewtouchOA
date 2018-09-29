CREATE TABLE  DEPT_GROUP (
  SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  GROUP_NAME varchar(200) default NULL,
  ORDER_NO varchar(45) ,
  DEPT_STR text
) ;

INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('221749','部门组管理','/core/funcs/doc/group/index.jsp','sys.gif','0');
