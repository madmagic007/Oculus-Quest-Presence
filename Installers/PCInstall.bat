@echo off
set "params=%*"
cd /d "%~dp0" && ( if exist "%temp%\getadmin.vbs" del "%temp%\getadmin.vbs" ) && fsutil dirty query %systemdrive% 1>nul 2>nul || (  echo Set UAC = CreateObject^("Shell.Application"^) : UAC.ShellExecute "cmd.exe", "/k cd ""%~sdp0"" && %~s0 %params%", "", "runas", 1 >> "%temp%\getadmin.vbs" && "%temp%\getadmin.vbs" && exit /B )

@echo off
mkdir "C:\Program Files (x86)\Oculus Quest Discord RPC"
xcopy "PCFiles\Oculus Quest Discord RPC.jar" "C:\Program Files (x86)\Oculus Quest Discord RPC\"
xcopy "PCFiles\unins000.exe" "C:\Program Files (x86)\Oculus Quest Discord RPC\"
xcopy "PCFiles\unins000.dat" "C:\Program Files (x86)\Oculus Quest Discord RPC\"

start "" javaw -jar "C:\Program Files (x86)\Oculus Quest Discord RPC\Oculus Quest Discord RPC.jar"
exit