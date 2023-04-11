-- ADMINISTRATEUR
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Contacts'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Liste de diffusion'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Catégories'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alarmes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alertes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Profils'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Fériés'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Maintenance'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'ADMINISTRATEUR'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Communication'), 0, 0);

-- EXPLOITANT
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Contacts'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Liste de diffusion'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Catégories'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alarmes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alertes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Profils'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Fériés'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Maintenance'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'EXPLOITANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Communication'), 0, 0);

-- COMMUNIQUANT
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Contacts'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Liste de diffusion'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Catégories'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alarmes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alertes'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Profils'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Fériés'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Maintenance'), 0, 0);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'COMMUNIQUANT'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Communication'), 0, 0);

-- SUPER ADMIN
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Contacts'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Liste de diffusion'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Catégories'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alarmes'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Alertes'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Profils'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Fériés'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Maintenance'), 1, 1);
INSERT INTO "APPINFO_SCH"."PROFILE_FUNCTIONALITY" (ID, PROFILE_ID, FUNCTIONALITY_ID, READ, WRITE) VALUES (PROFILE_FUNCTIONALITY_SEQ.nextval, (SELECT ID FROM PROFILE WHERE NAME = 'SUPER ADMIN'), (SELECT ID FROM FUNCTIONALITY WHERE NAME = 'Communication'), 1, 1);