<?php 
class CdataBase { 

	private $servername ="localhost"; 
	private $username = "pi";
	private $password ="raspberry";
	private $dbname = "maceraciones";
	private $conn;
	private $valCon=false;

	function __construct($uss,$pass){
		$this->username=$uss;
		$this->password=$pass;
		$this->Conexion();
		//mysql_set_charset('utf8');
	}
	
	/**=============================================================**/                                   
	
	
	
	
	/* public function pruebaAPI(){
		//echo "Aca tmb entra pruebaApi<br>";
		$sql="SELECT * FROM `SensedValues";
		$results = $this->conn->query($sql);
		$data=[];
		$json = array();
//	    if ($results->num_rows > 0) {
//			while($row = $results->fetch_assoc()) {
//				echo($row);
//        }


        foreach($results as $result) {
        	echo "aca tmb entra";
            if ($result !=NULL) {

                echo "- id: ".$result['id']."<br/> ";

                echo "- id_exp: ".$result['id_exp']."<br/>";

                echo "- fechayhora: ".$result['fechayhora']."<br/>";

                echo "- temp1: ".$result['temp1']."<br/>";

                echo "- temp2: ".$result['temp2']."<br/>";

                echo "- temp3: ".$result['temp3']."<br/>";

                echo "- temp4: ".$result['temp4']."<br/>";

                echo "- temp5: ".$result['temp5']."<br/>";

                echo "- tempPh: ".$result['tempPh']."<br/>";

                echo "- tempAmb: ".$result['tempAmb']."<br/>";

                echo "- humity: ".$result['humity']."<br/>";

                echo "- pH: ".$result['pH']."<br/>";
            }

            else {echo "<br/>No hay más datos: <br/>".$result;}

        }

	return 0;
	}

    /**
     * @param $nombreMacerac
     * @param $dur_min
     * @param $intMedTemp_seg
     * @param $intMedPH_seg
     */
    public function nuevaExp($nombreMacerac, $dur_min, $intMedTemp_seg, $intMedPH_seg){//Si no se le pasan variables todas las querys FALLAN y last_id=0
    	//---------Nueva Maceracion-----------
        $sql="INSERT IGNORE INTO Maceracion(nombre) VALUES ('".$nombreMacerac."')"; //INSERTA en maceracion esta maceracion, si no existe.
        $results = $this->conn->query($sql);
        //--------Nueva experiencia---------
        $sql="SELECT id from Maceracion WHERE nombre ='".$nombreMacerac."'"; //Obtendo el ID de la Maceracion realacionada.
        $resultado = $this->conn->query($sql);
        $fila=mysqli_fetch_array($resultado); //Obtengo la primer fila del arreglo, x mas q sea solo un valor, tengo q hacerlo
        $idMaceracion = $fila['id'];

        $sql="INSERT INTO Experimento(maceracion,duracion_min,intervaloMedicionTemp_seg,intervaloMedicionPH_seg) VALUES (".$idMaceracion.",".$dur_min.",".$intMedTemp_seg.",".$intMedPH_seg.")"; //INSERTA en maceracion esta maceracion, si no existe.
        $results = $this->conn->query($sql);
        $last_id = $this->conn->insert_id; //Obtengo el id de la ultima inserción, en este caso la experiencia nueva
        return $last_id;
    }

