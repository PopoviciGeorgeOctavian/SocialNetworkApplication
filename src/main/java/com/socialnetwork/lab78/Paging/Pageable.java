// Pageable class representing pagination information
package com.socialnetwork.lab78.Paging;

/**
 * Represents pagination information such as page number and page size.
 */
public class Pageable {
    private int pageNumber;
    private int pageSize;

    /**
     * Constructs a new Pageable with the given page number and page size.
     *
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     */
    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Gets the page number.
     *
     * @return The page number.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Gets the page size.
     *
     * @return The page size.
     */
    public int getPageSize() {
        return pageSize;
    }
}
