# Language-Translator
Language Translator using Firebase ML Kit


## Step 1: Create a New Project

## Step 2: Connect your app to Firebase

After creating a new project in Android Studio connect your app to Firebase. For connecting your app to firebase. Navigate to Tools on the top bar. After that click on Firebase. A new window will open on the right side. Inside that window click on Firebase ML and then click on Use Firebase ML kit in Android. 

After clicking on this option you will get to see the below screen. On this screen click on Connect to Firebase option to connect your app to Firebase. Click on Connect option to connect your app to Firebase and add the below dependency to your build.gradle file.


## Step 3: Adding Dependancies

Navigate to the Gradle Scripts > build.gradle(Module:app) and add the below dependency in the dependencies section.   

// dependancy for firebase core.

```bash

implementation’com.google.firebase:firebase-core:15.0.2′

```

// below two dependancy are used for language detection

```bash

implementation ‘com.google.firebase:firebase-ml-natural-language:22.0.0’

implementation ‘com.google.firebase:firebase-ml-natural-language-translate-model:20.0.8’

```

## Step 4: Adding permissions to access the Internet in your Android App

Navigate to the app > AndroidManifest.xml file and add the below code to it. Comments are added in the code to get to know in more detail.  


```bash
<!--permission for internet-->
<uses-permission android:name="android.permission.INTERNET"/>
```

## Step 5: Working with the activity_main.xml file

Navigate to the app > res > layout > activity_main.xml and add the below code to that file. 

## Step 6: Working with the MainActivity.java file

Go to the MainActivity.java file and write the code for the MainActivity.java
