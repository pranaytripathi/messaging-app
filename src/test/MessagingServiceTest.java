package test;

import com.messagingapp.exceptions.InvalidMessageTypeException;
import com.messagingapp.service.MessageServiceImpl;
import com.messagingapp.service.MessagingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MessagingServiceTest {

    @Test
    public void testGetValidMessageType() throws InvalidMessageTypeException {
        MessagingService messagingService = new MessageServiceImpl();
        assertEquals(messagingService.getValidMessageType("apples 20p"), 1);
        assertEquals(messagingService.getValidMessageType("20 apples 20p"), 2);
        assertEquals(messagingService.getValidMessageType("Add apples 20p"), 3);
        assertThrows(InvalidMessageTypeException.class, () -> messagingService.getValidMessageType("123 123"));
    }

    @Test
    public void testMatchMessageTypePattern() {
        MessagingService messagingService = new MessageServiceImpl();
        assertTrue(messagingService.matchMessageType("apples 20p", messagingService.MESSAGE_TYPE_PATTERN_1));
        assertTrue(messagingService.matchMessageType("20 apples 20p", messagingService.MESSAGE_TYPE_PATTERN_2));
        assertTrue(messagingService.matchMessageType("Add apples 20p", messagingService.MESSAGE_TYPE_PATTERN_3));
        assertFalse(messagingService.matchMessageType("apples 20 p", messagingService.MESSAGE_TYPE_PATTERN_1));
        assertFalse(messagingService.matchMessageType("20 20p", messagingService.MESSAGE_TYPE_PATTERN_2));
        assertFalse(messagingService.matchMessageType("20 20p", messagingService.MESSAGE_TYPE_PATTERN_3));
    }

}
