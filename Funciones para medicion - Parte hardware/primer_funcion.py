#Aca voy definir la primer funcion
# Se encarga de tomar los datos de todos los sensores y meterlos en un arreglo

# Librerias para la lectura de la temperatura
import os
import glob
import pyfirmata
import time
import Adafruit_DHT

def obtener_valores():
    # tengo 6 temperatura (1 para pH), amb y pH
    
    Valores=[] #en este arreglo voy a meter todos los valores sensados

    os.system('modprobe w1-gpio')
    os.system('modprobe w1-therm')
    Carpeta_Sensor=[]
    # Deberiamos cargar este vector con cada ID correspondiente de los sensores
    #Cargamos la ruta donde se encuentra nuestro sensor.
    Carpeta_Sensor.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[0])
    #Carpeta_Sensor.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[1])
    #Carpeta_Sensor.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[2])
    #Carpeta_Sensor.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[3])

    for i in range (0,len(Carpeta_Sensor)):
    fSensor = open(Carpeta_Sensor[i] + '/w1_slave','r')
    linSensor = fSensor.readlines()
    fSensor.close()
    posTemp = linSensor[1].find('t=')
    # Si la posicion es valida
    if posTemp != -1:
        # Anadimos son posiciones mas por t=
        strTemp = linSensor[1][posTemp+2:]
        # Calculamos la temperatura real
        Valores.append( float(strTemp) / 1000.0 )

    ## En teoria hasta aca ya medi todas las temperaturas.
    ## Paso a la medicion de pH
    calibration=21.7
    board=pyfirmata.Arduino("/dev/ttyACM2") #Puerto serial x el q la raspi lee el arduino, cambia con las conexiones
    pin0=board.get_pin('a:0:i')

    iterator = pyfirmata.util.Iterator(board)
    iterator.start()
    pin0.enable_reporting()
    buf=[]
    time.sleep(1)
    for i in range(10):
    	#print( "i : " + str(i) )
    	buf.append(pin0.read())
    	#print("valor sensado: " + str(buf[i]))
    	time.sleep(0.03)

    for i in range(9):
    	for j in range(i+1,10):
    		#print("i: " + str(i) + " j: " + str(j))
    		if buf[i] > buf[j]:
    			temp = buf[i]
    			buf[i]=buf[j]
    			buf[j]=temp

    avgValue=0
    for i in range(2,8):
    	avgValue += buf[i]

    phVol=avgValue*5.0/6
    phValue=-5.7 * phVol + calibration
    Valores.append( phValue )

    ##En teoria hasta aca media las temperaturas y pH.
    ## Paso a medir DHT11
    humidity,temperature = Adafruit_DHT.read_retry(22,21)
    Valores.append(humidity)
    Valores.append(temperature)

    return Valores
    ## si la vida fuera facil Valores es un vector que en las primeras 6 posiciones tiene temperaturas de los ds18b20, despues valor de pH y por ultimo humedad y tempamb