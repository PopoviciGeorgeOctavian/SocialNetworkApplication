package com.socialnetwork.lab78.Paging;

public class Page<E> {
    private Iterable<E> elementsOnPage;

    private int totalElementCount;

    public Page(Iterable<E> elemntsOnPage, int totalElementCount)
    {
        this.elementsOnPage = elemntsOnPage;
        this.totalElementCount = totalElementCount;
    }

    public Iterable<E> getElementsOnPage(){
        return elementsOnPage;
    }

    public int getTotalElementCount() {return totalElementCount;}

}
