document.addEventListener("DOMContentLoaded", function () {
    const openBtn = document.getElementById("openSearchPopup");
    const closeBtn = document.getElementById("closeSearchPopup");
    const overlay = document.getElementById("searchOverlay");
    const input = document.getElementById("searchInputPopup");
    const clearBtn = document.getElementById("clearSearchBtn");
    const resultsWrap = document.getElementById("searchResultsWrap");

    let debounceTimer = null;

    function openPopup() {
        if (!overlay) return;

        overlay.classList.add("show");
        document.body.classList.add("search-lock");

        if (input) {
            setTimeout(() => {
                input.focus();
            }, 120);
        }

        if (input && !input.value.trim()) {
            renderDefaultEmpty();
        }
    }

    function closePopup() {
        if (!overlay) return;

        overlay.classList.remove("show");
        document.body.classList.remove("search-lock");
    }

    function renderLoading() {
        if (!resultsWrap) return;
        resultsWrap.innerHTML = `<div class="search-loading">Đang tìm...</div>`;
    }

    function renderDefaultEmpty() {
        if (!resultsWrap) return;
        resultsWrap.innerHTML = `
            <div class="search-empty-state">
                <p>Gõ tên sản phẩm, đội bóng, thương hiệu, danh mục, size...</p>
            </div>
        `;
    }

    async function searchProducts(keyword) {
        const q = keyword.trim();

        if (!q) {
            renderDefaultEmpty();
            return;
        }

        renderLoading();

        try {
            const response = await fetch(
                `${window.contextPath}/tim_kiem?keyword=${encodeURIComponent(q)}`,
                {
                    method: "GET",
                    headers: {
                        "X-Requested-With": "XMLHttpRequest"
                    }
                }
            );

            if (!response.ok) {
                throw new Error(`HTTP error: ${response.status}`);
            }

            const html = await response.text();
            resultsWrap.innerHTML = html;
        } catch (error) {
            console.error("Search error:", error);
            resultsWrap.innerHTML = `
                <div class="search-empty-state">
                    <p>Có lỗi khi tìm kiếm. Thử lại sau nhé.</p>
                </div>
            `;
        }
    }

    if (openBtn) {
        openBtn.addEventListener("click", openPopup);
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
        if (e.key === "Escape" && overlay && overlay.classList.contains("show")) {
            closePopup();
        }
    });

    if (clearBtn) {
        clearBtn.addEventListener("click", function () {
            if (!input) return;
            input.value = "";
            renderDefaultEmpty();
            input.focus();
        });
    }

    if (input) {
        input.addEventListener("input", function () {
            clearTimeout(debounceTimer);

            const keyword = this.value;

            debounceTimer = setTimeout(() => {
                searchProducts(keyword);
            }, 250);
        });
    }
});