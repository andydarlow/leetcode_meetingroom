package org.leetcode.marketting;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MeetingTest {

    @Test
    public void testMeetingRecordCreation() {
        Meeting meeting = new Meeting(9, 10);
        assertEquals(9, meeting.startTime());
        assertEquals(10, meeting.endTime());
    }

    @Test
    public void testToMeeting() {
        int[] meetingTime = {8, 12};
        Meeting meeting = new Meeting(meetingTime);
        assertEquals(8, meeting.startTime());
        assertEquals(12, meeting.endTime());
    }

    @Test
    public void testDuration() {
        Meeting meeting = new Meeting(10, 15);
        assertEquals(5, meeting.duration());
    }

    @Test
    public void testDurationZeroLength() {
        Meeting meeting = new Meeting(10, 10);
        assertEquals(0, meeting.duration());
    }

    @Test
    public void testToMeetingsSingleMeeting() {
        int[][] meetingTimes = {{9, 11}};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);
        assertEquals(1, meetings.size());
        assertEquals(9, meetings.get(0).startTime());
        assertEquals(11, meetings.get(0).endTime());
    }

    @Test
    public void testToMeetingsMultipleMeetings() {
        int[][] meetingTimes = {{14, 16}, {9, 11}, {12, 13}};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);
        assertEquals(3, meetings.size());
        // Should be sorted by start time
        assertEquals(9, meetings.get(0).startTime());
        assertEquals(11, meetings.get(0).endTime());
        assertEquals(12, meetings.get(1).startTime());
        assertEquals(13, meetings.get(1).endTime());
        assertEquals(14, meetings.get(2).startTime());
        assertEquals(16, meetings.get(2).endTime());
    }

    @Test
    public void testToMeetingsEmptyArray() {
        int[][] meetingTimes = {};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);
        assertTrue(meetings.isEmpty());
    }

    @Test
    public void testToMeetingsOverlappingTimes() {
        int[][] meetingTimes = {{10, 12}, {11, 13}};
        List<Meeting> meetings = Meeting.toMeetings(meetingTimes);
        assertEquals(2, meetings.size());
        // Sorted by start time
        assertEquals(10, meetings.get(0).startTime());
        assertEquals(11, meetings.get(1).startTime());
    }
}
