<?php 
require_once 'db_connect.php';// Получаем данные из POST-запроса

$user = $_POST['username'];
$pass = $_POST['password'];

// Проверяем, существует ли пользователь с таким логином
$sql = "SELECT * FROM users WHERE login='$user'";
$result = $connect->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    // Проверяем пароль
    if (password_verify($pass, $row['password'])) {
        echo json_encode(["status" => "success", "message" => "Авторизация прошла успешно", "role" => $row['role']]);
    } else {
        echo json_encode(["status" => "error", "message" => "Неверный пароль"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Пользователь не найден"]);
}