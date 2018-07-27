import sys
import math
import time

import primera_funcion
import segunda_funcion

#Valores que el archivo recibe como parametro
#--------------------------------------------
idExp=sys.argv[1]
duracion_min=sys.argv[2]
interavalo_seg=sys.argv[3]
#--------------------------------------------


num_medidas=math.trunc((duracion_min * 60) / interavalo_seg)

for i in range(num_medidas):
	datos=primera_funcion()
	segunda_funcion(idExp,datos)
	time.sleep(interavalo_seg)
