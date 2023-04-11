<h1><?= $conf['title'] ?></h1>
<table>
<?php
$i = 0;
// Boucle sur la liste des applications
foreach ($conf['apps'] as $k => $v) 
{
   // Initialisation
   $code='';
   $hasticket=0;

   // Si pas de situation prochaine application
   if (count($v) == 0) continue;

   // Table nouvelle ligne (table row)
   if ($i % $cols == 0)
      echo "<tr>\n";

   // Table nouvelle col (table col)
   echo "<td>\n";

   // Boucle sur le situations
   foreach ($v as $j) 
   {
      // Si il y a un ticket pour cette situation
      if ($faulty_tickets[$j])
      {
         // Si c'est un ticket plus récent, on met à jour
         if ($faulty_tickets[$j] >$hasticket) {$hasticket=$faulty_tickets[$j];}
         // Ecriture du numéro de ticket à droite du nom de situation
         $code.= "<div class='" . $situations_color[$j] . "'>$j ";
         //$code.= "<span style=\"color:white;background-color:#b70713;border-radius:6px;padding:4px\">$faulty_tickets[$j]</span></div>\n";
         $code.= "<span style=\"color:black;background-color:#d9a7e2;border-radius:6px;padding:4px\">$faulty_tickets[$j]</span></div>\n";
      }
      else
      {
         $code.= "<div class='" . $situations_color[$j] . "'>$j</div>\n";
      }
   }
   // Si on a un ticket
   if ($hasticket)
   {
      echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k \n";
      echo "<span style=\"color:black;background-color:#d9a7e2;border-radius:6px;padding:4px\">$hasticket</span></div>\n";
      echo "<div class='sit'>\n";
   }
   // sinon
   else
   {
      echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k\n";
      echo "</div><div class='sit'>\n";
   }
   // Ecriture du code pour cette aplication
   echo $code;
   echo "</div>\n";
   echo "</td>\n";

   if ($i % $cols == $cols-1)
      echo "</tr>\n";

   ++$i;

}
?>
</table>
