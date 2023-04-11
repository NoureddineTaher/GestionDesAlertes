-- Les deux premiers parametres sont requisitionnes par les DBA
-- &1 : Chemin absolu du repertoire d'execution (ex : /app/admin_oracle)
-- &2 : nom du fichier de log <=> "nom du fichier sql courant "_"date et heure d'execution".log (ex : upd_from_1.8.4_to_2.0.0_20120115_141213.log)
spool "&1/&2" append

set autocommit off exitcommit off trimspool on sqlblanklines on linesize 1000 pagesize 40000 echo off verify off timing off feedback off
DEFINE K_LOGFILE="&1/&2"

alter session set nls_date_format='dd.mm.yyyy hh24:mi:ss';
col host_name format a40
col user      format a20
select SYSDATE, INSTANCE_NAME, HOST_NAME, NAME as DB_NAME, USER from V$INSTANCE, V$DATABASE;
prompt
pause "ctrl+D pour arreter"

set echo on verify on feedback on
WHENEVER SQLERROR EXIT 1 ROLLBACK
WHENEVER OSERROR  EXIT 1 ROLLBACK

SELECT TO_NUMBER('MUST BE AS A_XXX') FROM DUAL WHERE USER not like 'A/_%' escape '/';

-- #########################################################################################################
-- ==>

--- Commandes SQL à exécuter ---

--- Fichiers SQL à exécuter  ---

@V1.8.0_AddMeteo.sql
@V1.8.0_Insert-initial-data.sql
-- <==
-- #########################################################################################################

spool off
exit 0