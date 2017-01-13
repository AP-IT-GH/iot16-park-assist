#Verbinden met de lora mote:

putty werkt niet.
Wordt nu gebruik gemaakt van termite 3.2 wat zelf de juiste instellingen detecteert, er moet enkel nog ingesteld worden dat CR+LF append moeten worden.

##juiste instellingen:
- Baud rate: 57600
- Parity: none
- local echo: on
- data bits: 8
- stop bits: 1
- flow control: none
- append CR+LF: on

Mote wordt toegevoegd aan het netwerk aan de hand van abp (activation by personalization)
... uitleg abp vs otaa etc...

radio tx 48656c6C6F
zal via terminal hello zenden

We vermoeden dat dit gebruikt wordt voor van mote naar een andere mote data te verzenden.

we krijgen terug:

ok 
mac_tx_ok
In the responses, the first "ok" just shows that the command was accepted by the API, and the "mac_tx_ok" shows that the packet was transmitted, so should be expected.