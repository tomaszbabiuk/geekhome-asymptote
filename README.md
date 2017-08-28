# GeekHOME Asymptote is an end-to-end IoT system for ESP8266 and mobile devices with security in mind.

This subproject (**asymptote-android**) is an android mobile app that controls embedded devices with ESP8266 and geekHOME Asymptote firmware on board.

The rest parts of the projects are: asymptote-ios (mobile app for iOS - underconstruction) and asymptote-esp8266 (firmware for embedded devices).

## Which devices can be controlled?
Asymptote controls some of Sonoff devices from Itead.cc: SONOFF TOUCH, SONOFF BASIC, SONOFF TH (with DS18B20/AM2301), SONOFF SC, SONOFF POW, SONOFF 4CH. The project focuses on CE certified devices mostly.

Asymptote can also control Electrodragon's: Relay Board, Relay Board SPDT, Relay Board VDC and LED Strip Board.

## Is this project neccessary? Why is it different?
Sonoff devices are liked for it's availability and low price. Unfortunatelly, the original software is global and doesn't fit for every case. I don't believe in cloud-only solutions. I believe that end user should own the device in 100%. I'd like to control devices from both: LAN and Cloud.

Asymptote is simple. All you need is a router, embedded device and Android device. Everything will work out of the box (with automatic device discovery, easy pairing and control). The rest of LAN based devices are really complicated and requires long lasting setup procedure (and sometimes even an additional server must be installed on your local network). Even though... it's hard to find a solution that works as LANoT and IoT altogether.

## What about security?
It's much harder to protect devices in your LAN network. Most people trust their phones and let them connect to their house/appartament WiFi network without restrictions. And that's bad, because there's a lot of malware targetting IoT devices. Knowing that: Asymptote is build around security. About 70% of code is related to security. ESP8266 is a great processor but it's lacking resources for security operations (after enabling SSL you have about 10kB of memory for your program). I was stating from scratch few times before I found the balance between security and resources.

## How is Asymptote firmware protected?
* Asymptote is using full SSL stack (even within local network). There's no backdoor in passing certificate to device: it must be flashed before firmware. 
* Even if one device is physically captured by attacker (assuming firmware is copied and disassembled), it's impossible to attack other devices.
* Asymptote is using PKI infrastructure: every device has it's unique certificate which is signed by our certificate authority.
* There're special database rules that keeps data consistent and sandboxed to one user only. We also add additional read time limits for sensitive data (like control orders).
* Every firmware package is public (sensitive data and database keys are not present there).
* Every "Over the air" update is checked for consistency and the source must be signed by restore token (that's only known to the issuer and target device).
* Every certificate update is also checked for authenticity according to restore token.

## What do I need to run this project from sources?
You need to create an empty Firebase project and attach google-services.json file.

You'll need to create your local Certificate Authotity:

- private key of CA
- certificate of your CA
- certificate for embedded device

And of course you need an ESP8266 device with geekHOME Asymptote.