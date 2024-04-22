package org.example;

public interface Readable {
    public void readPages(int pg);
    public void markAsRead();
    public int getPagesRead();
    public boolean checkIsRead();

}
