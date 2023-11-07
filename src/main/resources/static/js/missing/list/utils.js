

  export const createFilterUrl = ({
        page, size, search, animalType, specifics, color, fromDate, endDate
    }) => {
            const dummyUrl = "http://www.dummy.com";
            const url = new URL(`${dummyUrl}/missing/list`);
            url.searchParams.append("page", page);
            url.searchParams.append("limit", size);
            if (search) url.searchParams.append("search", search);
            if (animalType) url.searchParams.append("animalType", animalType);
            if (specifics) url.searchParams.append("specifics", specifics);
            if (color) url.searchParams.append("color", color);
            if (fromDate) url.searchParams.append("fromDate", fromDate);
            if (endDate) url.searchParams.append("endDate", endDate);

            return `${url.pathname}${url.search}`;
          };