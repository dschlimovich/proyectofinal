import sys
import math
import time
import threading

import primer_funcion

datos = primer_funcion.funcion_uno(1)

#temp = (datos[0]+datos[1]+datos[2]+datos[3])/4
#ph = datos[6]
#salida = str(temp) + "&" + str(ph)
salida=make_string(datos)




print(salida)
def concat( cadena, valor):
# funcion para que concatene la cadena //valor// al final de la cadena
if (cadena == ""):
	return valor
return cadena + "&" + valor

def make_string(datos):
cadena = ""
for valor in datos:
	valorString=str(valor)
	cadena = concat(cadena, valorString)
return cadena