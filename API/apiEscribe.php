
<?php

include "CdataBase.php";
$db= new CdataBase("pi","raspberry");
//$db->pruebaAPI();
$v =$_POST["json"];
//$objJson= json_decode($v);
//---INSERTA NUEVA EXPERIENCIA----------
$idExp=$db->nuevaExp('NEGROPUTO', 10, 30, 30);
echo "El id de experiencia es: ".$idExp."<br>";
echo $idExp;
//ACA EJECUTARIA EL ARCHIVO PYTHON!!!

//echo nl2br(" \n ");echo nl2br(" \n ");
/*echo "--------------------------------Mensaje JSON recibido ---------------------------------";echo nl2br(" \n ");
echo "----------------------------------------------------------------------------------";echo nl2br(" \n ");

echo $v;

echo "----------------------------------------------------------------------------------";echo nl2br(" \n ");
echo nl2br(" \n ");echo nl2br(" \n ");


$nombre=$objJson->{'nombrePeee'};
$descripcion=$objJson->{'descripcion'};
$nro_resolucion=$objJson->{'nroResolucion'};
$nro_convocatoria=$objJson->{'nroConvocatoria'};
$duracion=$objJson->{'duracion'};
$instituciones_ids=$objJson->{'institucion'};
$docentes_ids=$objJson->{'docentes'};
$descripcionPol=$objJson->{'politicasPublicas'};
$descripcionEval=$objJson->{'evaluacion'};
$descripcionCri=$objJson->{'criterios'};
$presupuesto=$objJson->{'presupuesto'};
$ua_ids=$objJson->{'ua'};
$carreras_ids=[];
$asignaturas_ids=[];


echo nl2br(" \n ");
echo "------------Descripción del formulario----------";echo nl2br(" \n ");
echo "Nombre de la PEEE:".$nombre;echo nl2br(" \n ");
echo "Descripcion:".$descripcion;echo nl2br(" \n ");
echo "Nro de Resolucion:".$nro_resolucion;echo nl2br(" \n ");
echo "Nro de Convocatoria:".$nro_convocatoria;echo nl2br(" \n ");
echo "Duracion:".$duracion;echo nl2br(" \n ");
echo "Presupuesto:".$presupuesto;echo nl2br(" \n ");
$practicas_id=$db->addPractica($nombre,$descripcion,$nro_resolucion,$nro_convocatoria,$duracion,$presupuesto);
echo "Id generado nueva practica: ".$practicas_id;echo nl2br(" \n ");echo nl2br(" \n ");



echo "--------Instituciones Intervinientes-----";echo nl2br(" \n ");
foreach($instituciones_ids as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $agregar = $obj->agregar;
        $id = $obj->id;
        $nombre_ins = $obj->nombre;
        $direccion = $obj->direccion;
		echo "|-> Agregar:".$agregar;echo nl2br(" \n ");
		echo "|-> Id:".$id;echo nl2br(" \n ");
		echo "|-> Nombre:".$nombre_ins;echo nl2br(" \n ");
		echo "|-> Direccion:".$direccion;echo nl2br(" \n ");

		$id_inst=-1;
	
		if($agregar >=0){
			if($agregar==1){
				echo " [Nueva Institucion]";echo nl2br(" \n ");
				$newId=$db-> addInstituciones($nombre_ins,$direccion);
				$db-> addPracticasInstituciones($practicas_id,$newId);
			}else{
				echo " [Institución existente] ";echo nl2br(" \n ");
				$db-> addPracticasInstituciones($practicas_id,$id);
			}
		}else{
			echo " [No efectuar operación] ";echo nl2br(" \n ");
		
		}
	echo nl2br(" \n ");///echo nl2br(" \n ");
	
	}


echo "------Descripcion Politicas Públicas-----";echo nl2br(" \n ");
//politicasPublicas":[{"descripcion":" qwdqwd"},{"descripcion":"wqdqwd qwdqwd"}]
foreach($descripcionPol as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $descripcion = $obj->descripcion;
		echo "|-> Descripcion:".$descripcion;echo nl2br(" \n ");
	$db->addPracticasPoliticias($practicas_id,$descripcion);	
	echo nl2br(" \n ");
	//	echo nl2br(" \n ");
}

echo "--------Descripcion Evaluacion-----------";echo nl2br(" \n ");
//politicasPublicas":[{"descripcion":" qwdqwd"},{"descripcion":"wqdqwd qwdqwd"}]
foreach($descripcionEval as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $descripcion = $obj->descripcion;
		echo "|-> Descripcion:".$descripcion;echo nl2br(" \n ");
		$db->addPracticasEvaluacion($practicas_id,$descripcion);
		echo nl2br(" \n ");
		//echo nl2br(" \n ");
}

echo "---------Descripcion Criterios-----------";echo nl2br(" \n ");
//politicasPublicas":[{"descripcion":" qwdqwd"},{"descripcion":"wqdqwd qwdqwd"}]
foreach($descripcionCri as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $descripcion = $obj->descripcion;
		echo "|-> Descripcion:".$descripcion;echo nl2br(" \n ");
		$db->addPracticasCriterios($practicas_id,$descripcion);
		echo nl2br(" \n ");
}

echo "-----------------Docentes----------------";echo nl2br(" \n ");

//{"agregar":0,"dni":4,"nombre":"Airaudo Raquel","apellido":""}
foreach($docentes_ids as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $agregar = $obj->agregar;
        $dni = $obj->dni;
        $nombre_doc = $obj->nombre;
        //$nombre_doc = $obj->nombre;
        $apellido_doc = $obj->apellido;
        $id_doc = $obj->id;
        $id_sexo = $obj->sexo;
		echo "|-> Agregar:".$agregar;echo nl2br(" \n ");
		echo "|-> Dni:".$dni;echo nl2br(" \n ");
		echo "|-> Nombre:".$nombre_doc;echo nl2br(" \n ");
		echo "|-> Apellido:".$apellido_doc;echo nl2br(" \n ");
		echo "|-> Id:".$id_doc;echo nl2br(" \n ");
		echo "|-> Sexo:".$id_sexo;echo nl2br(" \n ");
		echo nl2br(" \n ");
		$id_inst=-1;
		
		if($agregar >=0){
		
			if($agregar==1){
				echo " [Agregar Docente]";echo nl2br(" \n ");
				$newId=$db-> addDocentes($nombre_doc,$apellido,$dni,$id_sexo);
				$db-> addPracticasDocentes($practicas_id,$newId);
			}else{
				echo " [Docente Existente] ";echo nl2br(" \n ");
				$db-> addPracticasDocentes($practicas_id,$id_doc);
			}
		}else{
			echo "[No efectuar operación]";echo nl2br(" \n ");
		}
	 	echo nl2br(" \n ");//echo nl2br(" \n ");
	
}
echo "-------------Practicas UA----------------";echo nl2br(" \n ");
//politicasPublicas":[{"descripcion":" qwdqwd"},{"descripcion":"wqdqwd qwdqwd"}]
foreach($ua_ids as $obj){
		//"agregar":0,"id":2,"nombre":"municipalidad lll","direccion":""
        $codigo = $obj->codigo;
        $nUni = $obj->nUni;
        $nAsi = $obj->nAsi;
        $nCar = $obj->nCar;
		$idUni=$obj->idUni;
		$idAsi=$obj->idAsi;
		$idCar=$obj->idCar;
	
	
        $descripcion = $obj->descripcion;
       	echo "|->    Nueva entrada     ";echo nl2br(" \n ");
		echo "|-> Codigo:".$codigo;echo nl2br(" \n ");
		echo "|-> nUni:".$nUni;echo nl2br(" \n ");
		echo "|-> nCar:".$nCar;echo nl2br(" \n ");
		echo "|-> nAsi:".$nAsi;echo nl2br(" \n ");
		echo "|-> idAsi:".$idAsi;echo nl2br(" \n ");
		echo "|-> idUni:".$idUni;echo nl2br(" \n ");
		echo "|-> idCar:".$idCar;echo nl2br(" \n ");
		$db->addPracticasUa($practicas_id,$idUni,$idCar,$idAsi,$nCar,$nAsi,$codigo);
		echo nl2br(" \n ");*/
		
//}

//echo "--------------------------------------------------------------------------";echo nl2br(" \n ");

 ?>	

