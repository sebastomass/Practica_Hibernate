package Acces_a_dades_Navegador;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "idioma")

public class idioma {

    @Id
    @Column(name = "idi_id")
    private int _id;

    @Column(name = "idi_cod")
    private String _idioma;

    public idioma() {
    }

    public idioma(int id, String idioma) {
        this.setId(id);
        this.setIdioma(idioma);
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String get_idioma() {
        return this._idioma;
    }

    public void setIdioma(String idioma) {
        this._idioma = idioma;
    }

}

