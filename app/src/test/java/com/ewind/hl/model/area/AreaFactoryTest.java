package com.ewind.hl.model.area;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AreaFactoryTest {
    @Test
    public void shouldParseBody() {
        Area body = AreaFactory.getBody();

        assertNotNull(body);
    }
}