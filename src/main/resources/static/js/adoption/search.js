document.addEventListener("DOMContentLoaded", function() {
    // 검색 버튼 클릭 이벤트 처리
    document.getElementById("search-button").addEventListener("click", function() {
        performSearch();
    });

    //검색 input에서 Enter 키를 누를 때 검색 수행
    document.getElementById("search-input").addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            performSearch();
        }
    });

    function performSearch() {
        var searchKeyword = document.getElementById("search-input").value.toLowerCase();
        var listItems = document.querySelectorAll("#adoption-list li");

        listItems.forEach(function(item) {
            var titleElement = item.querySelector(".content-area p:first-child span");
            var title = titleElement.textContent.toLowerCase();

            if (title.includes(searchKeyword)) {
                item.style.display = "block";
            } else {
                item.style.display = "none";
            }
        });
    }

    // select 요소 변경 이벤트 처리
    document.getElementById("center-filter").addEventListener("change", function() {
        performSelectSearchAndFilter();
    });

    function performSelectSearchAndFilter() {
        var centerFilter = document.getElementById("center-filter").value;
        var listItems = document.querySelectorAll("#adoption-list li");

        listItems.forEach(function(item) {
            var noticeElement = item.querySelector("#noticeEdt");
            var infoAreaElements = item.querySelectorAll(".info-area p");

            var hasNotice = false;
            var hasSpecialMark = false;

            infoAreaElements.forEach(function(infoElement) {
                var text = infoElement.textContent.trim();

                if (text.startsWith("공고종료일")) {
                    hasNotice = true;
                }

                if (text.startsWith("특이사항")) {
                    hasSpecialMark = true;
                }
            });

            if (centerFilter === "Y" && hasNotice) {
                item.style.display = "block";
            } else if (centerFilter === "N" && hasSpecialMark) {
                item.style.display = "block";
            } else if (centerFilter === "") {
                item.style.display = "block";
            } else {
                item.style.display = "none";
            }
        });
    }


});





