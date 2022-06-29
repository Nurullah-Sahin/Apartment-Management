package com.sahingroup.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Apartman.
 */
@Document(collection = "apartman")
public class Apartman implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("ad")
    private String ad;

    @Field("kat_sayisi")
    private Integer katSayisi;

    @Field("daire_sayisi")
    private Integer daireSayisi = 0;

    @Field("adres")
    private String adres;

    @Field("dolu_daire_sayisi")
    private Integer doluDaireSayisi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Apartman id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return this.ad;
    }

    public Apartman ad(String ad) {
        this.setAd(ad);
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Integer getKatSayisi() {
        return this.katSayisi;
    }

    public Apartman katSayisi(Integer katSayisi) {
        this.setKatSayisi(katSayisi);
        return this;
    }

    public void setKatSayisi(Integer katSayisi) {
        this.katSayisi = katSayisi;
    }

    public Integer getDaireSayisi() {
        return this.daireSayisi;
    }

    public Apartman daireSayisi(Integer daireSayisi) {
        this.setDaireSayisi(daireSayisi);
        return this;
    }

    public void setDaireSayisi(Integer daireSayisi) {
        this.daireSayisi = daireSayisi;
    }

    public String getAdres() {
        return this.adres;
    }

    public Apartman adres(String adres) {
        this.setAdres(adres);
        return this;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Integer getDoluDaireSayisi() {
        return this.doluDaireSayisi;
    }

    public Apartman doluDaireSayisi(Integer doluDaireSayisi) {
        this.setDoluDaireSayisi(doluDaireSayisi);
        return this;
    }

    public void setDoluDaireSayisi(Integer doluDaireSayisi) {
        this.doluDaireSayisi = doluDaireSayisi;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apartman)) {
            return false;
        }
        return id != null && id.equals(((Apartman) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apartman{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", katSayisi=" + getKatSayisi() +
            ", daireSayisi=" + getDaireSayisi() +
            ", adres='" + getAdres() + "'" +
            ", doluDaireSayisi=" + getDoluDaireSayisi() +
            "}";
    }
}
