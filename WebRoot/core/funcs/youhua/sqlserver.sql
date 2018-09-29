create index Index_3 on flow_run_data (RUN_ID);
create index FLOW_RUN_Index on flow_run (RUN_ID, FLOW_ID, BEGIN_USER, DEL_FLAG);
create index FLOW_RUN_PRCS on flow_run_prcs (RUN_ID, PRCS_FLAG, PRCS_ID, OP_FLAG, USER_ID, FLOW_PRCS, CHILD_RUN);
create index FLOW_TYPE_INDEX on flow_type (FLOW_SORT, FLOW_TYPE);
create index flow_run_log_INDEX on flow_run_log (RUN_ID, FLOW_ID, PRCS_ID, USER_ID);
create index flow_process_index on flow_process (FLOW_SEQ_ID, PRCS_ID);
create index flow_form_item_index on flow_form_item (FORM_ID, ITEM_ID);

create index doc_flow_run_Index_3 on doc_flow_run_data (RUN_ID);
create index DOC_RUN_Index on doc_run (RUN_ID, FLOW_ID, BEGIN_USER, DEL_FLAG);
create index doc_FLOW_RUN_PRCS on doc_flow_run_prcs (RUN_ID, PRCS_FLAG, PRCS_ID, OP_FLAG, USER_ID, FLOW_PRCS, CHILD_RUN);
create index doc_FLOW_TYPE_INDEX on doc_flow_type (FLOW_SORT, FLOW_TYPE);
create index doc_flow_run_log_INDEX on doc_flow_run_log (RUN_ID, FLOW_ID, PRCS_ID, USER_ID);
create index doc_flow_process_index on doc_flow_process (FLOW_SEQ_ID, PRCS_ID);
create index doc_flow_form_item_index on doc_flow_form_item (FORM_ID, ITEM_ID);

create index email_to_id on email (TO_ID);
create index email_delete_flag on email (DELETE_FLAG);
create index email_body_id on email (BODY_ID);
create index email_from_id on email_body (FROM_ID);
create index email_copy_to_id on email_body (COPY_TO_ID);
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