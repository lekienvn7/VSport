document.addEventListener("DOMContentLoaded", function () {
    const header = document.getElementById("siteHeader");
    if (!header) return;

    let lastScrollY = window.scrollY;
    let ticking = false;

    function updateHeader() {
        const currentScrollY = window.scrollY;

        if (currentScrollY > 40) {
            header.classList.add("shrink");
        } else {
            header.classList.remove("shrink");
        }

        lastScrollY = currentScrollY;
        ticking = false;
    }

    window.addEventListener("scroll", function () {
        if (!ticking) {
            window.requestAnimationFrame(updateHeader);
            ticking = true;
        }
    });

    updateHeader();
});

