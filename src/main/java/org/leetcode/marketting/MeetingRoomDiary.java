package org.leetcode.marketting;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A Diary manages all the meetings scheduled over a set of meeting rooms.
 */
public class MeetingRoomDiary {

    /** List of meeting rooms managed in this diary. */
    private final List<MeetingRoom> rooms;

    /**
     * Constructs a new MeetingRoomDiary with the specified number of rooms.
     * The diary manages bookings in these rooms.  Each room that this diary
     * manages had a unique ID, starting from 0.
     *
     * @param numberOfRooms the number of meeting rooms to create
     */
    public MeetingRoomDiary(int numberOfRooms) {
        rooms = IntStream.range(0, numberOfRooms)
                .mapToObj(roomId -> new MeetingRoom(roomId, 0, 0))
                .toList();
    }

    /**
     * Finds the first available meeting room that can accommodate the given meeting.
     * A room is considered available if it has no scheduling conflicts with the meeting time.
     *
     * @param meeting the meeting to find a room for
     * @return an Optional containing the first available room, or empty if no room is available that can accommodate
     * the meeting
     */
    public Optional<MeetingRoom> findAvailableRoom(Meeting meeting) {
        return rooms.stream()
                .filter(room -> room.isFree(meeting))
                .findFirst();
    }

    /**
     * Finds the meeting room that will become available the earliest.
     *
     * @return the room that will become free earliest
     * @throws RuntimeException if no rooms are available (empty list). This shouldn't happen
     */
    public MeetingRoom findRoomWithEarliestAvailability() {
        return rooms.stream()
                    .min(MeetingRoom::compareToEndTime)
                    .orElseThrow(); // empty list of rooms
    }

    /**
     * Books a meeting room for the given meeting. The method attempts to find a room that is
     * immediately available for the specified meeting time. If no such room is found, it selects
     * the room with the earliest future availability and schedules the meeting there.
     *
     * @param meeting the meeting to be booked, containing its start and end time
     */
    private void bookMeetingRoom(Meeting meeting) {
        findAvailableRoom(meeting)
                .orElse(findRoomWithEarliestAvailability())
                .addMeeting(meeting);
    }

    /**
     * Simulates booking all provided meetings into available rooms and returns the most used room.
     * For each meeting, attempts to find an available room; if none available, uses the room
     * with earliest availability. Then returns the room with the highest number of meetings
     * (ties broken by room ID). Not this method books the meetings into the room (side effect)
     *
     * @param meetings the list of meetings to book
     * @return the room with the most meetings booked
     * @throws RuntimeException if no rooms are available (empty list)
     */
    public MeetingRoom mostUsed(List<Meeting> meetings) {
        // side effect: diary updated with the rooms. Clone diary if you want to
        // keep this instance unchanged.
        meetings.forEach(this::bookMeetingRoom);
        return rooms.stream().max(MeetingRoom::compareByNumberOfMeetingsAndId)
                             .orElseThrow();
    }
}
