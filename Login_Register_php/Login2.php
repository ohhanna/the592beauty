<?php
$con = mysqli_connect("localhost", "id1149654_layla", "googoogoo99", "id1149654_practice");

$ID = $_POST["ID"];
$Password = $_POST["Password"];

$statement = mysqli_prepare($con, "SELECT * FROM user WHERE ID = ? AND Password = ?");
mysqli_stmt_bind_param($statement, "ss", $ID, $Password);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $ID, $Name, $Password);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)){
	$response["success"] = true;
	$response["ID"] = $ID;
	$response["Name"] = $Name;
	$response["Password"] = $Password;
}

echo json_encode($response);
?>