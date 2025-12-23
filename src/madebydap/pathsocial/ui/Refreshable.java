package madebydap.pathsocial.ui;

/**
 * Interface untuk komponen yang bisa di-refresh.
 * Digunakan oleh panel-panel yang perlu memuat ulang data dari DataStore.
 * 
 * @author madebydap
 * @version 1.0
 */
public interface Refreshable {
    
    /**
     * Merefresh konten komponen.
     * Dipanggil untuk memuat ulang data dari sumber data.
     */
    void refresh();
}
