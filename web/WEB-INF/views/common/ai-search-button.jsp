<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    .ai-search-fab {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 9999;
        width: 56px;
        height: 56px;
        border-radius: 50%;
        background: var(--bg-button);
        box-shadow: 0 4px 15px rgba(0,180,255,0.4);
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: transform 0.3s, box-shadow 0.3s;
        color: #fff;
    }
    .ai-search-fab:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 20px rgba(0,180,255,0.6);
    }

    .ai-popup-overlay {
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0,0,0,0.7);
        backdrop-filter: blur(6px);
        z-index: 10000;
        align-items: center;
        justify-content: center;
    }
    .ai-popup-overlay.show {
        display: flex;
    }

    .ai-popup {
        background: var(--bg-second);
        border: 1px solid var(--border-card-color);
        border-radius: var(--radius-lg);
        padding: var(--space-5);
        max-width: 500px;
        width: 90%;
        box-shadow: 0 25px 70px rgba(0,0,0,0.6);
        color: var(--text-heading);
        font-family: var(--font-body);
        position: relative;
    }

    .ai-popup h2 {
        font-family: var(--font-heading);
        font-size: var(--h2-font-size);
        margin-bottom: var(--space-3);
        text-align: center;
    }

    .drop-zone {
        border: 2px dashed rgba(255,255,255,0.2);
        border-radius: var(--radius-md);
        padding: var(--space-5);
        text-align: center;
        transition: background 0.3s, border-color 0.3s;
        cursor: pointer;
        margin-bottom: var(--space-3);
        position: relative;
        overflow: hidden;
    }

    .drop-zone.drag-over {
        border-color: var(--border-card-color);
        background: rgba(25,171,244,0.1);
    }

    .drop-zone img {
        max-width: 100%;
        max-height: 200px;
        display: none;
        margin: 0 auto;
        border-radius: var(--radius-sm);
    }

    .drop-zone p {
        margin: 0;
        color: var(--text-normal);
    }

    .file-input {
        display: none;
    }

    .btn-row {
        display: flex;
        gap: var(--space-2);
        justify-content: center;
        margin-top: var(--space-3);
    }

    .btn {
        padding: var(--space-2) var(--space-4);
        border-radius: var(--radius-sm);
        border: none;
        font-weight: var(--fw-semibold);
        cursor: pointer;
        transition: background 0.3s;
        font-family: var(--font-body);
        font-size: var(--text-base);
    }

    .btn-primary {
        background: var(--bg-button);
        color: #fff;
    }
    .btn-primary:disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }

    .btn-secondary {
        background: transparent;
        border: 1px solid rgba(255,255,255,0.2);
        color: var(--text-heading);
    }

    .close-btn {
        position: absolute;
        top: 10px;
        right: 10px;
        background: none;
        border: none;
        color: var(--text-heading);
        font-size: 1.5rem;
        cursor: pointer;
    }

    #aiStatusText {
        text-align: center;
        margin-top: 8px;
        color: var(--text-normal);
        font-size: var(--caption-font-size);
    }

    .loading-spinner {
        display: inline-block;
        width: 20px;
        height: 20px;
        border: 2px solid rgba(255,255,255,0.3);
        border-top-color: #fff;
        border-radius: 50%;
        animation: spin 0.8s linear infinite;
    }
    @keyframes spin { to { transform: rotate(360deg); } }
</style>

<!-- Nút AI -->
<div class="ai-search-fab" id="aiSearchFab">
    <i data-lucide="sparkles"></i>
</div>

<!-- Overlay popup -->
<div class="ai-popup-overlay" id="aiPopupOverlay">
    <div class="ai-popup">
        <button class="close-btn" id="closeAiPopup">&times;</button>
        <h2>Tìm kiếm bằng hình ảnh AI</h2>
        <div class="drop-zone" id="dropZone">
            <p>Kéo thả ảnh vào đây<br>hoặc click để chọn</p>
            <img id="previewImage" alt="Preview">
        </div>
        <input type="file" class="file-input" id="fileInput" accept="image/jpeg,image/png,image/webp">
        <p id="aiStatusText"></p>
        <div class="btn-row">
            <button class="btn btn-secondary" id="cancelAiBtn">Hủy</button>
            <button class="btn btn-primary" id="submitAiBtn" disabled>Tìm kiếm</button>
        </div>
    </div>
</div>

