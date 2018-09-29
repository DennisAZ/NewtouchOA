

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
