def concat( cadena, valor):
# funcion para que concatene la cadena //valor// al final de la cadena
if (cadena == ""):
	return valor
return cadena + "&" + valor

def make_string(datos):
cadena = ""
for valor in datos:
	cadena = concat(cadena, valor)
return cadena