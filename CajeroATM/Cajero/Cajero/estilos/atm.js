/**
 * Archivo: atm.js
 * Función: Contiene funciones de utilidad para la navegación del ATM.
 */

document.addEventListener('DOMContentLoaded', function() {
    // Obtener la ruta base del contexto (ej: /ATM2_UTH_GRUP4_war)
    // Esto asume que el script se carga usando #{request.contextPath}
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    /**
     * Navega a una página específica dentro del contexto del proyecto.
     * @param {string} pageName - El nombre del archivo XHTML (ej: 'help' o 'salir').
     */
    window.navigateAtm = function(pageName) {
        // Construye la URL completa: /ATM2_UTH_GRUP4_war/help.xhtml
        const url = contextPath + "/" + pageName + ".xhtml";
        console.log("Navegando a: " + url);
        window.location.href = url;
    }

    // Aquí podrías agregar efectos de sonido o animaciones de botón si quisieras
});