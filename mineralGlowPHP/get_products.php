<?php 
require_once 'db_connect.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='GET'){
        $result = $connect->query("SELECT * FROM `products`");        
        if ($result->num_rows > 0) {
            // Извлекаем все строки как ассоциативный массив
            $products = $result->fetch_all(MYSQLI_ASSOC);
        
            // Устанавливаем заголовок ответа как JSON
            header('Content-Type: application/json');
        
            // Преобразуем массив в JSON и выводим
            echo json_encode($products);
        } else {
            echo json_encode([]); // если товаров нет, возвращаем пустой массив
        }
}
else{
$response["message"] = "Error.";

echo json_encode($response);}

//header('Location: /index.php');
?>