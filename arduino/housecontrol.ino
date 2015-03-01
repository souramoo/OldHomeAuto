
int in = 2;

void setup()
{
    Serial.begin(9600);
    pinMode(in, INPUT);
    pinMode(10, OUTPUT);
    pinMode(9, OUTPUT);
    Serial.print("READY\n");
}

void loop()
{
    int i=0;
    char commandbuffer[100];
    commandbuffer[0] = '\0';

    if(Serial.available()){
        delay(100);
        while( Serial.available() && i< 99) {
            commandbuffer[i++] = Serial.read();
        }
        commandbuffer[i++]='\0';
    }

    if(i>0)
        Serial.println((char*)commandbuffer);
     
        if(((char*)commandbuffer)[0] == 'a') {
            Serial.println("OUTPUT1 ON");
            digitalWrite(10, HIGH);
        }
        if(((char*)commandbuffer)[0] == 'b') {
            Serial.println("OUTPUT1 OFF");
            digitalWrite(10, LOW);
        }
        if(((char*)commandbuffer)[0] == 'c') {
            Serial.println("OUTPUT2 ON");
            digitalWrite(9, HIGH);
        }
        if(((char*)commandbuffer)[0] == 'd') {
            Serial.println("OUTPUT2 OFF");
            digitalWrite(9, LOW);
        }
}
