[![headBanner](https://i.imgur.com/FrR9oGn.png)]()
## Project Description
Our project Insight addresses one of the main issues faced by the hearing impaired community; which is that there exists a large majority of the population who cannot understand sign language, and this puts a strain on their ability to communicate with each other.

We plan to tackle this problem by developing a mobile application which facilitates image processing such that it can be used primarily by those who are not versed in sign language, so they can understand those who rely on sign language. 

You can view a video demonstration Project Insight using this link: [Video Presentation](https://www.youtube.com/watch?v=dQw4w9WgXcQ)
## Table of Contents
- [ Features ](#features)
- [ Installation and Running ](#i-r)
- [ Testing ](#testing)
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
- A recognition model using holistic hand landmarks to increase precision.

<a name="i-r"></a>
## Installation and Running


<a name="testing"></a>
## Testing
#### Sign Language Recognition Model Tests:
#### Unit Tests:
#### Component Tests:

<a name="instructions"></a>
## Application Instructions
The application has three (3) main pages (Home, Translate and About respectively) which can be navigated through tags at the bottom of the app. 

Upon launching the application you are brought to the homepage which contains a "View Dictionary" button which will bring you to the dictionary page which contains a list of all signs which are possible within the application.

Through navigation to the "About" page you will be able to find a short project description as well as relevant information on the creators of the application.

Finally, after navigating to the Translate page, you will be able to switch camera views through interacting with the left most button and turn on/off text-to-speech by toggling the button on the right. When a hand is detect by the main camera view all landmarks will be drawn onto the live video feed and any detected hand signs will be outputted. 

<a name="references"></a>
## References
- [TensorFlow Pose-Classification](https://www.tensorflow.org/lite/tutorials/pose_classification)
- [TensorFlow Classification](https://www.tensorflow.org/tutorials/images/classification)
- [TensorFlow Lite Image Classification](https://www.tensorflow.org/lite/examples/image_classification/overview)
- [Android TensorFlowLite Quickstart](https://www.tensorflow.org/lite/guide/android)
