var editUserModal = document.getElementById("editUserModal");
editUserModal.addEventListener("show.bs.modal", function (event) {
    var button = event.relatedTarget; // Botón que abrió el modal
    var id = button.getAttribute("data-id");
    var fecha = button.getAttribute("data-fecha");
    var paciente = button.getAttribute("data-paciente");
    var dni = button.getAttribute("data-dni");
    var email = button.getAttribute("data-email");
    var consultorio = button.getAttribute("data-consultorio");
    var medico = button.getAttribute("data-medico");
    var actividad = button.getAttribute("data-actividad");
    var hora = button.getAttribute("data-hora");
    var situacion = button.getAttribute("data-situacion");
    var idHorario = button.getAttribute("data-idHorario");
    var idpaciente = button.getAttribute("data-idpaciente");

    var idInput = editUserModal.querySelector("#id");
    var fechaInput = editUserModal.querySelector("#fecha");
    var pacienteInput = editUserModal.querySelector("#paciente");
    var dniInput = editUserModal.querySelector("#dni");
    var emailInput = editUserModal.querySelector("#email");
    var consultorioInput = editUserModal.querySelector("#consultorio");
    var medicoInput = editUserModal.querySelector("#medico");
    var actividadInput = editUserModal.querySelector("#actividad");
    var horaInput = editUserModal.querySelector("#hora");
    var situacionInput = editUserModal.querySelector("#situacion");
    var idHorarioInput = editUserModal.querySelector("#idHorario");
    var idpacienteInput = editUserModal.querySelector("#idpaciente");

    idInput.value = id;
    fechaInput.value = fecha;
    pacienteInput.value = paciente;
    dniInput.value = dni;
    emailInput.value = email;
    consultorioInput.value = consultorio;
    medicoInput.value = medico;
    actividadInput.value = actividad;
    horaInput.value = hora;
    situacionInput.value = situacion;
    idHorarioInput.value = idHorario;
    idpacienteInput.value = idpaciente;
});