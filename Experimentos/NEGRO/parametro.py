import sys
import time

#for elements in sys.argv: 
#    print elements

print( "Bienvenido! \nEsta es la funcion que muestra los valores de los parametros" ) 
if len( sys.argv ) > 1:
    for i in range(1, len(sys.argv)):
        #print "\nInicio : %s" % time.ctime()
        print ("\nInicio " + time.strftime("%X"))

        print( 'parametro ' + str(i))
        print( 'valor ' + str( sys.argv[i] ) )

        time.sleep( 5 )
        #print "Fin : %s" % time.ctime()
        print ("Fin " + time.strftime("%X"))
print( "\nSe ha recorrido toda la lista" )
