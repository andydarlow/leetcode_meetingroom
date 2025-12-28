package org.leetcode.marketting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public record Meeting(int startTime, int endTime) {

    /**
     * new instance of Meeting from a tuple of (startTime, endTime)
     *
     * @param meetingTime tuple of (startTime, endTime)
     * @return meeting
     */
    public static Meeting toMeeting(int[] meetingTime) {
        return new Meeting(meetingTime[0], meetingTime[1]);
    }

    /**
     * Converts a 2D array of meeting times into a list of {@code Meeting} objects.
     * Each inner array contains two integers representing the start and end times
     * of a meeting. The resulting list is sorted in ascending order by the start time.
     *
     * @param meetingTimes a 2D array where each element represents a tuple of
     *                     start and end times for a meeting
     * @return a list of {@code Meeting} objects sorted by start time
     */
    public static List<Meeting> toMeetings(int[][] meetingTimes) {
        return Arrays.stream(meetingTimes)
                .map(Meeting::toMeeting)
                .sorted(Comparator.comparingInt(meetingA -> meetingA.startTime))
                .toList();
    }

    /**
     * duration of meeting.
     * @return how long the meeting will last.
     */
    public int duration() {
        return endTime - startTime;
    }
}
