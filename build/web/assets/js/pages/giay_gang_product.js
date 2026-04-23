document.addEventListener("DOMContentLoaded", function () {
    const wrapper = document.getElementById("giayGangLinksWrapper");
    const btnLeft = document.getElementById("giayGangScrollLeft");
    const btnRight = document.getElementById("giayGangScrollRight");

    if (!wrapper || !btnLeft || !btnRight) return;

    const scrollAmount = 320;

    function updateButtonState() {
        const maxScrollLeft = wrapper.scrollWidth - wrapper.clientWidth;

        btnLeft.style.pointerEvents = wrapper.scrollLeft > 8 ? "auto" : "none";
        btnLeft.style.opacity = wrapper.scrollLeft > 8 ? "1" : "0.35";

        btnRight.style.pointerEvents = wrapper.scrollLeft < maxScrollLeft - 8 ? "auto" : "none";
        btnRight.style.opacity = wrapper.scrollLeft < maxScrollLeft - 8 ? "1" : "0.35";
    }

    btnLeft.addEventListener("click", function () {
        wrapper.scrollBy({
            left: -scrollAmount,
            behavior: "smooth"
        });
    });

    btnRight.addEventListener("click", function () {
        wrapper.scrollBy({
            left: scrollAmount,
            behavior: "smooth"
        });
    });

    wrapper.addEventListener("scroll", updateButtonState);
    window.addEventListener("resize", updateButtonState);

    updateButtonState();
});