# Internet of Things - Park Assist
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
│   │   ├── img
│   │   │   ├── aangekregenpackets.JPG
│   │   │   ├── badpackets.png
│   │   │   ├── configuration.png
│   │   │   ├── connectie.png
│   │   │   ├── loragatewayadmin.png
│   │   │   ├── loragateway.png
│   │   │   ├── motedatasend.png
│   │   │   ├── netwerktest.png
│   │   │   ├── onlinelogs.png
│   │   │   ├── packetsreceived.png
│   │   │   └── workinglog.png
│   │   ├── LoRaGateway.md
│   │   └── LoRaMote.md
│   ├── parkeersensor
│   │   ├── img
│   │   │   ├── blokdiagram.png
│   │   │   ├── bovenaanzicht-3d-pcb.png
│   │   │   ├── case design parking sensor open cover.png
│   │   │   ├── case design parking sensor.png
│   │   │   ├── matrix-bord-bovenkant.jpg
│   │   │   ├── matrix-bord-onderkant.jpg
│   │   │   ├── onderaanzicht-3d-pcb.png
│   │   │   ├── park assist on caravan.png
│   │   │   ├── pcb-design-met-power-plane.png
│   │   │   ├── pcb designs.jpg
│   │   │   ├── pcb-design-zonder-power-plane.png
│   │   │   ├── schema parkeersensor.png
│   │   │   └── Software flowchart.png
│   │   ├── parkeersensor.md
│   │   └── pcb-design.md
│   ├── parkeersensor.md
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
    │   │   ├── app.iml
    │   │   ├── build
    │   │   ├── build.gradle
    │   │   ├── proguard-rules.pro
    │   │   └── src
    │   ├── application.iml
    │   ├── build
    │   │   ├── generated
    │   │   └── intermediates
    │   ├── build.gradle
    │   ├── gradle
    │   │   └── wrapper
    │   ├── gradle.properties
    │   ├── gradlew
    │   ├── gradlew.bat
    │   ├── local.properties
    │   └── settings.gradle
    ├── Arduino
    │   ├── ArduinoMote.ino
    │   ├── uitlezen_new_sensors.ino
    │   └── uitlezen_sensoren.ino
    ├── PCB design
    │   ├── park-assist-PCB.ewprj
    │   ├── Park-Assist-Schema.ms13
    │   └── README.md
    └── README.md
    ```
## Documentatie
Hier kan je alle nodige documentatie vinden in dit project.



## Groepsleden

* Mathias Samyn
* Joey Driessen
* Marijn Joosens
