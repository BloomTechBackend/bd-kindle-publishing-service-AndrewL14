package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequests = new LinkedList<BookPublishRequest>();
    @Inject
    public BookPublishRequestManager() {
    }

    public void addBookPublishRequest(BookPublishRequest request) {
        bookPublishRequests.offer(request);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        if (!bookPublishRequests.isEmpty()) {
            return bookPublishRequests.poll();
        } else {
            return null;
        }
    }
}
