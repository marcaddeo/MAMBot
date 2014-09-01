#javah mamclient.MAMPassword
gcc -shared "-I/Developer/SDKs/MacOSX10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers" -fPIC libMAMPassword.c -o libMAMPassword.so
