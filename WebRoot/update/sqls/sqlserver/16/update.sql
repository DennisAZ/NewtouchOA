CREATE INDEX flow_run_data_index ON flow_run_data (RUN_ID);
CREATE INDEX flow_run_index  ON flow_run (RUN_ID, FLOW_ID, BEGIN_USER, DEL_FLAG);
CREATE INDEX flow_run_prcs_index  ON flow_run_prcs (RUN_ID, PRCS_FLAG, PRCS_ID, OP_FLAG, USER_ID, FLOW_PRCS, CHILD_RUN);
CREATE INDEX flow_type_index  ON flow_type (FLOW_SORT, FLOW_TYPE);
CREATE INDEX flow_run_log_index  ON flow_run_log (RUN_ID, FLOW_ID, PRCS_ID, USER_ID);
CREATE INDEX flow_process_index  ON flow_process (FLOW_SEQ_ID, PRCS_ID);
CREATE INDEX flow_form_item_index  ON flow_form_item (FORM_ID, ITEM_ID);

INSERT INTO code_item (CLASS_NO,CLASS_CODE,CLASS_DESC,SORT_NO,CODE) VALUES ('SMS_REMIND','70','公文收文提醒','70',NULL);
update sys_function set FUNC_CODE = '/core/funcs/doc/flowrun/list/index.jsp?sortName=%25E5%258F%2591%25E6%2596%2587' WHERE FUNC_NAME = '发文办理';
update sys_function set FUNC_NAME = '发文承办' WHERE FUNC_NAME = '收文办理';
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221620' , '收文办理' , '/core/funcs/doc/flowrunRec/list/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221748' , '收文权限设置' , '/core/funcs/doc/docRecvPriv/index.jsp', 'edit.gif' , '0');
CREATE TABLE doc_recv_priv (
  SEQ_ID int  IDENTITY(1, 1) PRIMARY KEY,
  dept_id int ,
  USER_ID varchar(400)
);
alter table doc_receive add SEND_RUN_ID int  DEFAULT 0;
alter table flow_run_log ALTER COLUMN IP VARCHAR(50) ;
alter table doc_flow_run_log ALTER COLUMN IP VARCHAR(50) ;