<?php
$mysql_host = "ipaddress";
$mysql_user = "username";
$mysql_password = "password";
$mysql_database = "iyydb";
$conn = mysql_connect($mysql_host, $mysql_user, $mysql_password) or die("connect error");
mysql_select_db($mysql_database, $conn) or die("connect error");
mysql_set_charset("utf8");
?>
