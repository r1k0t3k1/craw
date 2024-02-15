package main.java.utils.event;

import java.util.EventListener;

public interface ICrawEventListener extends EventListener {
    public void onCrawEvent(CrawEvent crawEvent);
}
