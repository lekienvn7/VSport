document.addEventListener("DOMContentLoaded", function () {
    const openBtn = document.getElementById("openLoginPopup");
    const overlay = document.getElementById("loginPopupOverlay");
    const shouldOpenRegister = overlay && overlay.dataset.registerOpen === "true";

    if (shouldOpenRegister) {
        switchToRegister();
    } else {
        switchToLogin();
    }
    const closeBtn = document.getElementById("closeLoginPopup");
    const togglePasswordBtn = document.getElementById("togglePopupPassword");
    const passwordInput = document.getElementById("popupPassword");

    function openPopup() {
        if (!overlay) return;
        overlay.classList.add("show");
        document.body.classList.add("login-popup-open");
    }

    function closePopup() {
        if (!overlay) return;
        overlay.classList.remove("show");
        document.body.classList.remove("login-popup-open");
    }

    if (openBtn) {
        openBtn.addEventListener("click", function (e) {
            e.preventDefault();
            openPopup();
        });
    }

    if (closeBtn) {
        closeBtn.addEventListener("click", function () {
            closePopup();
        });
    }

    if (overlay) {
        overlay.addEventListener("click", function (e) {
            if (e.target === overlay) {
                closePopup();
            }
        });
    }

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            closePopup();
        }
    });

    if (togglePasswordBtn && passwordInput) {
        togglePasswordBtn.addEventListener("click", function () {
            const isPassword = passwordInput.getAttribute("type") === "password";
            passwordInput.setAttribute("type", isPassword ? "text" : "password");

            togglePasswordBtn.innerHTML = isPassword
                ? '<i data-lucide="eye"></i>'
                : '<i data-lucide="eye-off"></i>';

            if (window.lucide) {
                lucide.createIcons();
            }
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const switchBtns = document.querySelectorAll(".login-switch-btn");
    const input = document.getElementById("popupDangNhap");
    const label = document.getElementById("loginLabel");

    switchBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            // remove active
            switchBtns.forEach(b => b.classList.remove("active"));
            this.classList.add("active");

            const type = this.dataset.type;

            if (type === "email") {
                label.textContent = "E-MAIL ADDRESS";
                input.placeholder = "E-mail";
                input.type = "text"; // vẫn để text cho backend xử lý
            } else {
                label.textContent = "SỐ ĐIỆN THOẠI";
                input.placeholder = "Số điện thoại";
                input.type = "text";
            }

            input.value = ""; // clear khi switch
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("loginPopupOverlay");
    const openBtn = document.getElementById("openLoginPopup");
    const closeBtn = document.getElementById("closeLoginPopup");

    const switchBtns = document.querySelectorAll(".login-switch-btn");
    const dangNhapInput = document.getElementById("popupDangNhap");
    const passwordInput = document.getElementById("popupPassword");
    const label = document.getElementById("loginLabel");
    const errorText = document.getElementById("loginPopupSubmitError");
    const togglePasswordBtn = document.getElementById("togglePopupPassword");

    function openPopup() {
        if (!overlay) return;
        overlay.classList.add("show");
        document.body.classList.add("login-popup-open");
    }

    function closePopup() {
        if (!overlay) return;
        overlay.classList.remove("show");
        document.body.classList.remove("login-popup-open");
    }

    function setMode(mode) {
        if (!switchBtns.length || !dangNhapInput || !label) return;

        switchBtns.forEach(btn => {
            btn.classList.remove("active");
            if (btn.dataset.type === mode) {
                btn.classList.add("active");
            }
        });

        if (mode === "phone") {
            label.textContent = "SỐ ĐIỆN THOẠI";
            dangNhapInput.placeholder = "Số điện thoại";
        } else {
            label.textContent = "E-MAIL";
            dangNhapInput.placeholder = "E-mail";
        }
    }

    function clearLoginFields() {
        if (dangNhapInput) dangNhapInput.value = "";
        if (passwordInput) passwordInput.value = "";
    }

    if (openBtn) {
        openBtn.addEventListener("click", function (e) {
            e.preventDefault();
            openPopup();
        });
    }

    if (closeBtn) {
        closeBtn.addEventListener("click", closePopup);
    }

    if (overlay) {
        overlay.addEventListener("click", function (e) {
            if (e.target === overlay) {
                closePopup();
            }
        });
    }

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            closePopup();
        }
    });

    switchBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            const type = this.dataset.type;
            setMode(type);

            if (dangNhapInput) dangNhapInput.value = "";
            if (errorText) {
                errorText.textContent = "";
                errorText.classList.remove("show");
            }
        });
    });

    if (togglePasswordBtn && passwordInput) {
        togglePasswordBtn.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";

            togglePasswordBtn.innerHTML = isPassword
                ? '<i data-lucide="eye"></i>'
                : '<i data-lucide="eye-off"></i>';

            if (window.lucide) {
                lucide.createIcons();
            }
        });
    }

    const shouldOpenPopup = "${openLoginPopup}" === "true";
    const loginErrorMessage = `${loginError != null ? loginError : ""}`.trim();

    if (shouldOpenPopup) {
        openPopup();
        clearLoginFields();
        setMode("email"); // hoặc bỏ active nếu m thích
    }

    if (loginErrorMessage && errorText) {
        errorText.textContent = loginErrorMessage;
        errorText.classList.add("show");
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("loginPopupOverlay");
    if (!overlay) return;

    const shouldOpen = overlay.dataset.open === "true";

    if (shouldOpen) {
        overlay.classList.add("show");
        document.body.classList.add("login-popup-open");
    }
});



