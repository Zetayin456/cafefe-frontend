// 1. Importas los módulos ES6 arriba de todo
import { JS3 } from './modulos/j3.js';

document.addEventListener('DOMContentLoaded', () => {

    // 2. Probar la inicialización del Módulo JS3 / JS4
    let tres = new JS3();

    // 3. Consumir e Iniciar el Web Worker (Multihilo)
    if (window.Worker) {
        const miWorker = new Worker('js/contador.js');

        // Escucha cada mensaje que envía contador.js
        miWorker.onmessage = function(e) {
            console.log("Contador del Worker:", e.data);
            const spanResultado = document.getElementById("resultado");
            if (spanResultado) {
                spanResultado.innerText = e.data; // Muestra el número en pantalla
            }
        };
    }

    // 4. Manejo del evento del Botón Guardar
    const btnGuardar = document.getElementById('btnGuardar');

    if (btnGuardar) {
        btnGuardar.addEventListener('click', async () => {
            const nombre = document.getElementById('nombre').value;
            console.log(`Procesando registro: ${nombre}`);

            try {
                // Petición asíncrona simulada o hacia la API REST
                alert(`Dato procesado en Frontend: ${nombre}`);
            } catch (error) {
                console.error('Error al conectar con el servidor:', error);
            }
        });
    }
});