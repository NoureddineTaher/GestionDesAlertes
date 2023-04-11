<?php
/*
Version 2 :
Script créé par Sébastien Bonnet
18/10/2018 : Mise à jour par Damien Jourdan
   => ajout commentaires
   => prise en compte de l'acquittement des alertes
08/01/2019 : Mise à jour par Damien Jourdan
   => affichage du numéro de ticket PSSOFT contenu dans le champ Reference ID 
   
*/

/*
Déclaration des variables de connexion au serveur ITM
*/
$itm_server = 'http://hub-supervision.production-real.fr:1920';
$itm_user = 'appli.soapitm1';
$itm_pass = '%B02uMS?=DNIav!';


/*Cette variable définit le nombre de colonnes sur la page html créée.*/
$cols = 3;
/* Cette variable récupère le nom de la configuration à traiter, qui doit correspondre à un fichier de configuration dans conf/ */
$prefix = str_replace('/index.php', '', str_replace($_SERVER['DOCUMENT_ROOT'], '', $_SERVER['SCRIPT_FILENAME']));
date_default_timezone_set('Europe/Paris');


/*
Récupère la configuration :
- nom de l'application
- liste des situations associées
*/
function ReadConf($conffile)
{
   // Si le fichier conf/nom_url.txt n'existe pas exit
   if (!file_exists("conf/$conffile.txt")) {
      return FALSE;
   }

   // Initialisation
   $apps = array();
   $children = array();
   // Ouverture du fichier de conf
   $fh = fopen("conf/$conffile.txt", 'r');
   $app = '???';
   // Lecture du fichier
   // app : application
   // children : sous application
   while (($line = fgets($fh)) !== FALSE) {
      $line = preg_replace('/[\r\n]/', '', $line);
      if (preg_match('/^[\*-]?\s+(.+)/', $line, $regs)) {
         array_push($apps[$app], $regs[1]);
      }
      elseif (preg_match('/^x\s+(.+)/', $line, $regs) && file_exists("conf/${regs[1]}.txt")) {
         array_push($children, $regs[1]);
      }
      elseif (preg_match('/^\s*TITLE\s*=\s*(.+)/', $line, $regs)) {
         $title = $regs[1];
      }
      elseif (preg_match('/^\s*COLS\s*=\s*(\d+)/', $line, $regs)) {
         $cols = $regs[1];
      }
      elseif (preg_match('/^(.+)/', $line, $regs)) {
         $app = $regs[1];
         $apps[$app] = array();
      }
   }
   // Fermetrue du fichier
   fclose($fh);
   //echo "app \n";
   //print_r($apps);
   // Sortie : si count($children)>0 tableau des sous applications sinon tableau des situation 
   return count($children) ? array('children' => $children, 'title' => $title) : array('apps' => $apps, 'cols' => $cols, 'title' => $title);
}


/*
Récupère les sévérités des situations dans ITM
*/
function LoadSeverities($itm_server, $itm_user, $itm_pass)
{
   /*
   $severities tableau 
      - situation
      - sévérité
   */
   global $severities;

   /* Prepare la connection à ITM */
   $ch = curl_init();
   curl_setopt($ch, CURLOPT_URL, "$itm_server///cms/soap/");
   curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
   curl_setopt($ch, CURLOPT_POST, TRUE);
   curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 1);

   /* Alimente $severities avec les sévérités */
   $xml_request = "
   <SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">
     <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>
     <SOAP-ENV:Body>
       <CT_Get>
         <userid>$itm_user</userid>
         <password>$itm_pass</password>
         <table>O4SRV.ISITSTSH</table>
         <sql>SELECT SITNAME, SITINFO FROM O4SRV.TSITDESC</sql>
       </CT_Get>
     </SOAP-ENV:Body>
   </SOAP-ENV:Envelope>
   ";

   curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_request);

   $severities = array();

   // Si erreur de connexion sortie
   if (($xml_response = curl_exec($ch)) === FALSE) {
      return FALSE;
   }

   $xml_response = preg_replace('/<([\/\d\w-]+):([\d\w-]+)( xmlns:.+?)?>/i', '<\1-\2>', $xml_response);
   $xml = simplexml_load_string($xml_response);
   // Si erreur de récupération XML sortie
   if ($xml->{'SOAP-ENV-Body'}->{'SOAP-ENV-Fault'}->faultstring != '') {
      return FALSE;
   }
   // Boucle sur chaque ligne XML dans <TABLE><DATA>
   foreach ($xml->{'SOAP-ENV-Body'}->{'SOAP-CHK-Success'}->TABLE->DATA->ROW as $name => $row) {
      $severities[(string)$row->HSITNAME] = preg_match("/SEV=(.*?)(;|$)/", (string)$row->SITINFO, $regs) ? $regs[1][0] : 'U';
   }
   //echo "severities\n";
   //print_r($severities);
   return TRUE;
}

