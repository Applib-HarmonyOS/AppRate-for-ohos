package demo.apprate.enrique.com.appratedemo;

import com.enrique.apprater.AppRater;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class ExampleOhosTest {

    private DatabaseHelper databaseHelper;
    private Context context;
    private static final String APP_RATER = "apprater";

    @Before
    public void setUp() {

        context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        databaseHelper=new DatabaseHelper(context);
    }

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("demo.apprate.enrique.com.appratedemo", actualBundleName);
    }


    @Test
    public void testpreference()
    {
        ohos.data.preferences.Preferences preferences = databaseHelper.getPreferences(APP_RATER);
        assertNotNull(preferences);
    }

    @Test
    public void testinit()
    {
        AppRater appRater=new AppRater(context);
        assertNotNull(appRater.init());
    }


}
