<?php
require 'appliinfo.lib.php';

/* Load configuration from file */

$conffile = (@preg_match('/^[\d\w-]+$/', $_SERVER['conf'])) ? $_SERVER['conf'] : 'default';
$conf = ReadConf($conffile);

/* Display a specific message if configuration is not found */

if ($conf === FALSE) {
   # file not found
   require 'inc/header.php';
   require 'inc/noconf.php';
   require 'inc/footer.php';
   exit;
}
if ((count($conf['apps']) == 0) && (count($conf['children']) == 0)) {
   # no app and no child defined
   require 'inc/header.php';
   require 'inc/noconf.php';
   require 'inc/footer.php';
   exit;
}

/* Fetch data from ITM */

if (LoadSeverities($itm_server, $itm_user, $itm_pass) === FALSE) {
   require 'inc/header.php';
   require 'inc/noconn.php';
   require 'inc/footer.php';
   exit;
}
if (LoadFaultySituations($itm_server, $itm_user, $itm_pass) === FALSE) {
   require 'inc/header.php';
   require 'inc/noconn.php';
   require 'inc/footer.php';
   exit;
}

/* Compute applications and situations color */


require 'inc/header.php';
if (count($conf['apps'])) {
   ComputeColors($conf['apps']);
   require 'inc/main.php';
}
else {
   require 'inc/summary.php';
}
require 'inc/legend.php';
require 'inc/footer.php';
?>