document.addEventListener("DOMContentLoaded", function () {
    
    function switchToLogin() {
        if (loginWrap) loginWrap.classList.add("active");
        if (registerWrap) registerWrap.classList.remove("active");

        if (title) title.textContent = "CHÀO MỪNG TRỞ LẠI";
        if (subtitle) {
            subtitle.textContent = "Đăng nhập để không bỏ lỡ bất kỳ sản phẩm và ưu đãi nào.";
        }
        if (note) {
            note.innerHTML = 'Nhập thông tin tài khoản của bạn để tiếp tục mua sắm cùng <span style="font-weight: bold"><span style="color: var(--color-red)">V</span>$PORT</span>';
        }

        if (loginError) {
            loginError.classList.remove("show");
        }
    }

    function switchToRegister() {
        if (loginWrap) loginWrap.classList.remove("active");
        if (registerWrap) registerWrap.classList.add("active");

        if (title) title.textContent = "TẠO TÀI KHOẢN";
        if (subtitle) {
            subtitle.textContent = "Đăng ký để bắt đầu mua sắm và săn ưu đãi cùng VSport.";
        }
        if (note) {
            note.innerHTML = 'Điền thông tin của bạn để tạo tài khoản mới cùng <span style="font-weight: bold"><span style="color: var(--color-red)">V</span>$PORT</span>';
        }
    }
    
    const loginWrap = document.getElementById("loginFormWrap");
    const registerWrap = document.getElementById("registerFormWrap");

    const showRegisterBtn = document.getElementById("showRegisterForm");
    const showLoginBtn = document.getElementById("showLoginForm");

    const title = document.getElementById("authPopupTitle");
    const subtitle = document.getElementById("authPopupSubtitle");
    const note = document.getElementById("authPopupNote");

    const loginError = document.getElementById("loginPopupSubmitError");

    

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

    const registerPasswordToggles = document.querySelectorAll(".register-password-toggle");
    registerPasswordToggles.forEach(function (btn) {
        btn.addEventListener("click", function () {
            const wrap = btn.closest(".login-popup-password-wrap");
            const input = wrap ? wrap.querySelector("input") : null;
            if (!input) return;

            const isPassword = input.type === "password";
            input.type = isPassword ? "text" : "password";

            btn.innerHTML = isPassword
                ? '<i data-lucide="eye"></i>'
                : '<i data-lucide="eye-off"></i>';

            if (window.lucide) {
                lucide.createIcons();
            }
        });
    });
});