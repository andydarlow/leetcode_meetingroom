package org.leetcode.marketting;

/**
 * Represents a meeting room with scheduling capabilities.
 * Tracks  number of meetings held, and the end time of the last meeting.
 */
public class MeetingRoom {
    /** The unique identifier for this meeting room. */
    private final int id;
    /** The total number of meetings that have been scheduled in this room. */
    private long numberOfMeetings;
    /** The end time of the last meeting scheduled in this room.
     *  you cant schedule a meeting before this time*/
    private long lastMeetingEndTime;

    /**
     * Constructs a new MeetingRoom with the specified ID, initial meeting count, and last meeting end time.
     *
     * @param id the unique identifier for the room
     * @param numberOfMeetings the initial number of meetings (usually 0 for new rooms)
     * @param lastMeetingEndTime the end time of the last meeting (usually 0 for new rooms)
     */
    public MeetingRoom(int id, int numberOfMeetings, int lastMeetingEndTime) {
        this.id = id;
        this.numberOfMeetings = numberOfMeetings;
        this.lastMeetingEndTime = lastMeetingEndTime;
    }

    public int getId() {
        return id;
    }

    public long getNumberOfMeetings() {
        return numberOfMeetings;
    }

    /**
     * Checks if this room is free for the given meeting.
     * A room is free if its last meeting ends before or at the meeting's start time.
     * Its assumed hat the meeting room is fully booked before the end of the last meeting.
     *
     * @param meeting the meeting to check availability for
     * @return true if the room is available, false otherwise
     */
    public boolean  isFree(Meeting meeting) {
        return this.lastMeetingEndTime <= meeting.startTime();
    }

    /**
     * Compares this room with another based on their last meeting end times.
     * Used for sorting rooms by availability (to find the room which is available earliest).
     *
     * @param other the room to compare with
     * @return negative if this room is free earlier, positive if later, 0 if equal
     */
    public int compareToEndTime(MeetingRoom other) {
        return Long.compare(this.lastMeetingEndTime, other.lastMeetingEndTime);
    }

    /**
     * Adds a meeting to this room, updating the room's schedule.
     * The meeting starts at the maximum of its requested start time and the current end time,
     * ensuring no overlap with previous meetings.
     *
     * @param meeting the meeting to add to this room
     */
    public void addMeeting(Meeting meeting) {
        long meetingStartTime = Long.max(this.lastMeetingEndTime, meeting.startTime());
        this.lastMeetingEndTime = meetingStartTime + meeting.duration();
        this.numberOfMeetings++;
    }

    /**
     * Compares this room with another first by number of meetings (descending),
     * then by ID (descending) if meetings are equal.
     * Used to find the room with the most meetings, with ties broken by higher ID.
     *
     * @param other the room to compare with
     * @return negative if this room has fewer meetings or same meetings but lower ID,
     *         positive if more meetings or same meetings but higher ID, 0 if equal
     */
    public int compareByNumberOfMeetingsAndId(MeetingRoom other) {
        int maxMeetings = Long.compare(getNumberOfMeetings(), other.getNumberOfMeetings());
        return maxMeetings == 0 ? Integer.compare(other.getId(), getId()) : maxMeetings;
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "id=" + id +
                ", numberOfMeetings=" + numberOfMeetings +
                ", lastMeetingEndTime=" + lastMeetingEndTime +
                '}';
    }
}
