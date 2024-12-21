<?php
require 'db_connect.php';

// Проверяем, что ID товара передан через POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['id'])) {
        $product_id = intval($_POST['id']);

        // SQL-запрос на удаление товара
        $sql = "DELETE FROM `products` WHERE `products`.`id` = ?";
        $stmt = $connect->prepare($sql);
        $stmt->bind_param("i", $product_id);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Товар успешно удален"]);
        } else {
            echo json_encode(["success" => false, "message" => "Ошибка при удалении товара"]);
        }

        $stmt->close();
    } else {
        echo json_encode(["success" => false, "message" => "ID товара не передан"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Неверный метод запроса"]);
}

$conn->close();
?>
