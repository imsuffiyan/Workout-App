@echo off
REM Set JAVA_HOME to Android Studio JBR and run Gradle, capturing output to a log file
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME=%JAVA_HOME%
java -version
cd /d F:\GradleUpdateProjects\Workout
echo Running gradlew assembleDebug...
gradlew.bat assembleDebug --stacktrace --info > build_capture_log.txt 2>&1
echo gradle_exit=%ERRORLEVEL%
echo DONE

