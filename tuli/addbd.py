import sys
import time
import pymysql.cursors

print( "Bienvenido!")
print( "sintaxis addbd.py x y t")
print( "En esta funcion podra agregar el entero x a la base de datos")
print( "y cantidad de veces")
print( "cada t segundos")

#bien primero lo que tengo que hacer es verificar que tenga la cantidad de parametros correcta
if len( sys.argv) <> 4:
    print("Error en la cantidad de argumentos")
else:
    connection = pymysql.connect(host='localhost',user='phpmyadmin',password='raspberry',db='prueba',charset='utf8mb4',cursorclass=pymysql.cursors.DictCursor)
    valor = sys.argv[1]
    cant = int( sys.argv[2] )
    delay = float( sys.argv[3])

    for i in range( 1, cant + 1):
        print( "\nIteracion numero " + str( i ))
        print( "Inicio " + time.strftime("%X"))
        time.sleep(delay)
        connection.connect()
        try:
            with connection.cursor() as cursor:
                sql = "INSERT INTO tabla(numero) VALUES (" + valor + ");"
                cursor.execute(sql)
            connection.commit()
        finally:
            connection.close()
        print( "Fin " + time.strftime("%X"))

       
