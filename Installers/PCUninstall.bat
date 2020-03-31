@echo off
taskkill /f /im jqs.exe
taskkill /f /im javaw.exe
taskkill /f /im java.exe
start "" "C:\Program Files (x86)\Oculus Quest Discord RPC\unins000.exe"
rmdir /q /s %AppData%\oqrpc\
exit