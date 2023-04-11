--Creation des sequences
create sequence appinfo_SCH.historisation_seq START WITH 1 INCREMENT BY 1   MINVALUE 1 NOMAXVALUE CACHE 20 NOCYCLE NOORDER ;

-- Creation des tables

CREATE TABLE appinfo_sch.historisation
(
    id NUMBER(38) NOT NULL,
    id_mail NUMBER(38),
    objet varchar2(200 char),
    distinataires varchar2(250 char),
    a_la_une CLOB,
    icone varchar2(25 char),
    section NUMBER(19,0),
    agenda_periode varchar2(255 char),
    agenda_perimetre varchar2(150 char),
    agenda_nature_intervention varchar2(40 char),
    agenda_impact varchar2(90 char),
    -- column for long texts
    direction_activite CLOB,
    direction_relation_client CLOB,
    date_Envoi DATE
)

    TABLESPACE APPINFO_DATA;

comment on table appinfo_sch.historisation is 'historisation des services IT notaires / historisation SI Groupe';


-- CONSTRAINTS creation
--

ALTER TABLE appinfo_sch.historisation ADD CONSTRAINT PK_historisation PRIMARY KEY (id) USING INDEX TABLESPACE APPINFO_INDX;

-- Les grants
grant select, insert, update, delete on appinfo_sch.historisation to appinfo_uti ;

grant select on appinfo_sch.historisation_seq to appinfo_uti;

-- Les synonym

create synonym appinfo_uti.historisation_seq for appinfo_sch.historisation_seq;

create synonym appinfo_uti.historisation for appinfo_sch.historisation;