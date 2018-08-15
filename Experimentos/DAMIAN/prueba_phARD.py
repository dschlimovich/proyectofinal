import pyfirmata
import time

board=pyfirmata.Arduino("/dev/ttyACM0")
pin0=board.get_pin('a:0:i')

iterator = pyfirmata.util.Iterator(board)
iterator.start()
pin0.enable_reporting()

while True:
    if pin0.read()==None:
        pass
    else:
        print( "El valor sin transformar es =" + str(pin0.read()))
        x = (1000*pin0.read())/71.42
##        x = (1000*pin0.read())/73.07
##        x = pin0.read()
        print("El ph es: "+str(x))
    time.sleep(5)
