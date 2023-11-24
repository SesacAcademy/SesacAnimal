import { PAGE_RANGE } from './init.js'

  export const createFilterUrl = ({
        page, size, search, animalType, specifics, color, fromDate, endDate
    }) => {
            const href = document.getElementById("list-href").textContent;
            const dummyUrl = "http://www.dummy.com";
            const url = new URL(`${dummyUrl}${href}`);
            url.searchParams.append("page", page); // server page start from 0;
            url.searchParams.append("limit", size);
            if (search) url.searchParams.append("search", search);
            if (animalType) url.searchParams.append("animalType", animalType);
            if (specifics) url.searchParams.append("specifics", specifics);
            if (color) url.searchParams.append("color", color);
            if (fromDate) url.searchParams.append("fromDate", fromDate);
            if (endDate) url.searchParams.append("endDate", endDate);

            return `${url.pathname}${url.search}`;
          };




export const getAllPageNumber = (maxPage) => {
    const store = [];

    let temp = [];
    for (let i = 1; i <= maxPage; i++) {
        if (i !== 1 && i == maxPage) {
            temp.push(i);
            store.push(temp);
            break;
        } else if (i !== 1 && (i % PAGE_RANGE == 1)) {
            store.push(temp);
            temp = [];
        }

        temp.push(i);
    }

    return store;
};
