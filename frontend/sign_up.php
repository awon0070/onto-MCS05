<?php
// Database connection details
$host = 'ec2-34-206-106-3.compute-1.amazonaws.com';
$username = 'nsfwpgqcrxwclt';
$password = '7eed92ff3be37f373eed5e5e0b61294a66c275d3b6889b9700a0a20122d26c92';
$database = 'dfg904h9nmiqka';
$port = 5432;

// Create a database connection
$conn = new mysqli($host, $username, $password, $database, $port);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Process form data
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $firstName = $_POST['firstName'];
    $lastName = $_POST['lastName'];
    $doctorID = $_POST['doctorID'];
    $password = $_POST['signupPassword'];
    $specialization = $_POST['specialization'];

    // Insert data into the users table
    $sql = "INSERT INTO users (first_name, last_name, doctor_id, password_hash, specialization)
            VALUES (?, ?, ?, ?, ?)";
    
    // Use prepared statement
    $stmt = $conn->prepare($sql);
    $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

    // Bind parameters
    $stmt->bind_param("sssss", $firstName, $lastName, $doctorID, $hashedPassword, $specialization);

    if ($stmt->execute()) {
        // Registration successful
        echo "Registration successful!";
    } else {
        // Registration failed
        echo "Error: " . $stmt->error;
    }

    // Close the statement
    $stmt->close();
}

// Close the database connection
$conn->close();
?>



