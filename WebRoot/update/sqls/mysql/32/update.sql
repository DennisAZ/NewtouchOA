CREATE TABLE DEPT_GROUP (
  SEQ_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  GROUP_NAME VARCHAR(200),
  ORDER_NO VARCHAR(45),
  DEPT_STR TEXT,
  PRIMARY KEY (SEQ_ID)
)
ENGINE = InnoDB;
INSERT INTO sys_function (menu_id, func_name, func_code, icon, open_flag) VALUES
 ('221749','部门组管理','/core/funcs/doc/group/index.jsp','sys.gif','0');



 
