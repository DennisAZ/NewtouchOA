alter table doc_receive add REC_DOC_ID VARCHAR(200);
alter table doc_receive add REC_DOC_NAME VARCHAR(1000);
alter table doc_flow_process add DOC_SEND_FLAG VARCHAR(200);
alter table doc_flow_run add SEND_FLAG VARCHAR(1000);
alter table vote_title add SUBJECT_MAIN TEXT;
update SYS_FUNCTION set FUNC_CODE = '/core/funcs/doc/docword/index.jsp' where MENU_ID = '221747';
ALTER TABLE flow_form_item ADD  LV_ALIGN TEXT ;

ALTER TABLE flow_form_item ADD  SIGN_COLOR VARCHAR(45) ;
ALTER TABLE flow_form_item ADD  SIGN_TYPE VARCHAR(45) ;

 ALTER TABLE vehicle  ADD  insurance_date DATETIME;
 ALTER TABLE vehicle  ADD  before_day int;
 ALTER TABLE vehicle  ADD  last_insurance_date DATETIME;
 ALTER TABLE vehicle  ADD  insurance_flag int;
 
 delete from sys_function where func_name = '机要文件夹 '