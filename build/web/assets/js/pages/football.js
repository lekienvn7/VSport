document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".football-team-tab");
    const sliderWrappers = document.querySelectorAll(".football-team-slider-wrapper");

    tabs.forEach(tab => {
        tab.addEventListener("click", function () {
            const targetId = this.getAttribute("data-target");

            tabs.forEach(item => item.classList.remove("active"));
            sliderWrappers.forEach(item => item.classList.remove("active"));

            this.classList.add("active");

            const targetWrapper = document.getElementById(targetId);
            if (targetWrapper) {
                targetWrapper.classList.add("active");
            }
        });
    });

    const sliderButtons = document.querySelectorAll(".football-slider-btn");

    sliderButtons.forEach(button => {
        button.addEventListener("click", function () {
            const targetId = this.getAttribute("data-scroll-target");
            const target = document.getElementById(targetId);

            if (!target) return;

            const scrollAmount = 320;

            if (this.classList.contains("football-slider-btn-left")) {
                target.scrollBy({
                    left: -scrollAmount,
                    behavior: "smooth"
                });
            } else {
                target.scrollBy({
                    left: scrollAmount,
                    behavior: "smooth"
                });
            }
        });
    });
});