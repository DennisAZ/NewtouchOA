alter table flow_form_type add VERSION_NO int  default 1;
alter table flow_form_type add VERSION_TIME DATETIME;
alter table flow_form_type add FORM_ID int  default  0;
alter table flow_run add FORM_VERSION int default 1;

update flow_form_type set VERSION_NO = 1;
update flow_form_type set FORM_ID = 0;
update flow_run set FORM_VERSION = 1;