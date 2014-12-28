package crawler.service;

import org.jsoup.nodes.Document;

public class UnexpectedParserState extends RuntimeException {
    public UnexpectedParserState(String message) {
        super(message);
    }
    public UnexpectedParserState(String message, Document doc) {
        super(message+"\n"+doc.html());
    }
}
