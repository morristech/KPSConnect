#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building..."
./gradlew clean assembleRelease

if [ ! -a app/app-release.apk ]
  then
    cp app/build/outputs/apk/app-release.apk app/app-release.apk
fi

echo "Build finished!"
