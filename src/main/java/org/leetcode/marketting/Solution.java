package org.leetcode.marketting;


import java.util.List;

/**
 * Leet code entry point, Let code calls an instance of this class to test the code
 * see <a href="https://leetcode.com/problems/meeting-rooms-iii/description/?envType=daily-question&envId=2025-12-27">...</a>
 */

public class Solution {
    /**
     * leet code entry point for . Tells you the most booked meeting room
     * see <a href="https://leetcode.com/problems/meeting-rooms-iii/description/?envType=daily-question&envId=2025-12-27">...</a>
     * @param n number of meeting rooms
     * @param rawMeetings  list of meeting rooms of the format [starttime, endtime]
     * @return the Id of the room that was most booked for the meetings passed in.
     */
    public int mostBooked(int n, int[][] rawMeetings) {
            MeetingRoomDiary meetingRoomDiary = new MeetingRoomDiary(n);
            List<Meeting> meetings = Meeting.toMeetings(rawMeetings);
            return meetingRoomDiary.mostUsed(meetings).getId();
        }

}
