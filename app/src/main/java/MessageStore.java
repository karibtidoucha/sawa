import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by october on 28/04/2018.
 */

public class MessageStore {

    private static MessageStore messageStore;

    public FirebaseDatabase firebasedatabase;


    private MessageStore(){
        firebasedatabase = FirebaseDatabase.getInstance();

    }

    public static MessageStore getInstance(){
        if (messageStore == null)
            messageStore = new MessageStore();
        return messageStore;
    }




}
