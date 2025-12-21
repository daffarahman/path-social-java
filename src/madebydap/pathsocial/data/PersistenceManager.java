package madebydap.pathsocial.data;

import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

/**
 * Handles JSON persistence for users and moments.
 * Uses simple text-based JSON parsing (no external libraries).
 */
public class PersistenceManager {
    private static final String DATA_DIR = "pathdata";
    private static final String DATA_FILE = "data.json";
    private static final String IMAGES_DIR = "images";
    
    private final Path dataPath;
    private final Path imagesPath;
    private long lastModified = 0;

    public PersistenceManager() {
        Path baseDir = Paths.get(System.getProperty("user.home"), ".pathsocial");
        this.dataPath = baseDir.resolve(DATA_FILE);
        this.imagesPath = baseDir.resolve(IMAGES_DIR);
        
        try {
            Files.createDirectories(baseDir);
            Files.createDirectories(imagesPath);
        } catch (IOException e) {
            System.err.println("[Persistence] Failed to create directories: " + e.getMessage());
        }
    }

    /**
     * Check if data file has been modified externally.
     */
    public boolean hasExternalChanges() {
        try {
            if (Files.exists(dataPath)) {
                long currentModified = Files.getLastModifiedTime(dataPath).toMillis();
                if (currentModified > lastModified) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Ignore
        }
        return false;
    }

    /**
     * Copy an image to the app's images directory.
     * Returns the new path.
     */
    public String copyImage(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) return null;
        
        try {
            Path source = Paths.get(sourcePath);
            if (!Files.exists(source)) return sourcePath; // Already copied or invalid
            
            String fileName = UUID.randomUUID().toString() + getExtension(sourcePath);
            Path dest = imagesPath.resolve(fileName);
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            
            return dest.toString();
        } catch (IOException e) {
            System.err.println("[Persistence] Failed to copy image: " + e.getMessage());
            return sourcePath;
        }
    }

    private String getExtension(String path) {
        int dot = path.lastIndexOf('.');
        return dot > 0 ? path.substring(dot) : ".jpg";
    }

    /**
     * Save all data to JSON file.
     */
    public void save(Map<String, User> users, List<Moment> moments) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Users
        json.append("  \"users\": [\n");
        int userCount = 0;
        for (User user : users.values()) {
            if (userCount > 0) json.append(",\n");
            json.append(userToJson(user));
            userCount++;
        }
        json.append("\n  ],\n");
        
        // Moments
        json.append("  \"moments\": [\n");
        for (int i = 0; i < moments.size(); i++) {
            if (i > 0) json.append(",\n");
            json.append(momentToJson(moments.get(i)));
        }
        json.append("\n  ]\n");
        
        json.append("}\n");
        
        try {
            Files.writeString(dataPath, json.toString());
            lastModified = Files.getLastModifiedTime(dataPath).toMillis();
            System.out.println("[Persistence] Saved " + users.size() + " users, " + moments.size() + " moments");
        } catch (IOException e) {
            System.err.println("[Persistence] Failed to save: " + e.getMessage());
        }
    }

    private String userToJson(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"id\": \"").append(escape(user.getId())).append("\",\n");
        sb.append("      \"username\": \"").append(escape(user.getUsername())).append("\",\n");
        sb.append("      \"password\": \"").append(escape(user.getPassword())).append("\",\n");
        sb.append("      \"displayName\": \"").append(escape(user.getDisplayName())).append("\",\n");
        sb.append("      \"friendIds\": [");
        List<String> friends = user.getFriendIds();
        for (int i = 0; i < friends.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("\"").append(escape(friends.get(i))).append("\"");
        }
        sb.append("]\n");
        sb.append("    }");
        return sb.toString();
    }

    private String momentToJson(Moment moment) {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"id\": \"").append(escape(moment.getId())).append("\",\n");
        sb.append("      \"userId\": \"").append(escape(moment.getUserId())).append("\",\n");
        sb.append("      \"type\": \"").append(moment.getType().name()).append("\",\n");
        sb.append("      \"content\": \"").append(escape(moment.getContent())).append("\",\n");
        sb.append("      \"imagePath\": ").append(moment.getImagePath() != null ? "\"" + escape(moment.getImagePath()) + "\"" : "null").append(",\n");
        sb.append("      \"timestamp\": \"").append(moment.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\"\n");
        sb.append("    }");
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Load data from JSON file.
     */
    public LoadResult load() {
        LoadResult result = new LoadResult();
        
        if (!Files.exists(dataPath)) {
            System.out.println("[Persistence] No data file found, starting fresh");
            return result;
        }
        
        try {
            String json = Files.readString(dataPath);
            lastModified = Files.getLastModifiedTime(dataPath).toMillis();
            
            // Parse users
            result.users = parseUsers(json);
            
            // Parse moments
            result.moments = parseMoments(json);
            
            System.out.println("[Persistence] Loaded " + result.users.size() + " users, " + result.moments.size() + " moments");
        } catch (IOException e) {
            System.err.println("[Persistence] Failed to load: " + e.getMessage());
        }
        
        return result;
    }

    private Map<String, User> parseUsers(String json) {
        Map<String, User> users = new HashMap<>();
        
        Pattern userPattern = Pattern.compile(
            "\"id\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"username\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"password\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"displayName\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"friendIds\"\\s*:\\s*\\[([^\\]]*)\\]"
        );
        
        Matcher matcher = userPattern.matcher(json);
        while (matcher.find()) {
            String id = unescape(matcher.group(1));
            String username = unescape(matcher.group(2));
            String password = unescape(matcher.group(3));
            String displayName = unescape(matcher.group(4));
            String friendsStr = matcher.group(5);
            
            User user = new User(id, username, password, displayName);
            
            // Parse friend IDs
            Pattern friendPattern = Pattern.compile("\"([^\"]+)\"");
            Matcher friendMatcher = friendPattern.matcher(friendsStr);
            while (friendMatcher.find()) {
                user.addFriend(friendMatcher.group(1));
            }
            
            users.put(id, user);
        }
        
        return users;
    }

    private List<Moment> parseMoments(String json) {
        List<Moment> moments = new ArrayList<>();
        
        Pattern momentPattern = Pattern.compile(
            "\"id\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"userId\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"type\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"content\"\\s*:\\s*\"([^\"]*)\",\\s*" +
            "\"imagePath\"\\s*:\\s*(null|\"([^\"]*)\"),\\s*" +
            "\"timestamp\"\\s*:\\s*\"([^\"]*)\""
        );
        
        Matcher matcher = momentPattern.matcher(json);
        while (matcher.find()) {
            String id = unescape(matcher.group(1));
            String userId = unescape(matcher.group(2));
            MomentType type = MomentType.valueOf(matcher.group(3));
            String content = unescape(matcher.group(4));
            String imagePath = matcher.group(5).equals("null") ? null : unescape(matcher.group(6));
            LocalDateTime timestamp = LocalDateTime.parse(matcher.group(7), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            Moment moment = new Moment(id, userId, type, content, imagePath, timestamp);
            moments.add(moment);
        }
        
        return moments;
    }

    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    public static class LoadResult {
        public Map<String, User> users = new HashMap<>();
        public List<Moment> moments = new ArrayList<>();
    }
}
