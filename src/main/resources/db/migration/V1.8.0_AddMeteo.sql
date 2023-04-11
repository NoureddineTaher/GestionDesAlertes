--Creation des sequences
create sequence appinfo_SCH.meteo_seq START WITH 1 INCREMENT BY 1   MINVALUE 1 NOMAXVALUE CACHE 20 NOCYCLE NOORDER ;

-- Creation des tables
CREATE TABLE appinfo_sch.meteo
(
    id NUMBER(38) NOT NULL,
    a_la_une CLOB,
    icone varchar2(25 char),
    section NUMBER(19,0) NOT NULL,
    agenda_periode varchar2(255 char),
    agenda_perimetre varchar2(150 char),
    agenda_nature_intervention varchar2(40 char),
    agenda_impact varchar2(90 char),

    -- column for long texts
    direction_activite CLOB,
    direction_relation_client CLOB
    )

    TABLESPACE APPINFO_DATA;

comment on table appinfo_sch.meteo is 'Meteo des services IT notaires / Météo SI Groupe';


-- CONSTRAINTS creation
--

ALTER TABLE appinfo_sch.meteo ADD CONSTRAINT PK_meteo PRIMARY KEY (id) USING INDEX TABLESPACE APPINFO_INDX;

-- Les grants
grant select, insert, update, delete on appinfo_sch.meteo to appinfo_uti ;

grant select on appinfo_sch.meteo_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.meteo_seq for appinfo_sch.meteo_seq;

create synonym appinfo_uti.meteo for appinfo_sch.meteo;

--Insertion de tests

