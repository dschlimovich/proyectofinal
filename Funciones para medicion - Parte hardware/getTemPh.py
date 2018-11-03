import sys
import math
import time
import threading

import primer_funcion

datos = primer_funcion.funcion_uno(1)

temp = (datos[0]+datos[1]+datos[2]+datos[3])/4
ph = datos[6]
salida = temp + "-" + ph

print(salida)