--Creation des sequences
create sequence appinfo_sch.profile_functionality_seq start with 1 increment by  1;

-- Creation des tables
CREATE TABLE appinfo_sch.profile_functionality (id NUMBER(19,0) NOT NULL, profile_id NUMBER(19,0), functionality_id NUMBER(19,0), read NUMBER(1,0), write NUMBER(1,0), PRIMARY KEY (id));
comment on table appinfo_sch.profile_functionality is 'Fonctionnalités des profils';

-- Création des contraintes
alter table appinfo_sch.profile_functionality add constraint FKpf_profile_id foreign key (profile_id) references profile;
alter table appinfo_sch.profile_functionality add constraint FKpf_functionality_id foreign key (functionality_id) references functionality;

-- Les grants
grant select, insert, update, delete on appinfo_sch.profile_functionality to appinfo_uti;

grant select on appinfo_sch.profile_functionality_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.profile_functionality_seq for appinfo_sch.profile_functionality_seq;

create synonym appinfo_uti.profile_functionality for appinfo_sch.profile_functionality;
