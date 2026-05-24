/* ============================================================
 dat-san.js — Main JavaScript
 ============================================================ */

const DatSan = (() => {

    /* ---- State ---- */
    let currentSubSanId = null;
    let currentDate = null;
    let selectedSlots = [];   // array of {id, tenKhung, gioBatDau, gioKetThuc}
    let lichSanData = {};      // {date: [LichSan]}
    let extraSlotIds = [];     // additional checked slot ids

    const ctx = document.body.dataset.ctx || '';

    /* ---- Init ---- */
    function init() {
        const page = document.body.dataset.page;
        if (page === 'san-detail')
            initSanDetail();
        if (page === 'dat-san')
            initDatSan();
        if (page === 'admin')
            initAdmin();
        if (page === 'home')
            initHome();
        initNav();
    }

    /* ---- Navbar mobile toggle ---- */
    function initNav() {
        const toggle = document.getElementById('nav-toggle');
        const nav = document.getElementById('navbar-nav');
        if (toggle && nav) {
            toggle.addEventListener('click', () => nav.classList.toggle('open'));
        }
    }

    /* ---- HOME ---- */
    function initHome() {
        // Simple card hover 3d tilt
        document.querySelectorAll('.san-card').forEach(card => {
            card.addEventListener('mousemove', e => {
                const rect = card.getBoundingClientRect();
                const x = (e.clientX - rect.left) / rect.width - 0.5;
                const y = (e.clientY - rect.top) / rect.height - 0.5;
                card.style.transform = `perspective(600px) rotateY(${x * 8}deg) rotateX(${-y * 8}deg) translateY(-4px)`;
            });
            card.addEventListener('mouseleave', () => {
                card.style.transform = '';
            });
        });
    }

    /* ---- SAN DETAIL ---- */
    function initSanDetail() {
        // Sub-san tabs
        document.querySelectorAll('.subsan-tab').forEach(tab => {
            tab.addEventListener('click', () => {
                document.querySelectorAll('.subsan-tab').forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                const subSanId = parseInt(tab.dataset.subSanId);
                showSubSanSection(subSanId);
            });
        });

        // Select first tab
        const firstTab = document.querySelector('.subsan-tab');
        if (firstTab)
            firstTab.click();
    }

    function showSubSanSection(subSanId) {
        currentSubSanId = subSanId;
        selectedSlots = [];
        extraSlotIds = [];

        document.querySelectorAll('.subsan-section').forEach(s => {
            s.style.display = s.dataset.subSanId == subSanId ? 'block' : 'none';
        });

        // Load schedule via API if not cached
        if (!lichSanData[subSanId]) {
            loadLichSan(subSanId);
        } else {
            renderSchedule(subSanId);
        }
    }

    function loadLichSan(subSanId) {
        const container = document.querySelector(`.schedule-container[data-sub-san-id="${subSanId}"]`);
        if (!container)
            return;
        container.innerHTML = '<div class="loading"><div class="spinner"></div> Đang tải lịch sân...</div>';

        fetch(`${ctx}/api/lich-san?subSanId=${subSanId}`)
                .then(r => r.json())
                .then(data => {
                    lichSanData[subSanId] = data;
                    renderSchedule(subSanId);
                })
                .catch(() => {
                    container.innerHTML = '<div class="alert alert-danger">Lỗi tải lịch sân. Vui lòng thử lại.</div>';
                });
    }

    function renderSchedule(subSanId) {
        const container = document.querySelector(`.schedule-container[data-sub-san-id="${subSanId}"]`);
        if (!container)
            return;

        const data = lichSanData[subSanId];
        const dates = Object.keys(data).sort();

        if (!dates.length) {
            container.innerHTML = '<div class="alert alert-info">Chưa có lịch sân trong tuần này.</div>';
            return;
        }

        // Build tabs
        let html = '<div class="week-tabs">';
        dates.forEach((d, i) => {
            const label = formatDate(d);
            html += `<button class="week-tab${i === 0 ? ' active' : ''}" data-date="${d}" onclick="DatSan.selectDate('${subSanId}','${d}')">${label}</button>`;
        });
        html += '</div>';
        html += `<div class="slot-container" id="slots-${subSanId}"></div>`;
        container.innerHTML = html;

        // Show first date
        currentDate = dates[0];
        renderSlots(subSanId, dates[0]);
    }

    window.DatSan = window.DatSan || {};

    function selectDate(subSanId, date) {
        currentDate = date;
        document.querySelectorAll(`.week-tabs .week-tab`).forEach(t => {
            t.classList.toggle('active', t.dataset.date === date);
        });
        renderSlots(subSanId, date);
    }

    function renderSlots(subSanId, date) {
        const container = document.getElementById(`slots-${subSanId}`);
        if (!container)
            return;
        const slots = (lichSanData[subSanId] && lichSanData[subSanId][date]) || [];

        let html = '<div class="slot-grid">';
        slots.forEach(slot => {
            const isTrong = slot.trangThai === 'trong';
            const isSelected = selectedSlots.some(s => s.id === slot.id);
            let cls = 'slot-item ' + (isTrong ? 'trong' : (slot.trangThai === 'da_dat' ? 'da-dat' : 'dang-xu-ly'));
            if (isSelected)
                cls += ' selected';

            html += `<div class="${cls}" ${isTrong ? `onclick="DatSan.toggleSlot(${subSanId},'${date}',${slot.id},'${slot.gioBatDau}','${slot.gioKetThuc}','${slot.tenKhung}')"` : ''} data-id="${slot.id}">
        <div class="slot-time">${slot.gioBatDau} - ${slot.gioKetThuc}</div>
        <div class="slot-status">
          ${isTrong ? '<span class="badge badge-trong">Còn trống</span>' : '<span class="badge badge-het">Đã đặt</span>'}
        </div>
      </div>`;
        });
        html += '</div>';

        // Selected summary
        if (selectedSlots.length > 0) {
            html += renderSelectedSummary(subSanId);
        }
        container.innerHTML = html;
    }

    function toggleSlot(subSanId, date, id, start, end, tenKhung) {
        const idx = selectedSlots.findIndex(s => s.id === id);
        if (idx >= 0) {
            selectedSlots.splice(idx, 1);
        } else {
            // Only allow slots on same date
            selectedSlots = selectedSlots.filter(s => s.date === date);
            selectedSlots.push({id, date, gioBatDau: start, gioKetThuc: end, tenKhung, subSanId});
        }
        renderSlots(subSanId, date);

        // Load next available slots
        if (selectedSlots.length > 0) {
            loadNextSlots(subSanId, selectedSlots[selectedSlots.length - 1].id);
        }
    }

    function loadNextSlots(subSanId, lichSanId) {
        fetch(`${ctx}/api/next-slots?subSanId=${subSanId}&lichSanId=${lichSanId}`)
                .then(r => r.json())
                .then(slots => {
                    renderNextSlots(subSanId, slots);
                });
    }

    function renderNextSlots(subSanId, slots) {
        let panel = document.getElementById(`next-slots-${subSanId}`);
        if (!panel) {
            panel = document.createElement('div');
            panel.id = `next-slots-${subSanId}`;
            panel.className = 'next-slots-panel';
            const container = document.getElementById(`slots-${subSanId}`);
            if (container)
                container.appendChild(panel);
        }
        if (!slots || !slots.length) {
            panel.innerHTML = '';
            return;
        }

        let html = '<h4>⚡ Khung giờ trống tiếp theo</h4><p style="font-size:13px;color:var(--text-normal);margin-bottom:12px">Tích để đặt thêm khung giờ:</p>';
        slots.forEach(s => {
            html += `<div class="extra-slot-item">
        <input type="checkbox" id="extra-${s.id}" value="${s.id}" onchange="DatSan.toggleExtra(${s.id})">
        <label for="extra-${s.id}" style="color:var(--text-heading);cursor:pointer;font-size:14px">
          ${s.gioBatDau} – ${s.gioKetThuc}
          <span class="badge badge-trong" style="margin-left:6px">Còn trống</span>
        </label>
      </div>`;
        });
        panel.innerHTML = html;
    }

    function toggleExtra(slotId) {
        const cb = document.getElementById(`extra-${slotId}`);
        if (cb.checked) {
            if (!extraSlotIds.includes(slotId))
                extraSlotIds.push(slotId);
        } else {
            extraSlotIds = extraSlotIds.filter(id => id !== slotId);
        }
    }

    function renderSelectedSummary(subSanId) {
        if (!selectedSlots.length)
            return '';
        const gia = parseFloat(document.querySelector(`[data-sub-san-id="${subSanId}"]`)?.dataset.gia || 0);
        const tongTien = gia * selectedSlots.length;
        return `<div class="card" style="margin-top:16px;background:rgba(0,229,190,0.05);border-color:rgba(0,229,190,0.3)">
      <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:12px">
        <div>
          <div style="font-size:13px;color:var(--text-normal);margin-bottom:4px">Đã chọn ${selectedSlots.length} khung giờ</div>
          <div style="font-size:13px;color:var(--text-heading)">${selectedSlots.map(s => s.gioBatDau + '–' + s.gioKetThuc).join(', ')}</div>
        </div>
        <div style="text-align:right">
          <div class="price">${formatMoney(tongTien)}</div>
          <button class="btn btn-primary" style="margin-top:8px" onclick="DatSan.openBookingModal(${subSanId})">
            ⚽ Đặt sân ngay
          </button>
        </div>
      </div>
    </div>`;
    }

    /* ---- Booking Modal ---- */
    function openBookingModal(subSanId) {
        const allIds = [...selectedSlots.map(s => s.id), ...extraSlotIds];
        if (!allIds.length) {
            alert('Vui lòng chọn ít nhất 1 khung giờ');
            return;
        }

        // Build form
        const modal = document.getElementById('booking-modal');
        if (!modal)
            return;

        document.getElementById('bm-subSanId').value = subSanId;
        document.getElementById('bm-lichSanIds').value = allIds.join(',');

        const gia = parseFloat(document.querySelector(`[data-sub-san-id="${subSanId}"]`)?.dataset.gia || 0);
        document.getElementById('bm-summary').innerHTML =
                `<strong style="color:var(--text-heading)">${allIds.length} khung giờ</strong> · ` +
                `<strong class="price">${formatMoney(gia * allIds.length)}</strong>`;

        document.getElementById('booking-modal-backdrop').classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    function closeBookingModal() {
        document.getElementById('booking-modal-backdrop')?.classList.remove('show');
        document.body.style.overflow = '';
    }

    function submitBooking() {
        const form = document.getElementById('booking-form');
        const subSanId = document.getElementById('bm-subSanId').value;
        const lichSanIdsStr = document.getElementById('bm-lichSanIds').value;
        const tenKhach = document.getElementById('bm-tenKhach').value.trim();
        const sdt = document.getElementById('bm-sdt').value.trim();

        if (!tenKhach || !sdt) {
            alert('Vui lòng nhập đầy đủ họ tên và số điện thoại');
            return;
        }

        // Build and submit form
        const f = document.createElement('form');
        f.method = 'POST';
        f.action = ctx + '/dat-san';
        f.style.display = 'none';

        const addField = (name, value) => {
            const inp = document.createElement('input');
            inp.type = 'hidden';
            inp.name = name;
            inp.value = value;
            f.appendChild(inp);
        };

        addField('action', 'submit');
        addField('subSanId', subSanId);
        addField('tenKhach', tenKhach);
        addField('soDienThoai', sdt);
        addField('email', document.getElementById('bm-email').value);
        addField('ghiChu', document.getElementById('bm-ghiChu').value);

        lichSanIdsStr.split(',').forEach(id => addField('lichSanIds', id));

        document.body.appendChild(f);
        f.submit();
    }

    /* ---- DAT SAN PAGE (Confirm/Receipt) ---- */
    function initDatSan() {
        // Nothing extra needed - handled server-side
    }

    /* ---- ADMIN ---- */
    function initAdmin() {
        // Tab switching
        document.querySelectorAll('.admin-tab').forEach(tab => {
            tab.addEventListener('click', () => {
                document.querySelectorAll('.admin-tab').forEach(t => t.classList.remove('active'));
                document.querySelectorAll('.admin-panel').forEach(p => p.classList.remove('active'));
                tab.classList.add('active');
                document.getElementById(tab.dataset.target)?.classList.add('active');
            });
        });
    }

    function adminAction(datSanId, action) {
        if (!confirm(action === 'duyet' ? 'Xác nhận duyệt đặt sân này?' : 'Xác nhận từ chối đặt sân này?'))
            return;
        const f = document.createElement('form');
        f.method = 'POST';
        f.action = ctx + '/admin';
        const addHidden = (n, v) => {
            const i = document.createElement('input');
            i.type = 'hidden';
            i.name = n;
            i.value = v;
            f.appendChild(i);
        };
        addHidden('action', action);
        addHidden('datSanId', datSanId);
        document.body.appendChild(f);
        f.submit();
    }

    /* ---- UTILITIES ---- */
    function formatMoney(n) {
        return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(n);
    }

    function formatDate(dateStr) {
        const d = new Date(dateStr);
        const days = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
        return `${days[d.getDay()]} ${d.getDate()}/${d.getMonth() + 1}`;
    }

    // Expose public methods
    return {
        init,
        selectDate,
        toggleSlot,
        toggleExtra,
        openBookingModal,
        closeBookingModal,
        submitBooking,
        adminAction,
        formatMoney,
        formatDate
    };

})();

document.addEventListener('DOMContentLoaded', DatSan.init);