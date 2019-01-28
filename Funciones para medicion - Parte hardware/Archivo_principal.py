import sys
import math
import time
import threading

import primer_funcion
import segunda_funcion

#Valores que el archivo recibe como parametro
#--------------------------------------------
idExp=int(sys.argv[1])
duracion_min=int(sys.argv[2])
intervalo_temp=int(sys.argv[3]) #en segundos
intervalo_ph=int(sys.argv[4]) # en segundos pero multiplo de el intervalo de temperatura
#--------------------------------------------

num_medidas=math.trunc((duracion_min * 60) / intervalo_temp)
contador = int (intervalo_ph / intervalo_temp)
# el contador va decreciendo hasta que es 0, ahi realiza las mediciones de ph.

datos=[]
phHolder = -1
for i in range(num_medidas):
    timeInit = time.time()
    contador = contador - 1
    if (contador == 0):
        datos = primer_funcion.funcion_uno(1)
        contador = int (intervalo_ph / intervalo_temp)
        phHolder = datos[6]
    else:
        datos=primer_funcion.funcion_uno(0)
        datos[6]=phHolder
    segunda_funcion.escribe(idExp,datos) #Funcion q escribe en la BD
    #print('Termino la iteracion: '+str(i))
    while( (time.time() - timeInit) < intervalo_temp):
        time.sleep(1)
    #time.sleep(intervalo_temp-40)
print('Devuelvo algo')


#sys.exit()
