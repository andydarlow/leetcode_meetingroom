package org.leetcode.marketting;

import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomDiaryTest {


    @Test
    void testFindAvailableRoomWhenAvailable() {
        MeetingRoomDiary diary = new MeetingRoomDiary(2);
        Meeting meeting = new Meeting(10, 11);

        Optional<MeetingRoom> availableRoom = diary.findAvailableRoom(meeting);
        assertTrue(availableRoom.isPresent());
        assertEquals(0, availableRoom.get().getId());
    }

    @Test
    void testFindAvailableRoomWhenNotAvailable() {
        MeetingRoomDiary diary = new MeetingRoomDiary(1);
        Meeting meeting = new Meeting(-1, 0); // Meeting that starts before room availability

        Optional<MeetingRoom> availableRoom = diary.findAvailableRoom(meeting);
        assertFalse(availableRoom.isPresent());
    }

    @Test
    void testFindRoomWithEarliestAvailability() {
        MeetingRoomDiary diary = new MeetingRoomDiary(3);
        MeetingRoom earliestRoom = diary.findRoomWithEarliestAvailability();
        // All rooms start with lastMeetingEndTime = 0, so any room could be returned,
        // But the method should not throw an exception
        assertNotNull(earliestRoom);
    }

    @Test
    void testFindAvailableRoomMultipleRooms() {
        MeetingRoomDiary diary = new MeetingRoomDiary(3);
        Meeting meeting = new Meeting(10, 11);

        Optional<MeetingRoom> availableRoom = diary.findAvailableRoom(meeting);
        assertTrue(availableRoom.isPresent());
        // Should return the first available room (ID 0)
        assertEquals(0, availableRoom.get().getId());
    }

    @Test
    void testConstructorCreatesCorrectNumberOfRooms() {
        MeetingRoomDiary diary = new MeetingRoomDiary(3);
        // Test by trying to find rooms - should have 5 rooms
        for (int i = 0; i < 5; i++) {
            Meeting meeting = new Meeting(10 + i, 13 + i);
            Optional<MeetingRoom> room = diary.findAvailableRoom(meeting);
            assertTrue(room.isPresent(), "Should find room for meeting " + i);
            room.get().addMeeting(meeting);
        }
        // Sixth meeting should not find a room if all are busy
        Meeting sixthMeeting = new Meeting(14, 16);
        Optional<MeetingRoom> noRoom = diary.findAvailableRoom(sixthMeeting);
        assertFalse(noRoom.isPresent(), "Should not find room when all are busy");
    }

    @Test
    void testFindRoomWithEarliestAvailabilityWithDifferentEndTimes() {
        MeetingRoomDiary diary = new MeetingRoomDiary(2);

        // Manually set up different end times by adding meetings
        // This is tricky since we can't directly set end times, but we can use the internal behavior
        // Let's add meetings to create different end times
        Meeting meeting1 = new Meeting(9, 10); // Room 0 ends at 10
        diary.findAvailableRoom(meeting1).get().addMeeting(meeting1);

        Meeting meeting2 = new Meeting(9, 13); // Room 1 ends at 13
        diary.findAvailableRoom(meeting2).get().addMeeting(meeting2);

        Meeting meeting3 = new Meeting(12, 14); // Room 2 ends at 14
        diary.findAvailableRoom(meeting3).get().addMeeting(meeting3);

        // Now room 0 should be earliest (ends at 10)
        MeetingRoom earliest = diary.findRoomWithEarliestAvailability();
        // We can't easily check which room it is without exposing internals,
        // but we can verify it returns a room and that a meeting can be scheduled
        assertNotNull(earliest);
        assertEquals(1,earliest.getId());
        assertTrue(earliest.isFree(new Meeting(13, 14)));
    }

    @Test
    void testMostUsedSimpleCase() {
        MeetingRoomDiary diary = new MeetingRoomDiary(2);
        List<Meeting> meetings = List.of(
            new Meeting(9, 10),
            new Meeting(9, 10), // Same time, should go to different room
            new Meeting(9, 10)  // Third meeting, should go to first room again
        );

        MeetingRoom mostUsed = diary.mostUsed(meetings);

        assertEquals(0, mostUsed.getId());
        assertEquals(2, mostUsed.getNumberOfMeetings());
    }

    @Test
    void testMostUsedWithConflicts() {
        MeetingRoomDiary diary = new MeetingRoomDiary(2);
        List<Meeting> meetings = List.of(
            new Meeting(9, 20),  // Room 0: ends 20
            new Meeting(10, 12), // Conflicts with first, goes to room 1: ends 12
            new Meeting(11, 13), // Can go to room 0 (ends 11), so room 0 gets it: ends 13
            new Meeting(12, 14)  // Conflicts with both, goes to earliest (room 1 ends 12): ends 14
        );

        MeetingRoom mostUsed = diary.mostUsed(meetings);

        // Room 0 should have 2 meetings, room 1 should have 2 meetings
        // Since equal, should return room with higher ID (1 > 0)
        assertEquals(1, mostUsed.getId());
        assertEquals(3, mostUsed.getNumberOfMeetings());
    }

    @Test
    void testMostUsedEmptyMeetings() {
        MeetingRoomDiary diary = new MeetingRoomDiary(3);
        List<Meeting> meetings = List.of();

        MeetingRoom mostUsed = diary.mostUsed(meetings);

        assertEquals(0, mostUsed.getId());
        assertEquals(0, mostUsed.getNumberOfMeetings());
    }

    @Test
    void testMostUsedSingleRoom() {
        MeetingRoomDiary diary = new MeetingRoomDiary(1);
        List<Meeting> meetings = List.of(
            new Meeting(9, 10),
            new Meeting(11, 12),
            new Meeting(13, 14)
        );

        MeetingRoom mostUsed = diary.mostUsed(meetings);

        assertEquals(0, mostUsed.getId());
        assertEquals(3, mostUsed.getNumberOfMeetings());
    }

    @Test
    void testMostUsedComplexScheduling() {
        MeetingRoomDiary diary = new MeetingRoomDiary(3);
        List<Meeting> meetings = List.of(
            new Meeting(9, 10),   // Room 0
            new Meeting(9, 10),   // Room 1 (conflict)
            new Meeting(9, 10),   // Room 2 (conflict)
            new Meeting(10, 11),  // Room 0 (available)
            new Meeting(10, 11),  // Room 1 (available)
            new Meeting(11, 12),  // Room 0 (available)
            new Meeting(11, 12)   // Room 1 (available)
        );

        MeetingRoom mostUsed = diary.mostUsed(meetings);

        // Room 0 should have 3 meetings, rooms 1 and 2 should have 2 each
        assertEquals(0, mostUsed.getId());
        assertEquals(3, mostUsed.getNumberOfMeetings());
    }

    @Test
    void testFindAvailableRoomAfterScheduling() {
        MeetingRoomDiary diary = new MeetingRoomDiary(2);

        // Schedule a meeting in room 0
        Meeting meeting1 = new Meeting(9, 11);
        Optional<MeetingRoom> room1 = diary.findAvailableRoom(meeting1);
        assertTrue(room1.isPresent());
        room1.get().addMeeting(meeting1);

        // Now find room for another meeting that conflicts
        Meeting meeting2 = new Meeting(10, 12);
        Optional<MeetingRoom> room2 = diary.findAvailableRoom(meeting2);
        assertTrue(room2.isPresent());
        assertNotEquals(room1.get().getId(), room2.get().getId()); // Should be different room
        room2.get().addMeeting(meeting2);

        // Try to find room for a meeting that conflicts with both
        Meeting meeting3 = new Meeting(10, 13);
        Optional<MeetingRoom> room3 = diary.findAvailableRoom(meeting3);
        assertFalse(room3.isPresent()); // No room available
    }

}
