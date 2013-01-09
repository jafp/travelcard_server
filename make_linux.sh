#!/bin/bash

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.

cd src
javac dk/ihk/tcp/cardreader/usb/TCPUSB.java
javah -verbose -stubs dk.ihk.tcp.cardreader.usb.TCPUSB

# Compile
gcc -I/usr/include/libusb-1.0 -I/usr/lib/jvm/java-7-openjdk-i386/include -fPIC -Wall -g -c dk_ihk_tcp_cardreader_usb_TCPUSB.c


# Link
gcc -shared -Wl,-soname,libTCPUSB.so.1,-rpath=/usr/lib/i386-linux-gnu -o ../lib/libTCPUSB.so dk_ihk_tcp_cardreader_usb_TCPUSB.o -lusb-1.0
