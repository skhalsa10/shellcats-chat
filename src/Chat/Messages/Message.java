package Chat.Messages;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 * This is an abstract message class. It cannot be instansiated by itself but
 * It will guarantee certain behavior in all messages that extend from it.
 * I have added a timestamp. that will be added to all messages.
 * We can add other guarantees to this abstract class if we determine another one.
 */
public abstract class Message implements Comparable<Message> , Serializable {
    protected LocalDateTime timeStamp;

    public Message(){
        timeStamp = LocalDateTime.now();
    }

    /**
     * Will be used to sort messages in priority blocking queue. The earlier message should be processed first.
     * If we do not want to use a timestamp(which should work) we will need to identify a priority protocol that
     * we can all follow.
     *
     * @return timestamp in nanoseconds
     */
    public LocalDateTime getTimeStamp(){
        return this.timeStamp;
    }

    @Override
    public int compareTo(Message o) {
        return this.getTimeStamp().compareTo(o.getTimeStamp());
    }
}
