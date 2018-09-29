CREATE TABLE FLOW_REPORT
( SEQ_ID NUMBER,
  TID NUMBER(12, 0),
  FLOW_ID NUMBER(12, 0),
  R_NAME VARCHAR2(100),
  LIST_ITEM CLOB,
  QUERY_ITEM CLOB,
  CREATEUSER VARCHAR2(20),
  CREATEDATE DATE,
  GROUP_TYPE VARCHAR2(10),
  GROUP_FIELD VARCHAR2(20)
)
;
exec pr_CreateIdentityColumn('FLOW_REPORT','SEQ_ID');
CREATE TABLE FLOW_REPORT_PRIV
( SEQ_ID NUMBER,
  RID NUMBER,
  USER_STR CLOB,
  DEPT_STR CLOB
)
;
exec pr_CreateIdentityColumn('FLOW_REPORT_PRIV','SEQ_ID');
CREATE TABLE flow_hook (
  seq_id NUMBER,
  flow_id NUMBER NOT NULL,
  hname VARCHAR2(40) NOT NULL,
  hdesc VARCHAR2(200) NOT NULL,
  hmodule VARCHAR2(40) NOT NULL,
  plugin VARCHAR2(100) NOT NULL,
  status NUMBER default '0',
  map CLOB NOT NULL,
  condition CLOB NOT NULL,
  condition_set CLOB NOT NULL,
  system VARCHAR2(1) default '1'
);
exec pr_CreateIdentityColumn('flow_hook','SEQ_ID');
CREATE TABLE flow_run_hook (
  SEQ_ID NUMBER,
  run_id NUMBER NOT NULL,
  module VARCHAR2(40) NOT NULL,
  field VARCHAR2(40) NOT NULL,
  key_id NUMBER NOT NULL
);
exec pr_CreateIdentityColumn('flow_run_hook','SEQ_ID');
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
INSERT INTO hr_code (CODE_NO,CODE_NAME,CODE_ORDER,PARENT_NO,CODE_FLAG) VALUES ('HR_RECRUIT_FILTER','初选方式','1','','0');
INSERT INTO hr_code (CODE_NO,CODE_NAME,CODE_ORDER,PARENT_NO,CODE_FLAG) VALUES ('1','笔试','1','HR_RECRUIT_FILTER','0');
INSERT INTO hr_code (CODE_NO,CODE_NAME,CODE_ORDER,PARENT_NO,CODE_FLAG) VALUES ('2','面试','2','HR_RECRUIT_FILTER','0');
 
 INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (1,0,'请假登记','请假登记引擎','attend_leave','T9AttendLeave',0,'','','','1');
 INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES  (5,0,'办公用品领用','办公用品领用引擎','office_product_draw','T9OfficeProductDraw',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (6,0,'办公用品借用','办公用品借用引擎','office_product_borrow','T9OfficeProductBorrow',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (7,0,'办公用品归还','办公用品归还引擎','office_product_return','T9OfficeProductReturn',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (8,0,'加班登记','加班登记引擎','attendance_overtime','T9AttendanceOvertime',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (9,0,'出差登记','出差登记引擎','attend_evection','T9AttendEvection',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (10,0,'外出登记','外出登记引擎','attend_out','T9AttendOut',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (11,0,'会议申请','会议申请引擎','meeting_apply','T9MeetingApply',0,'','','','1');
  INSERT INTO flow_hook (seq_id,flow_id,hname,hdesc,hmodule,plugin,status,map,condition,condition_set,system) VALUES (12,0,'车辆申请','车辆申请引擎','vehicle_apply','T9VehicleApply',0,'','','','1');