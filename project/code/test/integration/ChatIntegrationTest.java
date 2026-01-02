package integration;

import model.Conversation;
import model.Message;
import model.User;
import repository.Database;
import service.AuthService;
import service.ChatService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatIntegrationTest {

	@BeforeEach
	void resetDatabaseSingleton() throws Exception {
		Field instance = Database.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
	}

	@Test
	void createConversationSendMessageAndFetchHistory_shouldWork() {
		AuthService auth = new AuthService();
		assertTrue(auth.registerUser("alice", "alice@example.com", "13800000000", "123456"));
		assertTrue(auth.registerUser("bob", "bob@example.com", "13900000000", "123456"));
		assertTrue(auth.login("alice", "123456"));

		Database db = Database.getInstance();
		User bob = db.getUserByUsername("bob");
		assertNotNull(bob);

		ChatService chatService = new ChatService(auth);
		Conversation conv1 = chatService.getOrCreateConversation(bob.getId());
		assertNotNull(conv1);

		Conversation conv2 = chatService.getOrCreateConversation(bob.getId());
		assertNotNull(conv2);
		assertEquals(conv1.getId(), conv2.getId());

		assertFalse(chatService.sendMessage(conv1.getId(), "   "));
		assertTrue(chatService.sendMessage(conv1.getId(), "hello"));

		List<Message> history = chatService.getConversationHistory(conv1.getId());
		assertNotNull(history);
		assertEquals(1, history.size());
		assertEquals("hello", history.get(0).getContent());
		assertEquals(auth.getCurrentUser().getId(), history.get(0).getSenderId());
	}
}
