# defino por separado las funciones de medicion
import os
import glob
import pyfirmata
import time
import Adafruit_DHT
def medir_temperatura(id_sensor):

	#la onda aca es que pasandome la el sensor que quiere medir yo le devuelvo el valor que tiene
	os.system('modprobe w1-gpio')
	os.system('modprobe w1-therm')
	# Deberiamos cargar este vector con cada ID correspondiente de los sensores
	#Cargamos la ruta donde se encuentra nuestro sensor.
	ruta_sensores = '/sys/bus/w1/devices/'
	Carpeta_Sensor = glob.glob( ruta_sensores + id_sensor)[0]
	print(Carpeta_Sensor)
	fSensor = open(Carpeta_Sensor + '/w1_slave', 'r')
	
	linSensor = fSensor.readlines()
	fSensor.close()

	posTemp = linSensor[1].find('t=')
	# Si la posicion es valida
	if posTemp != -1:
		# Anadimos son posiciones mas por t=
		strTemp = linSensor[1][posTemp+2:]
		# Calculamos la temperatura real
		temperatura =  float(strTemp) / 1000.0

	return temperatura

def medir_ph( id_sensor_temperatura):
	# Me devuelve un array de dos valores. Primero el valor de temperatura. Segundo el valor de pH

	calibration=21.7   ### VER SI ESTE VALOR NO CONVIENE PASARLO POR PARAMETRO osea VER COMO VAMOS A MANEJAR LA CALIBRACION
	board=pyfirmata.Arduino("/dev/ttyACM0") #Puerto serial x el q la raspi lee el arduino, cambia con las conexiones
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
	
	return medir_temperatura(id_sensor_temperatura), phValue

def medir_amb(tipo_sensor):
	#tipo de sensor es 11 o 22
	#humidity,temperature = Adafruit_DHT.read_retry(22,21)
	humidity,temperature = Adafruit_DHT.read_retry(tipo_sensor,21)
	return humidity,temperature
