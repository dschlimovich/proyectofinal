
<?php

include "CdataBase.php";
$db = new CdataBase("pi","raspberry");


//---------------------Json que recibo--------------------------
$v = file_get_contents("php://input");
$objJson = json_decode($v);
$nombre = $objJson->{'nombre'};//Nombre de la maceración
$idExp = $objJson->{'idExp'};//id Experimento de maceracion
$duracion_min = $objJson->{'duracion_min'};
$intervaloMedicionTemp_seg = $objJson->{'intervaloMedicionTemp_seg'};
$intervaloMedicionPH_seg = $objJson->{'intervaloMedicionPH_seg'};
//---------------------------------------------------------------

//------------------INSERTA NUEVA EXPERIENCIA--------------------
$db->nuevaExp($nombre, $idExp, $duracion_min, $intervaloMedicionTemp_seg, $intervaloMedicionPH_seg);
//---------------------------------------------------------------


//----------------ACA EJECUTA EL ARCHIVO PYTHON-------------------------

if($idExp!=0){
    $ruta = "/home/pi/Desktop/ProyectoFinal/Funciones\ para\ medicion\ -\ Parte\ hardware/Archivo_principal.py";
    
    $salida = shell_exec("sudo python3 " . $ruta . " " . $idExp . " " . $duracion_min . " " . $intervaloMedicionTemp_seg . " " . $intervaloMedicionPH_seg);
    
    echo "El comando es: "."python3 " . $ruta . " " . $idExp . " " . $duracion_min . " " . $intervaloMedicionTemp_seg . " " . $intervaloMedicionPH_seg . "<br>";
    echo $salida."<br>"; 
    if(is_null($salida))
        echo "FALLO EL COMANDO";
    else
     echo "Ejecucion Exitosa";
}
 ?>	

