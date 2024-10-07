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
    //Variant1:
//     public static Set<String> getMentionedUsers(List<Tweet> tweets) {
//     Set<String> mentionedUsers = new HashSet<>();
//     Pattern mentionPattern = Pattern.compile("@(\\w{1,15})");

//     for (Tweet tweet : tweets) {
//         Matcher matcher = mentionPattern.matcher(tweet.getText());
//         while (matcher.find()) {
//             mentionedUsers.add(matcher.group(1).toLowerCase());
//         }
//     }

//     return mentionedUsers;
// }


// Using stream API:
// public static Timespan getTimespan(List<Tweet> tweets) {
//     if (tweets.isEmpty()) {
//         return new Timespan(Instant.now(), Instant.now()); // Handle empty case
//     }

//     Instant start = tweets.stream()
//                           .map(Tweet::getTimestamp)
//                           .min(Instant::compareTo)
//                           .orElse(Instant.now()); // In case of failure, return current time

//     Instant end = tweets.stream()
//                         .map(Tweet::getTimestamp)
//                         .max(Instant::compareTo)
//                         .orElse(Instant.now()); // In case of failure, return current time

//     return new Timespan(start, end);
// }

//Variant2
// public static Set<String> getMentionedUsers(List<Tweet> tweets) {
//     Set<String> mentionedUsers = new HashSet<>();

//     for (Tweet tweet : tweets) {
//         String[] words = tweet.getText().split("\\s+");
//         for (String word : words) {
//             if (word.startsWith("@")) {
//                 String username = word.substring(1);
//                 if (username.matches("[A-Za-z0-9_]+") && username.length() > 0 && username.length() <= 15) {
//                     mentionedUsers.add(username.toLowerCase());
//                 }
//             }
//         }
//     }

//     return mentionedUsers;
// }



// public static Timespan getTimespan(List<Tweet> tweets) {
//     if (tweets.isEmpty()) {
//         return new Timespan(Instant.now(), Instant.now()); // Handle empty case
//     }

//     // Check if tweets are sorted
//     List<Tweet> sortedTweets = new ArrayList<>(tweets);
//     sortedTweets.sort(Comparator.comparing(Tweet::getTimestamp));

//     Instant start = sortedTweets.get(0).getTimestamp();
//     Instant end = sortedTweets.get(sortedTweets.size() - 1).getTimestamp();

//     return new Timespan(start, end);
// }

//Variant3
// public static Set<String> getMentionedUsers(List<Tweet> tweets) {
// 	    return tweets.stream()
// 	        .flatMap(tweet -> Stream.of(tweet.getText().split("\\s+"))) // Split and create a stream of words
// 	        .filter(word -> word.startsWith("@")) // Keep only words that start with '@'
// 	        .map(word -> word.substring(1).toLowerCase()) // Remove '@' and convert to lowercase
// 	        .filter(username -> username.matches("[A-Za-z0-9_]+") && username.length() > 0 && username.length() <= 15) // Validate username
// 	        .collect(Collectors.toSet()); // Collect unique usernames into a Set
// 	}

// public static Timespan getTimespan(List<Tweet> tweets) {
// 	    if (tweets.isEmpty()) {
// 	        return new Timespan(Instant.now(), Instant.now()); // Handle empty case
// 	    }

// 	    Instant start = tweets.parallelStream()
// 	                          .map(Tweet::getTimestamp)
// 	                          .min(Instant::compareTo)
// 	                          .orElse(Instant.now());

// 	    Instant end = tweets.parallelStream()
// 	                        .map(Tweet::getTimestamp)
// 	                        .max(Instant::compareTo)
// 	                        .orElse(Instant.now());

// 	    return new Timespan(start, end);
// 	}



}
