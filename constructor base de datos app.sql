CREATE TABLE Maceracion (
                id INTEGER PRIMARY KEY,
                nombre VARCHAR(190) UNIQUE,
                tipo VARCHAR(64),
                volumen FLOAT,
                densidadObjetivo FLOAT,
                intervaloMedTemp INTEGER,
                intervaloMedPh INTEGER); 

     
CREATE TABLE Intervalo(
                id INTEGER PRIMARY KEY,
                orden INTEGER,
                duracion INTEGER,
                temperatura FLOAT,
                desvioTemperatura FLOAT,
                ph FLOAT,
                desvioPh FLOAT,
                tempDecoccion FLOAT,
                desvioTempDecoccion FLOAT,
                maceracion INTEGER, 
                FOREIGN KEY (maceracion) REFERENCES Maceracion(id));

CREATE TABLE Grano(
                id INTEGER PRIMARY KEY, 
                nombre VARCHAR(190),
                cantidad FLOAT,
                extractoPotencial FLOAT,
                maceracion INTEGER,
                FOREIGN KEY (maceracion) REFERENCES Maceracion(id));
        
CREATE TABLE Experimento (id INTEGER PRIMARY KEY, 
                         fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
                         densidad FLOAT,
                         maceracion INTEGER,
                         FOREIGN KEY (maceracion) REFERENCES Maceracion(id));

CREATE TABLE SensedValues( 
                id INTEGER PRIMARY KEY,
                idRaspi INTEGER,
                id_exp INTEGER,
                fechayhora DATETIME DEFAULT CURRENT_TIMESTAMP,
                temp1 FLOAT,
                temp2 FLOAT,
                temp3 FLOAT,
                temp4 FLOAT,
                temp5 FLOAT,
                tempPh FLOAT,
                tempAmb FLOAT,
                humity FLOAT,
                pH FLOAT,
                FOREIGN KEY (id_exp) REFERENCES Experimento(id));