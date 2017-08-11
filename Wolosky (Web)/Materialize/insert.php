<?php
    
    $servername = "localhost";
    $username = "woloskyg_vaksfk";
    $password = "jaqart_56923";
    $dbname = "woloskyg_wolosky";
    
    $conn = new mysqli($servername, $username, $password, $dbname);
    
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    
    $_name = $_POST['name'];
    $_email = $_POST['email'];
    $_gender = $_POST['gender'];
    $_phone = $_POST['phone'];
    $_years = $_POST['years'];
    $_type = $_POST['type'];
    $_message = $_POST['message'];

    $sql = "INSERT INTO clients(nombre, email, sexo, telefono, edad, tipo) "
                ."VALUES(
                    '".$_name."',
                    '".$_email."',
                    '".$_gender."',
                    '".$_phone."',
                    ".$_years.",
                    '".$_type."'
                )";

    $conn->query("SET NAMES 'utf8'");
    $conn->query("SET CHARACTER_SET 'utf8'");
    
    if ($conn->query($sql) === TRUE) {
        echo "true";
    } else {
        echo "false";
    }
    
    $conn->close();
    
    $_toSend = "Nombre: " . $_name . "\nE-mail: " . $_email . "\n\nMensaje:\n" . $_message;
    
    $to = "gimnasiawolosky@gmail.com";
    $subject = "Nuevo contacto: " . $_name . " - " . $_email;
    $headers = "From: wolosky@woloskygimnasia.com" . "\r\n" .
        "CC: " . $_email;

    if (mail($to, $subject, $_toSend, $headers)) {
        //echo "Mail send";
    } else { 
        //echo "Mail was not sent";
    }
    
?>