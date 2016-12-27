/*
 * Author: JP Meijers
 * Date: 2016-02-07
 * Previous filename: TTN-Mapper-TTNEnschede-V1
 * 
 * This program is meant to be used with an Arduino UNO or NANO, conencted to an RNxx3 radio module.
 * It will most likely also work on other compatible Arduino or Arduino compatible boards, like The Things Uno, but might need some slight modifications.
 * 
 * Transmit a one byte packet via TTN. This happens as fast as possible, while still keeping to 
 * the 1% duty cycle rules enforced by the RN2483's built in LoRaWAN stack. Even though this is 
 * allowed by the radio regulations of the 868MHz band, the fair use policy of TTN may prohibit this.
 * 
 * CHECK THE RULES BEFORE USING THIS PROGRAM!
 * 
 * CHANGE ADDRESS!
 * Change the device address, network (session) key, and app (session) key to the values 
 * that are registered via the TTN dashboard.
 * The appropriate line is "myLora.initABP(XXX);" or "myLora.initOTAA(XXX);"
 * When using ABP, it is advised to enable "relax frame count".
 * 
 * Connect the RN2xx3 as follows:
 * RN2xx3 -- Arduino
 * Uart TX -- 10
 * Uart RX -- 11
 * Reset -- 12
 * Vcc -- 3.3V
 * Gnd -- Gnd
 * 
 * If you use an Arduino with a free hardware serial port, you can replace 
 * the line "rn2xx3 myLora(mySerial);"
 * with     "rn2xx3 myLora(SerialX);"
 * where the parameter is the serial port the RN2xx3 is connected to.
 * Remember that the serial port should be initialised before calling initTTN().
 * For best performance the serial port should be set to 57600 baud, which is impossible with a software serial port.
 * If you use 57600 baud, you can remove the line "myLora.autobaud();".
 * 
 */
#include <rn2xx3.h>

//create an instance of the rn2xx3 library, 
//giving the software serial as port to use
rn2xx3 myLora(Serial2);
#define pin A0
double distance = 0;
bool startSensor = false;
String start_sensor = "Y";
String stop_sensor = "N";
byte bytes_in;

// the setup routine runs once when you press reset:
void setup() 
{
  
  
  // Open serial communications and wait for port to open:
  Serial.begin(57600); //serial port to computer
  Serial2.begin(57600); //serial port to radio
  Serial3.begin(9600); //serial port to bluetooth
  Serial.println("Startup");

  pinMode(pin, INPUT);
  initialize_radio();

  //transmit a startup message
  myLora.tx("AP going to connect");

  delay(2000);
}

void initialize_radio()
{
  //reset rn2483
  pinMode(12, OUTPUT);
  digitalWrite(12, LOW);
  delay(500);
  digitalWrite(12, HIGH);

  delay(100); //wait for the RN2xx3's startup message
  Serial2.flush();

  //check communication with radio
  String hweui = myLora.hweui();
  while(hweui.length() != 16)
  {
    Serial.println("Communication with RN2xx3 unsuccesful. Power cycle the board.");
    Serial.println(hweui);
    delay(10000);
    hweui = myLora.hweui();
  }

  //print out the HWEUI so that we can register it via ttnctl
  Serial.println("When using OTAA, register this DevEUI: ");
  Serial.println(myLora.hweui());
  Serial.println("RN2xx3 firmware version:");
  Serial.println(myLora.sysver());

  //configure your keys and join the network
  Serial.println("Trying to join TTN");
  bool join_result = false;
  
  //ABP: initABP(String addr, String AppSKey, String NwkSKey);
  join_result = myLora.initABP("9CC4931F", "F6A0912BC9B5CC12075D87D9A0553C89", "82E46DC9ABE0730FA1694B91677E37C0");
  
  //OTAA: initOTAA(String AppEUI, String AppKey);
  //join_result = myLora.initOTAA("70B3D57ED00001A6", "A23C96EE13804963F8C2BD6285448198");

  while(!join_result)
  {
    Serial.println("Unable to join. Are your keys correct, and do you have TTN coverage?");
    delay(60000); //delay a minute before retry
    join_result = myLora.init();
  }
  Serial.println("Successfully joined TTN");
  
}

// the loop routine runs over and over again forever:
void loop() 
{

  if(Serial3.available()) {
        /*while(Serial2.available()<12) {}
        for(int n=0; n<12; n++) {*/
        bytes_in = Serial3.read();        
        //}
        String message = String((char)bytes_in);
    
        Serial.println(message);

        if (message.equals(start_sensor)) {
          startSensor = true;
          uint16_t value = analogRead (pin);
          distance = get_IR (value);  //Convert the analog voltage to the distance
        } else if(message.equals(stop_sensor)) {
          startSensor = false;
          distance = 0;
        }
   
        Serial.print (distance);
        Serial.println (" cm");
        Serial.println ();
        delay (500);   //Delay 0.5s
  }
  //Serial.println (distance);
  if(startSensor) {
    if((distance + 10 >= analogRead(pin)) && (distance -10 <= analogRead(pin))) {
      Serial.println("TXing");
      myLora.tx("An intruder has been detected!");
    }
  }
}

//return distance (cm)
double get_IR(uint16_t value){
        if (value < 16)  value = 16;
        return 2076.0 / (value - 11.0);
}

