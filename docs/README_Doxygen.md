Documentación Doxygen para ExpositoTOP

Requisitos:
- Doxygen (https://www.doxygen.nl)
- Graphviz (https://graphviz.org) para generar diagramas

Generar la documentación en Windows (PowerShell):

# Instalar Chocolatey (si no está) y luego instalar herramientas:
# Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
# choco install doxygen graphviz -y

Generar docs con el script provisto:

powershell -ExecutionPolicy Bypass -File .\tools\generate_docs.ps1

Archivos relevantes:
- `Doxyfile`: configuración principal (encabezado en el root del proyecto).
- `docs/mainpage.md`: página principal de la documentación.

Notas:
- Si Graphviz está instalado en una ruta no estándar, ajusta `DOT_PATH` en `Doxyfile`.
- Doxygen extraerá JavaDoc y comentarios de los archivos `.java`. Considera mejorar los comentarios en el código para documentación más rica.
