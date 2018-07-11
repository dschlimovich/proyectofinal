# Librerias para la lectura de la temperatura
import os
import glob

#Cargamos los módulos en el kernel del sistema para manejar el bus 1wire del conector GPIO del Raspberry Pi.
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

#Cargamos la ruta donde se encuentra nuestro sensor.
Carpeta_Sensor = glob.glob( '/sys/bus/w1/devices/' + '28*')[0]
"""
#Con esta linea cargamos el nombre del primer sensor de la lista disponible,
para cargar las demás rutas solo tendremos que cambiar “[0]” por la posición deseada.

Lo siguiente que haremos es leer el fichero “w1_slave”, para leer el valor de la temperatura del sensor.
Leemos el fichero y cargamos todas las lineas disponibles en una variable.
"""


fSensor = open(Carpeta_Sensor + '/w1_slave','r')
linSensor = fSensor.readlines()
fSensor.close()

"""
Una vez que ya tenemos el contenido del fichero, buscamos el parámetro “t” donde esta la temperatura.
Una vez que encontramos la temperatura, la multiplicamos por 1000, par obtener el valor real.
"""

posTemp = linSensor[1].find('t=')
# Si la posicion es valida
if posTemp != -1:
    # Anadimos son posiciones mas por t=
    strTemp = linSensor[1][posTemp+2:]
    # Calculamos la temperatura real
    temperatura = float(strTemp) / 1000.0
print(temperatura)
