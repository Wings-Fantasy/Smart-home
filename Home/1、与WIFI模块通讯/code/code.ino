#include <SoftwareSerial.h>

SoftwareSerial wifiSerial(11, 12);//RX,TX

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  wifiSerial.begin(9600);
  Serial.begin(9600);
  delay(1000);
}

void loop() {
  String msg = "";
  if (wifiSerial.available()) {
    while (wifiSerial.available() > 0) {
      msg += char(wifiSerial.read());
      delay(2);
    }
    Serial.println(msg);
    delay(500);
  }
  if (Serial.available()) {
    while (Serial.available() > 0) {
      msg += char(Serial.read());
      delay(2);
    }
    wifiSerial.println(msg);
    delay(500);
  }
}
