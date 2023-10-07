<?php
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $doctorID = $_POST['doctorID'];
    $password = $_POST['password'];

    // Connect to your database (replace with your actual database details)
    $conn = new mysqli("localhost", "your_username", "your_password", "your_database_name");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Use prepared statements to prevent SQL injection
    $stmt = $conn->prepare("SELECT doctorID, password FROM users WHERE doctorID = ?");
    $stmt->bind_param("s", $doctorID);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows == 1) {
        $stmt->bind_result($storedDoctorID, $storedPassword);
        $stmt->fetch();

        // Verify the password
        if (password_verify($password, $storedPassword)) {
            $_SESSION['doctorID'] = $storedDoctorID;
            // Redirect to a page after successful login
            header('Location: dashboard.php');
            exit();
        } else {
            // Sign-in failure message (incorrect password)
            echo "Incorrect password. Please try again.";
        }
    } else {
        // Sign-in failure message (doctorID not found)
        echo "Doctor ID not found. Please sign up.";
    }

    // Close the database connection
    $stmt->close();
    $conn->close();
}
?>


