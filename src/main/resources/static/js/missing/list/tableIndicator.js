import { page, size, maxPageCount, search, animalType, specifics, color, fromDate, endDate, PAGE_RANGE } from './init.js';
import { createFilterUrl, getAllPageNumber } from './utils.js';


let pageNumbers = getAllPageNumber(maxPageCount);

const createEachPage = (url, text, isSelected) => {
  const component = document.createElement("a");
  component.setAttribute("href", url);
  component.innerText = text;
  component.style.width = "30px";
  component.style.textAlign = "center";
  component.style.fontWeight = isSelected ? "bold" : "light";

  return component;
};

const drawIndicator = (pages, currentPage) => {
    const container = document.querySelector(".page-container");
    const tags = pages.map((page) => {
    const url = createFilterUrl({ page, size, search, animalType, specifics, color, fromDate, endDate })
    const component = createEachPage(url, page, page == currentPage);
    return component;
  });

  tags.forEach((tag) => {
    container.appendChild(tag);
  });
};

function handleDomContentLoaded(page = 1) {
  const targetGroup = pageNumbers.find((numbers) => numbers.includes(page));
  drawIndicator(targetGroup, page);
}

// 실행
     document.addEventListener("DOMContentLoaded", (event) => {
          console.log(getAllPageNumber(maxPageCount))
          handleDomContentLoaded(page);
        });

          document.querySelector(".prev").addEventListener("click", () => {
            const prevPage = page - 1;
            if (prevPage > 0) {
              window.location.href = createFilterUrl({ page: prevPage, size, search, animalType, specifics, color, fromDate, endDate });
            } else {
              alert("It is the last page");
            }
        });

        document.querySelector(".next").addEventListener("click", () => {
            const nextPage = page + 1;
            if (nextPage <= maxPageCount) {
              window.location.href = createFilterUrl({ page: nextPage, size, search, animalType, specifics, color, fromDate, endDate });
            } else {
              alert("It is the last page");
            }
        });
