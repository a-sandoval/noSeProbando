document.addEventListener("DOMContentLoaded", function() {
    // Obtiene una lista de todos los elementos de entrada de radio con el nombre "modos"
    var radioModos = document.querySelectorAll('input[name="modos"]');

    // Obtiene una referencia al contenedor del botón adicional
    var botonAdicionalContainer = document.getElementById("botonAdicionalContainer");

    // Agrega un event listener a cada elemento de entrada de radio
    radioModos.forEach(function(radioModo) {
        radioModo.addEventListener("change", function() {
            if (radioModo.value === "Sin apuros, configura tus horarios!") {
                // Muestra el botón adicional si la opción "particular" está seleccionada
                botonAdicionalContainer.style.display = "block";
            } else {
                // Oculta el botón adicional si se selecciona cualquier otra opción
                botonAdicionalContainer.style.display = "none";
            }
        });
    });
});