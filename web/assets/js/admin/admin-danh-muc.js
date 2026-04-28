document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("categoryModalOverlay");

    const categoryModal = document.getElementById("categoryModal");
    const deleteModal = document.getElementById("deleteCategoryModal");

    const openAddBtn = document.getElementById("openAddCategoryModal");
    const closeCategoryBtn = document.getElementById("closeCategoryModal");
    const cancelCategoryBtn = document.getElementById("cancelCategoryModal");

    const closeDeleteBtn = document.getElementById("closeDeleteCategoryModal");
    const cancelDeleteBtn = document.getElementById("cancelDeleteCategoryModal");

    const modalTitle = document.getElementById("categoryModalTitle");
    const modalLabel = document.getElementById("categoryModalLabel");

    const categoryAction = document.getElementById("categoryAction");
    const categoryId = document.getElementById("categoryId");
    const categoryName = document.getElementById("categoryName");
    const categorySlug = document.getElementById("categorySlug");

    const deleteCategoryId = document.getElementById("deleteCategoryId");
    const deleteCategoryName = document.getElementById("deleteCategoryName");

    const searchInput = document.getElementById("categorySearchInput");
    const tableBody = document.getElementById("categoryTableBody");

    function showOverlay() {
        if (overlay) overlay.classList.add("show");
    }

    function hideOverlay() {
        if (overlay) overlay.classList.remove("show");
    }

    function openModal(modal) {
        showOverlay();
        modal.classList.add("show");
        document.body.style.overflow = "hidden";

        if (window.lucide) {
            lucide.createIcons();
        }
    }

    function closeAllModals() {
        hideOverlay();

        if (categoryModal) categoryModal.classList.remove("show");
        if (deleteModal) deleteModal.classList.remove("show");

        document.body.style.overflow = "";
    }

    function resetCategoryForm() {
        categoryAction.value = "add";
        categoryId.value = "";
        categoryName.value = "";
        categorySlug.value = "";

        modalLabel.textContent = "Danh mục";
        modalTitle.textContent = "Thêm danh mục";
    }

    function makeSlug(text) {
        return text
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d")
            .replace(/Đ/g, "d")
            .replace(/[^a-z0-9\s-]/g, "")
            .trim()
            .replace(/\s+/g, "-")
            .replace(/-+/g, "-");
    }

    if (openAddBtn) {
        openAddBtn.addEventListener("click", function () {
            resetCategoryForm();
            openModal(categoryModal);
        });
    }

    document.querySelectorAll(".category-action-btn.edit").forEach(function (btn) {
        btn.addEventListener("click", function () {
            categoryAction.value = "update";
            categoryId.value = btn.dataset.id || "";
            categoryName.value = btn.dataset.name || "";
            categorySlug.value = btn.dataset.slug || "";

            modalLabel.textContent = "Cập nhật";
            modalTitle.textContent = "Sửa danh mục";

            openModal(categoryModal);
        });
    });

    document.querySelectorAll(".category-action-btn.delete").forEach(function (btn) {
        btn.addEventListener("click", function () {
            deleteCategoryId.value = btn.dataset.id || "";
            deleteCategoryName.textContent = btn.dataset.name || "";

            openModal(deleteModal);
        });
    });

    if (categoryName && categorySlug) {
        categoryName.addEventListener("input", function () {
            if (categoryAction.value === "add") {
                categorySlug.value = makeSlug(categoryName.value);
            }
        });
    }

    if (categorySlug) {
        categorySlug.addEventListener("input", function () {
            categorySlug.value = makeSlug(categorySlug.value);
        });
    }

    if (overlay) {
        overlay.addEventListener("click", closeAllModals);
    }

    if (closeCategoryBtn) {
        closeCategoryBtn.addEventListener("click", closeAllModals);
    }

    if (cancelCategoryBtn) {
        cancelCategoryBtn.addEventListener("click", closeAllModals);
    }

    if (closeDeleteBtn) {
        closeDeleteBtn.addEventListener("click", closeAllModals);
    }

    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener("click", closeAllModals);
    }

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            closeAllModals();
        }
    });

    if (searchInput && tableBody) {
        searchInput.addEventListener("input", function () {
            const keyword = searchInput.value.toLowerCase().trim();
            const rows = tableBody.querySelectorAll("tr");

            rows.forEach(function (row) {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(keyword) ? "" : "none";
            });
        });
    }

    setTimeout(function () {
        const toast = document.querySelector(".admin-category-toast");
        if (toast) {
            toast.style.opacity = "0";
            toast.style.transform = "translateY(-6px)";

            setTimeout(function () {
                toast.remove();
            }, 250);
        }
    }, 2500);
});