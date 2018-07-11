import gpiozero #Con esto importo la libreria de la GPIO

from gpiozero import LED
from time import sleep

led1=LED(17)
led2=LED(18)
led3=LED(15)

while True:
    
    led1.on()
    sleep(1)
    led1.off()

    
    led2.on()
    sleep(1)
    led2.off()

    
    led3.on()
    sleep(1)
    led3.off()

##while True:
##    led.on()
##    sleep(1)
##    led.off()
##    sleep(1)
