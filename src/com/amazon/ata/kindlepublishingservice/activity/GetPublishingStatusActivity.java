package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {
    private PublishingStatusDao publishingStatusDao;
    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        String publishingRecordId = publishingStatusRequest.getPublishingRecordId();
        List<PublishingStatusRecord> records = new ArrayList<>();
        List<PublishingStatusItem> items;

        items = publishingStatusDao.getPublishingStatus(publishingRecordId);

        for (PublishingStatusItem publishingStatusItem : items) {
            String statusMessage = publishingStatusItem.getStatusMessage();
            String bookId = publishingStatusItem.getBookId();
            String status = String.valueOf(publishingStatusItem.getStatus());

            records.add(PublishingStatusRecord.builder()
                    .withStatusMessage(statusMessage)
                    .withBookId(bookId)
                    .withStatus(status)
                    .build());
        }


        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(records)
                .build();
    }
}
