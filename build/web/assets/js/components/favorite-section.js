document.addEventListener("DOMContentLoaded", function () {
    const track = document.getElementById("favoriteTrack");
    const viewport = document.getElementById("favoriteViewport");
    const prevBtn = document.getElementById("favoritePrevBtn");
    const nextBtn = document.getElementById("favoriteNextBtn");
    const dotsWrap = document.getElementById("favoriteDots");

    if (!track || !viewport || !prevBtn || !nextBtn || !dotsWrap) {
        return;
    }

    const slides = Array.from(track.querySelectorAll(".home-favorite-slide"));
    if (slides.length === 0) {
        return;
    }

    let currentPage = 0;
    let slidesPerPage = getSlidesPerPage();
    let totalPages = calculateTotalPages();

    function getSlidesPerPage() {
        const width = window.innerWidth;

        if (width <= 640) return 1;
        if (width <= 992) return 2;
        if (width <= 1200) return 3;
        return 4;
    }

    function calculateTotalPages() {
        return Math.ceil(slides.length / slidesPerPage);
    }

    function updateSlider() {
        slidesPerPage = getSlidesPerPage();
        totalPages = calculateTotalPages();

        if (currentPage >= totalPages) {
            currentPage = totalPages - 1;
        }

        if (currentPage < 0) {
            currentPage = 0;
        }

        const offset = currentPage * viewport.clientWidth;
        track.style.transform = "translateX(-" + offset + "px)";

        renderDots();
        updateButtons();
    }

    function updateButtons() {
        prevBtn.disabled = currentPage === 0;
        nextBtn.disabled = currentPage >= totalPages - 1;
    }

    function renderDots() {
        dotsWrap.innerHTML = "";

        for (let i = 0; i < totalPages; i++) {
            const dot = document.createElement("button");
            dot.type = "button";
            dot.className = "home-favorite-dot" + (i === currentPage ? " active" : "");
            dot.setAttribute("aria-label", "Trang " + (i + 1));

            dot.addEventListener("click", function () {
                currentPage = i;
                updateSlider();
            });

            dotsWrap.appendChild(dot);
        }
    }

    prevBtn.addEventListener("click", function () {
        if (currentPage > 0) {
            currentPage--;
            updateSlider();
        }
    });

    nextBtn.addEventListener("click", function () {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateSlider();
        }
    });

    let resizeTimer;
    window.addEventListener("resize", function () {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            updateSlider();
        }, 120);
    });

    updateSlider();
});

document.addEventListener("DOMContentLoaded", function () {
    const favoriteSection = document.getElementById("favoriteSection");
    if (!favoriteSection) return;

    favoriteSection.classList.add("is-hidden");

    const observer = new IntersectionObserver(
        function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    favoriteSection.classList.add("is-visible");
                    favoriteSection.classList.remove("is-hidden");
                } else {
                    favoriteSection.classList.remove("is-visible");
                    favoriteSection.classList.add("is-hidden");
                }
            });
        },
        {
            threshold: 0.16,
            root: null,
            rootMargin: "0px 0px -8% 0px"
        }
    );

    observer.observe(favoriteSection);
});