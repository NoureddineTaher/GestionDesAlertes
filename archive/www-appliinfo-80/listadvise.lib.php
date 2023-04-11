<?php
/*
Version 1 :
18/10/2018 : Création par Damien Jourdan
   => Ce script sert à afficher les consignes des situations ITM
*/

/*
Déclaration des variables de connexion au serveur ITM
*/
$itm_server = 'http://hub-supervision.production-real.fr:1920';
$itm_user = 'appli.soapitm1';
$itm_pass = 'xxxx';


/*Cette variable définit le nombre de colonnes sur la page html créée.*/
$cols = 3;
/* Cette variable récupère le nom de la configuration à traiter, qui doit correspondre à un fichier de configuration dans conf/ */
//$prefix = str_replace('/index.php', '', str_replace($_SERVER['DOCUMENT_ROOT'], '', $_SERVER['SCRIPT_FILENAME']));
date_default_timezone_set('Europe/Paris');




/*
Récupère les situations et leur consigne dans ITM
*/
function LoadAdvise($itm_server, $itm_user, $itm_pass)
{
   /*
   $advises tableau 
      - situation
      - advise
   */
   global $advises;

   /* Prepare la connection à ITM */

   $ch = curl_init();
   curl_setopt($ch, CURLOPT_URL, "$itm_server///cms/soap/");
   curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
   curl_setopt($ch, CURLOPT_POST, TRUE);
   curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 1);

   /* Alimente $advises avec les consignes */

   $xml_request = "
   <SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">
     <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>
     <SOAP-ENV:Body>
       <CT_Get>
         <userid>$itm_user</userid>
         <password>$itm_pass</password>
         <table>O4SRV.ISITSTSH</table>
         <sql>SELECT SITNAME, ADVISE FROM O4SRV.TSITDESC</sql>
       </CT_Get>
     </SOAP-ENV:Body>
   </SOAP-ENV:Envelope>
   ";

   curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_request);

   $advises = array();

   if (($xml_response = curl_exec($ch)) === FALSE) {
      return FALSE;
   }

   $xml_response = preg_replace('/<([\/\d\w-]+):([\d\w-]+)( xmlns:.+?)?>/i', '<\1-\2>', $xml_response);
   $xml = simplexml_load_string($xml_response);
   if ($xml->{'SOAP-ENV-Body'}->{'SOAP-ENV-Fault'}->faultstring != '') {
      return FALSE;
   }
   foreach ($xml->{'SOAP-ENV-Body'}->{'SOAP-CHK-Success'}->TABLE->DATA->ROW as $name => $row) {
      if (preg_match('/^http:\/\/hub/', (string)$row->ADVISE))
      $advises[(string)$row->HSITNAME] = (string)$row->ADVISE;
   }
   //echo "advises\n";
   //print_r($advises);
   return TRUE;
}


?>


