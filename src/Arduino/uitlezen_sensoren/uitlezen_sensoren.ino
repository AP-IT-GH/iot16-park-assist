/*
 HC-SR04 Ping distance sensor:
 VCC to arduino 5v 
 GND to arduino GND
 Echo to Arduino pin 7 
 Trig to Arduino pin 8
 
 This sketch originates from Virtualmix: http://goo.gl/kJ8Gl
 Has been modified by Winkle ink here: http://winkleink.blogspot.com.au/2012/05/arduino-hc-sr04-ultrasonic-distance.html
 And modified further by ScottC here: http://arduinobasics.blogspot.com.au/2012/11/arduinobasics-hc-sr04-ultrasonic-sensor.html
 on 10 Nov 2012.
 */


#define echoPin1 5// Echo Pin 1
#define echoPin2 6 //Echo Pin 2
#define trigPin1 10 // Trigger Pin
#define trigPin2 11 //Trigger Pin
#define LEDPin 13 // Onboard LED

int maximumRange = 400; // Maximum range needed
int minimumRange = 0; // Minimum range needed
long duration1,duration2 , distance1, distance2; // Duration used to calculate distance

void setup() {
 Serial.begin (9600);
 pinMode(trigPin1, OUTPUT);
 pinMode(trigPin2, OUTPUT);
 pinMode(echoPin1, INPUT);
 pinMode(echoPin2, INPUT);
 pinMode(LEDPin, OUTPUT); // Use LED indicator (if required)
}

void loop() {
/* The following trigPin/echoPin cycle is used to determine the
 distance of the nearest object by bouncing soundwaves off of it. */ 
/* digitalWrite(trigPin1, LOW);
 
 delayMicroseconds(2); 
*/
 digitalWrite(trigPin1, HIGH);
 
 delayMicroseconds(10); 
 
 digitalWrite(trigPin1, LOW);
 
 duration1 = pulseIn(echoPin1, HIGH);

 //trigger/echo 2nd sensor
 /* digitalWrite(trigPin2, LOW); 

  delayMicroseconds(2); 
*/
  digitalWrite(trigPin2, HIGH);

  delayMicroseconds(10);
  
  digitalWrite(trigPin2, LOW);
 
 duration2 = pulseIn(echoPin2, HIGH);
 
 
 //Calculate the distance (in cm) based on the speed of sound.
 distance1 = duration1/58.2;
 distance2 = duration2/58.2;
 
 /*if ((distance1 >= maximumRange || distance1 <= minimumRange) && (distance2 >= maximumRange || distance2 <= minimumRange) ){
 // Send a negative number to computer and Turn LED ON 
 //to indicate "out of range" 
 Serial.println("-1");
 digitalWrite(LEDPin, HIGH);
 }
 else {
 /* Send the distance to the computer using Serial protocol, and
 turn LED OFF to indicate successful reading. */
 Serial.println(distance1);
 Serial.println(distance2);
 //digitalWrite(LEDPin, LOW); 
 //}

 
 
 //Delay 50ms before next reading.
 delay(150);
}
