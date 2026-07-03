package com.hms.service;

import java.util.Deque;

public interface NavigationService {
    void addNavigation(String page);
    String goBack();
    String peekCurrentPage();
    Deque<String> getHistory();
    boolean hasPrevious();
}
