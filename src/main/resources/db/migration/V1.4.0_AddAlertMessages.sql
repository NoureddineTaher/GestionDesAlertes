ALTER TABLE appinfo_sch.alert ADD (start_sms varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (end_sms varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (start_subject varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (start_body varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (end_subject varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (end_body varchar2(512));

ALTER TABLE appinfo_sch.alert ADD (astreinte varchar2(512));

ALTER TABLE appinfo_sch.alert MODIFY (name varchar2(512));

ALTER TABLE appinfo_sch.alert MODIFY (description varchar2(512));