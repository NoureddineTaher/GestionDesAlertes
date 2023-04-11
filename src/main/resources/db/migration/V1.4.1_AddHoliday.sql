--Creation des sequences
create sequence appinfo_sch.holiday_seq start with 1 increment by  1;

-- Creation des tables
CREATE TABLE appinfo_sch.holiday (id NUMBER(19,0) NOT NULL, day DATE, PRIMARY KEY (id), CONSTRAINT UC_Holiday UNIQUE(day));
comment on table appinfo_sch.holiday is 'Maintenance des jours feries ou chomes';

-- Les grants
grant select, insert, update, delete on appinfo_sch.holiday to appinfo_uti ;

grant select on appinfo_sch.holiday_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.holiday_seq for appinfo_sch.holiday_seq;

create synonym appinfo_uti.holiday for appinfo_sch.holiday;
