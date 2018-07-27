#Aca voy definir la primer funcion
# Se encarga de tomar los datos de todos los sensores y meterlos en un arreglo

import mediciones

def funcion_uno():
    # en mediciones tengo las 3 funciones: medir_temperatura, medir_ph, medir_amb
    # en &datos tengo que insertar los valores.
    datos = []

    id_sensor1 = 28

    datos.append( medir_temperatura(id_sensor1))
    
    id_sensor2 = 28

    datos.append( medir_temperatura(id_sensor2))

    #hacerlo para todos los sensores de temperatura.

    id_sensor_temp_ph = 28

    temp_ph, ph =  medir_ph(id_sensor_temp_ph)   #medir ph devuelve un array de dos valores: temperatura, ph

    datos.append(temp_ph) # ver que esto funcione
    datos.append(ph) # ver que esto funcione

    hum_amb, temp_amb = medir_amb(11)

    datos.append(hum_amb)
    datos.append(temp_amb)

    return datos
