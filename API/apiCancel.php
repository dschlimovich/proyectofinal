<?php

include "CdataBase.php";
$db = new CdataBase("pi","raspberry");


//---------------------Json que recibo--------------------------
$v = file_get_contents("php://input");
$objJson = json_decode($v);
$idExp = $objJson->{'idExp'};//id Experimento de maceracion
//---------------------------------------------------------------
//------------------Cancelo el Python en Ejecucion -------
if($idExp!=0){
    $salida = shell_exec("sudo pkill python3"); // Mato el proceso
}

//------------------Elimina los Sensed Values y el Experimento--------------------
$db->deleteExp($idExp);
//---------------------------------------------------------------


//----------------ACA EJECUTA EL ARCHIVO PYTHON-------------------------

if($idExp!=0){
    //$salida = shell_exec("sudo service apache2 restart");
        
    echo $salida."<br>"; 
    if(is_null($salida))
        echo "FALLO EL COMANDO";
    else
    	echo "Ejecucion Exitosa";
}
 ?>	