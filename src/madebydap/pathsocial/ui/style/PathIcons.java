package madebydap.pathsocial.ui.style;

import madebydap.pathsocial.model.MomentType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Custom painted icons for the Path app.
 */
public class PathIcons {
    
    public static Icon getMomentIcon(MomentType type, int size, Color color) {
        switch (type) {
            case AWAKE: return new SunIcon(size, color);
            case ASLEEP: return new MoonIcon(size, color);
            case MUSIC: return new MusicIcon(size, color);
            case PHOTO: return new CameraIcon(size, color);
            case LOCATION: return new LocationIcon(size, color);
            case THOUGHT: return new ThoughtIcon(size, color);
            case FRIENDSHIP: return new FriendshipIcon(size, color);
            default: return new SunIcon(size, color);
        }
    }

    // Sun icon for Awake
    public static class SunIcon implements Icon {
        private final int size;
        private final Color color;

        public SunIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int center = size / 2;
            int coreSize = size / 3;
            
            g2.fillOval(center - coreSize/2, center - coreSize/2, coreSize, coreSize);
            
            g2.setStroke(new BasicStroke(size / 12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                int x1 = (int) (center + (coreSize/2 + 2) * Math.cos(angle));
                int y1 = (int) (center + (coreSize/2 + 2) * Math.sin(angle));
                int x2 = (int) (center + (size/2 - 2) * Math.cos(angle));
                int y2 = (int) (center + (size/2 - 2) * Math.sin(angle));
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Moon icon for Asleep
    public static class MoonIcon implements Icon {
        private final int size;
        private final Color color;

        public MoonIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int margin = size / 6;
            Area moon = new Area(new Ellipse2D.Float(margin, margin, size - margin*2, size - margin*2));
            Area cutout = new Area(new Ellipse2D.Float(margin + size/4, margin - size/6, size - margin*2, size - margin*2));
            moon.subtract(cutout);
            g2.fill(moon);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Music icon
    public static class MusicIcon implements Icon {
        private final int size;
        private final Color color;

        public MusicIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int noteSize = size / 4;
            g2.setStroke(new BasicStroke(size / 10f));
            
            g2.fillOval(size/6, size - noteSize - size/6, noteSize, noteSize);
            g2.drawLine(size/6 + noteSize, size - noteSize/2 - size/6, size/6 + noteSize, size/4);
            
            g2.fillOval(size - size/6 - noteSize, size - noteSize - size/4, noteSize, noteSize);
            g2.drawLine(size - size/6, size - noteSize/2 - size/4, size - size/6, size/6);
            
            g2.drawLine(size/6 + noteSize, size/4, size - size/6, size/6);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Camera icon for Photo
    public static class CameraIcon implements Icon {
        private final int size;
        private final Color color;

        public CameraIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int margin = size / 8;
            
            g2.fillRoundRect(margin, size/3, size - margin*2, size/2, size/8, size/8);
            g2.fillRoundRect(size/3, size/4, size/3, size/6, size/10, size/10);
            
            int lensSize = size / 4;
            g2.setColor(Color.WHITE);
            g2.fillOval(size/2 - lensSize/2, size/2 - lensSize/4, lensSize, lensSize);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Location pin icon
    public static class LocationIcon implements Icon {
        private final int size;
        private final Color color;

        public LocationIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int margin = size / 5;
            Path2D pin = new Path2D.Float();
            pin.moveTo(size/2, size - margin);
            pin.lineTo(margin, size/3);
            pin.curveTo(margin, margin, size - margin, margin, size - margin, size/3);
            pin.closePath();
            g2.fill(pin);
            
            int dotSize = size / 5;
            g2.setColor(Color.WHITE);
            g2.fillOval(size/2 - dotSize/2, size/4, dotSize, dotSize);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Thought bubble icon
    public static class ThoughtIcon implements Icon {
        private final int size;
        private final Color color;

        public ThoughtIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int margin = size / 8;
            
            g2.fillRoundRect(margin, margin, size - margin*2, size/2 + margin, size/4, size/4);
            
            g2.fillOval(size/4, size*2/3, size/6, size/6);
            g2.fillOval(size/6, size*3/4, size/8, size/8);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Friendship icon (two people)
    public static class FriendshipIcon implements Icon {
        private final int size;
        private final Color color;

        public FriendshipIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.translate(x, y);

            int headSize = size / 4;
            int bodyWidth = size / 3;
            
            // Left person - head
            g2.fillOval(size/4 - headSize/2, size/6, headSize, headSize);
            // Left person - body
            g2.fillRoundRect(size/4 - bodyWidth/2, size/6 + headSize, bodyWidth, size/2, bodyWidth/2, bodyWidth/2);
            
            // Right person - head
            g2.fillOval(size*3/4 - headSize/2, size/6, headSize, headSize);
            // Right person - body
            g2.fillRoundRect(size*3/4 - bodyWidth/2, size/6 + headSize, bodyWidth, size/2, bodyWidth/2, bodyWidth/2);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // Plus icon
    public static class PlusIcon implements Icon {
        private final int size;
        private final Color color;

        public PlusIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(size / 6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);

            int margin = size / 4;
            g2.drawLine(size/2, margin, size/2, size - margin);
            g2.drawLine(margin, size/2, size - margin, size/2);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }
}
