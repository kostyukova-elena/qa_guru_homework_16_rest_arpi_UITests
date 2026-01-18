package models;

import java.util.List;

@lombok.Data
public class BookData {
    private String userId;
    private List<CollectionOfIsbns> collectionOfIsbns;
}
