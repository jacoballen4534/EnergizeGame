#!/usr/bin/env bash

set -e

javac --module-path /home/matteas/Downloads/javafx-sdk-11.0.2/lib --add-modules=javafx.controls helloworld/HelloWorldJFX.java
