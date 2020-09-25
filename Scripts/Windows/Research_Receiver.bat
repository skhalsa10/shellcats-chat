@echo off

echo this batch file generates the senders portion of the total clients

echo.

set /p CLIENTS="Please state the total number of clients used in this test? "



PATH=..\..\jars\ChatCLI.jar

setlocal enabledelayedexpansion

SET /A BOUND=%CLIENTS%/2

for /l %%i in (1, 1, %BOUND%) do (

    SET /A MIN = %%i - 1

    SET /A PAIR = %CLIENTS% - !MIN!

    START /B "C:\Program Files\Java\jre-10.0.2\bin\java -jar" %PATH% client!PAIR! 73.42.106.175 8558 research client%%i

)