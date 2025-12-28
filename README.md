# Meeting Rooms III - LeetCode Solution

This Java project provides a solution to the LeetCode problem [Meeting Rooms III](https://leetcode.com/problems/meeting-rooms-iii/description/), which involves scheduling meetings in multiple rooms and determining which room gets booked the most frequently.

## Project Structure

- `Meeting.java` - Record representing a meeting with start and end times
- `MeetingRoom.java` - Class representing a meeting room with scheduling capabilities
- `MeetingRoomDiary.java` - handles multiple rooms and meeting scheduling
- `Solution.java` - Main LeetCode solution class with the `mostBooked` method

## Key Features

I built this solution to demonstrate using classes to model the domain.
You could optimise this code further by:
- Using a priority queue to schedule meetings in order of earliest start time
- Using a hash map to store room availability instead of an array
However, the code passes the leetcode constraints and is easy to understand.

## Building and Running

### Prerequisites
- Java 11 or higher
- Gradle (wrapper included)

### Build the Project
```bash
./gradlew build
```

### Run Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests MeetingRoomDiaryTest
./gradlew test --tests MeetingRoomTest
./gradlew test --tests MeetingTest
```

## Usage Example

```java
Solution solution = new Solution();
int[][] meetings = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
int roomId = solution.mostBooked(2, meetings); // Returns the most booked room ID
```



## Contributing

This is a LeetCode solution project. To contribute:
1. Ensure all tests pass
2. Add tests for new functionality
3. Follow existing code style and documentation standards
4. Update README for significant changes
