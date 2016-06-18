<?php
include("config.php");

$phoneID = $_POST['phoneID'];
$sns = $_POST['sns'];
$from = $_POST['from'];
$to = $_POST['to'];

$res = mysql_query("select * from stest where phoneID='$phoneID' and sns='$sns' and date > '$from' and date < '$to'");
$result = array();
while ($row=mysql_fetch_array($res)) {
	array_push($result,
		array('PhoneID'=>$row[0], 'SNS'=>$row[1], 'Status'=>$row[2], 'Date'=>$row[3])
		);
}
echo json_encode(array("result"=>$result));
mysql_close($conn);
?>
