document.addEventListener("DOMContentLoaded", function () {
    const root = document.documentElement;
    const header = document.getElementById("siteHeader");

    function setTheme(theme) {
        if (theme === "dark") {
            root.classList.add("dark-mode");
        } else {
            root.classList.remove("dark-mode");
        }
        localStorage.setItem("theme", theme);
    }

    function toggleTheme() {
        const isDark = root.classList.contains("dark-mode");
        setTheme(isDark ? "light" : "dark");
    }

    /* đảm bảo đồng bộ sau khi DOM xong */
    const savedTheme = localStorage.getItem("theme") || "light";
    setTheme(savedTheme);

    /* HEADER SHRINK */
    if (header) {
        let ticking = false;

        function updateHeader() {
            const currentScrollY = window.scrollY;

            if (currentScrollY > 40) {
                header.classList.add("shrink");
            } else {
                header.classList.remove("shrink");
            }

            ticking = false;
        }

        window.addEventListener("scroll", function () {
            if (!ticking) {
                window.requestAnimationFrame(updateHeader);
                ticking = true;
            }
        });

        updateHeader();
    }

    /* render icon 1 lần */
    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }

    /* event delegation */
    document.addEventListener("click", function (e) {
        const themeBtn = e.target.closest("#themeToggleBtn");
        if (!themeBtn) return;
        e.preventDefault();
        toggleTheme();
    });
});