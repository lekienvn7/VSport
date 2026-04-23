document.addEventListener("DOMContentLoaded", function () {
    const viewport = document.getElementById("collectionViewport");
    const track = document.getElementById("collectionTrack");
    const prevBtn = document.getElementById("collectionPrevBtn");
    const nextBtn = document.getElementById("collectionNextBtn");
    const cards = Array.from(document.querySelectorAll(".collection-showcase-card"));

    if (!viewport || !track || cards.length === 0) {
        return;
    }

    let currentIndex = 0;
    let visibleCount = getVisibleCount();

    function getVisibleCount() {
        if (window.innerWidth <= 640) return 1;
        if (window.innerWidth <= 992) return 2;
        if (window.innerWidth <= 1280) return 3;
        return 4;
    }

    function getCardWidthWithGap() {
        if (!cards[0]) return 0;
        const trackStyle = window.getComputedStyle(track);
        const cardWidth = cards[0].offsetWidth;
        const gap = parseFloat(trackStyle.columnGap || trackStyle.gap || 0);
        return cardWidth + gap;
    }

    function getMaxIndex() {
        return Math.max(0, cards.length - visibleCount);
    }

    function updateSliderPosition() {
        const moveX = currentIndex * getCardWidthWithGap();
        track.style.transform = `translateX(-${moveX}px)`;
        updateButtons();
    }

    function updateButtons() {
        const maxIndex = getMaxIndex();
        prevBtn.disabled = currentIndex <= 0;
        nextBtn.disabled = currentIndex >= maxIndex;
    }

    prevBtn.addEventListener("click", function () {
        if (currentIndex > 0) {
            currentIndex--;
            updateSliderPosition();
        }
    });

    nextBtn.addEventListener("click", function () {
        if (currentIndex < getMaxIndex()) {
            currentIndex++;
            updateSliderPosition();
        }
    });

    window.addEventListener("resize", function () {
        visibleCount = getVisibleCount();
        if (currentIndex > getMaxIndex()) {
            currentIndex = getMaxIndex();
        }
        updateSliderPosition();
    });

    const revealObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.classList.add("is-visible");
        } else {
            entry.target.classList.remove("is-visible");
        }
    });
}, {
    threshold: 0.12
});

    cards.forEach((card, index) => {
        card.style.transitionDelay = `${index * 0.05}s`;
        revealObserver.observe(card);
    });

    updateSliderPosition();
});