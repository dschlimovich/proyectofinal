import Adafruit_DHT

while True:
    humidity,temperature = Adafruit_DHT.read_retry(22,21)
    print("humedad: ",humidity," y temperatura: ",temperature)
