<?php
include("config.php");
$phoneID = $_POST['phoneID'];
$sns = $_POST['sns'];
$date = $_POST['date'];

$result = mysql_query("insert into test (phoneID,sns,date) values ('$phoneID','$sns','$date')");

if($result){
	echo 'success';
}
else{
	$message  = 'Invalid query: ' . mysql_error() . "\n";
	die($message);
}

mysql_close($conn);
?>
