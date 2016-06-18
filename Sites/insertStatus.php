<?php
include("config.php");
$phoneID = $_POST['phoneID'];
$sns = $_POST['sns'];
$status = $_POST['status'];
$date = $_POST['date'];

$result = mysql_query("insert into stest (phoneID,sns,status,date) values ('$phoneID','$sns','$status','$date')");

if($result){
	echo 'success';
}
else{
	$message  = 'Invalid query: ' . mysql_error() . "\n";
	die($message);
}

mysql_close($conn);
?>
