ALTER TABLE doc_receive ADD COLUMN SEND_RUN_ID INTEGER UNSIGNED DEFAULT 0;
CREATE TABLE `doc_recv_priv` (
  SEQ_ID int(11)  NOT NULL auto_increment,
  `dept_id` int(11) NOT NULL,
  `USER_ID` varchar(200) NOT NULL ,
  PRIMARY KEY  (SEQ_ID)
);
INSERT INTO code_item (CLASS_NO,CLASS_CODE,CLASS_DESC,SORT_NO,CODE) VALUES ('SMS_REMIND','70','公文收文提醒','70',NULL);
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221620' , '收文办理' , '/core/funcs/doc/flowrunRec/list/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221748' , '收文权限设置' , '/core/funcs/doc/docRecvPriv/index.jsp', 'edit.gif' , '0');
ALTER TABLE doc_flow_run_log MODIFY COLUMN IP VARCHAR(50)  DEFAULT NULL;
ALTER TABLE flow_run_log MODIFY COLUMN IP VARCHAR(50)  DEFAULT NULL;


