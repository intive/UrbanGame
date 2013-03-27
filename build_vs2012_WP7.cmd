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
SetVersion.exe ..\UrbanGameWP7\UrbanGame\Properties\WMAppManifest.xml %BUILD_NUMBER% || goto error

echo ---
echo Building
echo ---
cd %CURRENT_DIR%\UrbanGameWP7
msbuild UrbanGame.sln /t:rebuild /p:Configuration="Release" || goto error
msbuild UrbanGame.sln /t:rebuild /p:Configuration="Debug" || goto error

echo ---
echo Testing
echo ---
cd %CURRENT_DIR%\Tools
UnitTestLauncher_WP8.exe "Emulator WXGA" UnitTestResult.txt {a7018df7-9cb9-4129-bc35-2d566c099a86} 60 ..UrbanGameWP7\UrbanGameTests\Bin\Debug\Assets\ApplicationIcon.png ..\UrbanGameWP7\UrbanGameTests\Bin\Debug\UrbanGameTests.xap || goto error

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
