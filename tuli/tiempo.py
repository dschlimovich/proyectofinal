import sys
import time

print( "Bienvenido! \nEsta es la funcion que muestra dami puto x cantidad de veces cada t segundos, siendo x y t pasados por parametro" )

if len( sys.argv ) <> 3:
    print ("pusiste menos o mas cantidad de parametros requeridos (2)")
else:
    #el primer parametro es la cantidad de veces
    cant = int (sys.argv[1])
    delay = float (sys.argv[2])

    for i in range(1, cant + 1):
        print ("\nInicio " + time.strftime("%X"))
        time.sleep(delay)
        
        print( "Iteracion numero " + str(i) )
        
        print( "Dami Puto")

        print ("Fin " + time.strftime("%X"))
