CREATE TABLE doc_recv_priv (
  SEQ_ID NUMBER,
  dept_id NUMBER NOT NULL,
  USER_ID  VARCHAR2(200) NOT NULL 
);
exec pr_CreateIdentityColumn('doc_recv_priv','SEQ_ID');
INSERT INTO code_item (CLASS_NO,CLASS_CODE,CLASS_DESC,SORT_NO,CODE) VALUES ('SMS_REMIND','70','公文收文提醒','70',NULL);
update sys_function set FUNC_CODE = '/core/funcs/doc/flowrun/list/index.jsp?sortName=%25E5%258F%2591%25E6%2596%2587' WHERE FUNC_NAME = '发文办理';
update sys_function set FUNC_NAME = '发文承办' WHERE FUNC_NAME = '收文办理';
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221620' , '收文办理' , '/core/funcs/doc/flowrunRec/list/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221748' , '收文权限设置' , '/core/funcs/doc/docRecvPriv/index.jsp', 'edit.gif' , '0');

ALTER TABLE doc_receive ADD (SEND_RUN_ID NUMBER default '0');
ALTER TABLE doc_flow_run_log modify (IP VARCHAR2(50));
ALTER TABLE flow_run_log modify (IP VARCHAR2(50));
