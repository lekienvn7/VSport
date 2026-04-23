document.addEventListener("DOMContentLoaded", function () {
    const contextPath = window.contextPath || "";

    const modal = document.getElementById("cancelOrderModal");
    const overlay = document.getElementById("cancelOrderOverlay");
    const closeBtn = document.getElementById("closeCancelOrderModal");
    const backBtn = document.getElementById("cancelOrderBackBtn");
    const confirmBtn = document.getElementById("confirmCancelOrderBtn");
    const input = document.getElementById("cancelOrderCodeInput");
    const errorText = document.getElementById("cancelOrderError");
    const codeText = document.getElementById("cancelOrderCodeText");
    const cancelButtons = document.querySelectorAll(".btn-huy-don");

    let currentOrderId = null;

    if (!modal || !confirmBtn || !input || !codeText || !errorText) return;

    if (typeof toastr !== "undefined") {
        toastr.options = {
            closeButton: true,
            progressBar: true,
            positionClass: "toast-top-right",
            timeOut: 2500,
            preventDuplicates: true
        };
    }

    cancelButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            currentOrderId = this.dataset.maDonHang || "";
            codeText.textContent = currentOrderId;
            input.value = "";
            errorText.textContent = "";
            confirmBtn.disabled = false;
            confirmBtn.textContent = "Xác nhận hủy";
            modal.classList.add("show");
        });
    });

    function closeModal() {
        modal.classList.remove("show");
        currentOrderId = null;
        input.value = "";
        errorText.textContent = "";
    }

    if (overlay) overlay.addEventListener("click", closeModal);
    if (closeBtn) closeBtn.addEventListener("click", closeModal);
    if (backBtn) backBtn.addEventListener("click", closeModal);

    confirmBtn.addEventListener("click", async function () {
        if (!currentOrderId) {
            errorText.textContent = "Không tìm thấy mã đơn hàng.";
            if (typeof toastr !== "undefined") {
                toastr.error("Không tìm thấy mã đơn hàng.");
            }
            return;
        }

        const typedValue = input.value.trim();

        if (!typedValue) {
            errorText.textContent = "Vui lòng nhập mã đơn hàng.";
            if (typeof toastr !== "undefined") {
                toastr.warning("Vui lòng nhập mã đơn hàng.");
            }
            return;
        }

        if (typedValue !== String(currentOrderId)) {
            errorText.textContent = "Mã đơn hàng không khớp.";
            if (typeof toastr !== "undefined") {
                toastr.error("Mã đơn hàng không khớp.");
            }
            return;
        }

        try {
            confirmBtn.disabled = true;
            confirmBtn.textContent = "Đang xử lý...";

            const body = new URLSearchParams();
            body.append("maDonHang", currentOrderId);

            const response = await fetch(contextPath + "/don-hang/huy", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: body.toString()
            });

            const text = await response.text();

            if (!response.ok) {
                errorText.textContent = text || "Không thể hủy đơn hàng.";
                confirmBtn.disabled = false;
                confirmBtn.textContent = "Xác nhận hủy";

                if (typeof toastr !== "undefined") {
                    toastr.error(text || "Không thể hủy đơn hàng.");
                }
                return;
            }

            closeModal();

            if (typeof toastr !== "undefined") {
                toastr.success("Hủy đơn hàng thành công.");
            }

            setTimeout(function () {
                window.location.reload();
            }, 900);

        } catch (e) {
            console.error(e);
            errorText.textContent = "Có lỗi xảy ra khi hủy đơn hàng.";
            confirmBtn.disabled = false;
            confirmBtn.textContent = "Xác nhận hủy";

            if (typeof toastr !== "undefined") {
                toastr.error("Có lỗi xảy ra khi hủy đơn hàng.");
            }
        }
    });
});