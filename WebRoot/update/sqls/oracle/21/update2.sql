ALTER TABLE flow_form_type ADD (VERSION_NO NUMBER  default '1');
ALTER TABLE flow_form_type ADD (VERSION_TIME DATE );
ALTER TABLE flow_form_type ADD (FORM_ID NUMBER default '0');
ALTER TABLE flow_run ADD (FORM_VERSION NUMBER default '1');

create index email_to_id on email (TO_ID);
create index email_delete_flag on email (DELETE_FLAG);
create index email_body_id on email (BODY_ID);
create index email_from_id on email_body (FROM_ID);
create index email_send_time on email_body (SEND_TIME);

update flow_form_type set version_no = '1';
update flow_run set FORM_VERSION = '1';
update flow_form_type set FORM_ID = '0';
