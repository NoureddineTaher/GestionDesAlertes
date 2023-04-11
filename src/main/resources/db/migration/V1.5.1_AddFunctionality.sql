--Creation des sequences
create sequence appinfo_sch.functionality_seq start with 1 increment by  1;

-- Creation des tables
CREATE TABLE appinfo_sch.functionality (id NUMBER(19,0) NOT NULL, type varchar2(255), name varchar2(255), PRIMARY KEY (id), CONSTRAINT UC_Functionality UNIQUE(name));
comment on table appinfo_sch.functionality is 'Fonctionnalité';

-- Les grants
grant select, insert, update, delete on appinfo_sch.functionality to appinfo_uti ;

grant select on appinfo_sch.functionality_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.functionality_seq for appinfo_sch.functionality_seq;

create synonym appinfo_uti.functionality for appinfo_sch.functionality;
