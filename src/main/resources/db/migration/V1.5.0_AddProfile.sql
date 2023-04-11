--Creation des sequences
create sequence appinfo_sch.profile_seq start with 1 increment by  1;

-- Creation des tables
CREATE TABLE appinfo_sch.profile (id NUMBER(19,0) NOT NULL, name varchar2(15), description varchar2(50), hidden NUMBER(1,0) DEFAULT 0, PRIMARY KEY (id), CONSTRAINT UC_Profile UNIQUE(name));
comment on table appinfo_sch.profile is 'Profils des utilisateurs';

-- Les grants
grant select, insert, update, delete on appinfo_sch.profile to appinfo_uti ;

grant select on appinfo_sch.profile_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.profile_seq for appinfo_sch.profile_seq;

create synonym appinfo_uti.profile for appinfo_sch.profile;
