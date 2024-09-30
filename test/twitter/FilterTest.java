package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "Let's discuss more about Rivest.", d3);


    // Test: Filter tweets written by a specific user
    @Test
    public void testWrittenBy_MultipleTweets_SingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        
        assertEquals("expected list size", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet3));
    }

    // Test: No tweets match the user
    @Test
    public void testWrittenBy_NoMatch_ReturnsEmptyList() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "nonexistent");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

   
    // Test: Filter tweets within a specific timespan
    @Test
    public void testInTimespan_MultipleTweets_MultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    // Test: No tweets in the specified timespan
    @Test
    public void testInTimespan_NoTweetsInRange_ReturnsEmptyList() {
        Instant testStart = Instant.parse("2016-02-17T13:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T14:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    // Test: Tweets at the exact boundaries of the timespan
    @Test
    public void testInTimespan_ExactBoundaries_IncludesBoundaryTweets() {
        Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
        assertTrue("expected list to contain tweet2", inTimespan.contains(tweet2));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    // Test: Filter tweets containing specific words
    @Test
    public void testContaining_SingleWord_MatchesMultipleTweets() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("rivest", "discuss"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
    }

    // Test: No matches for the given words
    @Test
    public void testContaining_NoMatches_ReturnsEmptyList() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("notfound"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    // Test: Case insensitivity in word matching
    @Test
    public void testContaining_CaseInsensitivity() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("RivEst"));
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
    }

    // Test: Filter tweets that contain no matching words
    @Test
    public void testContaining_MultipleWords_NoMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("not", "present"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    
}
