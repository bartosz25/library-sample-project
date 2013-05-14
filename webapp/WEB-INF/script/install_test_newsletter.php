<?php
/**
 * This script installs sample data to test newsletter sending.
 */

 
$newsletters = array();
for ($i = 0; $i < 12; $i++) {
    $newsletters[$i] = array('state' => 0, 'title' => "Newsletter #{$i}",
                             'content' => "Body for newsletter {$i}",
                             'date' => date("Y-m-d H:i:s"),
                       );
}

$subscribers = array();
for ($i = 0; $i < 134; $i++) {
    $subscribers[$i] = "bartkonieczny+".$i."@gmail.com";
}

$inserted = array('new' => 0, 'sub' => 0);
$pdo = new PDO("mysql:host=127.0.0.1;dbname=library", "root", "root");
$pdo->beginTransaction();
// insert newsletters
foreach ($newsletters as $newsletter) {
    $insert = $pdo->prepare("INSERT INTO newsletter (admin_id_ad, created_ne, state_ne, title_ne, text_ne, send_ne) VALUES  (1, :created, :state, :title, :text, :send)");
    $insert->bindParam(':created', $newsletter['date']);
    $insert->bindParam(':state', $newsletter['state']);
    $insert->bindParam(':title', $newsletter['title']);
    $insert->bindParam(':text', $newsletter['content']);
    $insert->bindParam(':send', $newsletter['date']);
    if($insert->execute()) $inserted['new']++;
}

// insert subscribers
foreach ($subscribers as $email) {
    $date = date("Y-m-d H:i:s");
    $insert = $pdo->prepare("INSERT INTO newsletter_subscriber (subscriber_id_su, password_ns, date_ns, email_ns, state_ns) VALUES  (NULL, :pass, :date, :email, 1)");
    $insert->bindParam(':date', $date);
    $insert->bindParam(':pass', $email);
    $insert->bindParam(':email', $email);
    if($insert->execute()) $inserted['sub']++;
    else print_r($insert->errorInfo());
}
$pdo->commit();
echo "Inserted {$inserted['new']} newsletters and {$inserted['sub']} subscribers";