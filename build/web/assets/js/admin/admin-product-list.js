document.addEventListener("DOMContentLoaded", function () {
    const checkAll = document.getElementById("checkAllProducts");
    const rowChecks = document.querySelectorAll(".product-row-check");
    const autoSubmitFields = document.querySelectorAll(".auto-submit");
    const dateInputs = document.querySelectorAll(".admin-product-date-input");
    const pageSize = document.querySelector(".admin-product-page-size");
    const filterForm = document.getElementById("adminProductFilterForm");
    const menuWraps = document.querySelectorAll(".admin-product-action-menu-wrap");

    if (checkAll && rowChecks.length > 0) {
        checkAll.addEventListener("change", function () {
            rowChecks.forEach(function (item) {
                item.checked = checkAll.checked;
            });
        });

        rowChecks.forEach(function (item) {
            item.addEventListener("change", function () {
                const checkedCount = document.querySelectorAll(".product-row-check:checked").length;
                checkAll.checked = checkedCount === rowChecks.length;
            });
        });
    }

    autoSubmitFields.forEach(function (field) {
        field.addEventListener("change", function () {
            if (filterForm) filterForm.submit();
        });
    });

    dateInputs.forEach(function (input) {
        input.addEventListener("change", function () {
            if (filterForm) filterForm.submit();
        });
    });

    if (pageSize && filterForm) {
        pageSize.addEventListener("change", function () {
            filterForm.submit();
        });
    }

    menuWraps.forEach(function (wrap) {
        const btn = wrap.querySelector(".admin-product-dots-btn");

        if (!btn) return;

        btn.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();

            menuWraps.forEach(function (otherWrap) {
                if (otherWrap !== wrap) {
                    otherWrap.classList.remove("open");
                }
            });

            wrap.classList.toggle("open");
        });
    });

    document.addEventListener("click", function (e) {
        menuWraps.forEach(function (wrap) {
            if (!wrap.contains(e.target)) {
                wrap.classList.remove("open");
            }
        });
    });
});