#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building..."
./gradlew clean build connectCheck assembleRelease clean build

echo "Build finished!"
