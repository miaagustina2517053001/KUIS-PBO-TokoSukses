import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemToko implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaToko;
    private HashMap<String, Pelanggan> daftarPelanggan = new HashMap<>();
    private HashMap<String, Produk> daftarProduk = new HashMap<>();
    private ArrayList<Transaksi> logTransaksi = new ArrayList<>();

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    public void tambahProduk(String kode, String nama, int stok) {
        daftarProduk.put(kode, new Produk(kode, nama, stok));
    }

    // Digit kedua = 0 -> format: ID>ACT-01.KD>OBJ-A.JML>3.  (satu transaksi per baris)
    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            int noBaris = 0;
            while ((baris = br.readLine()) != null) {
                noBaris++;
                baris = baris.trim();
                if (baris.isEmpty()) continue;

                try {
                    prosesBaris(baris);
                } catch (FormatDataException | AktorTidakDitemukanException
                         | ObjekTidakDitemukanException | StokTidakCukupException e) {
                    System.out.println("[GAGAL] Baris " + noBaris + ": " + e.getMessage());
                }
            }
        }
    }

    private void prosesBaris(String baris) throws FormatDataException, AktorTidakDitemukanException,
            ObjekTidakDitemukanException, StokTidakCukupException {
        String idAktor = null, kodeObjek = null;
        int qty = 0;

        // Pemisah antar-field: titik "." ; pemisah key dan value: ">"
        String[] bagian = baris.split("\\.");
        for (String b : bagian) {
            String[] kv = b.split(">");
            if (kv.length != 2) {
                throw new FormatDataException("Format tidak valid: '" + baris + "'");
            }
            String key = kv[0].trim();
            String value = kv[1].trim();
            switch (key) {
                case "ID":  idAktor = value; break;
                case "KD":  kodeObjek = value; break;
                case "JML":
                    try {
                        qty = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new FormatDataException("JML bukan angka: '" + value + "'");
                    }
                    break;
                default:
                    throw new FormatDataException("Key tidak dikenal: '" + key + "'");
            }
        }
        if (idAktor == null || kodeObjek == null || qty <= 0) {
            throw new FormatDataException("Data tidak lengkap: '" + baris + "'");
        }

        Pelanggan p = daftarPelanggan.get(idAktor);
        if (p == null) {
            throw new AktorTidakDitemukanException("Pelanggan dengan ID " + idAktor + " tidak ditemukan");
        }
        Produk produk = daftarProduk.get(kodeObjek);
        if (produk == null) {
            throw new ObjekTidakDitemukanException("Produk dengan kode " + kodeObjek + " tidak ditemukan");
        }

        produk.kurangiStok(qty);
        logTransaksi.add(new Transaksi(p, produk, qty));
        System.out.println("[OK] " + p.getNama() + " membeli " + qty + " x " + produk.getNama()
                + " (sisa stok: " + produk.getStok() + ")");
    }

    // Digit ketiga = 5 -> data_master.bak + laporan.txt format CSV
    public void simpanLaporanAkhir() {
        // 1. Object Serialization
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_master.bak"))) {
            oos.writeObject(this);
            System.out.println("Serialisasi berhasil -> data_master.bak");
        } catch (IOException e) {
            System.out.println("Gagal serialisasi: " + e.getMessage());
        }

        // 2. Laporan Ekspor CSV
        try (FileWriter fw = new FileWriter("laporan.txt")) {
            for (Transaksi t : logTransaksi) {
                fw.write(t.getPelanggan().getId() + "," + t.getProduk().getKode() + "," + t.getQty() + "\n");
            }
            System.out.println("Laporan CSV berhasil -> laporan.txt");
        } catch (IOException e) {
            System.out.println("Gagal menulis laporan: " + e.getMessage());
        }
    }
}