var editUserModal = document.getElementById("editUserModal");
editUserModal.addEventListener("show.bs.modal", function (event) {
  var link = event.relatedTarget; // Enlace que abrió el modal
  var userId = link.getAttribute("data-id");
  var username = link.getAttribute("data-username");
  var password = link.getAttribute("data-password");
  var nombre = link.getAttribute("data-nombre");
  var apellido = link.getAttribute("data-apellido");
  var dni = link.getAttribute("data-dni");
  var correo = link.getAttribute("data-correo");
  var celular = link.getAttribute("data-celular");
  var rol = link.getAttribute("data-rol");
  var password = link.getAttribute("data-password");

  var modalTitle = editUserModal.querySelector(".modal-title");
  var idInput = editUserModal.querySelector("#id_usuario");
  var usernameInput = editUserModal.querySelector("#username");
  var passwordInput = editUserModal.querySelector("#password");
  var nombreInput = editUserModal.querySelector("#nombre");
  var apellidoInput = editUserModal.querySelector("#apellido");
  var dniInput = editUserModal.querySelector("#dni");
  var correoInput = editUserModal.querySelector("#correo");
  var celularInput = editUserModal.querySelector("#celular");
  var rolInput = editUserModal.querySelector("#rol");
  var passwordInput = editUserModal.querySelector("#password");


  modalTitle.textContent = "Editar Usuario: " + username;
  idInput.value = userId;
  usernameInput.value = username;
  passwordInput.value = password;
  nombreInput.value = nombre;
  apellidoInput.value = apellido;
  dniInput.value = dni;
  correoInput.value = correo;
  celularInput.value = celular;
  rolInput.value = rol;
});
