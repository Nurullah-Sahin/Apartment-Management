package com.sahingroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sahingroup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Daire.class);
        Daire daire1 = new Daire();
        daire1.setId("id1");
        Daire daire2 = new Daire();
        daire2.setId(daire1.getId());
        assertThat(daire1).isEqualTo(daire2);
        daire2.setId("id2");
        assertThat(daire1).isNotEqualTo(daire2);
        daire1.setId(null);
        assertThat(daire1).isNotEqualTo(daire2);
    }
}
