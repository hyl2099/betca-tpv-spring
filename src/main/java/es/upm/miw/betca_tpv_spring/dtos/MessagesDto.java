package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Messages;

import java.time.LocalDateTime;

public class MessagesDto {

    private String fromUserName;
    private String messageContent;
    private LocalDateTime sentDate;
    private LocalDateTime readDate;

    public MessagesDto() {
        // Empty for framework
    }

    public MessagesDto(String fromUserName, String messageContent, LocalDateTime sentDate, LocalDateTime readDate) {
        this.fromUserName = fromUserName;
        this.messageContent = messageContent;
        this.sentDate = sentDate;
        this.readDate = readDate;
    }

    public MessagesDto(Messages messages) {
        this.fromUserName = messages.getFromUserName();
        this.messageContent = messages.getMessageContent();
        this.sentDate = messages.getSentDate();
        this.readDate = messages.getReadDate();
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
    public String toString() {
        return "MessagesDto [" +
                "fromUserName=" + fromUserName  +
                ", messageContent=" + messageContent +
                ", sentDate=" + sentDate +
                ", readDate=" + readDate +
                ']';
    }
}
