ALTER TABLE flow_process ADD COLUMN AUTO_SELECT_ROLE VARCHAR(45) ;
ALTER TABLE email_body MODIFY COLUMN COMPRESS_CONTENT MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE flow_print_tpl MODIFY COLUMN CONTENT MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

ALTER TABLE flow_process ADD COLUMN DOC_CREATE VARCHAR(45) ,
 ADD COLUMN DOC_ATTACH_PRIV VARCHAR(45) ;
 
delete from sys_function 
where menu_id = '221718'
or menu_id = '221719' 
or menu_id = '221716'
or menu_id = '221513' 
or menu_id = '221715'
OR MENU_ID = '221714';

insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221701' , '公文表单设计' , '/core/funcs/doc/flowform/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221702' , '公文流程设计' , '/core/funcs/doc/flowtype/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221703' , '公文流程分类' , '/core/funcs/doc/flowsort/index.jsp', 'edit.gif' , '0');

update sys_function set FUNC_CODE = '/core/funcs/doc/send/index.jsp' WHERE FUNC_NAME = '发文拟稿';
update sys_function set FUNC_CODE = '/core/funcs/doc/flowrun/list/index.jsp' WHERE FUNC_NAME = '发文办理';
update sys_function set FUNC_CODE = '/core/funcs/doc/receive/docindex.jsp' WHERE FUNC_NAME = '收文登记';
update sys_function set FUNC_CODE = '/core/funcs/doc/receive/index.jsp' WHERE FUNC_NAME = '收文办理';

insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221744' , '文件字管理' , '/core/funcs/doc/flowrun/docword/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221746' , '文件类型管理' , '/core/funcs/doc/flowrun/documentsType/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  ) 
values ('221747' , '主题词管理' , '/subsys/inforesource/docmgr/docword/index.jsp', 'edit.gif' , '0');

insert into port (FILE_NAME, DEPT_ID, STATUS, VIEW_TYPE, MODULE_LINES, MODULE_SCROLL)
values ('在办发文.js', 0, 0, 1, 5, 0);
insert into port (FILE_NAME, DEPT_ID, STATUS, VIEW_TYPE, MODULE_LINES, MODULE_SCROLL)
values ('待办发文.js', 0, 0, 1, 5, 0);
delete from  port where FILE_NAME = '待办公文.js';
DROP TABLE IF EXISTS doc_flow_run;
CREATE TABLE `doc_flow_run` (
  `RUN_ID` int(11) NOT NULL,
  `DOC_ID` varchar(200) default NULL,
  `DOC_NAME` varchar(200) default NULL,
  `SEQ_ID` int(11) NOT NULL auto_increment,
  `DOC_STYLE` varchar(200) default NULL,
  `DOC_NUM` int(10) unsigned default NULL,
  `DOC` text,
  `DOC_YEAR` varchar(45) default NULL,
  `DOC_WORD` int(10) unsigned default NULL,
  `DRAFT_TIME` datetime default NULL,
  `WRITTEN_TIME` datetime default NULL,
  `TO_FILE_TIME` datetime default NULL,
  `DOC_TYPE` int(10) unsigned default NULL,
  PRIMARY KEY  (`RUN_ID`),
  UNIQUE KEY `SEQ_ID` (`SEQ_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=173 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS subject_term;
CREATE TABLE `subject_term` (
  `seq_id` int(10) unsigned NOT NULL auto_increment,
  `word` varchar(4000) default NULL,
  `parent_id` int(10) unsigned default '0',
  `sort_no` int(10) unsigned default NULL,
  `type_flag` int(10) unsigned default NULL,
  PRIMARY KEY  (`seq_id`)
) ENGINE=MyISAM AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;