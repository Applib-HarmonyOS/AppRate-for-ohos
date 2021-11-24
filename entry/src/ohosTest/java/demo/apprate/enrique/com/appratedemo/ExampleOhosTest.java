/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
