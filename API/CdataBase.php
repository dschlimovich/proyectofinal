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
    	//echo "<br> SensedValues";
        if(empty($ArrayID)){
            $sql="SELECT * FROM SensedValues WHERE id_exp=".$idExp;}
        else {
            //$ArrayID_as_string = implode(',', $ArrayID); // e.g, "1,3,17,202"
            //$sql = "SELECT * FROM SensedValues WHERE id_exp =".$idExp." AND `id` NOT IN (".$ArrayID_as_string.")";
            $sql = "SELECT * FROM SensedValues WHERE id_exp =".$idExp." AND `id` NOT IN (".$ArrayID.")";
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
   	
	public function Conexion(){
		//echo "Hasta aca llego<br>";
		$this->conn=new mysqli($this->servername,$this->username,$this->password,$this->dbname);
		mysqli_set_charset($this->conn, "utf8");
		if($this->conn->connect_error){
			die("Fallo conexion<br>" . $this->conn->connect_error);
		}else{
			//echo "Conexion perfecta<br>";
		}
	return "todo joya";
	}
	function __destruct ( ){
		$this->conn->close();
	}

} 
?>
