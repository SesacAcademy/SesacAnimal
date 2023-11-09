 const formatDate = (javaDate) => {
    const date = new Date(javaDate);

    const day = ("0" + date.getDate()).slice(-2);
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();

    const formattedDate = year + '-' + month + '-' + day;
    return formattedDate;
};

const convertedDate = formatDate(_detail.missingTime);
const dateInput = document.getElementById("missing-edit-missingTime");
dateInput.value = convertedDate;