/*
Récupère la liste des situations 
- ouvertes (deltastat = y)
- acquittées (deltstat = a)
- reouvertes (deltstat = e)
*/
function LoadFaultySituations($itm_server, $itm_user, $itm_pass)
{
   /* $faulty_situations obsolete */
   global $faulty_situations;

   /* $faulty_ack_situations tableau
      - situation
      - statut (Y ou A)
   */
   global $faulty_ack_situations;
   /* $faulty_tickets tableau
      - situation
      - ticket
   */
   global $faulty_tickets;

   /* Prepare la connection à ITM */
   $ch = curl_init();
   curl_setopt($ch, CURLOPT_URL, "$itm_server///cms/soap/");
   curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
   curl_setopt($ch, CURLOPT_POST, TRUE);
   curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 1);

   /* Récupère le statut des situations  */
   $xml_request = "
   <SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">
     <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>
     <SOAP-ENV:Body>
       <CT_Get>
         <userid>$itm_user</userid>
         <password>$itm_pass</password>
         <table>O4SRV.ISITSTSH</table>
         <sql>SELECT GBLTMSTMP, SITNAME, DELTASTAT, NODE, ORIGINNODE FROM O4SRV.ISITSTSH WHERE DELTASTAT='Y' OR DELTASTAT='A' OR DELTASTAT='E' ORDER BY GBLTMSTMP</sql>
       </CT_Get>
     </SOAP-ENV:Body>
   </SOAP-ENV:Envelope>
   ";

   curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_request);

   $faulty_situations = array();
   $faulty_ack_situations = array();

   // Si erreur de connexion sortie
   if (($xml_response = curl_exec($ch)) === FALSE) {
      return FALSE;
   }

   $xml_response = preg_replace('/<([\/\d\w-]+):([\d\w-]+)( xmlns:.+?)?>/i', '<\1-\2>', $xml_response);
   $xml = simplexml_load_string($xml_response);
   // Si erreur de récupération XML sortie
   if ($xml->{'SOAP-ENV-Body'}->{'SOAP-ENV-Fault'}->faultstring != '') {
      return FALSE;
   }
   // Boucle sur chaque ligne XML dans <TABLE><DATA>
   foreach ($xml->{'SOAP-ENV-Body'}->{'SOAP-CHK-Success'}->TABLE->DATA->ROW as $name => $row) {
      // Ajoute la situation dans $faulty_situations
      array_push($faulty_situations, (string)$row->HSITNAME);
      
      // Ajoute si elle n'existe pas la sitation dans $faulty_ack_situations
      if (!isset($faulty_ack_situations[(string)$row->HSITNAME])) {
         $faulty_ack_situations[(string)$row->HSITNAME]=(string)$row->HDELTASTAT;
      }
      // Sinon met à jour le statut dans $faulty_ack_situations si situation ouverte
      elseif  ($faulty_ack_situations[(string)$row->HSITNAME]=='Y') 
      {
         $faulty_ack_situations[(string)$row->HSITNAME]=(string)$row->HDELTASTAT;
      }
      elseif  ($faulty_ack_situations[(string)$row->HSITNAME]=='A')
      {
         /*
         if (preg_match('/Reference ID associated with event/',$row->RESULTS)) {

            $tmp=split("=",(string)$row->RESULTS);
            //echo "REFID = (string)$row->HSITNAME -   $tmp[7]\n\n";
            $tmp2=split(";", $tmp[7]);
            //print_r($tmp2);
            $faulty_tickets[(string)$row->HSITNAME]=$tmp2[0];
         }
         */
         $faulty_ack_situations[(string)$row->HSITNAME]=(string)$row->HDELTASTAT;
      }                        
      /* Le statut E est considéré comme ouvert = Y */
      if ($faulty_ack_situations[(string)$row->HSITNAME]=='E')
      {
         $faulty_ack_situations[(string)$row->HSITNAME]='Y';
      }
   }

   //Seconde requête SOAP pour récupérer le numéro de ticket
   $xml_request2="
   <SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">
     <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"/>
       <SOAP-ENV:Body>
          <CT_Get>
            <userid>$itm_user</userid>
            <password>$itm_pass</password>
            <table>O4SRV.ISITSTSH</table>
            <sql>SELECT GBLTMSTMP, SITNAME, DELTASTAT, NODE, ORIGINNODE, RESULTS FROM O4SRV.TSITSTSH WHERE DELTASTAT='A' ORDER BY SITNAME,GBLTMSTMP</sql>
   </CT_Get>
   </SOAP-ENV:Body>
   </SOAP-ENV:Envelope>
   ";
   curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_request2);

   $faulty_tickets = array();

   // Si erreur de connexion sortie
   if (($xml_response = curl_exec($ch)) === FALSE) {
      return FALSE;
   }
   $xml_response = preg_replace('/<([\/\d\w-]+):([\d\w-]+)( xmlns:.+?)?>/i', '<\1-\2>', $xml_response);
   $xml = simplexml_load_string($xml_response);
   
   // Si erreur de récupération XML sortie
   if ($xml->{'SOAP-ENV-Body'}->{'SOAP-ENV-Fault'}->faultstring != '') {
      return FALSE;
   }

   // Boucle sur chaque ligne XML dans <TABLE><DATA>
   foreach ($xml->{'SOAP-ENV-Body'}->{'SOAP-CHK-Success'}->TABLE->DATA->ROW as $name => $row) 
   {
      // Si le champ reference ID existe et que la situation es ouverte ou déclenchée et qu'elle est acquittée récupération du numéro de ticket
      if (preg_match('/Reference ID associated with event/',$row->RESULTS) && isset($faulty_ack_situations[(string)$row->HSITNAME]) && $faulty_ack_situations[(string)$row->HSITNAME]=='A') 
      {
            $tmp=split("=",(string)$row->RESULTS);
            //echo "REFID = (string)$row->HSITNAME -   $tmp[7]\n\n";
            $tmp2=split(";", $tmp[7]);
            //print_r($tmp2);
            $faulty_tickets[(string)$row->HSITNAME]=$tmp2[0];
       }
       // Sinon on réinitialise le numéro de ticket pour éviter qu'un ancien ticket soit renseigné pour la même situation. 
       else
       {
           $faulty_tickets[(string)$row->HSITNAME]='';
       }
   }
  
   // Suppression des doublons
   $faulty_situations = array_unique($faulty_situations);
   //echo "faulty situation\n";
   //print_r($faulty_situations);
   //echo "faulty ack situation\n";
   //print_r($faulty_ack_situations);
   //echo "faulty tickets\n";
   //print_r($faulty_tickets);

   return TRUE;
}


