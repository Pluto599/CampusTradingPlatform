package service;

import model.*;
import repository.Database;

import java.util.List;

public class ChatService {
	private Database db;
	private AuthService authService;

	public ChatService(AuthService authService) {
		this.db = Database.getInstance();
		this.authService = authService;
	}

	/**
	 * 创建或获取会话
	 */
	public Conversation getOrCreateConversation(Long otherUserId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		User currentUser = authService.getCurrentUser();
		if (currentUser.getId().equals(otherUserId)) {
			System.out.println("不能与自己聊天！");
			return null;
		}

		User otherUser = db.getUserById(otherUserId);
		if (otherUser == null) {
			System.out.println("用户不存在！");
			return null;
		}

		// 查找是否已有会话
		Conversation conversation = db.getConversationBetweenUsers(currentUser.getId(), otherUserId);

		if (conversation == null) {
			// 创建新会话
			Long convId = db.generateConversationId();
			conversation = new Conversation(convId, currentUser.getId(), otherUserId);
			db.addConversation(conversation);
			System.out.println("已创建与 " + otherUser.getUsername() + " 的会话");
		}

		return conversation;
	}

	/**
	 * 发送消息
	 */
	public boolean sendMessage(Long conversationId, String content) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return false;
		}

		if (content == null || content.trim().isEmpty()) {
			System.out.println("消息内容不能为空！");
			return false;
		}

		Conversation conversation = db.getConversationById(conversationId);
		if (conversation == null) {
			System.out.println("会话不存在！");
			return false;
		}

		User currentUser = authService.getCurrentUser();
		if (!conversation.isParticipant(currentUser.getId())) {
			System.out.println("您不是该会话的参与者！");
			return false;
		}

		// 创建消息
		Long messageId = db.generateMessageId();
		Long receiverId = conversation.getOtherUserId(currentUser.getId());
		Message message = new Message(messageId, conversationId, currentUser.getId(), receiverId, content);

		db.addMessage(message);
		conversation.addMessage(message);
		db.updateConversation(conversation);

		System.out.println("消息已发送");
		return true;
	}

	/**
	 * 获取会话历史
	 */
	public List<Message> getConversationHistory(Long conversationId) {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		Conversation conversation = db.getConversationById(conversationId);
		if (conversation == null) {
			System.out.println("会话不存在！");
			return null;
		}

		if (!conversation.isParticipant(authService.getCurrentUser().getId())) {
			System.out.println("您不是该会话的参与者！");
			return null;
		}

		return db.getMessagesByConversationId(conversationId);
	}

	/**
	 * 获取我的所有会话
	 */
	public List<Conversation> getMyConversations() {
		if (!authService.isLoggedIn()) {
			System.out.println("请先登录！");
			return null;
		}

		return db.getConversationsByUserId(authService.getCurrentUser().getId());
	}

	/**
	 * 标记消息为已读
	 */
	public void markMessageAsRead(Long messageId) {
		Message message = db.getMessagesByConversationId(0L).stream()
				.filter(m -> m.getId().equals(messageId))
				.findFirst()
				.orElse(null);

		if (message != null) {
			message.markRead();
			db.updateMessage(message);
		}
	}
}