    public function getSensedValues($idExp,$ArrayID){
        if(empty($ArrayID)){
            $sql="SELECT * FROM SensedValues WHERE id_exp=".$idExp;}
        else {
            $ArrayID_as_string = implode(',', $ArrayID); // e.g, "1,3,17,202"
            $sql = "SELECT * FROM SensedValues WHERE id_exp =".$idExp." AND `id` NOT IN (".$ArrayID_as_string.")";
        }
        $resultados = $this->conn->query($sql);

        $data=[];
        $json = array();
        if ($resultados->num_rows > 0) {
            while($row = $resultados->fetch_assoc()) {
                $json['id'] = $row['id'];
                $json['id_exp'] = $row['id_exp'];
                $json['fechayhora'] = $row['fechayhora'];
                $json['temp1'] = $row['temp1'];
                $json['temp2'] = $row['temp2'];
                $json['temp3'] = $row['temp3'];
                $json['temp4'] = $row['temp4'];
                $json['temp5'] = $row['temp5'];
                $json['tempPh'] = $row['tempPh'];
                $json['tempAmb'] = $row['tempAmb'];
                $json['humity'] = $row['humity'];
                $json['pH'] = $row['pH'];

                $data[] = $json;//Pushea el json en el arreglo data (arreglo de jsons)
            }
        }
        //$ret=""; //Para el caso de q no pase la validacion del if devuelve una cadena vacía
        $ret= json_encode($data);

        /*if(json_encode($data,0,250)){//NI IDEA DE COMO FUNCIONA ESTA VALIDACION
            $ret= json_encode($data,0,4000);
        }else{
            echo "[Error en la construccion del Json Nro".json_last_error()." ]";
        }*/
        return $ret;
    }
   	/*public function getUnidadesAcademicas(){
		$sql="SELECT ua.id, ua.nombre from unidades_academicas ua ORDER by ua.nombre";
		$result = $this->conn->query($sql);
		$data=[];
		$json = array();
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$json['id'] = $row['id'];
        		$json['nombre'] = $row['nombre'];
        		$data[] = $json;
			}
		}
		$ret="";
		if(json_encode($data,0,250)){
			$ret= json_encode($data,0,4000);
		}else{
			echo "[Error en la construccion del Json Nro".json_last_error()." ]";
		}
	return $ret;
	}
	public function getCarreras(){
		$sql="SELECT cc.id, cc.nombre, cc.unidades_academicas_id from carreras cc ORDER BY cc.id ASC";
		$result = $this->conn->query($sql);
		$data=[];
		$json = array();
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$json['id'] = $row['id'];
        		$json['nombre'] = $row['nombre'];
        		$json['idua'] = $row['unidades_academicas_id'];
        		$data[] = $json;
			}
		}
		$ret="";
		if(json_encode($data,0,250)){
			$ret= json_encode($data,0,4000);
		}else{
			echo "[Error en la construccion del Json Nro".json_last_error()." ]";
		}
	return $ret;
	}
	public function getAsignaturas(){
		$sql="SELECT ass.id,ass.nombre, ass.carreras_id from asignaturas ass ORDER BY ass.id ASC";
		$result = $this->conn->query($sql);
		$data=[];
		$json = array();
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$json['id'] = $row['id'];
        		$json['nombre'] = $row['nombre'];
        		$json['idcar'] = $row['carreras_id'];
        		$data[] = $json;
			}
		}
		$ret="";
		if(json_encode($data,0,250)){
			$ret= json_encode($data,0,4000);
		}else{
			echo "[Error en la construccion del Json Nro".json_last_error()." ]";
		}
	return $ret;
	}
	public function existPersona($dni){
		$sql = "SELECT count(p.nrodoc) 'cantidad' from personas p where p.nrodoc=".'"'.$dni.'"'." GROUP by nrodoc ";
		$ret=false;
		$result = $this->conn->query($sql);
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$retorno= $row["cantidad"];
			echo "[Existe Persona cant=".$retorno."]";echo nl2br(" \n ");
			$ret=true;
		}else{
			echo "[No Existe Persona]";echo nl2br(" \n ");
		}
		return $ret;
	}
	public function existDocente($dni){
		$sql ="SELECT count(p.nrodoc) 'cantidad' from personas p inner join docentes d on p.id=d.personas_id where";
		$sql=$sql." p.nrodoc=".'"'.$dni.'"'." GROUP by nrodoc";
		$result = $this->conn->query($sql);
		$ret=false;
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$retorno= $row["cantidad"];
			echo "[ Existe Docente cant=".$retorno."]";	echo nl2br(" \n ");
			$ret =true;
		}else{
			echo "[No Existe Docente]";echo nl2br(" \n ");
		}
		return $ret;
	}
	public function getIdPersona($dni){
		$sql ="SELECT id from personas p where p.nrodoc=".'"'.$dni.'"';
		$retorno=-1;
		$result = $this->conn->query($sql);
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$retorno= $row["id"];
			echo "[Id Persona Obtenido id=".$retorno."]";echo nl2br(" \n ");
		}else{
			echo "[No se obtuvo id Persona]";echo nl2br(" \n ");
		}
		return $retorno;
	}*/
	/*

		$retorno=-1;
		$result = $this->conn->query($sql);
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$retorno= $row["id"];
			echo "[anda la consulta!]".$retorno;
		}else{
			echo "[no anda la consulta]";
			$retorno = -1;
		}
		return $retorno;


	*/
/*	public function addPracticasInstituciones($practicas_id,$instituciones_id){
		$sql="INSERT INTO practicas_instituciones (practicas_id,instituciones_id) VALUES";
		$sql=$sql.'('.$practicas_id.',';
		$sql=$sql.$instituciones_id.')';
	//	echo $sql;

		if ($this->conn->query($sql) === TRUE) {
			echo "[Insertada Practica - Institucion]";echo nl2br(" \n ");
		} else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}

	}
	public function addPracticasDocentes($practicas_id,$docentes_id){

		$sql="INSERT INTO practicas_docentes (practicas_id,docentes_id) VALUES";
			$sql=$sql.'('.$practicas_id.',';
			$sql=$sql.$docentes_id.')';
		//	echo $sql;

			if ($this->conn->query($sql) === TRUE) {
				echo "[Insertado Practicas -Docentes]";echo nl2br(" \n ");
			} else {
    			echo "Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
			}

		}
	public function addPracticasPoliticias($practicas_id,$descripcion){
			$sql="INSERT INTO practicas_politicas (practicas_id,descripcion) VALUES";
			$sql=$sql.'('.$practicas_id.',';
			$sql=$sql.'"'.$descripcion.'")';
			//echo $sql;
			if ($this->conn->query($sql) === TRUE) {
				echo "[Insertada Practicas - Politicas]"; echo nl2br(" \n ");
			} else {
    			echo "[Error: " . $sql . "<br>" . $conn->error ."]"; echo nl2br(" \n ");
			}

		}
	public function addPracticasEvaluacion($practicas_id,$descripcion){
			$sql="INSERT INTO practicas_evaluacion (practicas_id,descripcion) VALUES";
			$sql=$sql.'('.$practicas_id.',';
			$sql=$sql.'"'.$descripcion.'")';
			//echo $sql;

			if ($this->conn->query($sql) === TRUE) {
				echo "[Insertado Practicas - Evaluacion]";echo nl2br(" \n ");
			} else {
    			echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
			}

		}
	public function addPracticasCriterios($practicas_id,$descripcion){
			$sql="INSERT INTO practicas_criterios (practicas_id,descripcion) VALUES";
			$sql=$sql.'('.$practicas_id.',';
			$sql=$sql.'"'.$descripcion.'")';
			if ($this->conn->query($sql) === TRUE) {
				echo "[ Insertado Practicas- Criterios]";echo nl2br(" \n ");
			} else {
    			echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
			}

		}
	public function addPracticasUa($practicas_id,$ua_id,$carreras_id,$asignaturas_id,$nomCar,$nomAsi,$casos){
		$sql="";
		switch($casos){
			case 0:
				$sql="INSERT INTO practicas_ua (practicas_id,asignaturas_id) VALUES";
				$sql=$sql.'('.$practicas_id.',';
				$sql=$sql.''.$asignaturas_id.')';
				echo "[caso 0: Se agrega todo]";echo nl2br(" \n ");
				//echo $sql;
				break;
			case 1:
				$idAsignatura=$this->addAsignaturas($nomAsi,$carreras_id);
				$sql="INSERT INTO practicas_ua (practicas_id,asignaturas_id) VALUES";
				$sql=$sql.'('.$practicas_id.',';
				$sql=$sql.''.$idAsignatura.")";
				echo "[caso 1: Se agrega asignatura y las practicas]";echo nl2br(" \n ");
				//echo $sql;
				break;
			default:
				echo "[Caso no contemplado]";
				break;
			}
			if ($this->conn->query($sql) === TRUE) {
				echo "[Insertado Practicas Unidades Academicas]";echo nl2br(" \n ");
			} else {
    			echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
			}
		}*/
	/*	public function addUaCarAsi($ua_id,$nomCar,$nomAsi){
		echo "llego? ";
		$idTit=$this->addTitulos($nomCar);
		$idCarrera=$this->addCarreras($nomCar,$ua_id,$idTit);
		echo $idCarrera;
		if($idCarrera>=0){
			$idAsignatura=$this->addAsignaturas($nomAsi,$idCarrera);
			if($idAsignatura>=0){
				echo "Salio todo bien";
			}else{
				echo "Salio parcialmente bien";
			}
		}else{
			echo "salio todo mal";
		}
	}*/

/*	public function addPractica($nombre,$descripcion,$nro_resolucion,$nro_convocatoria,$duracion,$presupuesto){
		$sql="INSERT INTO practicas ( nombre, descripcion,nro_resolucion,nro_convocatoria,duracion,presupuesto) VALUES";
		$sql=$sql.'("'.$nombre.'",';
		$sql=$sql.'"'.$descripcion.'",';
		$sql=$sql.'"'.$nro_resolucion.'",';
		$sql=$sql.$nro_convocatoria.",";
		$sql=$sql.' '.$duracion.',';
		$sql=$sql.' '.$presupuesto.')';
		if ($this->conn->query($sql) === TRUE) {
 			echo "[Nueva Practica Insertada]";echo nl2br(" \n ");
		}else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		$ret= $this->getLastIdInsert();
		return $ret;
	}
	public function getIdDocente($dni){
		$sql ="SELECT d.id from docentes d inner join personas p on p.id=d.personas_id where p.nrodoc=".'"'.$dni.'"';
		$retorno=-1;
		$result = $this->conn->query($sql);
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$retorno= $row["id"];
			echo "[Get Docente id=".$retorno."]";echo nl2br(" \n ");
		}else{
			echo "[No se obtuvo id Docente]";echo nl2br(" \n ");
		}
		return $retorno;
	}
	public function addDocentes($nombre,$apellido,$dni,$idSexo){
		$rta=-2;
		$resultadoConsulta=$this->existPersona($dni);
		if($resultadoConsulta){
			if($this->existDocente($dni)){
				$idPer=$this->getIdPersona($dni);
				echo "[id Persona:".$idPer."]";
				$rta=$this->getIdDocente($dni);
			}else{
				echo "Paso por aca?";
				$rta=$this->addDocente($this->getIdPersona($dni));
			}
		}else{
			$idPersona=$this->addPersona($nombre,$apellido,$idSexo,$dni);
			$this->addDocente($idPersona);
		}
		return $rta;
	}
	public function addPersona($nombre,$apellido,$sexos_id,$nrodoc){
		$sql="INSERT INTO personas (nombre,apellido,sexos_id,nrodoc,ipUsuario,fechaCreado,fechaActualizado,nombreUsuario) VALUES";
		$sql=$sql.'("'.$nombre.'",';
		$sql=$sql.'"'.$apellido.'",';
		$sql=$sql.$sexos_id.',';
		$sql=$sql.'"'.$nrodoc.'",';
		$sql=$sql.'"192.168.0.1",';
		$sql=$sql.'CURDATE(),';
		$sql=$sql.'CURDATE(),';
		$sql=$sql.'"Ignacio Catena")';
		echo $sql;
		$rta=-2;

		if ($this->conn->query($sql) === TRUE) {
			$rta= $this->getLastIdInsert();
			echo "[Insertada Persona id=".$rta."]";echo nl2br(" \n ");
		} else {
   			echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		return $rta;
	}
	public function addTitulos($nombreCarr){
		$sql='INSERT INTO titulos (titulo_masculino,titulo_femenino, titulos_tipos_id, titulos_grados_id) VALUES("';
		$sql=$sql.$nombreCarr.'o",'.'"'.$nombreCarr.'a",'."1,1)";
		echo " lñego ";
		$rta=-1;
		if ($this->conn->query($sql) === TRUE) {
			$rta=$this->getLastIdInsert();
			echo "[Titulo agredado. Id=".$rta."]";echo nl2br(" \n ");
		} else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		return $rta;
	}*/

/*	public function addAsignaturas($nombre,$carreras_id){
		$sql="INSERT INTO asignaturas(nombre,carreras_id,descripcion) VALUES";
		$sql=$sql.'("'.$nombre.'",'.$carreras_id.',""'.")";
		//echo $sql;


		$rta=-1;
		if ($this->conn->query($sql) === TRUE) {
			$rta=$this->getLastIdInsert();
			echo "[Asignatura agredada. Id=".$rta."]";echo nl2br(" \n ");
		} else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		return $rta;
	}
	public function addCarreras($nombre,$ua_id,$id_titulo){
		$sql="INSERT INTO carreras(nombre,unidades_academicas_id,titulos_id) VALUES";
		$sql=$sql.'("'.$nombre.'",'.$ua_id.",".$id_titulo.")";
		$rta=-1;
		if ($this->conn->query($sql) === TRUE) {
			$rta=$this->getLastIdInsert();
			echo "[Carreras agredada. Id=".$rta."]";echo nl2br(" \n ");
		} else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		return $rta;
	}
	public function addDocente($idPersona){
		$sql="INSERT INTO docentes (personas_id) VALUES";
		$sql=$sql.'('.$idPersona.')';
		$resultado=true;
		$rta=-2;
		if ($this->conn->query($sql) === TRUE) {
			$rta=$this->getLastIdInsert();
			echo "[Docentes Insertado id=".$rta."]";echo nl2br(" \n ");
		} else {
    		echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}
		return $rta;
	}
	public function addInstituciones($nombre,$direccion){
		$sql="INSERT INTO instituciones(nombre,direccion,ipUsuario,fechaCreado,fechaActualizado,nombreUsuario) VALUES";
		$sql=$sql.'("'.$nombre.'",';
		$sql=$sql.'"'.$direccion.'",';
		$sql=$sql.'"192.168.0.1",';
		$sql=$sql.'CURDATE(),';
		$sql=$sql.'CURDATE(),';
		$sql=$sql.'"Ignacio Catena")';
		//echo $sql;
		$rta=-1;

		if ($this->conn->query($sql) === TRUE) {
			$rta= $this->getLastIdInsert();
			echo "[Nueva institución Insertada id=$rta]";echo nl2br(" \n ");
		}else{
			echo "[Error: " . $sql . "<br>" . $conn->error."]";echo nl2br(" \n ");
		}

		return $rta;
		}*/
	public function Conexion(){
		echo "Hasta aca llego<br>";
		$this->conn=new mysqli($this->servername,$this->username,$this->password,$this->dbname);
		mysqli_set_charset($this->conn, "utf8");
		if($this->conn->connect_error){
			die("Fallo conexion<br>" . $this->conn->connect_error);
		}else{
			echo "Conexion perfecta<br>";
		}
	return "todo joya";
	}
	function __destruct ( ){
		$this->conn->close();
	}

} 
?>
