package com.ewind.hl.model.area;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AreaFactoryTest {
    @Test
    public void shouldParseBody() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();

        Area body = AreaFactory.getBody(context);

        assertNotNull(body);
    }
}