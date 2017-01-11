 // this constant won't change.
 //It's the pin number 
 // of the sensor's output: 
 const int pingPin = 6;
 const int pingPin2 = 5;
 const int pingPin3 = 4;
 const int LedPin = 7;
 long distanceArray[3];
 long closestDistance;
 void setup() {
  // initialize serial communication:
  
  Serial.begin(9600); 
 }
 
void loop() {
  closestDistance = 99999;
  // establish variables for duration of the ping,   
  // and the distance result in inches and centimeters:
  long duration,duration2, duration3, cm,cm2,cm3;

   // The PING))) is triggered by a HIGH pulse of 2 or more microseconds.
   // Give a short LOW pulse beforehand to ensure a clean HIGH pulse:
   //set pinModes
   pinMode(LedPin, OUTPUT);
   pinMode(pingPin, OUTPUT);
   digitalWrite(pingPin,LOW);
   delayMicroseconds(2);
   digitalWrite(pingPin,HIGH);
   delayMicroseconds(5);
   digitalWrite(pingPin,LOW);
  /*same pin is used to read the signal from the PING */
   pinMode(pingPin, INPUT);
   duration = pulseIn(pingPin, HIGH);
   
   //trigger 2nd sensor

   pinMode(pingPin2, OUTPUT);
   digitalWrite(pingPin2,LOW);
   delayMicroseconds(2);
   digitalWrite(pingPin2,HIGH);
   delayMicroseconds(5);
   digitalWrite(pingPin2,LOW);

   pinMode(pingPin2, INPUT);
   duration2 = pulseIn(pingPin2, HIGH);

   //trigger 3rd sensor

   pinMode(pingPin3, OUTPUT);
   digitalWrite(pingPin3,LOW);
   delayMicroseconds(2);
   digitalWrite(pingPin3,HIGH);
   delayMicroseconds(5);
   digitalWrite(pingPin3,LOW);

   pinMode(pingPin3, INPUT);
   duration3 = pulseIn(pingPin3, HIGH);
   
   // The same pin is used to read the signal from the PING))):a HIGH  
   // pulse whose duration is the time (in microseconds) from the sending  
   // of the ping to the reception of its echo off of an object.
  
   
   
   
   
   // convert the time into a distance
   distanceArray[0] = microsecondsToCentimeters(duration);
   distanceArray[1] = microsecondsToCentimeters(duration);
   distanceArray[2] = microsecondsToCentimeters(duration);
   for(int i=0; i < 2; i++){
    if(distanceArray[i] < closestDistance){
      closestDistance = distanceArray[i];
    }
   }
   cm = microsecondsToCentimeters(duration);
   cm2= microsecondsToCentimeters(duration2);
   cm3 = microsecondsToCentimeters(duration3);
   
//   Serial.print(cm);
//   Serial.print("cm");
//   Serial.println();
//   Serial.print(cm2);
//   Serial.print("cm2");
//   Serial.println();
//   Serial.print(cm3);
//   Serial.print("cm3");
//   Serial.println();
//   Serial.print("closest distance");
   digitalWrite(LedPin,HIGH);
   Serial.print(closestDistance);
   delay(2000); 
   digitalWrite(LedPin,LOW);
   }
 
 
 long microsecondsToCentimeters(long microseconds) {
  // The speed of sound is 340 m/s or 29 microseconds per centimeter. 
  // The ping travels out and back, so to find the distance of the 
  // object we take half of the distance travelled. 
  return microseconds / 29 / 2; 
 }
