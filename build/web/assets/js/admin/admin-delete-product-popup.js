document.addEventListener("DOMContentLoaded", function () {
    const popup = document.getElementById("deleteProductPopup");
    const overlay = document.getElementById("deleteProductPopupOverlay");
    const closeBtn = document.getElementById("closeDeleteProductPopup");
    const cancelBtn = document.getElementById("cancelDeleteProductPopup");
    const form = document.getElementById("deleteProductForm");
    const deleteMaSanPham = document.getElementById("deleteMaSanPham");
    const deleteProductName = document.getElementById("deleteProductName");

    if (!popup || !overlay || !form) return;

    function openPopup() {
        popup.classList.add("show");
        overlay.classList.add("show");
        document.body.classList.add("admin-popup-open");
    }

    function closePopup() {
        popup.classList.remove("show");
        overlay.classList.remove("show");
        document.body.classList.remove("admin-popup-open");
    }

    document.querySelectorAll(".admin-product-delete-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const maSanPham = this.dataset.maSanPham || "";
            const tenSanPham = this.dataset.tenSanPham || "Sản phẩm này";

            deleteMaSanPham.value = maSanPham;
            deleteProductName.textContent = tenSanPham;

            openPopup();
        });
    });

    form.addEventListener("submit", async function (e) {
    e.preventDefault();

    try {
        const formData = new FormData(form);
        const payload = new URLSearchParams();

        for (const [key, value] of formData.entries()) {
            payload.append(key, value);
            console.log(key, "=", value);
        }

        const response = await fetch(form.action, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: payload.toString()
        });

        const rawText = await response.text();
        console.log("DELETE RAW RESPONSE:", rawText);
        console.log("DELETE STATUS:", response.status);

        let data;
        try {
            data = JSON.parse(rawText);
        } catch (parseError) {
            throw new Error("Response không phải JSON: " + rawText);
        }

        if (data.success) {
            alert(data.message || "Xóa sản phẩm thành công.");
            closePopup();
            window.location.reload();
        } else {
            alert(data.message || "Không thể xóa sản phẩm.");
        }
    } catch (e) {
        console.error("Delete product error:", e);
        alert("Không gửi được yêu cầu xóa.");
    }
});

    closeBtn?.addEventListener("click", closePopup);
    cancelBtn?.addEventListener("click", closePopup);
    overlay?.addEventListener("click", closePopup);
});