@echo off
echo ---
echo Configure VS2012
echo ---
call "%VS110COMNTOOLS%\..\..\VC\vcvarsall.bat" x86 || goto error

set CURRENT_DIR=%CD%

echo ---
echo Updating version
echo ---
cd %CURRENT_DIR%\Tools
SetVersion.exe ..\UrbanGame\UrbanGame\Properties\WMAppManifest.xml %BUILD_NUMBER% || goto error

echo ---
echo Building
echo ---
cd %CURRENT_DIR%\UrbanGame
msbuild UrbanGame.sln /t:rebuild /p:Configuration="Release" || goto error
msbuild UrbanGame.sln /t:rebuild /p:Configuration="Debug" || goto error

echo ---
echo Testing
echo ---
cd %CURRENT_DIR%\Tools
UnitTestLauncher_WP8.exe "Emulator WXGA" UnitTestResult.txt {670499a7-b78f-4073-b2eb-8b04d548e9f3} 60 ..UrbanGame\UrbanGameTests\Bin\Debug\Assets\ApplicationIcon.png ..\UrbanGame\UrbanGameTests\Bin\Debug\UrbanGameTests.xap || goto error

goto end

:error
echo ---
echo --- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ---
echo ---
echo ---
echo ------------- FAILED TO BUILD: %MODULE_NAME%
echo ---
echo ---
echo --- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ---
echo ---
cd %CURRENT_DIR%
exit /b 1

:end

