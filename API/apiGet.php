
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

$content = file_get_contents("php://input");


$objJson = json_decode($content);

$idExp= $objJson->{'idExp'};
$ArrayID=$objJson->{'ArrayID'};//Lista con ids de SensedValues, para que la función devuelva todos los valores sensados q no esten en esta lista

$SensedValuesJson=$db->getSensedValues($idExp,$ArrayID);


echo $SensedValuesJson;
?>	

