<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

//$content = file_get_contents("php://input");


//echo $idExp;
//$idExp=$objJson['idExp'];

//$ArrayID=$objJson['ArrayID']; //Array o lista con los ids q ya tiene la app



$ruta = "/home/pi/Desktop/ProyectoFinal/Funciones\ para\ medicion\ -\ Parte\ hardware/getTemPh.py";
    
$salida = shell_exec("sudo python3 " . $ruta);

var_dump($salida);


echo $salida;


//echo $TempandPh;


//------------------------------------------------------------------------------
 





 ?>