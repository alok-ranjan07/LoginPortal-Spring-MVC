function showPassword(id) {
  var input = document.getElementById(id);
  if (input.type === "password") {
    input.type = "text";
  } else {
    input.type = "password";
  }
}

function showPassword(oldpassword, newpassword) {
  var oldInput = document.getElementById(oldpassword);
  var newInput = document.getElementById(newpassword)
  if (oldInput.type === "password") {
    oldInput.type = "text";
  } else {
    oldInput.type = "password";
  }
    if (newInput.type === "password") {
    newInput.type = "text";
  } else {
    newInput.type = "password";
  }
}