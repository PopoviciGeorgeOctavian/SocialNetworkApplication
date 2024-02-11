package com.socialnetwork.lab78.Paging;
// Page class representing a page of elements with total count

/**
 * Represents a page of elements with total count.
 *
 * @param <E> The type of elements in the page.
 */
public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalElementCount;

    /**
     * Constructs a new Page with the given elements and total element count.
     *
     * @param elementsOnPage    The elements present on the current page.
     * @param totalElementCount The total count of elements across all pages.
     */
    public Page(Iterable<E> elementsOnPage, int totalElementCount) {
        this.elementsOnPage = elementsOnPage;
        this.totalElementCount = totalElementCount;
    }

    /**
     * Gets the iterable collection of elements on the current page.
     *
     * @return The iterable collection of elements on the current page.
     */
    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }

    /**
     * Gets the total count of elements across all pages.
     *
     * @return The total count of elements across all pages.
     */
    public int getTotalElementCount() {
        return totalElementCount;
    }
}
