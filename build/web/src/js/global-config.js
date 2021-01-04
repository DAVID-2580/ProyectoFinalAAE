function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

(function () {
  const account_info = document.getElementById("account-info");
  const account_button = document.getElementById("account-button");

  account_info.addEventListener("click", () => displayMessage());
  account_button.addEventListener("click", () => displayMessage());

  function displayMessage() {
    Qual.infod(
      "Esta feature estara habilitada pronto",
      "Lamentamos los inconvenientes 😭",
      inf
    );
  }
})();
