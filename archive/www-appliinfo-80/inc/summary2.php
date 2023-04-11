<h1><?= $conf['title'] ?></h1>
<table>
<tr>
<?php
$i = 0;
foreach ($conf['children'] as $child) {

   $childconf = ReadConf($child);
   ComputeColors($childconf['apps']);
   echo "<td>\n";
   require 'inc/child.php';
   echo "</td>\n";

}
?>
</tr>
</table>
