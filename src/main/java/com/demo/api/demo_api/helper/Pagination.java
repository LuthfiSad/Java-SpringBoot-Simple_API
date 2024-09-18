package com.demo.api.demo_api.helper;

import java.util.List;

import lombok.Data;

@Data
public class Pagination<T> {
    private List<T> data;
    private Meta meta;

    public Pagination(List<T> data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    @Data
    public static class Meta {
        private int perPage;
        private int page;
        private int totalPages;
        private long totalData;

        public Meta(int perPage, int page, int totalPages, long totalData) {
            this.perPage = perPage;
            this.page = page;
            this.totalPages = totalPages;
            this.totalData = totalData;
        }
    }
}
