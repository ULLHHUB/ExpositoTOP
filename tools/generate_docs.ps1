Param()

$here = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $here\..

if (-not (Get-Command doxygen -ErrorAction SilentlyContinue)) {
    Write-Host "Doxygen no está en PATH. Instálalo o añadelo al PATH." -ForegroundColor Yellow
    Pop-Location
    exit 1
}

$doxy = "doxygen"
Write-Host "Generando documentación con Doxygen..."
& $doxy Doxyfile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Documentación generada en docs/doxygen/html" -ForegroundColor Green
} else {
    Write-Host "Error al generar documentación (código: $LASTEXITCODE)" -ForegroundColor Red
}

Pop-Location
