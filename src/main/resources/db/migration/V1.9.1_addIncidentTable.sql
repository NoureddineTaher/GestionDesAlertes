--Creation des sequences
create sequence appinfo_SCH.incident_seq START WITH 1 INCREMENT BY 1   MINVALUE 1 NOMAXVALUE CACHE 20 NOCYCLE NOORDER

create table  appinfo_sch.incident (
id NUMBER(38) NOT NULL,
INCIDENT_NUMBER INTEGER,
INCIDENT_OBJET varchar2(200 char),
INCIDENT_DESTINATION varchar2(255 char),
INCIDENT_GRAPHICAL_CHARTER varchar2(200 char),
DESCRIPTION varchar2(1000 char),
INCIDENT_NATURE varchar2(255 char),
INCIDENT_APPLICATION varchar2(255 char),
INCIDENT_ENVIRONMENT varchar2(100 char),
INCIDENT_IMPACT_TYPE varchar2(255 char),
INCIDENT_IMPACT_DESCRIPTION varchar2(255 char),
INCIDENT_START varchar2(80 char),
INCIDENT_END varchar2(80 char),
INCIDENT_COMPLEMENTARY_INFOS varchar2(200 char)
                      )


    TABLESPACE APPINFO_DATA;

-- CONSTRAINTS creation

ALTER TABLE appinfo_sch.incident ADD CONSTRAINT PK_incident PRIMARY KEY (id) USING INDEX TABLESPACE APPINFO_INDX;

-- Les grants
grant select, insert, update, delete on appinfo_sch.incident to appinfo_uti ;

grant select on appinfo_sch.incident_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.incident_seq for appinfo_sch.incident_seq;

create synonym appinfo_uti.incident for appinfo_sch.incident;