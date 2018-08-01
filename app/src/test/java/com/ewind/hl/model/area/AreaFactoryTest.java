package com.ewind.hl.model.area;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class AreaFactoryTest {
    @Test
    public void shouldParseBody() throws IOException {
        try (InputStream stream = AreaFactory.class.getClassLoader().getResourceAsStream("body.yml")) {
            AreaFactory.initBody(stream);
            Area body = AreaFactory.getBody();
            assertNotNull(body);

            List<Area> areas = Collections.singletonList(body);
            print(areas, 0);
        }
    }

    private void print(List<Area> areas, int indention) {
        StringBuilder ind = new StringBuilder();
        for (int i = 0; i < indention; i++) {
            ind.append("\t");
        }
        for (Area area : areas) {
            System.out.println(ind.toString() + area);
            print(area.getParts(), indention+1);
        }

    }
}