/* Compute applications and situations color */
/* A partir de la liste des situations ouvertes
   et des sévérités
   construction des deux array :
      - $apps_color
      - $situations_color
*/
function ComputeColors($apps)
{
   global $severities;
   global $faulty_situations;
   global $faulty_ack_situations;
   global $situations_color;
   global $apps_color;

   //echo "ComputeColors\n";
   //echo "*************\n";

   // Initialisation
   $apps_color = array();
   if (!isset($situations_color))
      $situations_color = array();
   //Boucle sur les applications
   foreach ($apps as $k => $v) {
      $colors = '';
      foreach ($v as $j) {
         //print "Application " . $j . "\n";
         if (!isset($situations_color[$j])){
            if (isset($faulty_ack_situations[$j]))
            {
               $situations_color[$j]=isset($severities[$j]) ? $severities[$j] : 'U';
               /*Si la situation est acquittée, on suffixe la sévérité avec un A */
               if ($faulty_ack_situations[$j] =="A")
               {
                  $situations_color[$j]= $situations_color[$j] . "A";
               }
            }
            else
            {
               $situations_color[$j]='O';
            }
            //$situations_color[$j] = in_array($j, $faulty_situations) ? (isset($severities[$j]) ? $severities[$j] : 'U') : 'O';
         }
         $colors .= $situations_color[$j];
         //print "Situation Color " . $j . " : " .$situations_color[$j] ."\n";
         //print "Colors ". $colors . "\n";
      }
      // Affectation de la sévérité la plus haute pour l'application
      foreach (array('FA', 'F', 'CA', 'C', 'MA', 'M', 'WA', 'W', 'UA', 'U', 'OA', 'O') as $c) {
         $apps_color[$k] = 'O';
         if (strpos($colors, $c) !== FALSE) {
            $apps_color[$k] = $c;
            break;
         }
      }
      //echo "Apps_color : \n"; 
      //print_r($apps_color);
      //echo "\n";
   }
}
?>
