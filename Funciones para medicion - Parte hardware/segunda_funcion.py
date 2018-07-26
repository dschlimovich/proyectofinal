import pymysql.cursors

def escribe(num_de_exp, datos):
	"""" Recibe como parametros: num_de_exp, [temp1, temp2, temp3, temp4, temp5, tempPh, tempAmb, humity, pH] """
		# Connect to the database
	connection = pymysql.connect(host='localhost',
	                             user='pi',
	                             password='raspberry',
	                             db='maceraciones',
	                             charset='utf8mb4',
	                             cursorclass=pymysql.cursors.DictCursor)

	try:
	    with connection.cursor() as cursor:
	        # Create a new record
	        sql = "INSERT INTO `sensedValues`(exp, temp1, temp2, temp3, temp4, tempPh, tempAmb, humity, pH)  VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
	        cursor.execute(sql, ('num_de_exp, datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7]'))

	    # connection is not autocommit by default. So you must commit to save
	    # your changes.
	    connection.commit()

	finally:
	    connection.close()

