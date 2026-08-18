$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$sdkRoot = Join-Path $env:LOCALAPPDATA "Android\Sdk"
$adbPath = Join-Path $sdkRoot "platform-tools\adb.exe"

if (-not (Test-Path $adbPath)) {
    throw "ADB not found at $adbPath. Install Android SDK Platform-Tools first."
}

$jdkHome = [Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
if (-not $jdkHome -or -not (Test-Path (Join-Path $jdkHome "bin\java.exe"))) {
    $adoptiumRoot = Join-Path $env:ProgramFiles "Eclipse Adoptium"
    $jdkHome = Get-ChildItem $adoptiumRoot -Directory -ErrorAction SilentlyContinue |
        Where-Object { Test-Path (Join-Path $_.FullName "bin\java.exe") } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1 -ExpandProperty FullName
}

if (-not $jdkHome) {
    throw "JDK not found. Install JDK 17 before running this task."
}

$env:JAVA_HOME = $jdkHome.TrimEnd("\")

$deviceLine = & $adbPath devices |
    Select-Object -Skip 1 |
    Where-Object { $_ -match "\sdevice$" } |
    Select-Object -First 1

if (-not $deviceLine) {
    throw "No Android device or Emulator is connected. Start the Emulator first."
}

$deviceSerial = ($deviceLine -split "\s+")[0]
Write-Host "Using device: $deviceSerial"
Write-Host "Using JDK: $env:JAVA_HOME"

Push-Location $projectRoot
try {
    & ".\gradlew.bat" installDebug
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    & $adbPath -s $deviceSerial shell am force-stop com.example.project
    & $adbPath -s $deviceSerial shell am start -n com.example.project/.App_page1
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    Write-Host "SugarFruit is running on $deviceSerial."
}
finally {
    Pop-Location
}
