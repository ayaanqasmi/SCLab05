/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */

import java.util.HashSet;

import java.time.Instant;
public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return new Timespan(Instant.now(), Instant.now()); // Handle empty case
        }

        Instant start = Instant.MAX;
        Instant end = Instant.MIN;

        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (timestamp.isBefore(start)) {
                start = timestamp;
            }
            if (timestamp.isAfter(end)) {
                end = timestamp;
            }
        }
        return new Timespan(start, end);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();

        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            String[] words = text.split("\\s+");
            for (String word : words) {
                if (word.startsWith("@")) {
                    String username = word.substring(1);
                    // Check if username is valid
                    if (username.length() > 0 && username.length() <= 15 && username.matches("[A-Za-z0-9_]+")) {
                        mentionedUsers.add(username.toLowerCase()); // Store usernames in lowercase
                    }
                }
            }
        }
        return mentionedUsers;
    }
}
