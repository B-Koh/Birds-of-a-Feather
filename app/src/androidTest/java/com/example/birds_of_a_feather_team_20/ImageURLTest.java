package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ImageURLTest {
    @Test
    public void testFullImage(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MyProfile.singleton(appContext).setName("George W. Bush");
        MyProfile.singleton(appContext).setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg");

        assertNotEquals(MyProfile.singleton(appContext).getPhoto(), null);

        //Test against known image properties (not great since image in link may change, but getting
        //the image again from the link is redundant).
        assertEquals(MyProfile.singleton(appContext).getPhoto().getWidth(), 2267);
        assertEquals(MyProfile.singleton(appContext).getPhoto().getHeight(), 3000);
    }

    @Test
    public void testBrokenImage(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MyProfile.singleton(appContext).setName("Jimmy Carter");
        MyProfile.singleton(appContext).setPhotoURL("https://en.wikipedia.org/wiki/Jimmy_Carter");

        assertEquals(MyProfile.singleton(appContext).getPhoto(), null);
    }

    @Test
    public void testThumbnail(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MyProfile.singleton(appContext).setName("Walter Mondale");
        MyProfile.singleton(appContext).setPhotoURL(null);
        assertEquals(MyProfile.singleton(appContext).getThumbnail(), null);

        MyProfile.singleton(appContext).setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/6/6c/Vice_President_Mondale_1977_closeup.jpg");
        Bitmap originalThumbnail = MyProfile.singleton(appContext).getThumbnail();
        assertEquals(originalThumbnail.getWidth(), 256);
        assertEquals(originalThumbnail.getHeight(), 256);

        MyProfile.singleton(appContext).setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/9/93/Mondale_as_Senator.jpg");
        Bitmap newThumbnail = MyProfile.singleton(appContext).getThumbnail();


        assertEquals(newThumbnail.sameAs(originalThumbnail), false);
    }
}
