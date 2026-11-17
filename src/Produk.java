public class Produk extends ObjekToko {
    private static final long serialVersionUID = 1L;

    public Produk(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategori() {
        return "Produk";
    }
}