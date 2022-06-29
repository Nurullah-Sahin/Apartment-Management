package com.sahingroup.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Daire.
 */
@Document(collection = "daire")
public class Daire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("no")
    private String no;

    @DBRef
    @Field("oturanBilgi")
    private User oturanBilgi;

    @DBRef
    @Field("apartmanid")
    private Apartman apartmanid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Daire id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return this.no;
    }

    public Daire no(String no) {
        this.setNo(no);
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public User getOturanBilgi() {
        return this.oturanBilgi;
    }

    public void setOturanBilgi(User user) {
        this.oturanBilgi = user;
    }

    public Daire oturanBilgi(User user) {
        this.setOturanBilgi(user);
        return this;
    }

    public Apartman getApartmanid() {
        return this.apartmanid;
    }

    public void setApartmanid(Apartman apartman) {
        this.apartmanid = apartman;
    }

    public Daire apartmanid(Apartman apartman) {
        this.setApartmanid(apartman);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Daire)) {
            return false;
        }
        return id != null && id.equals(((Daire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Daire{" +
            "id=" + getId() +
            ", no='" + getNo() + "'" +
            "}";
    }
}
