import java.io.Serializable;

// Association: Transaksi menghubungkan Pelanggan dan Produk
public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    private Pelanggan pelanggan;
    private Produk produk;
    private int qty;

    public Transaksi(Pelanggan pelanggan, Produk produk, int qty) {
        this.pelanggan = pelanggan;
        this.produk = produk;
        this.qty = qty;
    }

    public Pelanggan getPelanggan() { return pelanggan; }
    public Produk getProduk() { return produk; }
    public int getQty() { return qty; }
}