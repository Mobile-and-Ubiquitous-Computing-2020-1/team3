#include <SoftwareSerial.h>

SoftwareSerial BTSerial(2, 3); // (TX,RX)
void fb_right(){
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
}

void fb_left(){
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
}

void fb_up(){
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, HIGH);
  digitalWrite(7, LOW);
}

void fb_down(){
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, HIGH);
}

void off_all(){
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  }
  
void setup() {
  Serial.begin(9600);
  Serial.println("Hello verW_1!");
  BTSerial.begin(9600);//시리얼 통신을 사용하며 통신 속도는 9600으로 설정합니다.
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  digitalWrite(4, HIGH);
  digitalWrite(5, HIGH);
  digitalWrite(6, HIGH);
  digitalWrite(7, HIGH);
}
byte data ;
void loop() {
  if (BTSerial.available()) {                   
    data = BTSerial.read();
    Serial.println(data);
    switch (data) {
      case 'r': fb_right(); break;        
     
      case 'l': fb_left(); break;       
      
      case 'u': fb_up(); break;      
     
      case 'd': fb_down(); break;        
     
      default : off_all();
    }
  }
}
