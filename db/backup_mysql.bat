@echo off
REM Set date for filename (YYYY-MM-DD)
for /f "tokens=2-4 delims=/ " %%a in ('date /t') do set mydate=%%c-%%a-%%b

REM MySQL credentials
set DB_NAME=strategy-prod
set DB_USER=root
set DB_PASS=root

REM Backup location (external drive, e.g., E:\MySQLBackups)
REM set BACKUP_DIR=E:\MySQLBackups
 set BACKUP_DIR=D:\MySQLBackups

REM Create backup directory if it doesn't exist
if not exist %BACKUP_DIR% mkdir %BACKUP_DIR%

REM Run mysqldump and gzip the output (requires gzip installed, optional)
mysqldump -u %DB_USER% -p%DB_PASS% %DB_NAME% > %BACKUP_DIR%\%DB_NAME%_%mydate%.sql

REM Optional: compress using 7zip if installed
REM "C:\Program Files\7-Zip\7z.exe" a %BACKUP_DIR%\%DB_NAME%_%mydate%.zip %BACKUP_DIR%\%DB_NAME%_%mydate%.sql

REM Optional: delete uncompressed SQL after compression
REM del %BACKUP_DIR%\%DB_NAME%_%mydate%.sql