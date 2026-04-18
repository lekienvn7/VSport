document.addEventListener("DOMContentLoaded", function () {
    const wrapper = document.getElementById("clubLinksWrapper");
    const leftBtn = document.getElementById("clubScrollLeft");
    const rightBtn = document.getElementById("clubScrollRight");

    if (wrapper && leftBtn && rightBtn) {
        leftBtn.addEventListener("click", function () {
            wrapper.scrollBy({
                left: -300,
                behavior: "smooth"
            });
        });

        rightBtn.addEventListener("click", function () {
            wrapper.scrollBy({
                left: 300,
                behavior: "smooth"
            });
        });
    }
});


document.addEventListener("DOMContentLoaded", function () {
    const favoriteButtons = document.querySelectorAll(".club-favorite-btn");

    favoriteButtons.forEach(button => {
        button.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();

            const maSanPham = this.dataset.productId;
            const currentButton = this;

            fetch(window.contextPath + "/yeu_thich/toggle", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: "maSanPham=" + encodeURIComponent(maSanPham)
            })
            .then(response => response.json())
            .then(data => {
                if (!data.success) {
                    if (data.needLogin) {
                        openLoginPopup();
                        return;
                    }

                    alert(data.message || "Có lỗi xảy ra");
                    return;
                }

                if (data.action === "added") {
                    currentButton.classList.add("active");
                    currentButton.textContent = "♥";
                } else if (data.action === "removed") {
                    currentButton.classList.remove("active");
                    currentButton.textContent = "♡";
                }
            })
            .catch(error => {
                console.error("Lỗi yêu thích:", error);
                alert("Không thể xử lý yêu thích lúc này");
            });
        });
    });

    function openLoginPopup() {
        const loginOverlay = document.getElementById("loginPopupOverlay");
        const loginPopup = document.getElementById("loginPopupOverlay");

        if (loginOverlay) {
            loginOverlay.classList.add("show");
            document.body.classList.add("login-lock");
        }

        if (loginPopup) {
            loginPopup.classList.add("show");
        }
    }
});