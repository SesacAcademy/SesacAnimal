document.addEventListener('DOMContentLoaded', function() {
    var deleteButtons = document.querySelectorAll('.image-delete-button');

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var imageId = this.getAttribute('value'); // imagePath.imageId 값을 가져옵니다.
            addDeletedId(imageId);
        });
    });
});

function addDeletedId(imageId) {
    var deletedIdsInput = document.querySelector('input[name="deletedIds"]');
    var currentIds = deletedIdsInput.value ? JSON.parse(deletedIdsInput.value) : [];

    if (!currentIds.includes(imageId)) {
        currentIds.push(imageId);
    }

    deletedIdsInput.value = JSON.stringify(currentIds); // input 필드를 업데이트합니다.
}