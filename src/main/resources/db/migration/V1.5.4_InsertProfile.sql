-- Insertion des fonctionnalités
INSERT INTO "APPINFO_SCH"."PROFILE" (ID, NAME, DESCRIPTION) VALUES (PROFILE_SEQ.nextval, 'ADMINISTRATEUR', 'Profil administrateur');
INSERT INTO "APPINFO_SCH"."PROFILE" (ID, NAME, DESCRIPTION) VALUES (PROFILE_SEQ.nextval, 'EXPLOITANT', 'Profil pour les équipes de l''exploitation');
INSERT INTO "APPINFO_SCH"."PROFILE" (ID, NAME, DESCRIPTION) VALUES (PROFILE_SEQ.nextval, 'COMMUNIQUANT', 'Profil pour les équipes de communication');
INSERT INTO "APPINFO_SCH"."PROFILE" (ID, NAME, DESCRIPTION, HIDDEN) VALUES (PROFILE_SEQ.nextval, 'SUPER ADMIN', 'Profil nom visible', 1);
