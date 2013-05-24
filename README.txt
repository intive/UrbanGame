========================
        UrbanGame
========================

Urban games are nowadays a very popular phenomena in a field of social events. They are becoming bigger and bigger in scale, attracting large groups of new players. Together with Patronage participants we would like to create a common framework, that would facilitate performing them. Mobile application, giving unique flexibility and new, inspiring possibilities, supported by a server, allowing results tracking, will altogether bring fresh quality to location-based gaming. We strongly believe that program participants, being guided and mentored by our specialists, benefiting from their experience and knowledge and using cutting-edge technologies will take the opportunity to stun modern urban gaming community!

Available under Apache License 2.0. The project is made under Patronage program, Wroclaw 2013 edition.

What is Patronage?
==================

Patronage is a unique program combining our experience with your abilities and interests. Clue of the program is the actual project that you will participate in - real life experience which will enable you to recognize your skills and competences. The outcome of the project is running application. Taking part in the program you may try yourselves in mobile and web technologies. It’s a great chance to see what Scrum means in practice. Patronage will give you an opportunity to work with our experts and to leverage best programming practices. Launch your career with Patronage. The best students will have possibility to join BLStream as employees.

External pages
==============

Visit our FanPage: http://www.facebook.com/BLStreamPatronage
Program page: http://blstream.com/student
GitHub page: http://blstream.github.com/UrbanGame/

Branch info
===========
This is Android branch. Please check other branches if you are interesting in other platforms.

########################################

To prepare environment you should take the following steps:

# COMMON STEPS:

1. Download Java JDK from http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
2. Add the JDK installation directory as a JAVA_HOME environment variable.
3. Download Android SDK from http://developer.android.com/sdk/index.html#download
4. Add the android-sdk installation directory as ANDROID_HOME environment variable.
5. Add the platform-tools/ as well as the tools/ directory to your PATH environment variable.
6. Clone UrbanGame Android branch from GitHub https://github.com/blstream/UrbanGame/tree/android
7. Add the UrbanGame/UrbanGame directory as DEV_HOME environment variable.

# 1st solution # BUILD ANDROID APP USING ANT

1. Download Ant http://ant.apache.org/bindownload.cgi
2. Add the apache-ant installation directory as a ANT_HOME environment variable
3. Go to /UrbanGame directory in clonned repository (also called UrbanGame)
4. Open comand line in current directory
5. Type in "ant debug"

# 2nd solution # BUILD ANDROID APP FROM COMMAND LINE

1. Create R.java
	aapt
		com.blstream.urbangame
		-v
		-f
		-m
		-S DEV_HOME/res
		-J DEV_HOME/src
		-M DEV_HOME/AndroidManifest.xml
		-I ANDROID_HOME/platforms/android-17/android.jar
	
2. Compile code
	javac
		-verbose
		-d DEV_HOME/bin
		-classpath ANDROID_HOME/platforms/android-17/android.jar;DEV_HOME/bin
		-sourcepath DEV_HOME/src
		DEV_HOME/src/com/blstream/*.java

3. Create DEX file
	dx
		--dex
		--verbose
		--output=DEV_HOME/bin/classes.dex
		DEV_HOME/bin
		DEV_HOME/lib

4. Create APK file
	aapt
		com.blstream.urbangame
		-v
		-f
		-M DEV_HOME/AndroidManifest.xml
		-S DEV_HOME/res
		-I ANDROID_HOME/platforms/android-17/android.jar
		-F DEV_HOME/bin/UrbanGame-debug-unsigned.apk
		DEV_HOME/bin

5. Create keystore
	keytool
		-genkeypair
		-validity 10000
		-dname "CN=blstream,
				OU=blstream,
				O=blstream,
				L=Wroclaw,
				S=Poland,
				C=PL"
		-keystore DEV_HOME/UrbanGame.keystore
		-storepass password
		-keypass password
		-alias UrbanGame
		-keyalg RSA
		-v

6. Sign APK file
	jarsigner
		-verbose
		-keystore DEV_HOME/UrbanGame.keystore
		-storepass password
		-keypass password
		-signedjar 	DEV_HOME/bin/UrbanGame-debug-unaligned.apk
					DEV_HOME/bin/UrbanGame-debug-unsigned.apk
		UrbanGame

7. Zip-align APK file
	zipalign
		-v
		-f
		4
		DEV_HOME/bin/UrbanGame-debug-unaligned.apk
		DEV_HOME/bin/UrbanGame-debug.apk
		
# RUN ANDROID APPLICATION

1. Create Emulator (AVD)
	android
		--verbose
		create avd
		--name UrbanGameEmulator
		--target android-17
		--sdcard 1024M

2. Run AVD
	emulator
		-wipe-data
		-avd UrbanGameEmulator
	
3a. Install in emulator
	adb
		-e
		install DEV_HOME/bin/UrbanGame-debug.apk

To run application on a deveice just enable USB debugging there.
    - On most devices running Android 3.2 or older, you can find the option under Settings > Applications > Development.
    - On Android 4.0 and newer, it's in Settings > Developer options.
	- On Android 4.2 and newer, go to Settings > About phone and tap Build number seven times. Return to the previous screen to find Developer options.
	
3b. Install on device
	adb
		-d
		install DEV_HOME/bin/UrbanGame-debug.apk
		
# 3rd solution # BUILD ANDROID APP FROM ECLIPSE IDE

1. Download Eclipse for Mobile Developers from http://www.eclipse.org/downloads/packages/eclipse-mobile-developers/junosr2

2. Import repository to eclipse:
Select File > Import > Android > then choose cloned reposiotry
You might also clean project to be sure that all dependecies will be added without conflicts.
To do this just choose Project -> Clean and select all projects.

3. Now press F11 to debug application on either emulator or device.

4. If there's no device connected you will be asked to create android emulator, after that, your applicaiont will be installed there.

########################################