ALTER TABLE flow_run_data ADD INDEX Index_3(RUN_ID);
ALTER TABLE flow_run ADD INDEX FLOW_RUN_Index(RUN_ID, FLOW_ID, BEGIN_USER, DEL_FLAG);
ALTER TABLE flow_run_prcs ADD INDEX FLOW_RUN_PRCS(RUN_ID, PRCS_FLAG, PRCS_ID, OP_FLAG, USER_ID, FLOW_PRCS, CHILD_RUN);
ALTER TABLE flow_type ADD INDEX FLOW_TYPE_INDEX(FLOW_SORT, FLOW_TYPE);
ALTER TABLE flow_run_log ADD INDEX flow_run_log_INDEX(RUN_ID, FLOW_ID, PRCS_ID, USER_ID);
ALTER TABLE flow_process ADD INDEX flow_process_index(FLOW_SEQ_ID, PRCS_ID);
ALTER TABLE flow_form_item ADD INDEX flow_form_item_index(FORM_ID, ITEM_ID);

ALTER TABLE doc_flow_run_data ADD INDEX doc_flow_run_Index_3(RUN_ID);
ALTER TABLE doc_run ADD INDEX DOC_RUN_Index(RUN_ID, FLOW_ID, BEGIN_USER, DEL_FLAG);
ALTER TABLE doc_flow_run_prcs ADD INDEX doc_FLOW_RUN_PRCS(RUN_ID, PRCS_FLAG, PRCS_ID, OP_FLAG, USER_ID, FLOW_PRCS, CHILD_RUN);
ALTER TABLE doc_flow_type ADD INDEX doc_FLOW_TYPE_INDEX(FLOW_SORT, FLOW_TYPE);
ALTER TABLE doc_flow_run_log ADD INDEX doc_flow_run_log_INDEX(RUN_ID, FLOW_ID, PRCS_ID, USER_ID);
ALTER TABLE doc_flow_process ADD INDEX doc_flow_process_index(FLOW_SEQ_ID, PRCS_ID);
ALTER TABLE doc_flow_form_item ADD INDEX doc_flow_form_item_index(FORM_ID, ITEM_ID);

create index email_delete_flag on email (DELETE_FLAG);
create index BODY_ID on email (BODY_ID);
create index email_from_id on email_body (FROM_ID);
create index email_to_id on email (TO_ID);
create index email_to_id on email (TO_ID);
create index email_send_time on email_body (SEND_TIME);
create index email_is_webmail on email_body (IS_WEBMAIL);
create index email_from_webmail on email_body (FROM_WEBMAIL);
create index webmail_body_bodyId on webmail_body (BODY_ID);
create index webmail_body_deleteFlag on webmail_body (DELETE_FLAG);

create index index_sms_body_formid on sms_body (FROM_ID);
create index index_sms_body_smstype on sms_body (SMS_TYPE);
create index index_sms_body_sendtime on sms_body (SEND_TIME);

create index index_sms_toid on sms (TO_ID);
create index index_sms_remindflag on sms (REMIND_FLAG);
create index index_sms_deleteflag on sms (DELETE_FLAG);
create index index_sms_bodyseqid on sms (BODY_SEQ_ID);
create index index_sms_remindtime on sms (REMIND_TIME);