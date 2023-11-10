const addImageButton = document.querySelector(".missing-add-image-button");
const imageBox = document.querySelector(".missing-new-image-box");

addImageButton.addEventListener("click", () => {

    const input = document.createElement('input');
    input.type = 'file';
    input.name = 'images';

    imageBox.appendChild(input);
})