package vn.huythanh.springbootexercise.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private List<T> data;
}
