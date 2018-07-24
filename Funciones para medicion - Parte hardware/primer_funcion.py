#Aca voy definir la primer funcion
# Se encarga de tomar los datos de todos los sensores y meterlos en un arreglo

# Librerias para la lectura de la temperatura
import os
import glob

def obtener_valores():
    # tengo 6 temperatura (1 para pH), amb y pH
    os.system('modprobe w1-gpio')
    os.system('modprobe w1-therm')
    Temperaturas=[]
    #Cargamos la ruta donde se encuentra nuestro sensor.
    Temperaturas.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[0])
    Temperaturas.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[1])
    Temperaturas.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[2])
    Temperaturas.append(glob.glob( '/sys/bus/w1/devices/' + '28*')[3])
