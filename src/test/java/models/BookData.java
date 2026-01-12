package models;

import java.util.List;

@lombok.Data
public class BookData {
    private String userId = "602ee7c4-f266-4909-9ad3-97715dce95fe";
    private List<CollectionOfIsbns> collectionOfIsbns = List.of(new CollectionOfIsbns("9781449365035"));

}
