import java.io.Serializable;

// Abstract class + Encapsulation
public abstract class Aktor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nama;

    public Aktor(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }

    // Polymorphism: tiap turunan punya peran sendiri
    public abstract String getPeran();

    @Override
    public String toString() {
        return getPeran() + " [" + id + " - " + nama + "]";
    }
}