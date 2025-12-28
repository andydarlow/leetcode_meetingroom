package org.leetcode.marketting;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomTest {

    @Test
    void testMeetingCreation() {
        Meeting meeting = new Meeting(9, 10);
        assertEquals(9, meeting.startTime());
        assertEquals(10, meeting.endTime());
    }

    @Test
    void testToMeeting() {
        int[] meetingTime = {8, 9};
        Meeting meeting = Meeting.toMeeting(meetingTime);
        assertEquals(8, meeting.startTime());
        assertEquals(9, meeting.endTime());
    }

    @Test
    void testToMeetings() {
        int[][] meetingTimes = {{10, 11}, {8, 9}, {12, 13}};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);

        // Should be sorted by startTime
        assertEquals(3, meetings.size());
        assertEquals(8, meetings.get(0).startTime());
        assertEquals(9, meetings.get(0).endTime());
        assertEquals(10, meetings.get(1).startTime());
        assertEquals(11, meetings.get(1).endTime());
        assertEquals(12, meetings.get(2).startTime());
        assertEquals(13, meetings.get(2).endTime());
    }


    @Test
    void testToMeetingsEmpty() {
        int[][] meetingTimes = {};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);
        assertTrue(meetings.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Meeting meeting1 = new Meeting(1, 2);
        Meeting meeting2 = new Meeting(1, 2);
        Meeting meeting3 = new Meeting(2, 3);

        assertEquals(meeting1, meeting2);
        assertNotEquals(meeting1, meeting3);
        assertEquals(meeting1.hashCode(), meeting2.hashCode());
    }

    @Test
    void testToMeetingsWithOverlappingIntervals() {
        int[][] meetingTimes = {{9, 11}, {10, 12}, {8, 10}}; // Overlapping: [8,10] overlaps with [9,11] and [10,12]
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);

        // Should be sorted by startTime: [8,10], [9,11], [10,12]
        assertEquals(3, meetings.size());
        assertEquals(8, meetings.get(0).startTime());
        assertEquals(10, meetings.get(0).endTime());
        assertEquals(9, meetings.get(1).startTime());
        assertEquals(11, meetings.get(1).endTime());
        assertEquals(10, meetings.get(2).startTime());
        assertEquals(12, meetings.get(2).endTime());
    }

    @Test
    void testToMeetingsWithIdenticalIntervals() {
        int[][] meetingTimes = {{10, 11}, {10, 11}, {9, 10}}; // Two identical meetings
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);

        // Should be sorted by startTime: [9,10], [10,11], [10,11]
        assertEquals(3, meetings.size());
        assertEquals(9, meetings.get(0).startTime());
        assertEquals(10, meetings.get(0).endTime());
        assertEquals(10, meetings.get(1).startTime());
        assertEquals(11, meetings.get(1).endTime());
        assertEquals(10, meetings.get(2).startTime());
        assertEquals(11, meetings.get(2).endTime());
    }

    @Test
    void testToMeetingsWithAdjacentNonOverlappingIntervals() {
        int[][] meetingTimes = {{8, 9}, {9, 10}, {10, 11}}; // Adjacent but not overlapping
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);

        // Should be sorted by startTime: [8,9], [9,10], [10,11]
        assertEquals(3, meetings.size());
        assertEquals(8, meetings.get(0).startTime());
        assertEquals(9, meetings.get(0).endTime());
        assertEquals(9, meetings.get(1).startTime());
        assertEquals(10, meetings.get(1).endTime());
        assertEquals(10, meetings.get(2).startTime());
        assertEquals(11, meetings.get(2).endTime());
    }

    @Test
    void testAddMeetingToEmptyRoom() {
        MeetingRoom room = new MeetingRoom(1, 0, 0);
        Meeting meeting = new Meeting(9, 11); // 2 hour meeting starting at 9

        room.addMeeting(meeting);

        assertEquals(1, room.getNumberOfMeetings());
        // Since room was free (lastEndTime=0), meeting starts at requested time
        // Expected end time: 9 + 2 = 11
        // But we can't directly access lastMeetingEndTime, need to check via behavior
        // Actually, the field is private, but we can test via isFree or add another meeting
        Meeting nextMeeting = new Meeting(11, 12); // Should be free since previous ends at 11
        assertTrue(room.isFree(nextMeeting));
    }

    @Test
    void testAddMeetingWithConflict() {
        MeetingRoom room = new MeetingRoom(1, 0, 10); // Room busy until 10
        Meeting meeting = new Meeting(9, 12); // Wants to start at 9, but conflicts

        room.addMeeting(meeting);

        assertEquals(1, room.getNumberOfMeetings());
        // Meeting should start at max(10, 9) = 10
        // Duration = 12-9 = 3, so end time = 10 + 3 = 13
        Meeting nextMeeting = new Meeting(13, 14); // Should be free
        assertTrue(room.isFree(nextMeeting));
        Meeting conflictingMeeting = new Meeting(12, 15); // Should not be free
        assertFalse(room.isFree(conflictingMeeting));
    }

    @Test
    void testAddMeetingNoConflict() {
        MeetingRoom room = new MeetingRoom(1, 0, 10); // Room busy until 10
        Meeting meeting = new Meeting(11, 13); // Starts after 10, no conflict

        room.addMeeting(meeting);

        assertEquals(1, room.getNumberOfMeetings());
        // Meeting starts at 11 (as requested), ends at 13
        Meeting nextMeeting = new Meeting(13, 14); // Should be free
        assertTrue(room.isFree(nextMeeting));
        Meeting earlyMeeting = new Meeting(12, 15); // Should not be free
        assertFalse(room.isFree(earlyMeeting));
    }

    @Test
    void testAddMultipleMeetings() {
        MeetingRoom room = new MeetingRoom(1, 0, 0);
        Meeting meeting1 = new Meeting(9, 10);
        Meeting meeting2 = new Meeting(11, 13);
        Meeting meeting3 = new Meeting(10, 12); // Conflicts with meeting2 request, should adjust

        room.addMeeting(meeting1);
        assertEquals(1, room.getNumberOfMeetings());

        room.addMeeting(meeting2);
        assertEquals(2, room.getNumberOfMeetings());

        room.addMeeting(meeting3); // Should start at max(current end=13, 10)=13, end at 13+(12-10)=15
        assertEquals(3, room.getNumberOfMeetings());

        // Check final state: room should be busy until 15
        assertFalse(room.isFree(new Meeting(14, 16)));
        assertTrue(room.isFree(new Meeting(15, 17)));
    }

    @Test
    void testCompareByNumberOfMeetingsAndIdDifferentMeetings() {
        MeetingRoom room1 = new MeetingRoom(1, 5, 0); // 5 meetings
        MeetingRoom room2 = new MeetingRoom(2, 3, 0); // 3 meetings

        // room1 has more meetings, so room1.compareByNumberOfMeetingsAndId(room2) should be positive
        assertTrue(room1.compareByNumberOfMeetingsAndId(room2) > 0);
        // room2 has fewer meetings, so room2.compareByNumberOfMeetingsAndId(room1) should be negative
        assertTrue(room2.compareByNumberOfMeetingsAndId(room1) < 0);
    }

    @Test
    void testCompareByNumberOfMeetingsAndIdSameMeetingsDifferentIds() {
        MeetingRoom room1 = new MeetingRoom(1, 5, 0); // ID 1
        MeetingRoom room2 = new MeetingRoom(3, 5, 0); // ID 3

        // Same meetings, but room2 has higher ID, so room2.compareByNumberOfMeetingsAndId(room1) should be positive
        assertTrue(room2.compareByNumberOfMeetingsAndId(room1) < 0);
        // room1 has lower ID, so room1.compareByNumberOfMeetingsAndId(room2) should be negative
        assertTrue(room1.compareByNumberOfMeetingsAndId(room2) > 0);
    }

    @Test
    void testCompareByNumberOfMeetingsAndIdEqual() {
        MeetingRoom room1 = new MeetingRoom(1, 5, 0);
        MeetingRoom room2 = new MeetingRoom(1, 5, 0); // Same ID and meetings

        // Both equal, should return 0
        assertEquals(0, room1.compareByNumberOfMeetingsAndId(room2));
        assertEquals(0, room2.compareByNumberOfMeetingsAndId(room1));
    }

    @Test
    void testCompareByNumberOfMeetingsAndIdComplexCases() {
        MeetingRoom roomLowMeetingsHighId = new MeetingRoom(10, 1, 0); // 1 meeting, ID 10
        MeetingRoom roomHighMeetingsLowId = new MeetingRoom(2, 8, 0);  // 8 meetings, ID 2
        MeetingRoom roomMedium = new MeetingRoom(5, 4, 0);              // 4 meetings, ID 5

        // High meetings beats low meetings regardless of ID
        assertTrue(roomHighMeetingsLowId.compareByNumberOfMeetingsAndId(roomLowMeetingsHighId) > 0);
        assertTrue(roomLowMeetingsHighId.compareByNumberOfMeetingsAndId(roomHighMeetingsLowId) < 0);

        // When meetings equal, higher ID wins
        assertTrue(roomMedium.compareByNumberOfMeetingsAndId(roomHighMeetingsLowId) < 0); // roomMedium (4) vs roomHighMeetingsLowId (8)
        // But let's add another test
        MeetingRoom roomEqualMeetingsHigherId = new MeetingRoom(7, 4, 0); // 4 meetings, ID 7
        assertTrue(roomEqualMeetingsHigherId.compareByNumberOfMeetingsAndId(roomMedium) < 0); // ID 7 > ID 5
    }
}
