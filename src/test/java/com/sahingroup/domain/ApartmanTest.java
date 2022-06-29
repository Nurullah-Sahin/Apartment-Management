package com.sahingroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sahingroup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApartmanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apartman.class);
        Apartman apartman1 = new Apartman();
        apartman1.setId("id1");
        Apartman apartman2 = new Apartman();
        apartman2.setId(apartman1.getId());
        assertThat(apartman1).isEqualTo(apartman2);
        apartman2.setId("id2");
        assertThat(apartman1).isNotEqualTo(apartman2);
        apartman1.setId(null);
        assertThat(apartman1).isNotEqualTo(apartman2);
    }
}
