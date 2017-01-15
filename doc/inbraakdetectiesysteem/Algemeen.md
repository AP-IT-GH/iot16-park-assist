# Anti-inbraaksysteem
In dit document kunt u alles lezen over hoe het inbraaksysteem in zijn werking gaat.
U vindt hier tevens ook hoe het systeem in zijn geheel is opgemaakt. U zou dit document kunnen gebruiken als guide om een vergelijkbaar systeem te maken.

## Beschrijving anti-inbraaksysteem

### Doel van het anti-inbraaksysteem
Iets wat campers/caravans momenteel niet erg bestendig tegen zijn, is inbraak. Met ons systeem zouden de eigenaars, al dan niet met een iets geruster hart, hun caravan kunnen achterlaten.
De bedoeling van ons product is dat, na installatie in de caravan, de eigenaar zijn sleurhut zou kunnen achterlaten zonder toch een gevoel van paranoïa te hebben. Ons systeem zou ervoor zorgen dat, stel er toch een inbraak plaatsvindt, de eigenaar een notificatie op zijn/haar smartphone zou aankrijgen.

### Werking en staat van het anti-inbraaksysteem

In principe is het systeem ongeveer plug & play. Vanaf je het systeem power geeft en je op de app aangeeft dat je het systeem in werking hebt gesteld, kan men meldingen ontvangen.
Dus eens men deze dingen gedaan heeft, begint de sensor te detecteren of er objecten/personen voorbij komen.

Het detecteren gebeurt via een infrarood sensor die data verstuurd naar de Arduino. Op de Arduino runt een programma dat constant de gemiddelde afstand berekent en als er een anomalie tussenkomt dan zal er een melding optreden.
Dit gaat als volgt; de Arduino zal dan een bepaalde code in actie stellen. Deze zorgt ervoor dat er via de LoRa Mote een pakketje zal worden verstuurd naar de dichtsbijzijnde LoRa Gateway. De Gateway zal dan via het Things Network terugkunnen sturen naar de eigenaar. Vervolgens zal de eigenaar dus een melding krijgen op zijn smartphone.

Schematisch gezien ziet het er zo uit:
![diagram-inbraak](img/diagram-inbraak.png)

De huidige staat van het anti-inbraaksysteem is realistisch gezien niet exact hetzelfde.
Om ons systeem te laten werken, is er echter een gateway nodig die actieve verbinding heeft met het TTN netwerk.
Aangezien dit een technologie is die voorlopig nog in verdere ontwikkeling zit, is dit niet zo voornamelijk. Gateways zijn nog niet zo heel frequent maar meerdere telecom bedrijven beginnen met deze technologie te experimenteren dus we blijven hoopvol.
Er is ook nog het probleem dat we de Mote om de zoveel keren terug moeten activeren. Het TTN netwerk zet de Mote op inactief als er enige tijd niets meer wordt verzonden. Als de power wegvalt, zal dit tevens ook moeten gebeuren.

## Besluit

Hoewel we pas laat aan dit project zijn beginnen, is het toch vrij vlot en goed verlopen. We hebben een werkende use case, wat de grootste vereiste was van dit project. Tijdens het project was het soms moeilijk om aan informatie omtrent LoRaWan te komen, aangezien dit een technologie is die continu wordt uitgebreid, maar dankzij de paar site's die hier wel mee bezig zijn hebben we alles kunnen oplossen.

Indien er naar de schaalbaarheid van het project wordt gekeken, kan er wel gezegd worden dat het systeem te duur is voor wat het doet. Nu moesten we zelf een LoRa Gateway gebruiken wat de grootste kostfactor was, maar indien het systeem in productie zou gaan, zou dit wegvallen omdat we dan afhankelijk zijn van al reeds geïnstalleerde gateways. Ondanks het wegvallen van de gateway lijkt dit product nog steeds te duur. In termen van haalbaarheid is dit project zeker haalbaar met de producten die al reeds op de markt te koop zijn.

Omdat de technologie nog volop in ontwikkeling is en er nog niet overal LoRaWan coverage is, kan het project enkel gebruikt worden in gebieden waar dit wel het geval is. Dit zorgt ervoor dat de schaalbaarheid en haalbaarheid van het project beperkt worden tot gebieden waar deze coverage voldoende is. 
