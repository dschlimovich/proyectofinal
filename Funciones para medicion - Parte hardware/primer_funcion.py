#Aca voy definir la primer funcion
# Se encarga de tomar los datos de todos los sensores y meterlos en un arreglo
import os.path
import mediciones

def funcion_uno(mido_ph):
    # en mediciones tengo las 3 funciones: medir_temperatura, medir_ph, medir_amb
    # en &datos tengo que insertar los valores.
    datos = []
    
    """
    Num de Sensor------------->ID----------------Uso
    1-------------->28-000008e280f3--------------Olla principal
    2-------------->28-000008e44af9--------------Olla principal
    3-------------->28-000008e3a29b--------------Olla principal
    4-------------->28-00000901cc93--------------Olla principal
    5-------------->28-000008e44df6--------------Olla secundaria
    6-------------->28-000008e270f2--------------Phimetro
    """
    
    ruta_sensores = '/sys/bus/w1/devices/'
    
    ruta_sensor = []
    ruta_sensor.append('28-000008e280f3')
    ruta_sensor.append('28-000008e44af9')
    ruta_sensor.append('28-000008e3a29b')
    ruta_sensor.append('28-00000901cc93')
    ruta_sensor.append('28-000008e44df6')
    ruta_sensor.append('28-000008e270f2') #posicion 6 es para pH
    
    for i in range(0,6):
        if os.path.isdir(ruta_sensores + ruta_sensor[i]):
            datos.append( mediciones.medir_temperatura( ruta_sensores + ruta_sensor[i] ) )
        else:
            datos.append(-1000)
            
    if( mido_ph ):
        ph =  mediciones.medir_ph()
    else:
        ph = -1
            
    datos.append(ph) # ver que esto funcione

    hum_amb, temp_amb = mediciones.medir_amb(22)

    datos.append(hum_amb)
    datos.append(temp_amb)

    return datos
