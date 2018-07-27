import Adafruit_DHT


def get_temptumamb(temperature,humidity):
    while True:
        humidity, temperature = Adafruit_DHT.read_retry(22, 21)
        print("humedad: ", humidity, " y temperatura: ", temperature)

