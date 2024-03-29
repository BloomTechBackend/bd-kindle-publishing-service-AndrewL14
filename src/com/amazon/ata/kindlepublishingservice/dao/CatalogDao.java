package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    public void removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = this.getLatestVersionOfBook(bookId);
        if(book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("DELETE | No book found for id: %s", bookId));
        }
        book.setInactive(true);
        this.dynamoDbMapper.save(book);
    }

    public boolean validateBookExists(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        return book == null;
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook formattedBook) {
        CatalogItemVersion catalogItem;
        if (formattedBook.getBookId() == null) {
            catalogItem = new CatalogItemVersion();
            catalogItem.setBookId(KindlePublishingUtils.generateBookId());
            catalogItem.setVersion(1);
            catalogItem.setInactive(false);

        } else {
            catalogItem = getBookFromCatalog(formattedBook.getBookId());
            catalogItem.setVersion(catalogItem.getVersion() + 1);
            removeBookFromCatalog(formattedBook.getBookId());
        }
        catalogItem.setTitle(formattedBook.getTitle());
        catalogItem.setAuthor(formattedBook.getAuthor());
        catalogItem.setText(formattedBook.getText());
        catalogItem.setGenre(formattedBook.getGenre());
        dynamoDbMapper.save(catalogItem);
        return catalogItem;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

}
