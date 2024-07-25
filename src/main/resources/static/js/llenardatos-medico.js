var editUserModal = document.getElementById("editUserModal");
editUserModal.addEventListener("show.bs.modal", function (event) {
  var link = event.relatedTarget; // Enlace que abrió el modal
  var userId = link.getAttribute("data-id");
  var username = link.getAttribute("data-username");
  console.log(userId);
  console.log(username);


  var idInput = editUserModal.querySelector("#idMedico");
  var usernameInput = editUserModal.querySelector("#nombre");
  console.log(idInput);
  console.log(usernameInput);

  idInput.value = userId;
  usernameInput.value = username;
});
