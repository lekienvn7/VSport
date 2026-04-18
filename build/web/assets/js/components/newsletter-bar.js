document.addEventListener("DOMContentLoaded", function () {
    const registerBtn = document.getElementById("showRegisterFormBanner");

    if (registerBtn) {
        registerBtn.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            openRegisterPopup();
        });
    }

    function openRegisterPopup() {
    const loginOverlay = document.getElementById("loginPopupOverlay");
    const loginFormWrap = document.getElementById("loginFormWrap");
    const registerFormWrap = document.getElementById("registerFormWrap");

    if (loginOverlay) {
        loginOverlay.classList.add("show");
        document.body.classList.add("login-lock");
    }

    if (loginFormWrap) loginFormWrap.classList.remove("active");
    if (registerFormWrap) registerFormWrap.classList.add("active");
}
});