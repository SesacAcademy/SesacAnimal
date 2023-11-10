const recommentConatiner = document.querySelectorAll('.missing-detail-recomment-card-container');

recommentConatiner.forEach((dom) => {
    const showMoreButton = dom.querySelector(".show-more-recomment");
    const recommentContainer = dom.querySelector(".missing-recomment-card-container");

    showMoreButton?.addEventListener("click", () => {
        const currentDisplay = recommentContainer.style.display;
        recommentContainer.style.display = currentDisplay === "block" ? "none" : "block";
    });
});