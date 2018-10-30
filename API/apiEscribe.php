
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");


//---------------------Json que recibo--------------------------
$v =$_POST["json"];
$objJson= json_decode($v);
$nombre=$objJson->{'nombre'};//Nombre de la maceración
$duracion_min=$objJson->{'duracion_min'};
$intervaloMedicionTemp_seg=$objJson->{'intervaloMedicionTemp_seg'};
$intervaloMedicionPH_seg=$objJson->{'intervaloMedicionPH_seg'};
//---------------------------------------------------------------

//------------------INSERTA NUEVA EXPERIENCIA--------------------
//$idExp=$db->nuevaExp('NEGROPUTO', 10, 30, 30);//EXP HARDCODEADA
//echo "El id de experiencia es: ".$idExp."<br>";
$idExp=$db->nuevaExp($nombre, $duracion_min, $intervaloMedicionTemp_seg, $intervaloMedicionPH_seg);
//---------------------------------------------------------------

//---------Devuelvo el idExp luego de hacer la inserción----------------------
$json = array();
$json['idExp'] = $idExp;
$idExpJson=json_encode($json);
echo $idExpJson;// Si el id = 0 quiere decir q no insertó nada
//----------------------------------------------------------------------------

//----------------ACA EJECUTA EL ARCHIVO PYTHON-------------------------
$idExp=1;
if($idExp!=0){
//    $ruta = "~/Desktop/ProyectoFinal/Funciones\ para\ medicion\ -\ Parte\ hardware/Archivo_principal.py";
    $ruta = "/home/pi/Desktop/ProyectoFinal/Funciones\ para\ medicion\ -\ Parte\ hardware/Archivo_principal.py";
    //$salida = shell_exec("python3 " . $ruta . " " . $idExp . " " . $duracion_min . " " . $intervaloMedicionTemp_seg . " " . $intervaloMedicionPH_seg);
    $salida = exec("sudo python3 " . $ruta . " " . 2 . " " . 1 . " " . 30 . " " . 30 . " 2>&1");
    //echo "El comando es: "."python3 " . $ruta . " " . 1 . " " . 1 . " " . 30 . " " . 30 . "<br>";
    echo $salida."<br>";
    if(is_null($salida))
        echo "FALLO EL COMANDO";
    else
     echo "Ejecucion Exitosa";
}
 ?>	

