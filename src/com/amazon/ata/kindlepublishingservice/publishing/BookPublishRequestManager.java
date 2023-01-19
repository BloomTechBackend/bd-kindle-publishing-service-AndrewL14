package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookPublishRequestManager {
    private static Queue<BookPublishRequest> bookPublishRequests = new ConcurrentLinkedQueue<BookPublishRequest>();
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

    public Queue<BookPublishRequest> getAllBookPublishRequestToPublish() {
        Queue<BookPublishRequest> copyQueue = new ConcurrentLinkedQueue<>(bookPublishRequests);
        return copyQueue;
    }
}
