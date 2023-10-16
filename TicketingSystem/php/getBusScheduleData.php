<?php
// Database connection
$host = 'sql12.freemysqlhosting.net';
$username = 'sql12652268';
$password = '2sMIHit1zM';
$database = 'sql12652268';
$connection = mysqli_connect($host, $username, $password, $database);

// Check for connection errors
if (!$connection) {
    die("Connection failed: " . mysqli_connect_error());
}

// SQL query to retrieve train schedule data
$sql = "SELECT * FROM Bus_schedule";
$result = mysqli_query($connection, $sql);

// Check if there are results
if (mysqli_num_rows($result) > 0) {
    $data = mysqli_fetch_all($result, MYSQLI_ASSOC);
    echo json_encode($data);
} else {
    echo json_encode([]);
}

// Close the database connection
mysqli_close($connection);
?>
