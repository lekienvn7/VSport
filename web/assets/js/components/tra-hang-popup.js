// /assets/js/components/tra-hang-popup.js
document.addEventListener('DOMContentLoaded', function () {
    (function () {
        const modal = document.getElementById('traHangModal');
        if (!modal || modal.dataset.initialized === 'true') return;
        modal.dataset.initialized = 'true';

        const closeBtn       = document.getElementById('closeTraHangModal');
        const cancelBtn      = document.getElementById('cancelTraHang');
        const form           = document.getElementById('traHangForm');
        const btnTaoYeuCau   = document.getElementById('btnTaoYeuCau');
        const maDonHangInput = document.getElementById('maDonHangTraHang');
        const lyDoSelect     = document.getElementById('lyDoTraHang');
        const soTienInput    = document.getElementById('soTienHoan');
        const soTienDisplay  = document.getElementById('soTienHoanDisplay');
        const ghiChuInput    = document.getElementById('ghiChuTraHang');

        if (!closeBtn || !cancelBtn || !form || !btnTaoYeuCau ||
            !maDonHangInput || !lyDoSelect || !soTienInput || !soTienDisplay || !ghiChuInput) {
            console.error('❌ Thiếu elements trong traHangModal');
            return;
        }

        // -------------------------------------------------------
        // Helpers
        // -------------------------------------------------------

        function formatCurrency(amount) {
            if (!amount) return '0 ₫';
            return new Intl.NumberFormat('vi-VN').format(amount) + ' ₫';
        }

        function setLoading(isLoading) {
            btnTaoYeuCau.disabled = isLoading;
            btnTaoYeuCau.textContent = isLoading ? 'Đang xử lý...' : 'Tạo yêu cầu';
        }

        function closeModal() {
            modal.classList.remove('show');
        }

        // -------------------------------------------------------
        // Validate
        // -------------------------------------------------------

        function checkFormValidity() {
            btnTaoYeuCau.disabled = !lyDoSelect.value;
        }

        lyDoSelect.addEventListener('change', checkFormValidity);

        // -------------------------------------------------------
        // Mở modal (gọi từ bên ngoài)
        // -------------------------------------------------------

        window.openTraHangModal = function (maDonHang, tongTien) {
            form.reset();
            maDonHangInput.value = maDonHang;
            soTienInput.value    = tongTien || 0;
            soTienDisplay.value  = formatCurrency(tongTien);
            btnTaoYeuCau.disabled = true;
            modal.classList.add('show');
        };

        // -------------------------------------------------------
        // Bắt click nút trả hàng (event delegation)
        // -------------------------------------------------------

        document.addEventListener('click', function (e) {
            const btn = e.target.closest('.btn-tra-hang');
            if (!btn) return;
            e.preventDefault();
            e.stopPropagation();
            const maDonHang = btn.getAttribute('data-ma-don-hang');
            const tongTien  = btn.getAttribute('data-tong-tien');
            if (maDonHang) window.openTraHangModal(maDonHang, tongTien);
        });

        // -------------------------------------------------------
        // Đóng modal
        // -------------------------------------------------------

        closeBtn.addEventListener('click', closeModal);
        cancelBtn.addEventListener('click', closeModal);
        modal.addEventListener('click', function (e) {
            if (e.target === modal) closeModal();
        });
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape' && modal.classList.contains('show')) closeModal();
        });

        // -------------------------------------------------------
        // Submit
        // -------------------------------------------------------

        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const lyDo = lyDoSelect.value;
            if (!lyDo) {
                toastr.warning('Vui lòng chọn lý do trả hàng.');
                return;
            }

            setLoading(true);

            const url = (window.contextPath || '') + '/tra-hang';

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: new URLSearchParams({
                    action    : 'yeu_cau',
                    maDonHang : maDonHangInput.value,
                    lyDo      : lyDo,
                    soTienHoan: soTienInput.value,
                    ghiChu    : ghiChuInput.value
                })
            })
            .then(function (res) {
                if (!res.ok) throw new Error('Lỗi máy chủ (HTTP ' + res.status + ')');
                return res.json();
            })
            .then(function (data) {
                if (data.success) {
                    toastr.success(data.message || 'Yêu cầu trả hàng đã được gửi!');
                    closeModal();
                    setTimeout(function () { location.reload(); }, 1500);
                } else {
                    toastr.error(data.message || 'Không thể tạo yêu cầu.');
                }
            })
            .catch(function (err) {
                console.error('Lỗi tra hàng:', err);
                toastr.error(err.message || 'Đã xảy ra lỗi không xác định.');
            })
            .finally(function () {
                setLoading(false);
            });
        });
    })();
});

// ================================================================
// Hàm dùng bên ADMIN để duyệt / từ chối (gọi từ trang admin)
// ================================================================

window.adminDuyetTraHang = function (maTraHang, callback) {
    _adminActionTraHang('duyet', maTraHang, callback);
};

window.adminTuChoiTraHang = function (maTraHang, callback) {
    _adminActionTraHang('tu_choi', maTraHang, callback);
};

function _adminActionTraHang(action, maTraHang, callback) {
    const url = (window.contextPath || '') + '/tra-hang';

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: new URLSearchParams({ action: action, maTraHang: maTraHang })
    })
    .then(function (res) {
        if (!res.ok) throw new Error('Lỗi máy chủ (HTTP ' + res.status + ')');
        return res.json();
    })
    .then(function (data) {
        if (data.success) {
            toastr.success(data.message);
            if (typeof callback === 'function') callback(true);
            else setTimeout(function () { location.reload(); }, 1200);
        } else {
            toastr.error(data.message || 'Thao tác thất bại');
            if (typeof callback === 'function') callback(false);
        }
    })
    .catch(function (err) {
        console.error('Admin tra hang error:', err);
        toastr.error(err.message || 'Lỗi không xác định');
        if (typeof callback === 'function') callback(false);
    });
}