<script>
(function() {
    // ==================== API CONFIG ====================
    const ROBOFLOW_API_KEY = 'W213sLhSgw6nbwOjxtf0'; // Thay bằng API key thực tế
    const WORKFLOW_URL = 'https://detect.roboflow.com/infer/workflows/ls-workspace-rcwad/football-jersey-recognition-api-1779651254921';

    document.addEventListener('DOMContentLoaded', function() {
        const fab = document.getElementById('aiSearchFab');
        const overlay = document.getElementById('aiPopupOverlay');
        const closeBtn = document.getElementById('closeAiPopup');
        const cancelBtn = document.getElementById('cancelAiBtn');
        const submitBtn = document.getElementById('submitAiBtn');
        const dropZone = document.getElementById('dropZone');
        const fileInput = document.getElementById('fileInput');
        const previewImage = document.getElementById('previewImage');
        const statusText = document.getElementById('aiStatusText');

        let selectedFile = null;

        function openPopup() {
            overlay.classList.add('show');
            resetForm();
        }

        function closePopup() {
            overlay.classList.remove('show');
        }

        function resetForm() {
            fileInput.value = '';
            previewImage.style.display = 'none';
            previewImage.src = '';
            statusText.innerHTML = '';
            selectedFile = null;
            submitBtn.disabled = true;
            dropZone.classList.remove('drag-over');
        }

        fab.addEventListener('click', openPopup);
        closeBtn.addEventListener('click', closePopup);
        cancelBtn.addEventListener('click', closePopup);
        overlay.addEventListener('click', function(e) {
            if (e.target === overlay) closePopup();
        });

        // ==================== KÉO THẢ & CHỌN FILE ====================
        dropZone.addEventListener('dragover', function(e) {
            e.preventDefault();
            dropZone.classList.add('drag-over');
        });
        dropZone.addEventListener('dragleave', function(e) {
            e.preventDefault();
            dropZone.classList.remove('drag-over');
        });
        dropZone.addEventListener('drop', function(e) {
            e.preventDefault();
            dropZone.classList.remove('drag-over');
            const files = e.dataTransfer.files;
            if (files.length > 0) handleFile(files[0]);
        });
        dropZone.addEventListener('click', () => fileInput.click());
        fileInput.addEventListener('change', function(e) {
            if (e.target.files.length > 0) handleFile(e.target.files[0]);
        });

        function handleFile(file) {
            if (!file.type.match(/image\/(jpeg|png|webp|avif)/)) {
                toastr.warning('Chỉ chấp nhận file JPG, PNG, WEBP, AVIF');
                return;
            }

            selectedFile = file;
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImage.src = e.target.result;
                previewImage.style.display = 'block';
                submitBtn.disabled = false;
                statusText.textContent = 'Ảnh đã sẵn sàng. Nhấn "Tìm kiếm" để gửi.';
            };
            reader.readAsDataURL(file);
        }

        // ==================== GỬI ẢNH LÊN ROBOFLOW WORKFLOW ====================
        async function analyzeJersey(file) {
            statusText.innerHTML = '<span class="loading-spinner"></span> Đang xử lý...';
            submitBtn.disabled = true;

            try {
                const base64 = await fileToBase64(file);
                const cleanBase64 = base64.split(',')[1];

                const response = await fetch(WORKFLOW_URL, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        api_key: ROBOFLOW_API_KEY,
                        inputs: {
                            image: { type: 'base64', value: cleanBase64 }
                        }
                    })
                });

                if (!response.ok) {
                    throw new Error(`API Error: ${response.status}`);
                }

                const data = await response.json();
                console.log('Roboflow raw response:', data);

                // Hỗ trợ cả hai dạng: mảng trực tiếp hoặc { outputs: [...] }
                let outputs = [];
                if (Array.isArray(data)) {
                    outputs = data;
                } else if (data.outputs && Array.isArray(data.outputs)) {
                    outputs = data.outputs;
                }

                if (outputs.length === 0) {
                    throw new Error('Không có kết quả từ AI');
                }

                // Lấy phần tử đầu tiên
                const firstOutput = outputs[0];

                // Trích xuất giá trị, xử lý cả dạng string trực tiếp hoặc object { value: ... }
                function extractValue(field) {
                    if (typeof firstOutput[field] === 'object' && firstOutput[field] !== null) {
                        return firstOutput[field].value ?? String(firstOutput[field]);
                    }
                    return firstOutput[field] ?? '';
                }

                const team = String(extractValue('team') || '').trim();
                const brand = String(extractValue('brand') || '').trim();
                const jerseyTypeVi = String(extractValue('jersey_type_vi') || '').trim();

                console.log('Extracted:', { team, brand, jerseyTypeVi });

                // Tạo search query
                const searchQuery = [team, brand, jerseyTypeVi]
                    .filter(v => v && v !== 'unknown')
                    .join(' ');

                if (searchQuery) {
                    closePopup();
                    const searchOpenBtn = document.getElementById('openSearchPopup');
                    const searchInput = document.getElementById('searchInputPopup');
                    if (searchOpenBtn && searchInput) {
                        searchInput.value = searchQuery;
                        searchOpenBtn.click();
                        searchInput.dispatchEvent(new Event('input', { bubbles: true }));
                    } else {
                        toastr.error('Không tìm thấy popup tìm kiếm trên trang');
                    }
                } else {
                    toastr.warning('Không trích xuất được thông tin từ ảnh');
                }
            } catch (err) {
                console.error(err);
                statusText.textContent = 'Có lỗi xảy ra. Vui lòng thử lại.';
                toastr.error(err.message || 'Lỗi khi xử lý ảnh');
                submitBtn.disabled = false;
            }
        }

        function fileToBase64(file) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = () => resolve(reader.result);
                reader.onerror = reject;
                reader.readAsDataURL(file);
            });
        }

        submitBtn.addEventListener('click', function() {
            if (!selectedFile) return;
            analyzeJersey(selectedFile);
        });
    });
})();
</script>