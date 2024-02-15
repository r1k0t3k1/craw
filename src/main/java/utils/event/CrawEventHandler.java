package main.java.utils.event;

import javax.swing.event.EventListenerList;

public class CrawEventHandler {
    EventListenerList listeners = new EventListenerList();

    public void addEventListener(ICrawEventListener listener) {
        listeners.add(ICrawEventListener.class, listener);
    }
    public void removeEventListener(ICrawEventListener listener) {
        listeners.remove(ICrawEventListener.class, listener);
    }

    protected void fireCrawEvent(Object source) {
        CrawEvent eventObject = new CrawEvent(source);
        ICrawEventListener[] listeners = this.listeners.getListeners(ICrawEventListener.class);

        for(ICrawEventListener l: listeners) {
            l.onCrawEvent(eventObject);
        }
    }
}
