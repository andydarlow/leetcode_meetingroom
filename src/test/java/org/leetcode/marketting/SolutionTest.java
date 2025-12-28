package org.leetcode.marketting;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    private static int[][] readMeetings(String resourceFilename) {
        try  {
            URL resource = SolutionTest.class.getClassLoader().getResource(resourceFilename);
            return (int[][]) Files.readAllLines(Path.of(resource.toURI())).stream()
                    .map(line -> line.split(","))
                    .map(data -> new int[] {Integer.parseInt(data[0].strip()),                                               Integer.parseInt(data[1].strip())})
                    .toArray(int[][]::new);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSolution_largeMeetingsList() {
        int[][] meetings = readMeetings("large-meetings-list.txt");
        assertEquals(1, new Solution().mostBooked(10, meetings));
    }

    @Test
    void testSolution_example1() {
        int[][] meetings = readMeetings("example1.txt");
        assertEquals(0, new Solution().mostBooked(2, meetings));
    }

    @Test
    void testSolution_example2() {
        int[][] meetings = readMeetings("example2.txt");
        assertEquals(1, new Solution().mostBooked(3, meetings));
    }

}