# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

set(OpenCV_DIR ../OpenCV-android-sdk/sdk/native/jni)
find_package(OpenCV REQUIRED)

# include_directories(../dlib-19.7)
include(../dlib-19.7/dlib/cmake)

set(FaceAlignment_DIR src/main/cpp/FaceAlignmentForAndroid)
set(Liblinear_DIR src/main/cpp/FaceAlignmentForAndroid/liblinear)
set(LiblinearBlas_DIR src/main/cpp/FaceAlignmentForAndroid/liblinear/blas)

aux_source_directory(${FaceAlignment_DIR} FaceAlignment_SRC)
aux_source_directory(${Liblinear_DIR} Liblinear_SRC)
aux_source_directory(${LiblinearBlas_DIR} LiblinearBlas_SRC)

set(FaceAlignment_SRCES ${FaceAlignment_SRC} ${Liblinear_SRC} ${LiblinearBlas_SRC})

set(ImageUtils_DIR src/main/cpp/ImageUtils)
aux_source_directory(${ImageUtils_DIR} ImageUtils_SRC)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/ImageUtils-lib.cpp
             src/main/cpp/FaceDetect-lib.cpp
             # ${DLIB_SRC}
             ${FaceAlignment_SRCES}
             ${ImageUtils_SRC})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${OpenCV_LIBS}
                       dlib
                       )