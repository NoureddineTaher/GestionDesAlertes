--Creation des sequences
create sequence appinfo_sch.alarm_event_seq start with 1 increment by  1;
create sequence appinfo_sch.alarm_seq start with 1 increment by  1;
create sequence appinfo_sch.alert_event_seq start with 1 increment by  1;
create sequence appinfo_sch.alert_seq start with 1 increment by  1;
create sequence appinfo_sch.category_seq start with 1 increment by  1;
create sequence appinfo_sch.contact_seq start with 1 increment by  1;
create sequence appinfo_sch.diffusion_group_seq start with 1 increment by  1;
create sequence appinfo_sch.job_event_seq start with 1 increment by  1;


 
-- Creation des tables
create table appinfo_sch.alarm (id number(19,0) not null, name varchar2(255 char) not null, description varchar2(255 char),  server varchar2(255 char), weight number(10,0) not null, primary key (id));
comment on table appinfo_sch.alarm is 'Configuration des alarmes';
create table appinfo_sch.alarm_event (id number(19,0) not null, begin timestamp, alarm_id number(19,0), primary key (id));
comment on table appinfo_sch.alarm_event is 'Evènements d''alarme';
create table appinfo_sch.alert (id number(19,0) not null, name varchar2(255 char), description varchar2(255 char), active number(1,0), hours_when_astreinte_is_allowed varchar2(255 char), hours_when_mail_is_allowed varchar2(255 char), hours_when_sms_is_allowed varchar2(255 char), category_id number(19,0), primary key (id));
comment on table appinfo_sch.alert is 'Configuration des alertes';
create table appinfo_sch.alert_alarms (alerts_id number(19,0) not null, alarms_id number(19,0) not null, primary key (alerts_id, alarms_id));
comment on table appinfo_sch.alert_alarms is 'Association alert alarm';
create table appinfo_sch.alert_diffusion_groups (alert_id number(19,0) not null, diffusion_groups_id number(19,0) not null, primary key (alert_id, diffusion_groups_id));
comment on table appinfo_sch.alert_diffusion_groups is 'Association Alert_diffusion_group';
create table appinfo_sch.alert_event (id number(19,0) not null, status varchar2(255 char), end_mail_sent timestamp, end_sms_sent timestamp, last_update timestamp, begin timestamp, start_mail_sent timestamp, start_sms_sent timestamp, weight_sum number(10,0), alert_id number(19,0), primary key (id));
comment on table appinfo_sch.alert_event is 'Evènement d''alerte';
create table appinfo_sch.alert_event_alarm_events (alert_event_id number(19,0) not null, alarm_events_id number(19,0) not null);
comment on table appinfo_sch.alert_event_alarm_events is 'Association alert_event alarm_event';
create table appinfo_sch.category (id number(19,0) not null, name varchar2(255 char) not null, description varchar2(255 char),  primary key (id));
comment on table appinfo_sch.category is 'Catégorie d''alerte';
create table appinfo_sch.contact (id number(19,0) not null, firstname varchar2(255 char) not null,  name varchar2(255 char) not null, email varchar2(255 char),  phone varchar2(255 char), primary key (id));
comment on table appinfo_sch.contact is 'Contact';
create table appinfo_sch.diffusion_group (id number(19,0) not null, name varchar2(255 char) not null, description varchar2(255 char),  primary key (id));
comment on table appinfo_sch.diffusion_group is 'Groupe de diffusion';
create table appinfo_sch.diffusion_group_contacts (diffusion_groups_id number(19,0) not null, contacts_id number(19,0) not null, primary key (diffusion_groups_id, contacts_id));
comment on table appinfo_sch.diffusion_group_contacts is 'Association contact diffusion_group';
create table appinfo_sch.job_event (id number(19,0) not null, begin timestamp, end timestamp, success number(1,0), type varchar2(255 char), primary key (id));
comment on table appinfo_sch.job_event is 'Evénement de job';


