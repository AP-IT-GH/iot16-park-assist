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

#Inbraakdetectie systeem
##Korte inleiding
Als een extra toevoeging aan ons project, gaan we een inbraakdetectie systeem maken dat gebruik maakt van LoRaWan technologie. Door gebruik te maken van LoRaWan kunnen we de module verbinden met het internet, zelfs op meer afgelegen plaatsen, waardoor dit systeem uitermate geschikt is voor een caravan.

##Hardware
- LoRa Gateway (http://www.microchip.com/DevelopmentTools/ProductDetails.aspx?PartNO=dm164138)
- LoRa Mote
- IR Distance Sensor
- Arduino DUE
- Bluetooth module HC05

##Module Hardware
###LoRa Mota verbinden met Arduino
Verbind de Mote op de volgende manier met de Arduino DUE:

![lora_arduino_connect.png](img/lora_arduino_connect.png)

Indien je de LoRa Mote gebruikt, zorg er dan altijd voor dat de antenne verbonden is met het board voor je er stroom opzet. Indien je dit niet doet, kan dit ernstige schade toebrengen. 

###Arduino verbinden met Bluetooth module
Verbind de Arduino DUE op de volgende manier met de Bluetooth module:

![bluetooth_arduino_connection.png](img/bluetooth_arduino_connection.png)

###IR sensor verbinden met Arduino
Verbind de IR sensor op de volgende manier met de Arduino DUE:

![sensor_arduino_connection.png](img/sensor_arduino_connection.png)

##Module software
###Registreren op TTN
Ga naar de volgende site van TTN (https://v1.account.thethingsnetwork.org/users/login) en maak hier een account aan. Eenmaal je ingelogd bent, ga je naar je dashboard. --> is allemaal veranderd naar deze site precies: https://console.thethingsnetwork.org/applications/add

####ABP vs OTAA

###Waarom TTN?

The Things Network is een netwerk dat Crowd Sourced IOT van vrij gebruik voorziet; Op dit netwerk is het voor ons dus ook toegestaan om ons project verder te realiseren.

Semtech is een bedrijf dat een router host dat alle gateways mogen gebruiken voor demonstratie doeleinden.
Semtech stelt dus enkel hun services vrij voor demonstraties, het zou maar een tijdelijke oplossing zijn.

Loraley is een alternatief open source data netwerk, deze is nog in primitieve staat dus nog niet aan te raden als betrouwbaar netwerk. Het is dus nog niet Up & Running naar behoren.

###Verbinden met LoRa Mote
Indien je rechtstreeks verbinding wilt maken met de RN2483 Transceiver module die zich op dit bord bevind, zal je je laptop via een USB to Micro-USB kabel moeten verbinden met de LoRa Mote.

Nadat dit is gebeurt, zal de LoRa Mote opstarten, waarna we Serieel verbinding kunnen maken. Dit doen we door een programma termite 3.2 te gebruiken, aangezien Putty hier niet zal werken. Op thermite stel je de volgende instellingen moeten aanpassen:

- Baud rate: 57600
- Parity: none
- local echo: on
- data bits: 8
- stop bits: 1
- flow control: none
- append CR+LF: on

Eenmaal dit alles gedaan is, kunnen we verbinding maken met de RN2483 Transceiver module. De verschillende commando's die hier gebruikt kunnen worden, zijn terug te vinden op de volgende site: ...

Indien je een message wilt verzenden, doe je dit met het volgende commando: ....

###Arduino code
De code voor de arduino kan je in deze GitHub repository terugvinden op de volgende plek: iot16-park-assist/src/Arduino/LoRa_volledig_werkend/LoRa_volledig_werkend.ino

##LoRa Gateway



######Source: https://www.thethingsnetwork.org/wiki/Backend/Connect/Gateway


