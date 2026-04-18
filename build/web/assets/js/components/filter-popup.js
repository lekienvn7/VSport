document.addEventListener("DOMContentLoaded", function () {

    const openBtn = document.getElementById("openFilterPopup");
    const closeBtn = document.getElementById("closeFilterPopup");
    const overlay = document.getElementById("filterOverlay");
    const drawer = document.getElementById("filterDrawer");

    function openDrawer() {
        overlay.classList.add("show");
        drawer.classList.add("show");
        document.body.classList.add("filter-lock");
    }

    function closeDrawer() {
        overlay.classList.remove("show");
        drawer.classList.remove("show");
        document.body.classList.remove("filter-lock");
    }

    // mở popup
    if (openBtn) {
        openBtn.addEventListener("click", openDrawer);
    }

    // đóng popup
    if (closeBtn) {
        closeBtn.addEventListener("click", closeDrawer);
    }

    if (overlay) {
        overlay.addEventListener("click", closeDrawer);
    }

});