-- création des contraintes
alter table appinfo_sch.alert add constraint UK_alert_name unique (name);
--alter table appinfo_sch.alert_event_alarm_events add constraint UK_alarm_events_id unique (alarm_events_id);
alter table appinfo_sch.category add constraint UK_categroy_name unique (name);
alter table appinfo_sch.diffusion_group add constraint UK_diffusion_groupe_name unique (name);
alter table appinfo_sch.alarm_event add constraint FKae_alarm_id foreign key (alarm_id) references alarm;
alter table appinfo_sch.alert add constraint FKa_category_id foreign key (category_id) references category;
alter table appinfo_sch.alert_alarms add constraint FKaa_alarms_id foreign key (alarms_id) references alarm;
alter table appinfo_sch.alert_alarms add constraint FKaa_alerts_id foreign key (alerts_id) references alert;
alter table appinfo_sch.alert_diffusion_groups add constraint FKadg_diffusion_groups_id foreign key (diffusion_groups_id) references diffusion_group;
alter table appinfo_sch.alert_diffusion_groups add constraint FKadg_alert_id foreign key (alert_id) references alert;
alter table appinfo_sch.alert_event add constraint FKae_alert_id foreign key (alert_id) references alert;
alter table appinfo_sch.alert_event_alarm_events add constraint FKae_ae_alarm_events_id foreign key (alarm_events_id) references alarm_event;
alter table appinfo_sch.alert_event_alarm_events add constraint FKae_ae_alert_event_id foreign key (alert_event_id) references alert_event;
alter table appinfo_sch.diffusion_group_contacts add constraint FKdgc_contacts_id foreign key (contacts_id) references contact;
alter table appinfo_sch.diffusion_group_contacts add constraint FKdgc_diffusion_groups_id foreign key (diffusion_groups_id) references diffusion_group;

 -- les grants
grant select, insert, update, delete on appinfo_sch.alarm to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alarm_event to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alert to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alert_alarms to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alert_diffusion_groups to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alert_event to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.alert_event_alarm_events to appinfo_uti ; 
grant select, insert, update, delete on appinfo_sch.category to appinfo_uti ; 
grant select, insert, update, delete on appinfo_sch.contact to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.diffusion_group to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.diffusion_group_contacts to appinfo_uti ;
grant select, insert, update, delete on appinfo_sch.job_event to appinfo_uti ;

grant select on appinfo_sch.alarm_event_seq to appinfo_uti;
grant select on appinfo_sch.alarm_seq to appinfo_uti;
grant select on appinfo_sch.alert_event_seq to appinfo_uti;
grant select on appinfo_sch.alert_seq to appinfo_uti;
grant select on appinfo_sch.category_seq to appinfo_uti;
grant select on appinfo_sch.contact_seq to appinfo_uti;
grant select on appinfo_sch.diffusion_group_seq to appinfo_uti;
grant select on appinfo_sch.job_event_seq to appinfo_uti;


 -- les synonymes
 
create synonym appinfo_uti.alarm_event_seq for appinfo_sch.alarm_event_seq;
create synonym appinfo_uti.alarm_seq for appinfo_sch.alarm_seq;
create synonym appinfo_uti.alert_event_seq for appinfo_sch.alert_event_seq;
create synonym appinfo_uti.alert_seq for appinfo_sch.alert_seq;
create synonym appinfo_uti.category_seq for appinfo_sch.category_seq;
create synonym appinfo_uti.contact_seq for appinfo_sch.contact_seq;
create synonym appinfo_uti.diffusion_group_seq for appinfo_sch.diffusion_group_seq;
create synonym appinfo_uti.job_event_seq for appinfo_sch.job_event_seq;
 
create synonym appinfo_uti.alarm for appinfo_sch.alarm;
create synonym appinfo_uti.alarm_event for appinfo_sch.alarm_event;
create synonym appinfo_uti.alert for appinfo_sch.alert;
create synonym appinfo_uti.alert_alarms for appinfo_sch.alert_alarms;
create synonym appinfo_uti.alert_diffusion_groups for appinfo_sch.alert_diffusion_groups;
create synonym appinfo_uti.alert_event for appinfo_sch.alert_event;
create synonym appinfo_uti.alert_event_alarm_events for appinfo_sch.alert_event_alarm_events;
create synonym appinfo_uti.category for appinfo_sch.category;
create synonym appinfo_uti.contact for appinfo_sch.contact;
create synonym appinfo_uti.diffusion_group for appinfo_sch.diffusion_group;
create synonym appinfo_uti.diffusion_group_contacts for appinfo_sch.diffusion_group_contacts;
create synonym appinfo_uti.job_event for appinfo_sch.job_event;
