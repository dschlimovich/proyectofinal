#Aca voy definir la primer funcion
# Se encarga de tomar los datos de todos los sensores y meterlos en un arreglo

import mediciones

def funcion_uno():
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

    #id_sensor1 = '28-000008e280f3'
    #id_sensor2 = '28-000008e44af9'
    #id_sensor3 = '28-000008e3a29b'
    id_sensor4 = '28-00000901cc93'
    id_sensor5 = '28-000008e44df6'
    #id_sensor_temp_ph = '28-000008e270f2'

    #datos.append( mediciones.medir_temperatura(id_sensor1))
    #datos.append( mediciones.medir_temperatura(id_sensor2))
    #datos.append( mediciones.medir_temperatura(id_sensor3))
    datos.append( mediciones.medir_temperatura(id_sensor4))
    datos.append( mediciones.medir_temperatura(id_sensor5))

    #hacerlo para todos los sensores de temperatura.

    id_sensor_temp_ph=id_sensor5
    temp_ph, ph =  mediciones.medir_ph(id_sensor_temp_ph)   #medir ph devuelve un array de dos valores: temperatura, ph

    datos.append(temp_ph) # ver que esto funcione
    datos.append(ph) # ver que esto funcione

    hum_amb, temp_amb = mediciones.medir_amb(11)

    datos.append(hum_amb)
    datos.append(temp_amb)

    return datos
