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

    public function nuevaExp($nombreMacerac,$idExp, $dur_min, $intMedTemp_seg, $intMedPH_seg){//Si no se le pasan variables todas las querys FALLAN y last_id=0
    	//---------Nueva Maceracion-----------
        $sql="INSERT IGNORE INTO Maceracion(nombre) VALUES ('".$nombreMacerac."')"; //INSERTA en maceracion esta maceracion, si no existe.
        $results = $this->conn->query($sql);
        //--------Nueva experiencia---------
        $sql="SELECT id from Maceracion WHERE nombre ='".$nombreMacerac."'"; //Obtendo el ID de la Maceracion realacionada.
        $resultado = $this->conn->query($sql);
        $fila=mysqli_fetch_array($resultado); //Obtengo la primer fila del arreglo, x mas q sea solo un valor, tengo q hacerlo
        $idMaceracion = $fila['id'];

        $sql="INSERT INTO Experimento(id,maceracion,duracion_min,intervaloMedicionTemp_seg,intervaloMedicionPH_seg) VALUES (".$idExp.",".$idMaceracion.",".$dur_min.",".$intMedTemp_seg.",".$intMedPH_seg.")"; //INSERTA en maceracion esta maceracion, si no existe.
        $results = $this->conn->query($sql);
        //$last_id = $this->conn->insert_id; //Obtengo el id de la ultima inserción, en este caso la experiencia nueva
        //return $last_id;
        return 0;
    }

    public function getSensedValues($idExp,$ArrayID){
    	if(empty($ArrayID)){
            $sql="SELECT * FROM SensedValues WHERE id_exp=".$idExp;}
        else {
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
        
        $ret= json_encode($data);
        
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
