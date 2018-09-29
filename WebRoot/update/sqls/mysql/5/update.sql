CREATE TABLE FLOW_REPORT_PRIV
( SEQ_ID  int primary key auto_increment,
  RID int,
  USER_STR text,
  DEPT_STR text
)
;
CREATE TABLE FLOW_REPORT
( SEQ_ID int primary key auto_increment,
  TID int ,
  FLOW_ID int ,
  R_NAME VARCHAR(100),
  LIST_ITEM text,
  QUERY_ITEM text,
  CREATEUSER VARCHAR(20),
  CREATEDATE DATE,
  GROUP_TYPE VARCHAR(10),
  GROUP_FIELD VARCHAR(20)
)
;
CREATE TABLE `flow_hook` (
  `seq_id` int(11) NOT NULL auto_increment,
  `flow_id` int(11) NOT NULL,
  `hname` varchar(40) ,
  `hdesc` varchar(200) ,
  `hmodule` varchar(40) ,
  `plugin` varchar(100),
  `status` int(1) NOT NULL default '0',
  `map` text ,
  `condition` text ,
  `condition_set` text,
  `system` varchar(1) NOT NULL default '1',
  PRIMARY KEY  (`seq_id`)
);
CREATE TABLE flow_run_hook (
  SEQ_ID int(11) NOT NULL auto_increment,
  run_id int(11) NOT NULL,
  module varchar(40) ,
  field varchar(40),
  key_id int(11) NOT NULL,
  PRIMARY KEY  (SEQ_ID)
);
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('600650' , '报表设置' , '/core/funcs/workflow/flowreport/index.jsp', 'edit.gif' , '0');
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('0471' , '数据报表' , '/core/funcs/workflow/report/index.jsp', 'edit.gif' , '0'); 
insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('600651' , '业务引擎设置' , '/core/funcs/workflow/flowhook/index.jsp', 'edit.gif' , '0');
 insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('0519' , '管理简报' , '/core/funcs/workstat/index.jsp', 'edit.gif' , '0');
   insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('100701' , '考勤审批' , '/core/funcs/attendance/manage/index.jsp', 'edit.gif' , '0');
    insert into sys_function (MENU_ID , FUNC_NAME , FUNC_CODE ,ICON ,OPEN_FLAG  )
   values ('100702' , '考勤统计' , '/core/funcs/attendance/manage/query/index.jsp', 'edit.gif' , '0');
  
INSERT INTO `hr_code` (`SEQ_ID`,`CODE_NO`,`CODE_NAME`,`CODE_ORDER`,`PARENT_NO`,`CODE_FLAG`) VALUES  
 (239,'HR_RECRUIT_FILTER','初选方式','1','','0'),
 (240,'1','笔试','1','HR_RECRUIT_FILTER','0'),
 (241,'2','面试','2','HR_RECRUIT_FILTER','0');
 ALTER TABLE `t9`.`webmail_body` MODIFY COLUMN `CONTENT_HTML` MEDIUMTEXT DEFAULT NULL;
ALTER TABLE `t9`.`webmail_body` MODIFY COLUMN `REPLY_MAIL` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `TO_MAIL` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `CC_MAIL` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `SUBJECT` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `ATTACHMENT_ID` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `ATTACHMENT_NAME` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `FROM_MAIL` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `t9`.`webmail_body` MODIFY COLUMN `FROM_MAIL` VARCHAR(5000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE `t9`.`email_body` MODIFY COLUMN `SUBJECT` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `CONTENT` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `FROM_WEBMAIL` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `FROM_WEBMAIL_ID` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `RECV_FROM_NAME` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `RECV_FROM` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `RECV_TO_ID` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 MODIFY COLUMN `RECV_TO` VARCHAR(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

  INSERT INTO `flow_hook` (`seq_id`,`flow_id`,`hname`,`hdesc`,`hmodule`,`plugin`,`status`,`map`,`condition`,`condition_set`,`system`) VALUES 
 (1,0,'请假登记','请假登记引擎','attend_leave','T9AttendLeave',0,'','','','1'),
 (5,0,'办公用品领用','办公用品领用引擎','office_product_draw','T9OfficeProductDraw',0,'','','','1'),
 (6,0,'办公用品借用','办公用品借用引擎','office_product_borrow','T9OfficeProductBorrow',0,'','','','1'),
 (7,0,'办公用品归还','办公用品归还引擎','office_product_return','T9OfficeProductReturn',0,'','','','1'),
 (8,0,'加班登记','加班登记引擎','attendance_overtime','T9AttendanceOvertime',0,'','','','1'),
 (9,0,'出差登记','出差登记引擎','attend_evection','T9AttendEvection',0,'','','','1'),
 (10,0,'外出登记','外出登记引擎','attend_out','T9AttendOut',0,'','','','1'),
 (11,0,'会议申请','会议申请引擎','meeting_apply','T9MeetingApply',0,'','','','1'),
 (12,0,'车辆申请','车辆申请引擎','vehicle_apply','T9VehicleApply',0,'','','','1');