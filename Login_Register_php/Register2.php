<?php
$con = mysqli_connect("localhost", "id1149654_layla", "googoogoo99", "id1149654_practice");

$ID = $_POST["ID"];
$Name = $_POST["Name"];
$Password = $_POST["Password"];
$statement = mysqli_prepare($con, "INSERT INTO user (ID, Name, Password) VALUES (?, ?, ?)");
mysqli_stmt_bind_param($statement, "sss", $ID, $Name, $Password);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);
?>