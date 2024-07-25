function mostrarFechaHoraActual() {
    var ahora = new Date();

    var dia = ahora.getDate();
    var mes = ahora.getMonth() + 1; // Los meses comienzan desde 0
    var anio = ahora.getFullYear();

    var horas = ahora.getHours();
    var minutos = ahora.getMinutes();
    var segundos = ahora.getSeconds();

    // Agregar un cero al principio si el valor es menor a 10
    dia = (dia < 10) ? '0' + dia : dia;
    mes = (mes < 10) ? '0' + mes : mes;
    horas = (horas < 10) ? '0' + horas : horas;
    minutos = (minutos < 10) ? '0' + minutos : minutos;
    segundos = (segundos < 10) ? '0' + segundos : segundos;

    var fechaActual = dia + '/' + mes + '/' + anio;
    var horaActual = horas + ':' + minutos + ':' + segundos;

    document.getElementById('fecha-hora1').innerText = 'Fecha Actual: ' + fechaActual + ' - Hora Actual: ' + horaActual;
    document.getElementById('fecha-hora2').innerText = 'Fecha Actual: ' + fechaActual + ' - Hora Actual: ' + horaActual;
    document.getElementById('fecha-hora3').innerText = 'Fecha Actual: ' + fechaActual + ' - Hora Actual: ' + horaActual;
}

document.addEventListener('DOMContentLoaded', mostrarFechaHoraActual);
