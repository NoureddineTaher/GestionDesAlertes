-- Insertion des catégories
INSERT INTO "APPINFO_SCH"."CATEGORY" (ID, NAME, DESCRIPTION) VALUES (CATEGORY_SEQ.nextval, 'MICEN', 'Service MICEN');
INSERT INTO "APPINFO_SCH"."CATEGORY" (ID, NAME, DESCRIPTION) VALUES (CATEGORY_SEQ.nextval, 'PLANETE', 'Service PLANETE');

-- Insertion des alarm
INSERT INTO "APPINFO_SCH"."ALARM" (ID, NAME, DESCRIPTION, SERVER, WEIGHT) VALUES (ALARM_SEQ.nextval , 'REAL_TEST1_NOTIF_SMS', 'Test CONSOLIDATION', 'S60.*LBAITM.*', '60');
INSERT INTO "APPINFO_SCH"."ALARM" (ID, NAME, DESCRIPTION, SERVER, WEIGHT) VALUES (ALARM_SEQ.nextval , 'REAL_TEST2_NOTIF_SMS', 'Test ASTREINTE', 'S601LBAITM1', '100');
INSERT INTO "APPINFO_SCH"."ALARM" (ID, NAME, DESCRIPTION, SERVER, WEIGHT) VALUES (ALARM_SEQ.nextval , 'REAL_TEST3_NOTIF_SMS', 'Test PLANETE', 'S603LBAITM3', '100');

--Insertion des alert
INSERT INTO "APPINFO_SCH"."ALERT" (ID, ACTIVE, DESCRIPTION, HOURS_WHEN_ASTREINTE_IS_ALLOWED, HOURS_WHEN_MAIL_IS_ALLOWED, HOURS_WHEN_SMS_IS_ALLOWED, NAME, CATEGORY_ID) VALUES (ALERT_SEQ.nextval, '1', 'ALERTE MICEN DEPOT', '1(00-06:00);1(21:00;23:59);2(00-06:00);2(21:00;23:59);3(00-06:00);3(21:00;23:59);4(00-06:00);4(21:00;23:59);5(00-06:00);5(21:00;23:59);6(00-08:00);6(12:00;23:59);7(00:00;23:59)', '1(00:00-23:59);2(00:00-23:59);3(00:00-23:59);4(00:00-23:59);5(00:00-23:59);6(00:00-23:59);7(00:00-23:59);', '1(06:00-21:00);2(06:00-21:00);3(06:00-21:00);4(06:00-21:00);5(06:00-21:00);6(08:00-12:00)', 'MICEN_DEPOT', (select id from category where name='MICEN'));
INSERT INTO "APPINFO_SCH"."ALERT" (ID, ACTIVE, DESCRIPTION, HOURS_WHEN_MAIL_IS_ALLOWED, HOURS_WHEN_SMS_IS_ALLOWED, NAME, CATEGORY_ID) VALUES (ALERT_SEQ.nextval, '1', 'ALERTE PLANETE', '1(00:00-23:59);2(00:00-23:59);3(00:00-23:59);4(00:00-23:59);5(00:00-23:59);6(00:00-23:59);7(00:00-23:59);', '1(06:00-21:00);2(06:00-21:00);3(06:00-21:00);4(06:00-21:00);5(06:00-21:00);6(08:00-12:00)', 'PLANETE', (select id from category where name='PLANETE'));

--Insertion Alert_alarms
INSERT INTO "APPINFO_SCH"."ALERT_ALARMS" (ALERTS_ID, ALARMS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from alarm where name='REAL_TEST1_NOTIF_SMS'));
INSERT INTO "APPINFO_SCH"."ALERT_ALARMS" (ALERTS_ID, ALARMS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from alarm where name='REAL_TEST2_NOTIF_SMS'));
INSERT INTO "APPINFO_SCH"."ALERT_ALARMS" (ALERTS_ID, ALARMS_ID) VALUES ((select id from alert where name='PLANETE'), (select id from alarm where name='REAL_TEST3_NOTIF_SMS'));

--InsertionContact
INSERT INTO "APPINFO_SCH"."CONTACT" (ID, EMAIL, FIRSTNAME, NAME, PHONE) VALUES (CONTACT_SEQ.nextval, 'lionel.vitielli.groupeadsn@notaires.fr', 'lionel', 'vitielli', '0661108268');
INSERT INTO "APPINFO_SCH"."CONTACT" (ID, EMAIL, FIRSTNAME, NAME, PHONE) VALUES (CONTACT_SEQ.nextval, 'smaine.mougari.groupeadsn@notaires.fr', 'smaine', 'mougari', '0609092001');
INSERT INTO "APPINFO_SCH"."CONTACT" (ID, EMAIL, FIRSTNAME, NAME, PHONE) VALUES (CONTACT_SEQ.nextval, 'smougari@gmail.com', 'smaine2', 'mougari2', '0609092001');
INSERT INTO "APPINFO_SCH"."CONTACT" (ID, EMAIL, FIRSTNAME, NAME, PHONE) VALUES (CONTACT_SEQ.nextval, 'smougari@gmail.com', 'smaine3', 'mougari3', '0609092001');

--Insertion Diffusion_group
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'ALL_MNGT', 'L''équipe de direction');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'MICEN_MNGT', 'Les responsables MICEN');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'MICEN_TECH', 'L''équipe technique MICEN');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'PLANETE_MNGT', 'Les responsables PLANETE');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'PLANETE_TECH', 'L''équipe technique PLANETE');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'ASTREINTE_MNGT', 'Les responsables d''astreinte');
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP" (ID, NAME, DESCRIPTION) VALUES (DIFFUSION_GROUP_SEQ.nextval, 'ASTREINTE_TECH', 'Les personnes technique d''astreinte');

--Insertion Diffusion_group_Contacts
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='ALL_MNGT'), (select id from contact where name='vitielli'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='MICEN_MNGT'), (select id from contact where name='vitielli'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='MICEN_TECH'), (select id from contact where name='mougari'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='PLANETE_MNGT'), (select id from contact where name='vitielli'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='PLANETE_TECH'), (select id from contact where name='mougari2'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='ASTREINTE_MNGT'), (select id from contact where name='vitielli'));
INSERT INTO "APPINFO_SCH"."DIFFUSION_GROUP_CONTACTS" (DIFFUSION_GROUPS_ID, CONTACTS_ID) VALUES ((select id from diffusion_group where name='ASTREINTE_TECH'), (select id from contact where name='mougari3'));


--InsertionAlert_Diffusion_groups
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from diffusion_group where name='ALL_MNGT'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from diffusion_group where name='MICEN_MNGT'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from diffusion_group where name='MICEN_TECH'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from diffusion_group where name='ASTREINTE_MNGT'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='MICEN_DEPOT'), (select id from diffusion_group where name='ASTREINTE_TECH'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='PLANETE'), (select id from diffusion_group where name='PLANETE_MNGT'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='PLANETE'), (select id from diffusion_group where name='PLANETE_TECH'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='PLANETE'), (select id from diffusion_group where name='ASTREINTE_MNGT'));
INSERT INTO "APPINFO_SCH"."ALERT_DIFFUSION_GROUPS" (ALERT_ID, DIFFUSION_GROUPS_ID) VALUES ((select id from alert where name='PLANETE'), (select id from diffusion_group where name='ASTREINTE_TECH'));





