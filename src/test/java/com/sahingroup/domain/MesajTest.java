package com.sahingroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sahingroup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MesajTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mesaj.class);
        Mesaj mesaj1 = new Mesaj();
        mesaj1.setId("id1");
        Mesaj mesaj2 = new Mesaj();
        mesaj2.setId(mesaj1.getId());
        assertThat(mesaj1).isEqualTo(mesaj2);
        mesaj2.setId("id2");
        assertThat(mesaj1).isNotEqualTo(mesaj2);
        mesaj1.setId(null);
        assertThat(mesaj1).isNotEqualTo(mesaj2);
    }
}
