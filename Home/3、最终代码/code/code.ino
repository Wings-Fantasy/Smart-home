#include <SoftwareSerial.h>

SoftwareSerial wifiSerial(11, 12);//RX,TX

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  wifiSerial.begin(9600);
  delay(2000);
  wifiSerial.println("AT+CIPSTART=\"TCP\",\"192.168.50.100\",23598");
  delay(2000);
  wifiSerial.println("AT+CIPSEND=37");
  delay(500);
  wifiSerial.println("{\"msg\":\"CLIENT_ONLINE\",\"name\":\"home\"}");
  delay(1000);
}

void loop() {
  String msg = "";
  if (wifiSerial.available()) {
    while (wifiSerial.available() > 0) {
      msg += char(wifiSerial.read());
      delay(2);
    }
    if (!msg.compareTo("\r\n+IPD,14:{\"msg\":\"Doki\"}")) {
      wifiSerial.println("AT+CIPSEND=14");
      delay(500);
      wifiSerial.println("{\"msg\":\"Doki\"}");
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,28:{\"aims\":\"L1\",\"status\":\"off\"}")) {
      digitalWrite(2, LOW);
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,27:{\"aims\":\"L1\",\"status\":\"on\"}")) {
      digitalWrite(2, HIGH);
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,28:{\"aims\":\"L2\",\"status\":\"off\"}")) {
      digitalWrite(3, LOW);
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,27:{\"aims\":\"L2\",\"status\":\"on\"}")) {
      digitalWrite(3, HIGH);
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,28:{\"aims\":\"L3\",\"status\":\"off\"}")) {
      digitalWrite(4, LOW);
      delay(500);
    } else if (!msg.compareTo("\r\n+IPD,27:{\"aims\":\"L3\",\"status\":\"on\"}")) {
      digitalWrite(4, HIGH);
      delay(500);
    }
  }
}
