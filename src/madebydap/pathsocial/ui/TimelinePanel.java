package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.ui.components.MomentCard;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Panel timeline yang menampilkan daftar moment dari pengguna dan teman.
 * Mendukung auto-refresh untuk sinkronisasi real-time.
 * 
 * @author madebydap
 * @version 1.0
 */
public class TimelinePanel extends BasePanel {
    
    /** Container untuk daftar moment */
    private JPanel momentsContainer;
    
    /** Scroll pane untuk moments */
    private JScrollPane scrollPane;
    
    /** Executor untuk auto-refresh */
    private ScheduledExecutorService refreshService;
    
    /** Jumlah moment saat ini (untuk deteksi perubahan) */
    private int momentCount = 0;

    /** Interval auto-refresh dalam detik */
    private static final int REFRESH_INTERVAL_SECONDS = 15;

    /**
     * Konstruktor TimelinePanel.
     */
    public TimelinePanel() {
        super();
    }

    /**
     * Menginisialisasi komponen UI.
     */
    @Override
    protected void initComponents() {
        add(createHeader(), BorderLayout.NORTH);

        momentsContainer = new JPanel();
        momentsContainer.setLayout(new BoxLayout(momentsContainer, BoxLayout.Y_AXIS));
        momentsContainer.setBackground(PathColors.CARD);

        scrollPane = new JScrollPane(momentsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PathColors.BACKGROUND);
        scrollPane.getViewport().setBackground(PathColors.BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Membuat header panel dengan logo Path.
     * 
     * @return JPanel header
     */
    @Override
    protected JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PathColors.BACKGROUND_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        JLabel logoLabel = new JLabel("path");
        logoLabel.setFont(new Font("Georgia", Font.ITALIC, 32));
        logoLabel.setForeground(PathColors.PRIMARY);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(logoLabel, BorderLayout.CENTER);

        return header;
    }

    /**
     * Memulai auto-refresh timeline.
     * Thread berjalan sebagai daemon dan refresh setiap REFRESH_INTERVAL_SECONDS.
     */
    public void startAutoRefresh() {
        if (refreshService != null && !refreshService.isShutdown()) {
            return;
        }

        refreshService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Timeline-AutoRefresh");
            t.setDaemon(true);
            return t;
        });

        refreshService.scheduleAtFixedRate(() -> {
            if (!isShowing() || DataStore.getInstance().getCurrentUser() == null) {
                return;
            }

            List<Moment> moments = DataStore.getInstance().getTimelineMoments();
            int newCount = moments.size();

            if (newCount != momentCount) {
                SwingUtilities.invokeLater(this::refreshContent);
            }
        }, REFRESH_INTERVAL_SECONDS, REFRESH_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Menghentikan auto-refresh timeline.
     */
    public void stopAutoRefresh() {
        if (refreshService != null && !refreshService.isShutdown()) {
            refreshService.shutdown();
        }
    }

    /**
     * Merefresh timeline dan memulai auto-refresh.
     */
    @Override
    public void refresh() {
        refreshContent();
        startAutoRefresh();
    }

    /**
     * Merefresh konten timeline.
     * Memuat ulang moment dari DataStore dan menampilkan di UI.
     */
    private void refreshContent() {
        momentsContainer.removeAll();

        List<Moment> moments = DataStore.getInstance().getTimelineMoments();
        momentCount = moments.size();

        if (moments.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setBackground(PathColors.CARD);
            emptyPanel.setPreferredSize(new Dimension(400, 200));
            
            JPanel emptyContent = new JPanel();
            emptyContent.setOpaque(false);
            emptyContent.setLayout(new BoxLayout(emptyContent, BoxLayout.Y_AXIS));
            
            JLabel emptyIcon = new JLabel("+");
            emptyIcon.setFont(new Font("SansSerif", Font.PLAIN, 48));
            emptyIcon.setForeground(PathColors.BORDER);
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyContent.add(emptyIcon);
            
            emptyContent.add(Box.createVerticalStrut(12));
            
            JLabel emptyLabel = new JLabel("Share your first moment");
            emptyLabel.setFont(PathFonts.BODY);
            emptyLabel.setForeground(PathColors.TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyContent.add(emptyLabel);
            
            emptyPanel.add(emptyContent);
            momentsContainer.add(emptyPanel);
        } else {
            for (int i = 0; i < moments.size(); i++) {
                Moment moment = moments.get(i);
                MomentCard card = new MomentCard(moment);
                
                card.setFirstInTimeline(i == 0);
                card.setLastInTimeline(i == moments.size() - 1);
                
                momentsContainer.add(card);
            }
        }

        momentsContainer.revalidate();
        momentsContainer.repaint();
    }
}
