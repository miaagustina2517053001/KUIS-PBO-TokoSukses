import java.io.Serializable;

public abstract class ObjekToko implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kode;
    private String nama;
    private int stok;

    public ObjekToko(String kode, String nama, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getStok() { return stok; }

    // Mengurangi stok, melempar Custom Exception jika tidak cukup
    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException("Stok " + nama + " (" + kode + ") tidak cukup. Sisa "
                    + stok + ", diminta " + jumlah);
        }
        stok -= jumlah;
    }

    public abstract String getKategori();

    @Override
    public String toString() {
        return getKategori() + " [" + kode + " - " + nama + ", stok=" + stok + "]";
    }
}