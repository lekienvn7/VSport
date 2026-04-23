document.addEventListener("DOMContentLoaded", function () {
    const wrap = document.querySelector(".admin-sidebar-user-wrap");
    const btn = document.getElementById("adminSidebarUserMore");
    const card = document.getElementById("adminSidebarUserCard");

    if (!wrap || !btn || !card) return;

    function toggleUserDropdown(e) {
        e.preventDefault();
        e.stopPropagation();
        wrap.classList.toggle("open");
    }

    btn.addEventListener("click", toggleUserDropdown);
    card.addEventListener("click", toggleUserDropdown);

    document.addEventListener("click", function (e) {
        if (!wrap.contains(e.target)) {
            wrap.classList.remove("open");
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const popup = document.getElementById("profilePopup");
    const overlay = document.getElementById("profilePopupOverlay");
    const openFromSidebar = document.getElementById("openProfilePopupFromSidebar");
    const closeButton = document.getElementById("closeProfilePopup");
    const cancelButton = document.getElementById("cancelProfilePopup");

    function openProfilePopup() {
        if (popup) popup.classList.add("show");
        if (overlay) overlay.classList.add("show");
        document.body.classList.add("profile-popup-open");
    }

    function closeProfilePopup() {
        if (popup) popup.classList.remove("show");
        if (overlay) overlay.classList.remove("show");
        document.body.classList.remove("profile-popup-open");
    }

    if (openFromSidebar) {
        openFromSidebar.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            openProfilePopup();
        });
    }

    if (closeButton) {
        closeButton.addEventListener("click", closeProfilePopup);
    }

    if (cancelButton) {
        cancelButton.addEventListener("click", closeProfilePopup);
    }

    if (overlay) {
        overlay.addEventListener("click", closeProfilePopup);
    }
    
    
});