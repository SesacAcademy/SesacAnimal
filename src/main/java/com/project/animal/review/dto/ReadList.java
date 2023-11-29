package com.project.animal.review.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Log4j2
public class readList {
    private List<ReviewPostAllDto> list;
    private int totalPages;
    private int currentPage;
    private final int maxPage = 10;
}
