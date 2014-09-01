#include "jni.h"
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mamclient_MAMPassword.h"

unsigned char rc5_cipher[] = {
    0xBC, 0x54, 0xE8, 0xEB, 0xF7, 0x98, 0x49, 0xB0, 0x8C, 0xA8, 0xFA, 0xFF, 0xBB, 0x54, 0xE8, 0x96,
    0x56, 0x55, 0x91, 0xA9, 0x10, 0x41, 0xE4, 0x48, 0x8F, 0x30, 0x32, 0x9F, 0x3E, 0x1D, 0xF4, 0x27,
    0x23, 0x35, 0x4F, 0xCF, 0xB4, 0xC6, 0xC3, 0xEA, 0x03, 0x5E, 0xEA, 0xE9, 0xBA, 0x4B, 0x97, 0xE5,
    0x92, 0x76, 0x4D, 0x33, 0x2E, 0xCF, 0x6B, 0x2C, 0x74, 0x3B, 0xC5, 0x0D, 0xA6, 0x92, 0x5C, 0x99,
    0x77, 0x6D, 0x4F, 0x7E, 0x9F, 0xB7, 0xB2, 0x1E, 0x89, 0x8D, 0x34, 0x1D, 0x54, 0x13, 0x64, 0xED,
    0x9D, 0x4A, 0xE0, 0x15, 0x59, 0xA1, 0x8D, 0x48, 0xD3, 0x17, 0x78, 0x64, 0x20, 0xBC, 0xA0, 0x8C,
    0xFE, 0xF7, 0x64, 0x92, 0x6C, 0x8C, 0xE7, 0x91, 0xFB, 0x07, 0x9A, 0x5C, 0xCE, 0xDC, 0xD4, 0xAB,
    0x8D, 0xF9, 0x16, 0x64, 0x5B, 0xAB, 0x42, 0x66
};

int32_t _rotr( int32_t a1, int32_t a2 )
{
    return (int32_t)((a1 << a2 % 0x20u) | ((unsigned int)a1 >> (char)(32 - a2 % 0x20u)));
}

intptr_t _encrypt_password( intptr_t a1, intptr_t a2, int32_t a3 )
{
    signed int  v10; // [sp+10h] [bp-8h]@3
    intptr_t    result; // eax@1
    intptr_t    v4; // ebx@3
    intptr_t    v5; // edi@3
    intptr_t    v6; // esi@3
    intptr_t    v7; // eax@4
    intptr_t    v8; // [sp+14h] [bp-4h]@2
    intptr_t    v9; // [sp+Ch] [bp-Ch]@2
    
    result = 8 * a3 / 8;
    a3 = 8 * a3 / 8;
    if ( result > 0 )
    {
        result = a2;
        v8 = 0;
        v9 = a2;
        while ( a3 / 8 > v8 )
        {
            v4 = *(int32_t *)v9 + *(int32_t *)(a1 + 16);
            v6 = *(int32_t *)(v9 + 4) + *(int32_t *)(a1 + 20);
            v10 = 1;
            v5 = a1 + 24;
            do
            {
                v7 = *(int32_t *)v5 + _rotr((int32_t)(v6 ^ v4), (int32_t)v6);
                v4 = v7;
                v6 = *(int32_t *)(v5 + 4) + _rotr((int32_t)(v7 ^ v6), (int32_t)v7);
                ++v10;
                v5 += 8;
            }
            while ( v10 <= 12 );
            result = v9;
            *(int32_t *)v9 = (int32_t)v4;
            *(int32_t *)(v9 + 4) = (int32_t)v6;
            ++v8;
            v9 += 8;
        }
    }
    return result;
}

void encrypt_password( char *outbuffer, char *password )
{
    unsigned char buffer[] = {
        0x00, 0x5D, 0x1E, 0xEE, 0xFF, 0xFF, 0xFF, 0xFF, 0xE4, 0x55, 0xA5, 0x71, 0x00, 0x00, 0x00, 0x00
    };
    
    memcpy( ( void * )buffer, ( void * )password, ( strlen( password ) + 1 ) );
    
    _encrypt_password( (intptr_t) &rc5_cipher, (intptr_t) &buffer, 16 );
    
    memcpy( ( void * )outbuffer, ( void * )buffer, 16 );
}

JNIEXPORT jbyteArray JNICALL Java_mamclient_MAMPassword_encryptPassword( JNIEnv *env, jobject obj, jstring jPassword )
{
	const char *password 	= (*env)->GetStringUTFChars( env, jPassword, 0 );
	jbyteArray result 		= (*env)->NewCharArray( env, 16 );
	char buffer[16] 		= { 0 };
    
	if ( result == NULL )
		return NULL;
    
	encrypt_password( ( char * )&buffer, ( char * )password );
    
	(*env)->ReleaseStringUTFChars( env, jPassword, password );
	(*env)->SetByteArrayRegion( env, result, 0, 16, buffer );
    
	return result;
}
