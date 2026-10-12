let contador = 0;

function contar() {
    contador++;
    // Envía el valor al hilo principal de la página
    postMessage(contador);
    setTimeout(contar, 500); // Incrementa cada 0.5 seg
}

contar();