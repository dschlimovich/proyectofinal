import gpiozero
from gpiozero import DistanceSensor

ultrasonic=DistanceSensor(echo=21, trigger =20)

while True:
    print(ultrasonic.distance)
