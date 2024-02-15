package main.java.utils.event;

import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.util.EventObject;

public class CrawEvent extends EventObject {
    public CrawEvent(Object source) {
        super(source);
    }
}

