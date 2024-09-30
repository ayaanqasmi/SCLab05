/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;
import java.util.HashSet;
import org.junit.Test;

public class ExtractTest {
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "user123", "Hello @User and @user123", d3);
    
    @Test
    public void getTimespan_TwoTweets_ReturnsCorrectStartAndEnd() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void getTimespan_SingleTweet_ReturnsSameStartAndEnd() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    @Test
    public void getTimespan_MultipleTweets_ReturnsCorrectSpan() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void getTimespan_EmptyList_ReturnsNonNullTimespan() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertNotNull(timespan);
    }
    
    @Test
    public void getMentionedUsers_NoMention_ReturnsEmptySet() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void getMentionedUsers_SingleMention_ReturnsCorrectUser() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        Set<String> expected = new HashSet<>(Arrays.asList("user", "user123"));
        assertEquals("expected mentioned users", expected, mentionedUsers);
    }

    @Test
    public void getMentionedUsers_CaseInsensitivity_ReturnsUniqueUsernames() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, new Tweet(4, "test", "@USER123! test", d2)));
        Set<String> expected = new HashSet<>(Arrays.asList("user", "user123"));
        assertEquals("expected mentioned users", expected, mentionedUsers);
    }

    @Test
    public void getMentionedUsers_InvalidMention_ReturnsEmptySet() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(new Tweet(5, "test", "@user123! test", d2)));
        assertTrue("expected empty set due to invalid mention", mentionedUsers.isEmpty());
    }
}