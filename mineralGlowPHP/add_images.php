<?php
require 'db_connect.php';

$response = [];
// print_r($_FILES);
// die;
if (isset($_POST['name'], $_POST['price'], $_POST['desc']) && isset($_FILES['image'])) {
    $name = urldecode($_POST['name']);
    $price = $_POST['price'];
    $desc = mb_convert_encoding($_POST['desc'], 'UTF-8', 'auto');;
    $composition = isset($_POST['comp']) ? $_POST['comp'] : '';
    // Обработка загрузки изображения
    $target_dir = "images/";
    $file_name = basename($_FILES["image"]["name"]);
    $target_file = $target_dir . $file_name;

    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        $image_src = $target_file;

        // Добавление данных в базу
        $query = "INSERT INTO products (name, price, `description`, composition, image_src) VALUES (?, ?, ?, ?, ?)";
        $stmt = $connect->prepare($query);
        $stmt->bind_param("sisss", $name, $price, $desc, $composition, $image_src);

        if ($stmt->execute()) {
            $response['success'] = true;
            $response['message'] = "Товар успешно добавлен!";
            // $response['message'] = implode(',', $_POST);
        } else {
            $response['success'] = false;
            $response['message'] = "Ошибка добавления товара в базу.";
        }
    } else {
        $response['success'] = false;
        $response['message'] = "Ошибка загрузки изображения.";
    }
} else {
    $response['success'] = false;
    // $response['message'] = "Не все данные переданы.";
    $response['message'] = $_POST;
}

echo json_encode($response);
?>
