--Creation des sequences
create sequence appinfo_sch.maintenance_seq start with 1 increment by  1;

-- Creation des tables
CREATE TABLE appinfo_sch.maintenance (id NUMBER(19,0) NOT NULL, start_time TIMESTAMP, end_time TIMESTAMP, comments VARCHAR2(255 CHAR), alert_id NUMBER(19,0), PRIMARY KEY (id));
comment on table appinfo_sch.maintenance is 'Maintenance des alertes';

-- Création des contraintes
alter table appinfo_sch.maintenance add constraint FKm_alert_id foreign key (alert_id) references alert;

-- Les grants
grant select, insert, update, delete on appinfo_sch.maintenance to appinfo_uti ;

grant select on appinfo_sch.maintenance_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.maintenance_seq for appinfo_sch.maintenance_seq;

create synonym appinfo_uti.maintenance for appinfo_sch.maintenance;


