package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation {
	private Long id;
	private Long user1Id;
	private Long user2Id;
	private Date createdAt;
	private List<Message> messages;

	public Conversation() {
		this.createdAt = new Date();
		this.messages = new ArrayList<>();
	}

	public Conversation(Long id, Long user1Id, Long user2Id) {
		this();
		this.id = id;
		this.user1Id = user1Id;
		this.user2Id = user2Id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Long user1Id) {
		this.user1Id = user1Id;
	}

	public Long getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Long user2Id) {
		this.user2Id = user2Id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public boolean isParticipant(Long userId) {
		return user1Id.equals(userId) || user2Id.equals(userId);
	}

	public Long getOtherUserId(Long currentUserId) {
		return user1Id.equals(currentUserId) ? user2Id : user1Id;
	}
}
