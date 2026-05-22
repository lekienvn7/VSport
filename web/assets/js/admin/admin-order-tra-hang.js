toastr.options = {
    closeButton: true,
    progressBar: true,
    newestOnTop: true,
    preventDuplicates: true,
    positionClass: "toast-top-right",
    timeOut: "2500",
    extendedTimeOut: "1000",
    showDuration: "250",
    hideDuration: "250",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

document.addEventListener("DOMContentLoaded", function () {
    // Duyệt trả hàng
    document.querySelectorAll('.btn-duyet-tra-hang').forEach(btn => {
        btn.addEventListener('click', function () {
            const maTraHang = this.dataset.returnId;
            const action = this.dataset.action;
            showAdminConfirm({
                title: 'Duyệt trả hàng',
                message: 'Xác nhận duyệt yêu cầu #' + maTraHang + '?',
                onConfirm: () => xuLyTraHang(maTraHang, action)
            });
        });
    });

    // Từ chối trả hàng
    document.querySelectorAll('.btn-tu-choi-tra-hang').forEach(btn => {
        btn.addEventListener('click', function () {
            const maTraHang = this.dataset.returnId;
            const action = this.dataset.action;
            showAdminConfirm({
                title: 'Từ chối trả hàng',
                message: 'Xác nhận từ chối yêu cầu #' + maTraHang + '?',
                onConfirm: () => xuLyTraHang(maTraHang, action)
            });
        });
    });

    function xuLyTraHang(maTraHang, action) {
        fetch(window.contextPath + '/admin/tra-hang/duyet', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'maTraHang=' + encodeURIComponent(maTraHang) + '&action=' + encodeURIComponent(action)
        })
                .then(async res => {
                    const text = await res.text();
                    if (!res.ok)
                        throw new Error(text || 'Lỗi xử lý');
                    return text;
                })
                .then(msg => {
                    toastr.success(msg || 'Thao tác thành công');
                    setTimeout(() => window.location.reload(), 700);
                })
                .catch(err => toastr.error(err.message));
    }

    function showAdminConfirm(options) {
        const overlay = document.getElementById("adminConfirmOverlay");
        const titleEl = document.getElementById("adminConfirmTitle");
        const messageEl = document.getElementById("adminConfirmMessage");
        const cancelBtn = document.getElementById("adminConfirmCancel");
        const okBtn = document.getElementById("adminConfirmOk");

        if (!overlay || !titleEl || !messageEl || !cancelBtn || !okBtn) {
            if (options && typeof options.onConfirm === "function")
                options.onConfirm();
            return;
        }

        titleEl.textContent = options.title || "Xác nhận thao tác";
        messageEl.textContent = options.message || "Bạn có chắc muốn thực hiện thao tác này?";
        overlay.classList.add("open");

        const closeModal = function () {
            overlay.classList.remove("open");
            okBtn.onclick = null;
            cancelBtn.onclick = null;
            overlay.onclick = null;
        };

        cancelBtn.onclick = closeModal;
        overlay.onclick = function (e) {
            if (e.target === overlay)
                closeModal();
        };

        okBtn.onclick = function () {
            closeModal();
            if (typeof options.onConfirm === "function")
                options.onConfirm();
        };
    }
});