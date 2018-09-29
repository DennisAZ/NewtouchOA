CREATE TABLE email_name (
  SEQ_ID int   NOT NULL IDENTITY(1, 1) PRIMARY KEY,
  USER_ID int NOT NULL,
  NAME text NOT NULL,
  IS_USE char(1) NOT NULL default '1'
);
delete from code_item where CLASS_DESC = '图片新闻';
INSERT INTO code_item (CLASS_NO,CLASS_CODE,CLASS_DESC,SORT_NO,CODE) VALUES ('NEWS','11','图片新闻','1',NULL);
insert into port (FILE_NAME, DEPT_ID, STATUS, VIEW_TYPE, MODULE_LINES, MODULE_SCROLL)
values ('图片新闻.js', 0, 0, 1, 5, 0);
delete from sys_function where func_name = '机要文件夹';