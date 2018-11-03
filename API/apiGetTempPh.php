<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

//$content = file_get_contents("php://input");


//echo $idExp;
//$idExp=$objJson['idExp'];

//$ArrayID=$objJson['ArrayID']; //Array o lista con los ids q ya tiene la app

$TempandPh=$db->getTempandPh();




echo $TempandPh;


//------------------------------------------------------------------------------
 





 ?>