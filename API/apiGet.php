
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");
$v =$_POST["json"];
$objJson= json_decode($v);

$idExp=$objJson->{'idExp'};
$ArrayID=$objJson->{'ArrayID'}; //Array o lista con los ids q ya tiene la app
//$nombre=$objJson->{'nombrePeee'};

//$SensedValuesJson=$db->getSensedValues(2,[1]); //Hardcodeada de prueba
$SensedValuesJson=$db->getSensedValues($idExp,$ArrayID);


echo $SensedValuesJson;


 ?>	

