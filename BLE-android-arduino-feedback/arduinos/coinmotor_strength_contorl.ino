#include <SoftwareSerial.h>

SoftwareSerial BTSerial(2, 3); // (TX,RX)
void fb_right(){
  analogWrite(4, 255);
  analogWrite(5, 100);
  analogWrite(6, 100);
  analogWrite(7, 100);
}

void fb_left(){
  analogWrite(4, 100);
  analogWrite(5, 255);
  analogWrite(6, 100);
  analogWrite(7, 100);
}

void fb_up(){
  analogWrite(4, 100);
  analogWrite(5, 100);
  analogWrite(6, 255);
  analogWrite(7, 100);
}

void fb_down(){
  analogWrite(4, 100);
  analogWrite(5, 100);
  analogWrite(6, 100);
  analogWrite(7, 255);
}

void off_all(){
  analogWrite(4, 0);
  analogWrite(5, 0);
  analogWrite(6, 0);
  analogWrite(7, 0);
  }
  
void setup() {
  Serial.begin(9600);
  Serial.println("Hello verW_1!");
  BTSerial.begin(9600);//시리얼 통신을 사용하며 통신 속도는 9600으로 설정합니다.
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  analogWrite(4, 255);
  analogWrite(5, 255);
  analogWrite(6, 255);
  analogWrite(7, 255);
}
byte data ;
void loop() {
  if (BTSerial.available()) {                   //수신받은 데이터가 있다면.
    data = BTSerial.read();
    Serial.println(data);
    switch (data) {//switch (case) 함수를 사용하여 수신 받은 내용에 따라 led를 제어합니다.
      case 'r': fb_right(); break;        //0일경우(0의                                                         데이터를 받을 경우)4번핀에 연결한  led를 끕니다.
      //case 1: digitalWrite(4, HIGH); break;        //1일 경우 4번핀에 연결한 led를 켭니다.
      case 'l': fb_left(); break;        //2 일 경우 5번핀에 연결한 led를 끕니다.
      //case 3: digitalWrite(5, HIGH); break;       //3일 경우 5번핀에 연결한 led를 켭니다.
      case 'u': fb_up(); break;        //4일 경우 6번핀에 연결한 led를 끕니다.
      //case 5: digitalWrite(6, HIGH); break;       //5일 경우 6번핀에 연결한 led를 켭니다.
      case 'd': fb_down(); break;        //6일 경우 7번핀에 연결한 led를 끕니다.
      //case 7: digitalWrite(7, HIGH); break;       //7일 경우 7번핀에 연결한 led를 켭니다.
      default : off_all();
    }
  }
}
