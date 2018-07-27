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
interavalo_seg=int(sys.argv[3])
#--------------------------------------------


num_medidas=math.trunc((duracion_min * 60) / interavalo_seg)

for i in range(num_medidas):
    datos=primer_funcion.funcion_uno()
    segunda_funcion.escribe(idExp,datos)
    print('Termino la iteracion: '+str(i))
    time.sleep(interavalo_seg)
print('Salio del For')


#sys.exit()
