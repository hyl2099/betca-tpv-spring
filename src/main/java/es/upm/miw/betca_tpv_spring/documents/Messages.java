package es.upm.miw.betca_tpv_spring.documents;

import java.time.LocalDateTime;

public class Messages {

    private String fromUserName;
    private String messageContent;
    private LocalDateTime sentDate;
    private LocalDateTime readDate;

    public Messages(String fromUserName, String messageContent, LocalDateTime sentDate, LocalDateTime readDate){
        this.fromUserName = fromUserName;
        this.messageContent = messageContent;
        this.sentDate = sentDate;
        this.readDate = readDate;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDateTime readDate) {
        this.readDate = readDate;
    }

    @Override
    public String toString(){
        return "Messages{" +
                "fromUserName='" + fromUserName + "\'" +
                ", messageContent='" + messageContent + "\'" +
                ", sentDate='" + sentDate + "\'" +
                ", readDate='" + readDate + "\'" +
                "}";
    }
}
