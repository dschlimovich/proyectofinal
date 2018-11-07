<?php
// imaginate que recibis una cadena
$salida = "temp1&temp2&temp3&temp4&temp5&tempPh&pH&humity&tempAmb]";
$keys = array(
    1 => "Temp1",
    2 => "Temp2",
    3 => "Temp3",
    4 => "Temp4",
	5 => "Temp5",
	6 => "TempPh",
	7 => "Ph",
	8 => "Humidity",
	9 => "TempAmb"
);

$trimmed = trim($salida, " \n"); //supongo que esto le saca el utltimo caracter
$json = array();
//suponiendo que foreach arranca desde el primer valor y sigue hasta el ultimo avanzando de a uno
foreach ($keys as $key){
	//obtengo la primer posicion de &
	$pos = strpos($trimmed, '&');
	
	if( pos === false){ //en la documentacion explican porque es triple = (===)
		//ya saque todos y queda el ultimo valor.
		json[$key] = trimmed;
	} 
	else {
		// si encontro al & asi que tengo que amputar el valor de la cadena y insertarlo en el json-
			
		$value = substr($trimmed, 0, $pos); //me agarra todos los valores menos donde esta el ampersand
		// la meto en el json con la clave correspondiente. 
		json[$key] = $value;
		// ahora tengo que amputar la cadena que acabo de sacar.
		$trimmed = substr( $trimmed, $pos + 1, strlen($salida) - $pos);
	}
}

$ret=json_encode($json);

echo $ret;
?>