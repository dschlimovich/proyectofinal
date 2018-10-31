
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

$content = file_get_contents("php://input");


$objJson = json_decode($content);

$idExp= $objJson->{'idExp'};
$ArrayID=$objJson->{'ArrayID'};
//echo $idExp;
//$idExp=$objJson['idExp'];

//$ArrayID=$objJson['ArrayID']; //Array o lista con los ids q ya tiene la app

$SensedValuesJson=$db->getSensedValues($idExp,$ArrayID);


echo $SensedValuesJson;


//------------------------------------------------------------------------------
 





 ?>	

