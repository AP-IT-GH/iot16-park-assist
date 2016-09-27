#Projectplan Park Assist

##Team 
- Mathias Samyn
- Joey Driessen
- Marijn Joosens

##Project keuze 

Onze project keuze is gevallen voor *Park Assist* omdat dit ons wel een zeer interresant project leek. 
Met dit project gaan we een bestaand product om een andere manier realiseren. Uiteraard bestaan er al park assist producten op de markt, maar nog geen die afneembaar zijn. Dit stelt het mogelijk om het op je auto te gebruiken of op je caravan wanneer nodig. Tevens vinden wij het interresant om IoT te verbinden met de mobiele telefoon om zo een goeie ervaring voor de gebruiker te realiseren.

##Projectbeschrijving

Wij hebben gekozen voor park assist van Mr. Overdulve. Er wordt van ons verwacht dat we een toestel maken om caravans, opleggers of auto’s zonder sensors te voorzien van parkeersensoren. Aangezien men op caravans geen parkeersensoren plaatst vanuit de fabrikant zelf, komt het regelmatig voor dat men de caravan beschadigt tijdens het inparkeren op de camping/stalling. Men heeft heel beperkt zicht naar achteren toe en al zeker als men om een bochtje moet inparkeren. Dit zorgt voor frustraties bij vele en ook voorkombare kosten. 

Maar niet enkel caravans hebben dit probleem, hedendaags heeft niet elke auto parkeersensoren. Met dit systeem zouden we dus niet enkel reizigers met een sleurhut uit de nood kunnen helpen, maar ook mensen met een oudere auto. Zo hoeft men niet noodzakelijk een nieuwe auto aan te kopen om toch die sensoren te hebben uit voorzorg. Ons systeem zal dus niet enkel mensen redden van een aanzienlijke kost, het zal ook het inparkeren voor mensen met verminderd zicht kunnen vergemakkelijken. Zo zou ons systeem ook kunnen dienen als een oplossing voor het dode hoek-probleem bij grote vrachtwagens/opleggers. Het komt wel vaker op het nieuws voor dat er weer een dode hoek-ongeval is gebeurd met jonge kinderen/verwarde fietsers. Dit komt door het feit dat de bestuurders van deze grote voertuigen vaak beperkt zicht hebben naar buiten toe, en meer bepaald naar de ‘dode hoek’ omdat ze op een hoogte zitten. Zo zouden onze sensoren dus mensen kunnen detecteren die zich naast de vrachtwagen bevinden en het zou zo heel wat ongevallen kunnen voorkomen.

Wij gaan dit probleem oplossen door afneembare modules te maken met telkens 6 sensoren. Iedere sensor heeft een optimale leeshoek van 15°, om dus een volledige hoek te kunnen dekken, maken we gebruik van 6 sensoren. Zo creëren we een hoek van 90° om zo optimaal objecten te kunnen detecteren. De sensoren hebben een bereik vanaf minimaal 2 cm tot ongeveer een maximum van 400cm. Op deze manier hebben we een groot genoeg “kijkvlak” om alles te detecteren en kunnen we meldingen geven aan de hand van vooraf bepaalde afstanden. Deze meldingen zouden we sturen naar een mobiele app die via bluetooth met deze modules kan verbinden om zo het park assist-systeem te vervolledigen. 

Het idee is om modules te creëren om op de hoeken te plaatsen. Per module maken we gebruik van 6 ultrasone sensoren, een Arduino voor het verwerken van de informatie en het berekenen van de afstanden en een bluetoothmodule waarmee men kan verbinden met de mobiele applicatie. Een idee kan zijn om vanaf 1.5m een onderbroken pieptoon te geven, vanaf 50cm een onderbroken pieptoon met een snellere frequentie en vanaf 30cm een durende pieptoon om aan te geven dat men zeer dicht tot een bepaald object zit.

##Functionele analyse

###Bruikbare technologieën

####Software
- Android
- Bluetooth
- C++ ( Arduino )


####Hardware

- Bluetooth module (HC-05)
- Ultra Sonic range measurement module ( M:SEN136B5B)
- Arduino ( ATMEGA328P-PU )
- Android phone

###Backlog
- [ ] As a developer I need to prototype with the Arduino platform to work with the sensors
- [ ] As a developer I need to make sure the sensors work together in a 90 degrees cone
- [ ] As a developer I need a PCB design to start developing with the Arduino
- [ ] As a developer I need a Multisim design to start developing the PCB
- [ ] As a developer I need to send data from the PCB to the Mobile App

- [ ] As a user I need a mobile application to show where the parking sensors are
- [ ] As a user I want to choose where I put my parking sensor in the mobile app
- [ ] As a user I want to see the distance from the car to the object aprouching in the mobile app
- [ ] As a user I want to hear a sound telling me that I am nearing a solid object
- [ ] As a user I want a simple interface to show the distance from the parking sensor




