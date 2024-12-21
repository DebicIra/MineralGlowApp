<?php 
require_once 'db_connect.php';


$user = $_POST['username'];
$pass = $_POST['password'];

// Хешируем пароль для безопасности
$hashed_password = password_hash($pass, PASSWORD_DEFAULT);

// Проверяем, существует ли пользователь с таким логином
$sql = "SELECT * FROM users WHERE login='$user'";
$result = $connect->query($sql);

if ($result->num_rows > 0) {
    // Если пользователь уже существует
    echo json_encode(["status" => "error", "message" => "Пользователь с таким логином уже существует"]);
} else {
    // Если нет — добавляем пользователя в базу
    $insert_sql = "INSERT INTO users (login, password, role) VALUES ('$user', '$hashed_password', 2)";
    
    if ($connect->query($insert_sql) === TRUE) {
        echo json_encode(["status" => "success", "message" => "Пользователь успешно зарегистрирован"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Ошибка при регистрации"]);
    }
}
