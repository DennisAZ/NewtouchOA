ALTER TABLE doc_receive ADD (REC_DOC_ID VARCHAR2(200));
ALTER TABLE doc_receive ADD (REC_DOC_NAME VARCHAR2(1000));

ALTER TABLE doc_flow_process ADD (REC_DOC_ID VARCHAR2(10));
ALTER TABLE doc_flow_run ADD (SEND_FLAG VARCHAR2(45));
ALTER TABLE doc_flow_process ADD (DOC_SEND_FLAG VARCHAR2(45));

ALTER TABLE vote_title ADD (SUBJECT_MAIN CLOB);
update SYS_FUNCTION set FUNC_CODE = '/core/funcs/doc/docword/index.jsp' where MENU_ID = '221747';

ALTER TABLE flow_form_item ADD (LV_ALIGN CLOB);
ALTER TABLE flow_form_item ADD (SIGN_COLOR VARCHAR2(45));
ALTER TABLE flow_form_item ADD (SIGN_TYPE VARCHAR2(45));

ALTER TABLE vehicle ADD (insurance_date DATE);
ALTER TABLE vehicle ADD (before_day NUMBER);
ALTER TABLE vehicle ADD (last_insurance_date DATE);
ALTER TABLE vehicle ADD (insurance_flag NUMBER);
 
 delete from sys_function where func_name = '机要文件夹 '