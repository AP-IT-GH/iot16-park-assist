# Internet of Things - Park Assist
In deze repo ga je al onze code en documentatie die dit project mogelijk maken


## Inhoudsopgave
Hieronder is een inhoudsopgave van de belangrijkste onderdelen in ons project.

- [documentatie](doc/)
  - [Bill of materials](doc/Bill%20of%20materials.md)
  - [parkeer sensor](doc/parkeersensor/)
    - [images](doc/parkeersensor/img)
    - [parkeersensor documentatie](doc/parkeersensor/parkeersensor.md)
    - [parkeersensor PCB documentatie](doc/parkeersensor/pcb-design.md)
  - [inbraakdetectiesysteem](doc/inbraakdetectiesysteem/)
  	- [images](doc/inbraakdetectiesysteem/img)
  	- [lgemeen](doc/inbraakdetectiesysteem/Algemeen.md)
  	- [handleiding](doc/inbraakdetectiesysteem/Handleiding.md)
- [source](src/)
  - [inbraakdetectiesysteem](src/inbraakdetectiesysteem)
  	- [Android applicatie](src/inbraakdetectiesysteem/Android/application)
  	- [Arduino code](src/inbraakdetectiesysteem/Arduino/LoRa_volledig_werkend.ino)
  - [parkeer sensor](src/parkeersensor)
  	- [Android applicatie](src/parkeersensor/Android/application)
  	- [Arduino code](src/parkeersensor/Arduino/uitlezen3sensorenenVerzendenbt.ino)
  	- [Multisim schema](src/parkeersensor/PCB%20design/Park-Assist-Schema.ms13)
  	- [PCB design](src/parkeersensor/PCB%20design/park-assist-PCB.ewprj)

## Beschrijving Project
Park Assist is een portable parkeer sensor voor bijvoorbeeld op een aanhanger of caraven.
De bedoeling van dit project is om parkeer sensoren te bieden aan voortuigen die dit niet hebben.
Tevens bied dit project ook een inbraak systeem die controleert of een deur wordt open gemaakt op een moment dat dit niet mag.
Hierbij wordt gebruikt van LORA en het Things Network om de gebruiker te waarschuwen.


##Folder structuur
```
.
├── doc
│   ├── Bill of materials.md
│   ├── deliverables.md
│   ├── inbraakdetectiesysteem
│   │   ├── Algemeen.md
│   │   ├── Handleiding.md
│   │   ├── img
│   │   ├── loragatewayinstall.pdf
│   │   ├── loragatewaymanual.pdf
│   │   ├── LoRaGateway.md
│   │   └── LoRaMote.md
│   ├── parkeersensor
│   │   ├── img
│   │   ├── parkeersensor.md
│   │   └── pcb-design.md
│   ├── Projectplan Park Assist.md
│   ├── README.md
│   ├── schema
│   │   ├── 1 cell battery protect.ms13
│   │   ├── 1 cell battery protect.ms13 (Security copy)
│   │   ├── battery-protection (Exemplaar met conflict van Marijn Joosens 2016-10-16).ms13
│   │   ├── battery-protection.ms13
│   │   ├── battery-protection.ms13 (Security copy)
│   │   ├── park-assist-PCB@2016-10-15-2147.ewnet
│   │   ├── park-assist-PCB.ewprj
│   │   ├── park-assist-PCB.ewprj (Security copy)
│   │   ├── park-assist-PCB (Exemplaar met conflict van joey driessen 2016-10-15).ewprj
│   │   ├── Park-assist-Schema@2016-10-16-2014.ewnet
│   │   ├── Park-assist-Schema@2016-10-16-2016.ewnet
│   │   ├── Park-assist-Schema.ms13
│   │   ├── Park-assist-Schema.ms13 (Security copy)
│   │   ├── pcb-design.md
│   │   ├── protection-with-blocks.ms13
│   │   ├── protection-with-blocks.ms13 (Security copy)
│   │   ├── UltrasonicSensor.ms13
│   │   └── UltrasonicSensor.ms13 (Security copy)
│   └── timing.md
├── README.md
└── src
    ├── application
    │   ├── app
    │   │   └── build
    │   └── build
    │       ├── generated
    │       └── intermediates
    ├── inbraakdetectiesysteem
    │   ├── Android
    │   │   └── application
    │   └── Arduino
    │       └── LoRa_volledig_werkend.ino
    ├── parkeersensor
    │   ├── Android
    │   │   └── application
    │   ├── Arduino
    │   │   └── uitlezen3sensorenenVerzendenbt.ino
    │   └── PCB design
    │       ├── park-assist-PCB.ewprj
    │       ├── Park-Assist-Schema.ms13
    │       └── README.md
    └── README.md
```


## Groepsleden

* Mathias Samyn
* Joey Driessen
* Marijn Joosens
