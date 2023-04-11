<?php
require 'listadvise.lib.php';
/*
/* Get advises and situation from ITM */
/* Get advises and situation from ITM */
if (LoadAdvise($itm_server, $itm_user, $itm_pass) === FALSE) {
      require 'inc/header.php';
         require 'inc/noconn.php';
            require 'inc/footer.php';
               exit;
}
require 'inc/header.php';
echo "<h1>CONSIGNES</h1>\n";
echo "<table>\n";
$i = 0;
foreach ($advises as $sit => $adv)
{
//      if ($i % $cols == 0)
  //       echo "<tr>\n";
      echo "<td>\n";
      //echo "<h2><a href="$adv">$sit</a></h2>\n";
      echo "<a href='$adv'>$sit</a>\n";
      echo "</td>\n";
      if ($i % $cols == $cols-1)
         echo "</tr>\n";
      ++$i;
}
echo "</table>\n";
echo "
</center>
</div>
</body>
</html>
"
?>
