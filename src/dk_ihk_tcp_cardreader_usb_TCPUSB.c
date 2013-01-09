
#include <stdio.h>
#include <string.h>
#include <libusb.h>

#include "dk_ihk_tcp_cardreader_usb_TCPUSB.h"

static libusb_context * context = NULL;
static libusb_device ** devices = NULL;
static libusb_device_handle * active_handle = NULL;
static struct libusb_device_descriptor active_descriptor;

static int string_matches(libusb_device_handle * handle, int index, const char *string)
{
	unsigned char buffer[64];
	int r = libusb_get_string_descriptor_ascii(handle, index, buffer, sizeof(buffer));
	if (r > 0)
	{
		if (strcmp(string, (const char *) buffer) == 0)
		{
			return 1;
		}
	}
	return 0;
}

JNIEXPORT jint JNICALL Java_dk_ihk_tcp_cardreader_usb_TCPUSB_init
  (JNIEnv * env, jobject obj)
{
	int r = libusb_init(&context);
	if (r != 0)
	{
		return -1;
	}
	return 0;
}

/*
 * Class:     TCPUSB
 * Method:    connect
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_dk_ihk_tcp_cardreader_usb_TCPUSB_connect
  (JNIEnv * env, jobject obj, jstring serial)
{
	int i, r, cnt;
	
	if (serial == NULL)
	{
		return -1;
	}
	
	const char * serialNumber = (*env)->GetStringUTFChars(env, serial, NULL);

	cnt = libusb_get_device_list(context, &devices);
	if (cnt < 0)
	{
		return -1;
	}

	for (i = 0; i < cnt; i++)
	{
		libusb_device_handle* h;
		libusb_device* dev = devices[i];
		struct libusb_device_descriptor desc;

		r = libusb_get_device_descriptor(dev, &desc);
		if (r < 0)
		{
			continue;
		}

		if (desc.idVendor == 0x16c0 && desc.idProduct == 0x5dc)
		{
			int r = libusb_open(dev, &h);
			if (r < 0)
			{
				printf("ERROR: %s\n", libusb_error_name(r));
			}
			else
			{
				if (string_matches(h, desc.iManufacturer, "IHK")
					&& string_matches(h, desc.iProduct, "TCP")
					&& string_matches(h, desc.iSerialNumber, serialNumber))
				{
					active_handle = h;
					active_descriptor = desc;

					libusb_claim_interface(h, 0);

					return 1;
				}
				else
				{
					libusb_close(h);
				}
			}
		}
	}

	libusb_free_device_list(devices, 1);

	return -1;
}

/*
 * Class:     TCPUSB
 * Method:    disconnect
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dk_ihk_tcp_cardreader_usb_TCPUSB_disconnect
  (JNIEnv * env, jobject obj)
{
	if (active_handle != NULL)
	{
		libusb_close(active_handle);
		active_handle = NULL;
	}
	return 0;
}

/*
 * Class:     TCPUSB
 * Method:    send
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_dk_ihk_tcp_cardreader_usb_TCPUSB_send
  (JNIEnv * env, jobject obj, jint command, jshortArray data)
{
	if (active_handle != NULL)
	{
		jshort *elements = (*env)->GetShortArrayElements(env, data, 0);

		int i;
		unsigned char buf[8];

		for (i = 0; i < 8; i++)
		{
			unsigned char c = elements[i] & 0xff;
			buf[i] = c;
		}

		return libusb_control_transfer(active_handle,
				LIBUSB_ENDPOINT_OUT | LIBUSB_REQUEST_TYPE_VENDOR | LIBUSB_RECIPIENT_DEVICE,
				(int) command, 0, 0, buf, 8, 0);
	}
	return -1;
}

/*
 * Class:     TCPUSB
 * Method:    interrupt
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_dk_ihk_tcp_cardreader_usb_TCPUSB_interrupt
  (JNIEnv * env, jobject obj, jshortArray data)
{
	if (active_handle != NULL)
	{
		unsigned char buf[8] = { 0 };
		int transferred = 0;
		int res = libusb_interrupt_transfer(active_handle, 0x81, buf, 8, &transferred, 0);

		if (res < 0)
		{
			return -1;
		}
		else
		{
			int i;
			jshort b[8];

			for (i = 0; i < 8; i++) {
				b[i] = buf[i] & 0xff;
			}

			(*env)->SetShortArrayRegion(env, data, 0, 8, b);
		}

		return transferred;
	}
	return -1;
}
