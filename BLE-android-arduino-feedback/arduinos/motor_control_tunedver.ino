#include <SoftwareSerial.h>
const int right_mt_pin = 5;
const int left_mt_pin = 6;
const int up_mt_pin = 9;
const int down_mt_pin = 10;

const int weak_vib = 0;
const int strong_vib = 255;

SoftwareSerial BTSerial(2, 3); // (TX,RX)
void fb_right(){
  Serial.println("right fb!");
  analogWrite(right_mt_pin, strong_vib);
  analogWrite(left_mt_pin, weak_vib);
  analogWrite(up_mt_pin, weak_vib);
  analogWrite(down_mt_pin, weak_vib);
}

void fb_left(){
  Serial.println("left fb!");
  analogWrite(right_mt_pin, weak_vib);
  analogWrite(left_mt_pin, strong_vib);
  analogWrite(up_mt_pin, weak_vib);
  analogWrite(down_mt_pin, weak_vib);
}

void fb_up(){
  Serial.println("up fb!");
  analogWrite(right_mt_pin, weak_vib);
  analogWrite(left_mt_pin, weak_vib);
  analogWrite(up_mt_pin, strong_vib);
  analogWrite(down_mt_pin, weak_vib);
}

void fb_down(){
  Serial.println("down fb!");
  analogWrite(right_mt_pin, weak_vib);
  analogWrite(left_mt_pin, weak_vib);
  analogWrite(up_mt_pin, weak_vib);
  analogWrite(down_mt_pin, strong_vib);
}

void off_all(){
  Serial.println("nothing!");
  analogWrite(right_mt_pin, 0);
  analogWrite(left_mt_pin, 0);
  analogWrite(up_mt_pin, 0);
  analogWrite(down_mt_pin, 0);
  }
  
void setup() {
  Serial.begin(9600);
  Serial.println("Hello verW_1!");
  BTSerial.begin(9600);//시리얼 통신을 사용하며 통신 속도는 9600으로 설정합니다.
  pinMode(right_mt_pin, OUTPUT);
  pinMode(left_mt_pin, OUTPUT);
  pinMode(up_mt_pin, OUTPUT);
  pinMode(down_mt_pin, OUTPUT);
  analogWrite(right_mt_pin, strong_vib);
  analogWrite(left_mt_pin, strong_vib);
  analogWrite(up_mt_pin, strong_vib);
  analogWrite(down_mt_pin, strong_vib);
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
