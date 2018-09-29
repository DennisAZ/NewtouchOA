CREATE TABLE email_name (
  SEQ_ID NUMBER,
  USER_ID NUMBER NOT NULL,
  name CLOB,
  IS_USE VARCHAR2(10) default '1'
);
exec pr_CreateIdentityColumn('email_name','SEQ_ID');
delete from code_item where CLASS_DESC = '图片新闻';
INSERT INTO code_item (CLASS_NO,CLASS_CODE,CLASS_DESC,SORT_NO,CODE) VALUES ('NEWS','11','图片新闻','1',NULL);
insert into port (FILE_NAME, DEPT_ID, STATUS, VIEW_TYPE, MODULE_LINES, MODULE_SCROLL)
values ('图片新闻.js', 0, 0, 1, 5, 0);
delete from sys_function where func_name = '机要文件夹';