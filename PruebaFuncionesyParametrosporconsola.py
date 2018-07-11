import sys


nom=sys.argv[1]
Ape=sys.argv[2]
MuchasBoludeces=[]
if len(sys.argv) > 3:
    for i in range(3,len(sys.argv)):
       MuchasBoludeces.append(sys.argv[i])

 
    
def funcionPrueba(Nombre,Apellido,MuchasBoludeces):
    print(Nombre)
    print(Nombre + Apellido)
    if len(MuchasBoludeces) >0:
        for i in range(0,len(MuchasBoludeces)):
            print(MuchasBoludeces[i])



#funcionPrueba("Damian"," esta cagado de miedo xq ")
#funcionPrueba("Damian"," esta cagado de miedo xq ","le","ofrecieron","darle","un","depa para irse a vivir solo")
            
funcionPrueba(nom,Ape)

