@echo off
setlocal

set SEMGREP_IMAGE=semgrep/semgrep
set SEMGREP_CONFIG=semgrep/rules
set SEMGREP_REPORT_DIR=build\semgrep

if "%1"=="" goto scan
if "%1"=="scan" goto scan
if "%1"=="json" goto json
if "%1"=="sarif" goto sarif
if "%1"=="strict" goto strict
if "%1"=="validate" goto validate
if "%1"=="help" goto help

echo Unknown command: %1
echo.
goto help

:scan
docker run --rm ^
  -v "%CD%:/src" ^
  -w /src ^
  %SEMGREP_IMAGE% ^
  semgrep scan --config %SEMGREP_CONFIG% .
goto end

:json
if not exist "%SEMGREP_REPORT_DIR%" mkdir "%SEMGREP_REPORT_DIR%"

docker run --rm ^
  -v "%CD%:/src" ^
  -w /src ^
  %SEMGREP_IMAGE% ^
  semgrep scan --config %SEMGREP_CONFIG% . ^
  --json-output=build/semgrep/semgrep.json
goto end

:sarif
if not exist "%SEMGREP_REPORT_DIR%" mkdir "%SEMGREP_REPORT_DIR%"

docker run --rm ^
  -v "%CD%:/src" ^
  -w /src ^
  %SEMGREP_IMAGE% ^
  semgrep scan --config %SEMGREP_CONFIG% . ^
  --sarif-output=build/semgrep/semgrep.sarif
goto end

:strict
docker run --rm ^
  -v "%CD%:/src" ^
  -w /src ^
  %SEMGREP_IMAGE% ^
  semgrep scan --config %SEMGREP_CONFIG% . --error
goto end

:validate
docker run --rm ^
  -v "%CD%:/src" ^
  -w /src ^
  %SEMGREP_IMAGE% ^
  semgrep scan --config %SEMGREP_CONFIG% --validate
goto end

:help
echo Usage:
echo   semgrep.bat scan       Run Semgrep scan
echo   semgrep.bat json       Run Semgrep and save JSON report
echo   semgrep.bat sarif      Run Semgrep and save SARIF report
echo   semgrep.bat strict     Run Semgrep and fail on findings
echo   semgrep.bat validate   Validate Semgrep rules
echo.
echo Default:
echo   semgrep.bat
echo is the same as:
echo   semgrep.bat scan

:end
endlocal