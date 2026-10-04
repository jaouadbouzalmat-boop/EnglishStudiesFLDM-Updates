@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"

echo =================================================
echo   EnglishStudiesFLDM - APK RELEASE SIGNE v1.3
echo =================================================
echo.

echo Recherche de Java...
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" goto java_ok
if exist "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    goto java_ok
)
if exist "%LOCALAPPDATA%\Programs\Android Studio\jbr\bin\java.exe" (
    set "JAVA_HOME=%LOCALAPPDATA%\Programs\Android Studio\jbr"
    goto java_ok
)
if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" (
    set "JAVA_HOME=%ProgramFiles%\Android\Android Studio\jbr"
    goto java_ok
)
echo ERREUR : Java introuvable.
pause
exit /b 1

:java_ok
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME = %JAVA_HOME%
java -version

echo.
echo Verification de la version et de la signature release...
findstr /C:"versionCode = 4" /C:"versionName = \"1.3\"" app\build.gradle.kts >nul
if errorlevel 1 (
    echo ERREUR : version 1.3 introuvable dans app\build.gradle.kts.
    pause
    exit /b 1
)
findstr /C:"signingConfig" /C:"create(\"release\")" app\build.gradle.kts >nul
if errorlevel 1 (
    echo ERREUR : configuration de signature release introuvable.
    pause
    exit /b 1
)
if not exist "keystore\englishstudies-upload.jks" (
    echo ERREUR : keystore release introuvable.
    pause
    exit /b 1
)
if not exist "keystore.properties" (
    echo ERREUR : keystore.properties introuvable.
    pause
    exit /b 1
)
echo versionCode = 4
echo versionName = 1.3

echo.
echo Recherche du SDK Android...
if defined ANDROID_HOME if exist "%ANDROID_HOME%\platform-tools" goto sdk_ok
if defined ANDROID_SDK_ROOT if exist "%ANDROID_SDK_ROOT%\platform-tools" (
    set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
    goto sdk_ok
)
if exist "%LOCALAPPDATA%\Android\Sdk\platform-tools" (
    set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
    goto sdk_ok
)
if exist "%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools" (
    set "ANDROID_HOME=%USERPROFILE%\AppData\Local\Android\Sdk"
    goto sdk_ok
)
echo ERREUR : SDK Android introuvable.
pause
exit /b 1

:sdk_ok
> local.properties echo sdk.dir=%ANDROID_HOME:\=\\%
echo ANDROID_HOME = %ANDROID_HOME%

set "APKSIGNER="
for /f "delims=" %%D in ('dir /b /ad /o-n "%ANDROID_HOME%\build-tools" 2^>nul') do (
    if not defined APKSIGNER if exist "%ANDROID_HOME%\build-tools\%%D\apksigner.bat" set "APKSIGNER=%ANDROID_HOME%\build-tools\%%D\apksigner.bat"
)
if not defined APKSIGNER (
    echo ERREUR : apksigner.bat introuvable.
    echo Installe Android SDK Build-Tools via Android Studio ^> SDK Manager ^> SDK Tools.
    pause
    exit /b 1
)
echo APKSIGNER = %APKSIGNER%

echo.
echo Suppression des anciens builds et de l'ancien APK...
if exist app\build rmdir /s /q app\build
if exist build rmdir /s /q build
if exist "EnglishStudiesFLDM_v1.3.apk" del /q "EnglishStudiesFLDM_v1.3.apk"

echo.
echo Generation du RELEASE APK...
call gradlew.bat clean :app:assembleRelease --no-daemon
if errorlevel 1 goto build_fail

set "APK_IN=app\build\outputs\apk\release\app-release.apk"
if not exist "%APK_IN%" (
    echo ERREUR : app-release.apk introuvable apres compilation.
    goto build_fail
)

echo.
echo Verification de la signature APK...
call "%APKSIGNER%" verify --verbose "%APK_IN%"
if errorlevel 1 (
    echo ERREUR : la signature APK n'est pas valide.
    echo N'installe pas cet APK sur la tablette.
    pause
    exit /b 1
)

echo.
echo Copie de l'APK final signe...
copy /Y "%APK_IN%" "EnglishStudiesFLDM_v1.3.apk" >nul
if errorlevel 1 (
    echo ERREUR : impossible de copier l'APK final.
    pause
    exit /b 1
)

echo.
echo =================================================
echo   APK v1.3 SIGNE GENERE AVEC SUCCES
echo =================================================
echo.
echo Fichier :
echo %CD%\EnglishStudiesFLDM_v1.3.apk
echo.
echo La signature a ete verifiee avec apksigner.
echo Conserve le fichier keystore en lieu sur.
echo.
pause
exit /b 0

:build_fail
echo.
echo =================================================
echo   ECHEC DE LA COMPILATION
echo =================================================
echo.
echo Corrige le message d'erreur affiche ci-dessus avant toute installation.
pause
exit /b 1
