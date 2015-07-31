rem get current directory
set CURRENT_DIR=%~dp0

echo %CURRENT_DIR%
if "%JAVA_HOME%" == "" goto noJavaHome
set _JAVA_CMD=%JAVA_HOME%/jre/bin/java
goto run

:noJavaHome
set _JAVA_CMD=D:/ENV/jdk6/jre/bin/java.exe

:run
set JAVA_OPTS = "-Xms32m -Xmx128m"

"%_JAVA_CMD%" %JAVA_OPTS% -Djava.ext.dirs="%CURRENT_DIR%\lib" -cp ".;%CURRENT_DIR%;%CURRENT_DIR%\file" com.ray.tool.EntityTool

pause

:end
set SPY2LOGGER_HOME=
set _JAVA_CMD=
set MYCLASSPATH=