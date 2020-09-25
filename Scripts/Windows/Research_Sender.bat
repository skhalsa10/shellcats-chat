@echo off

echo this batch file generates the senders portion of the total clients

echo.

set /p CLIENTS="Please state the total number of clients used in this test? "



PATH=%CD%\..\..\jars\ChatCLI.jar



setlocal enabledelayedexpansion

SET /A BOUND=%CLIENTS%/2

for /l %%i in (1, 1, %BOUND%) do (

    SET /A MIN = %%i - 1

    SET /A PAIR = %CLIENTS% - !MIN!

    java -jar %PATH% client%%i localhost 8558 research client!PAIR!

)