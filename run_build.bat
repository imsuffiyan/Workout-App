@echo off
REM Temporary build script: set JAVA_HOME to Android Studio JBR and run Gradle assembleDebug
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d F:\GradleUpdateProjects\Workout
call gradlew.bat assembleDebug --stacktrace --info
pause

