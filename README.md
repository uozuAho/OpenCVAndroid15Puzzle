# OpenCVAndroid15Puzzle
Enhancement of OpenCV's 15-puzzle android sample. Improvements:
- gradle build system
- desktop test applicaton
- cross platform logging using slf4j

# Dependencies
- Java JDK (tested with OpenJDK 1.7.0_79)
- Android SDK (for android builds/testing, tested on Android 4.4.4)
- [OpenCV](http://opencv.org/) (tested using 3.0.0 desktop & android versions)
    + For Desktop:
        * Download and build/install OpenCV 3.0.0, ensuring java bindings are built
        * Edit Puzzle15Desktop/build.gradle to point to your OpenCV installation
    + For Android:
        * Download OpenCV for Android, and install the appropriate apk for your
          phone. Last time I checked, the OpenCV manager for android didn't
          have version 3.0.0

Android Studio 1.3.2 used for development, however theoretically everything
should be do-able through gradle.

# TODO
- mouse clicks = touch events for desktop application
