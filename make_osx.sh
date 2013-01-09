#!/bin/bash

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.

cd src
javac dk/ihk/tcp/cardreader/usb/TCPUSB.java
javah -verbose -stubs dk.ihk.tcp.cardreader.usb.TCPUSB

gcc -Wall -I/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers/ -lusb-1.0 -shared -o ../lib/libTCPUSB.jnilib dk_ihk_tcp_cardreader_usb_TCPUSB.c