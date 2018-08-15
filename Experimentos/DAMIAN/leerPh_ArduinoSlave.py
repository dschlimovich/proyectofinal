import pyfirmata
import time

calibration=21.7
board=pyfirmata.Arduino("/dev/ttyACM0") #Puerto serial x el q la raspi lee el arduino, cambia con las conexiones
pin0=board.get_pin('a:0:i')

iterator = pyfirmata.util.Iterator(board)
iterator.start()
pin0.enable_reporting()
buf=[]
time.sleep(1)
for i in range(10):
    #print( "i : " + str(i) )
    buf.append(pin0.read())
    #print("valor sensado: " + str(buf[i]))
    time.sleep(0.03)

for i in range(9):
    for j in range(i+1,10):
        #print("i: " + str(i) + " j: " + str(j))
        if buf[i] > buf[j]:
            temp = buf[i]
            buf[i]=buf[j]
            buf[j]=temp
avgValue=0
for i in range(2,8):
    avgValue += buf[i]
phVol=avgValue*5.0/6
phValue=-5.7 * phVol + calibration



print(phValue)
    

"""
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
"""