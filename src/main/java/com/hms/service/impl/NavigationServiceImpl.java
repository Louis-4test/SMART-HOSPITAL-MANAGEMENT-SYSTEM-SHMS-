package com.hms.service.impl;

import com.hms.service.NavigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
@Transactional
public class NavigationServiceImpl implements NavigationService {

    private final Deque<String> navigationHistory = new ArrayDeque<>();
    private static final Logger log = LoggerFactory.getLogger(NavigationServiceImpl.class);

    @Override
    public void addNavigation(String page) {
        log.info("Entering addNavigation({})", page);
        navigationHistory.push(page);
        log.debug("Exiting addNavigation: history size {}", navigationHistory.size());
    }

    @Override
    public String goBack() {
        log.info("Entering goBack()");
        if (navigationHistory.size() <= 1) {
            log.debug("Exiting goBack: no previous page");
            return null;
        }
        navigationHistory.pop();
        String current = navigationHistory.peek();
        log.debug("Exiting goBack: returning to {}", current);
        return current;
    }

    @Override
    public String peekCurrentPage() {
        return navigationHistory.peek();
    }

    @Override
    public Deque<String> getHistory() {
        return new ArrayDeque<>(navigationHistory);
    }

    @Override
    public boolean hasPrevious() {
        return navigationHistory.size() > 1;
    }
}
