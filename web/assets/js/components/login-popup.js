document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("loginPopupOverlay");
    if (!overlay) return;

    const body = document.body;

    const openBtns = document.querySelectorAll(
        "#openLoginPopup, #showLoginPopup, .open-login-popup"
    );
    const closeBtn = document.getElementById("closeLoginPopup");

    const loginWrap = document.getElementById("loginFormWrap");
    const registerWrap = document.getElementById("registerFormWrap");

    const showRegisterBtn = document.getElementById("showRegisterForm");
    const showLoginBtn = document.getElementById("showLoginForm");

    const title = document.getElementById("authPopupTitle");
    const subtitle = document.getElementById("authPopupSubtitle");
    const note = document.getElementById("authPopupNote");

    const switchBtns = document.querySelectorAll(".login-switch-btn");
    const dangNhapInput = document.getElementById("popupDangNhap");
    const passwordInput = document.getElementById("popupPassword");
    const label = document.getElementById("loginLabel");
    const errorText = document.getElementById("loginPopupSubmitError");
    const togglePasswordBtn = document.getElementById("togglePopupPassword");

    const loginForm = document.getElementById("loginPopupForm");

    function refreshLucide() {
        if (window.lucide) {
            lucide.createIcons();
        }
    }

    function openPopup() {
        overlay.classList.add("show");
        body.classList.add("login-popup-open");
    }

    function closePopup() {
        overlay.classList.remove("show");
        body.classList.remove("login-popup-open");
    }

    function clearLoginError() {
        if (errorText) {
            errorText.textContent = "";
            errorText.classList.remove("show");
        }
    }

    function clearLoginFields() {
        if (dangNhapInput) dangNhapInput.value = "";
        if (passwordInput) {
            passwordInput.value = "";
            passwordInput.type = "password";
        }

        if (togglePasswordBtn) {
            togglePasswordBtn.innerHTML = '<i data-lucide="eye-off"></i>';
            refreshLucide();
        }
    }

    function setLoginMode(mode) {
        if (!dangNhapInput || !label) return;

        switchBtns.forEach((btn) => {
            btn.classList.toggle("active", btn.dataset.type === mode);
        });

        if (mode === "phone") {
            label.textContent = "SỐ ĐIỆN THOẠI";
            dangNhapInput.placeholder = "Số điện thoại";
            dangNhapInput.type = "text";
        } else {
            label.textContent = "E-MAIL ADDRESS";
            dangNhapInput.placeholder = "E-mail";
            dangNhapInput.type = "email";
        }
    }

    function switchToLogin() {
        if (loginWrap) loginWrap.classList.add("active");
        if (registerWrap) registerWrap.classList.remove("active");

        if (title) title.textContent = "CHÀO MỪNG TRỞ LẠI";
        if (subtitle) {
            subtitle.textContent =
                "Đăng nhập để không bỏ lỡ bất kỳ sản phẩm và ưu đãi nào.";
        }
        if (note) {
            note.innerHTML =
                'Nhập thông tin tài khoản của bạn để tiếp tục mua sắm cùng <span style="font-weight: bold"><span style="color: var(--color-red)">V</span>$PORT</span>';
        }

        clearLoginError();
        setLoginMode("email");
    }

    function switchToRegister() {
        if (loginWrap) loginWrap.classList.remove("active");
        if (registerWrap) registerWrap.classList.add("active");

        if (title) title.textContent = "TẠO TÀI KHOẢN";
        if (subtitle) {
            subtitle.textContent =
                "Đăng ký để bắt đầu mua sắm và săn ưu đãi cùng VSport.";
        }
        if (note) {
            note.innerHTML =
                'Điền thông tin của bạn để tạo tài khoản mới cùng <span style="font-weight: bold"><span style="color: var(--color-red)">V</span>$PORT</span>';
        }
    }

    function applyTheme(theme) {
        if (theme === "dark") {
            body.classList.add("dark-mode");
        } else {
            body.classList.remove("dark-mode");
        }
    }

    function saveTheme(theme) {
        localStorage.setItem("theme", theme);
        applyTheme(theme);
    }

    function loadTheme() {
        const savedTheme = localStorage.getItem("theme");
        if (savedTheme === "dark") {
            applyTheme("dark");
        } else {
            applyTheme("light");
        }
    }

    loadTheme();

    openBtns.forEach((btn) => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            switchToLogin();
            clearLoginFields();
            openPopup();
        });
    });

    if (closeBtn) {
        closeBtn.addEventListener("click", closePopup);
    }

    overlay.addEventListener("click", function (e) {
        if (e.target === overlay) {
            closePopup();
        }
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape" && overlay.classList.contains("show")) {
            closePopup();
        }
    });

    if (showRegisterBtn) {
        showRegisterBtn.addEventListener("click", function (e) {
            e.preventDefault();
            switchToRegister();
        });
    }

    if (showLoginBtn) {
        showLoginBtn.addEventListener("click", function (e) {
            e.preventDefault();
            switchToLogin();
        });
    }

    switchBtns.forEach((btn) => {
        btn.addEventListener("click", function () {
            const type = this.dataset.type || "email";
            setLoginMode(type);
            if (dangNhapInput) dangNhapInput.value = "";
            clearLoginError();
        });
    });

    if (togglePasswordBtn && passwordInput) {
        togglePasswordBtn.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";

            togglePasswordBtn.innerHTML = isPassword
                ? '<i data-lucide="eye"></i>'
                : '<i data-lucide="eye-off"></i>';

            refreshLucide();
        });
    }

    const registerPasswordToggles = document.querySelectorAll(
        ".register-password-toggle"
    );

    registerPasswordToggles.forEach((btn) => {
        btn.addEventListener("click", function () {
            const wrap = btn.closest(".login-popup-password-wrap");
            const input = wrap ? wrap.querySelector("input") : null;
            if (!input) return;

            const isPassword = input.type === "password";
            input.type = isPassword ? "text" : "password";

            btn.innerHTML = isPassword
                ? '<i data-lucide="eye"></i>'
                : '<i data-lucide="eye-off"></i>';

            refreshLucide();
        });
    });

    const shouldOpenPopup = overlay.dataset.open === "true";
    const shouldOpenRegister = overlay.dataset.registerOpen === "true";
    const loginErrorMessage = (overlay.dataset.loginError || "").trim();
    const loginSuccess = overlay.dataset.loginSuccess === "true";

    if (shouldOpenRegister) {
        switchToRegister();
        openPopup();
    } else if (shouldOpenPopup) {
        switchToLogin();
        openPopup();
        clearLoginFields();
    } else {
        switchToLogin();
    }

    if (loginErrorMessage && errorText) {
        errorText.textContent = loginErrorMessage;
        errorText.classList.add("show");
    }

    if (loginSuccess) {
        closePopup();
    }

    if (loginForm) {
        loginForm.addEventListener("submit", function () {
            clearLoginError();
        });
    }
});