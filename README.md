# Internet of Things - Park Assist
In deze repo ga je al onze code en documentatie vinden die dit project mogelijk maken.


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
  	- [algemeen](doc/inbraakdetectiesysteem/Algemeen.md)
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
│   │   └── img
│   ├── parkeersensor
│   │   ├── img
│   │   ├── parkeersensor.md
│   │   └── pcb-design.md
│   ├── Projectplan Park Assist.md
│   ├── README.md
│   └── timing.md
├── README.md
└── src
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
