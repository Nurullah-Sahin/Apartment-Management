package com.sahingroup.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Mesaj.
 */
@Document(collection = "mesaj")
public class Mesaj implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("mesaj_icerik")
    private String mesajIcerik;

    @Field("aktif")
    private Boolean aktif;

    @DBRef
    @Field("sahib")
    private User sahib;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Mesaj id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMesajIcerik() {
        return this.mesajIcerik;
    }

    public Mesaj mesajIcerik(String mesajIcerik) {
        this.setMesajIcerik(mesajIcerik);
        return this;
    }

    public void setMesajIcerik(String mesajIcerik) {
        this.mesajIcerik = mesajIcerik;
    }

    public Boolean getAktif() {
        return this.aktif;
    }

    public Mesaj aktif(Boolean aktif) {
        this.setAktif(aktif);
        return this;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public User getSahib() {
        return this.sahib;
    }

    public void setSahib(User user) {
        this.sahib = user;
    }

    public Mesaj sahib(User user) {
        this.setSahib(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mesaj)) {
            return false;
        }
        return id != null && id.equals(((Mesaj) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mesaj{" +
            "id=" + getId() +
            ", mesajIcerik='" + getMesajIcerik() + "'" +
            ", aktif='" + getAktif() + "'" +
            "}";
    }
}
