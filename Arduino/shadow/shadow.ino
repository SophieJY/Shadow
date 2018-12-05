
#include <TinyGPS++.h>
#include <SoftwareSerial.h>

SoftwareSerial GPS(3, 4);
TinyGPSPlus gps;
SoftwareSerial GPRS(7, 8);

int count = 0;
String latitude = "0";
String longitude = "0";
String user = "arduinotest";
String URL = "http://safe-hollows-71202.herokuapp.com/?";

void setup() {
  GPRS.begin(19200);
  Serial.begin(19200);
  GPS.begin(9600);
  delay(2000);

  GPRS.listen();
  GPRS.println("AT");
  delay(500);
  read_message();

  GPRS.println("AT+HTTPINIT");
  delay(500);
  read_message();

  GPRS.println("AT+SAPBR=3,1,\"APN\",\"fast.t-mobile.com\"");
  delay(500);
  read_message();

  GPRS.println("AT+SAPBR=1,1");
  delay(1500);
  read_message();
  Serial.println("finish setup");
  GPS.listen();
  delay(1500);
}

void loop() {

  count += 1;
  while (GPS.available() > 0){
    if (gps.encode(GPS.read()))
    {
      latitude = String(gps.location.lat(),6);
      longitude = String(gps.location.lng(),6);
    }
  }
  if(count%50000 == 0){    
    sendtoserver();
  }
}

void read_message(){
  while(GPRS.available()){
    Serial.write(GPRS.read());
  }
}

void sendtoserver(){
  
  GPRS.listen();
  String current_URL = URL + "user=";
  current_URL += user;
  current_URL += "&latitude=";
  current_URL += latitude;
  current_URL += "&longitude=";
  current_URL += longitude;

  GPRS.println("AT+HTTPPARA= \"URL\", \""+ current_URL + "\"");
  Serial.println(current_URL);
  delay(1000);
  read_message();

  GPRS.println("AT+HTTPACTION=0");
  delay(5000);
  read_message();
  GPS.listen();

}
