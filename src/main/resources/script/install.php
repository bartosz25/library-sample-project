<?php
/**
 * Install some of library Spring project parts : 
 * - database for webapp (with sample data)
 * - database for unit tests
 * - datasource.xml with database credentials (/META-INF/spring)
 *
 * The script is adapted only for MySQL databases.
 *
 * This script must be called with 3 parameters : 
 * 1) database's username
 * 2) database's user password
 * 3) database name (if not exists, it will be created)
 * 4) JDBC URL (localhost:3306)
 * 
 * Firstly, the script checks if those 3 parameters are defined. Otherwise, it shows an error message.
 * If those parameters are defined, the script checks if some of PHP extensions are installed. If one
 * of them is absent, an error message is shown. The first and the last check concerns directory and files
 * writting permissions. If PHP can't write datasource.xml file, another error message is displayed.
 *  When an error occurs, the installation is stopped.
 * 
 * After a succesfull installation, this directory will be removed from the project.
 *
 */

$callSample = "php install.php databaseUsername databaseUserPassword databaseName localhost:3306";
$exts = array("libxml", "simplexml");
$datasource = __DIR__."/../../META-INF/spring/datasource.xml";

if (!isset($argv) || count($argv) < 5) {
    echo "An error occured on calling install.php . You must specify 4 parameters and call the installation script like that : \n$callSample  \n";
    die();
} elseif (!checkExtensions()) {
    echo "Some extensions are needed to execute this script :  \n";    
    print_r($exts);
    echo "Please, install it and retry after \n";
    die();
} elseif (!is_writable($datasource)) {
    echo "datasource.xml is not writable. Make it writable and retry after \n";
    die();
}

// Prepare datasource.xml file
$argMap = array("user" => 1, "password" => 2);
$config = simplexml_load_file($datasource);
foreach ($config->bean->property as $key => $child) {
    if (isset($argMap[trim($child['name'])])) {
        $child['value'] = $argv[$argMap[trim($child['name'])]];
    } elseif ($child['name'] == 'jdbcUrl') {
        $child['value'] = "jdbc:mysql://".$argv[4]."/".$argv[3];
    }
    $config->bean->attributes()->value = $child['value'];
}

// Install database : check before creating (if not exists) and after (if created correctly)
checkDatabase("before");
$mysqlCreateDatabase = "mysql -u {$argv[1]} -p{$argv[2]} -e \"CREATE DATABASE {$argv[3]}\"";
echo "Creating database with : $mysqlCreateDatabase \n";
shell_exec($mysqlCreateDatabase);
checkDatabase("after");

// Import dump sql file
$mysqlImportDump = "mysql -u {$argv[1]} -p{$argv[2]} -D {$argv[3]} < create_database.sql";
echo "Importing dump file : $mysqlImportDump \n";
shell_exec($mysqlImportDump);

// at all, MySQL must create 38 tables
$allTablesCount = 38;
$tables = shell_exec("mysql -u {$argv[1]} -p{$argv[2]} -e \"SHOW TABLES FROM {$argv[3]}\"");
$tablesCount = count(explode("\n", $tables)) - 2; // 0 => Tables_in__librarynew and 39 => ""
if ($tablesCount != $allTablesCount) {
    $mysqlDrop = "mysql -u {$argv[1]} -p{$argv[2]} -e \"DROP DATABASE {$argv[3]}\"";
    echo "An error occured on import dump. Only {$tablesCount} tables were created (instead of {$allTablesCount}). Created database will be dropped. \n";
    echo "Executing {$mysqlDrop} \n";
    echo shell_exec($mysqlDrop);
    die();
}

file_put_contents($datasource, $config->asXML());

// TODO : remove installation directory
echo "Project's database was correctly installed \n";


// check if database was correctly created
function checkDatabase($when) {
    global $argv;
    $mysqlCheckDatabase = "mysqlshow -u {$argv[1]} -p{$argv[2]} {$argv[3]}";
    $mysqlCheckResult = shell_exec($mysqlCheckDatabase);
    $tablesCount = (int) substr_count($mysqlCheckResult, " Tables ");

    if ($tablesCount == 1 && $when == "after") {
        echo "Database {$argv[3]} created successfully \n";
    } elseif ($tablesCount == 1 && $when == "before") {
        echo "An error occured on creating database. Database {$argv[3]} already exists \n";
        die();
    } elseif ($tablesCount == 0 && $when == "after") {
        echo "An error occured on creating database. Installation program will exit \n";
        die();
    }
}

// check if extensions are loaded
function checkExtensions() {
    global $exts;
    foreach ($exts as $k => $ext) {
        if (extension_loaded($ext)) unset($exts[$k]);
    }
    return (count($exts) == 0);
}