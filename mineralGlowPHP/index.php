<?
if ($_REQUEST['passTest'])
    echo password_hash($_REQUEST['passTest'], PASSWORD_DEFAULT);
?>