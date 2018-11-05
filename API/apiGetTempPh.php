<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");

//$content = file_get_contents("php://input");


//echo $idExp;
//$idExp=$objJson['idExp'];

//$ArrayID=$objJson['ArrayID']; //Array o lista con los ids q ya tiene la app



$ruta = "/home/pi/Desktop/ProyectoFinal/Funciones\ para\ medicion\ -\ Parte\ hardware/getTemPh.py";
    
$salida = shell_exec("sudo python3 " . $ruta);

//var_dump($salida);



$trimmed = trim($salida, " \n");
$pos = strpos($trimmed, '&'); // Encuentra la posicion donde esta el &

$temp = substr($trimmed, 0, $pos); //Start and length
$ph = substr($trimmed, $pos + 1,strlen($salida) - $pos);

 $json = array();
 $json['Temp'] = $temp;
 $json['pH'] = $ph;

$ret=json_encode($json);

echo $ret;


//echo $TempandPh;


//------------------------------------------------------------------------------
 





 ?>