@echo off
echo =============================================
echo  Generador de PDF Doxygen - ExpositoTOP
echo =============================================

REM --- Comprobación de refman.tex ---
if not exist refman.tex (
    echo [ERROR] No se encontro refman.tex en esta carpeta.
    pause
    exit /b
)

REM --- Intentar reparar paquetes faltantes ---
echo.
echo [INFO] Comprobando paquetes requeridos...
findstr /C:"\usepackage{tabu}" refman.tex >nul
if errorlevel 1 (
    echo [INFO] Añadiendo paquetes adicionales al preámbulo...
    powershell -Command "(Get-Content refman.tex) -replace '\\usepackage{graphicx}', '\\usepackage{graphicx}`r`n\\usepackage{etoolbox}`r`n\\usepackage{longtable}`r`n\\usepackage{tabu}`r`n\\usepackage{array}' | Set-Content refman_mod.tex"
    move /Y refman_mod.tex refman.tex >nul
)

REM --- Compilacion LaTeX ---
echo.
echo [INFO] Compilando con pdflatex...
pdflatex -interaction=nonstopmode refman.tex
makeindex refman.idx
pdflatex -interaction=nonstopmode refman.tex
pdflatex -interaction=nonstopmode refman.tex

REM --- Verificación del resultado ---
if exist refman.pdf (
    echo.
    echo [OK] PDF generado correctamente.
    ren refman.pdf ExpositoTOP_Documentacion.pdf
    echo [OK] Archivo final: ExpositoTOP_Documentacion.pdf
) else (
    echo.
    echo [ERROR] No se pudo generar el PDF. Revisa los mensajes anteriores.
)

echo.
pause
