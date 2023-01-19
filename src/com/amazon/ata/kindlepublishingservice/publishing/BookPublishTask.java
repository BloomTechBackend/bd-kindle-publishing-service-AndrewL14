package com.amazon.ata.kindlepublishingservice.publishing;


import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;
import java.util.List;

public class BookPublishTask implements Runnable {
    private final BookPublishRequestManager requestManager;
    private final CatalogDao catalogDao;
    private final PublishingStatusDao publishingStatusDao;
    @Inject
    public BookPublishTask(BookPublishRequestManager manager, CatalogDao dao,
                            PublishingStatusDao publishingStatusDao) {
        this.requestManager = manager;
        this.catalogDao = dao;
        this.publishingStatusDao = publishingStatusDao;
    }
    @Override
    public void run() {
        BookPublishRequest request = requestManager.getBookPublishRequestToProcess();
        int i = 0;
        while (request == null && i < 500) {
            try {
                Thread.sleep(1000);
                request = requestManager.getBookPublishRequestToProcess();
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS, request.getBookId());
        KindleFormattedBook formattedBook = KindleFormatConverter.format(request);
        try {
            CatalogItemVersion book = catalogDao.createOrUpdateBook(formattedBook);
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, book.getBookId());
        } catch (Exception e) {
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId());
        }
    }
}
