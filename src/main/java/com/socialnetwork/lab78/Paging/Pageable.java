package com.socialnetwork.lab78.Paging;

public class Pageable {
    private int pageNumber;

    private int pageSize;

    public Pageable(int pageNumber, int pageSize)
    {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() { return pageNumber; }

    public int getPageSize() { return pageSize; }
}
