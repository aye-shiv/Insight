[![headBanner](https://i.imgur.com/FrR9oGn.png)]()
## Project Description
Our project Insight addresses one of the main issues faced by the hearing impaired community; which is that there exists a large majority of the population who cannot understand sign language, and this puts a strain on their ability to communicate with each other.

We tackled this problem by developing a mobile application which facilitates image processing such that it can be used primarily by those who are not versed in sign language, so they can understand those who rely on sign language while learning more themselves. 

You can view a video demonstration Project Insight using this link: [Video Presentation](https://www.youtube.com/watch?v=dQw4w9WgXcQ)
## Table of Contents
- [ Features ](#features)
- [ Installation and Running ](#i-r)
  - [ Using APK ](#i-rAPK)
  - [ Android App Folder ](#i-rAAF)
- [ Evaluation & Testing ](#testing)
  - [ Model Evaluation ](#model-eval)
  - [ Unit Tests ](#unit-tests)
  - [ Component Tests ](#comp-tests)
- [ Application Instructions ](#instructions)
- [ References ](#references) 

<a name="features"></a>
## Features
- Real-Time tracking as most other solutions are reliant on a previously stored form of media.
- Text–To-Speech capabilities which allow for greater accessibility as another form of output delivery.
- Offers offline capability.
- Android support from as low as Android 7.0 Nougat.
- Support for both front and back cameras as modern devices have more than 1 camera modules.
- The ability to sign using single letters of the english alphabet.
- An extensive dictionary that covers the entire alphabet to create words/phrases.
- A recognition model using holistic hand landmarks to increase precision regardless of background.

<a name="i-r"></a>
## Installation and Running
Check out the About page on the application for clarification on certain features!
<a name="i-rAPK"></a>
#### Installation/Running using APK
Download the APK provided in the "Project Insight APK" folder and install it using a package installer of your choosing (I recommend [SAI](https://play.google.com/store/apps/details?id=com.aefyr.sai)). After completing this step you can launch the app and ensure you allow camera permissions.

<a name="i-rAAF"></a>
#### Installation/Running using Android App Folder
Download the "Project Insight Android Application" directory and open the project in Android Studio. Using a previously set up emulated device or a connected android device you may run the application and all necessary files will be written to the user device and the app will be launched. Note: Ensure you allow camera permissions.

<a name="testing"></a>
## Evaluation & Testing
<a name="model-eval"></a>
#### Model Evaluations:
Note that training/testing data was all captured manually thus many improvements can potentially be made with better hardware.<br/>
##### Model Accuracy Plot:<br/>
[![Model Accuracy](https://i.imgur.com/vVFU7DA.png)]()<br/>

<a name="unit-tests"></a>
#### Unit Tests:
<a name="comp-tests"></a>
#### Component Tests:

<a name="instructions"></a>
## Application Instructions
The application has three (3) main pages (Home, Translate and About respectively) which can be navigated through tags at the bottom of the app. 

Upon launching the application you are brought to the homepage which contains a "View Dictionary" button which will bring you to the dictionary page which contains a list of all signs which are possible within the application.

Through navigation to the "About" page you will be able to find a short project description, a list of basic features and how they work as well as relevant information on the creators of the application.

Finally, after navigating to the Translate page, you will be able to switch camera views through interacting with the left most button and turn on/off text-to-speech by toggling the button on the right. When a hand is detect by the main camera view all landmarks will be drawn onto the live video feed and any detected hand signs will be outputted. 

<a name="references"></a>
## References
- [Mediapipe](https://google.github.io/mediapipe/solutions/hands)
- [TensorFlow Pose-Classification](https://www.tensorflow.org/lite/tutorials/pose_classification)
- [TensorFlow Classification](https://www.tensorflow.org/tutorials/images/classification)
- [TensorFlow Lite Image Classification](https://www.tensorflow.org/lite/examples/image_classification/overview)
- [Android TensorFlowLite Quickstart](https://www.tensorflow.org/lite/guide/android)
- [Android TensorFlowLite Documentation](https://www.tensorflow.org/lite/api_docs/java/org/tensorflow/lite/package-summary)
