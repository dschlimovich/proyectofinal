
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

$content = file_get_contents("php://input");


$objJson = json_decode($content);

$idExp= $objJson->{'idExp'};
$ArrayID=$objJson->{'ArrayID'};

$SensedValuesJson=$db->getSensedValues($idExp,$ArrayID);


echo $SensedValuesJson;
?>	

