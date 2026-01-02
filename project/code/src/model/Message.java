package model;

import java.util.Date;

public class Message {
	private Long id;
	private Long conversationId;
	private Long senderId;
	private Long receiverId;
	private String content;
	private Date sentAt;
	private String type; // "text", "image", "contact"
	private boolean read;

	public Message() {
		this.sentAt = new Date();
		this.type = "text";
		this.read = false;
	}

	public Message(Long id, Long conversationId, Long senderId, Long receiverId, String content) {
		this();
		this.id = id;
		this.conversationId = conversationId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public void markRead() {
		this.read = true;
	}
}
