const recommentConatinerEdit = document.querySelectorAll('.missing-detail-recomment-card-container');

recommentConatinerEdit.forEach((dom) => {
    const editButton = dom.querySelector(".missing-comment-button-edit");
    const editForm = dom.querySelector(".missing-comment-edit-form");
    const content = dom.querySelector(".missing-comment-content");

    editButton.addEventListener('click', () => {
        const isContentShowing = content.style.display === "block";
        content.style.display = isContentShowing ? "none" : "block";
        editForm.style.display = isContentShowing ? "block" : "none";
    });

});

recommentConatinerEdit.forEach((dom) => {
    const recommentEditButton = dom.querySelector(".missing-recomment-button-edit");
    const recommentEditForm = dom.querySelector(".missing-recomment-edit-form");
    const recommentContent = dom.querySelector(".missing-recomment-content");

    recommentEditButton?.addEventListener('click', () => {
        const isContentShowing = recommentContent.style.display === "block";
        recommentContent.style.display = isContentShowing ? "none" : "block";
        recommentEditForm.style.display = isContentShowing ? "block" : "none";
    });
});