// Basic bluetooth test sketch. HC-0x_FC-114_01_9600
//  Uses hardware serial to talk to the host computer and software serial for communication with the bluetooth module
//
//  Pins
//  BT VCC to Arduino 5V out. 
//  BT GND to GND
//  Arduino D3 to BT RX through a voltage divider
//  Arduino D2 BT TX (no need voltage divider)
//
//  When a command is entered in the serial monitor on the computer 
//  the Arduino will relay it to the bluetooth module and display the result.
//
// The HC-0x FC-114 modules require CR and NL

#include <SoftwareSerial.h>
SoftwareSerial BTSerial(2, 3); // RX | TX


char c = ' ';
boolean NL = true;

void setup() 
{
      Serial.begin(9600);
      Serial.println("Sketch HC-0x_FC-114_01_9600");
      Serial.println("Arduino with HC-0x FC-114 is ready");
      Serial.println("Make sure Both NL & CR are set");
      Serial.println("");
      
      // FC-114 default baud rate is 9600
      BTSerial.begin(9600);  
      Serial.println("BTserial started at 9600");
      Serial.println("");
      
}

void loop()
{

    // Read from the Bluetooth module and send to the Arduino Serial Monitor
    if (BTSerial.available())
    {
        c = BTSerial.read();
        Serial.write(c);
    }
    
      
  
    // Read from the Serial Monitor and send to the Bluetooth module
    if (Serial.available())
    {
        c = Serial.read();
        BTSerial.write(c);   
        
        // Echo the user input to the main window. The ">" character indicates the user entered text.
        if (NL) { Serial.print(">");  NL = false; }
        Serial.write(c);
        if (c==10) { NL = true; }
    }

}
