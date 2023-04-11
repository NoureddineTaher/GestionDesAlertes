<h2><a href="<?= $child ?>/"><?= $childconf['title'] ?></a></h2>
<?php
foreach ($childconf['apps'] as $k => $v) {
   $code='';
   $hasticket=0;
   if (count($v) == 0) continue;

   //echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k</div><div class='sit'>\n";
   //echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k\n";
   //echo "<span class='ticket'>ticket</span></div>\n";
   //echo "<div class='sit'>\n";
   foreach ($v as $j) {
      if (isset($faulty_tickets[$j])) 
      {
         if ($faulty_tickets[$j] >$hasticket) {$hasticket=$faulty_tickets[$j];}
         $code.= "<div class='" . $situations_color[$j] . "'>$j ";
         $code.= "<span style=\"color:white;background-color:#cb96d5;border-radius:6px;padding:4px\">$faulty_tickets[$j]</span></div>\n";
      }
      else
      {
         $code.= "<div class='" . $situations_color[$j] . "'>$j</div>\n";
      }
   }
   if ($hasticket)
   {
      echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k \n";
      echo "<span style=\"color:white;background-color:#cb96d5;border-radius:6px;padding:4px\">$hasticket</span></div>\n";
      echo "<div class='sit'>\n";

   }
   else
   {
      echo "<div class='app'><img src='img/". $apps_color[$k] .".png'>$k\n";
      echo "</div><div class='sit'>\n";
   }
   echo $code;
   echo "</div>\n";
}
?>
