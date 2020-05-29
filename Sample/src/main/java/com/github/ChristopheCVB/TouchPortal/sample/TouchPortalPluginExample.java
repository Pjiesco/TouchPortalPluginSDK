/*
 * Touch Portal Plugin SDK
 *
 * Copyright 2020 Christophe Carvalho Vilas-Boas
 * christophe.carvalhovilasboas@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.ChristopheCVB.TouchPortal.sample;

import com.github.ChristopheCVB.TouchPortal.Annotations.*;
import com.github.ChristopheCVB.TouchPortal.TouchPortalPlugin;
import org.json.JSONObject;

@Plugin(version = 1000, colorDark = "#203060", colorLight = "#4070F0", name = "Touch Portal Plugin Example")
public class TouchPortalPluginExample extends TouchPortalPlugin implements TouchPortalPlugin.TouchPortalPluginListener {
    @State(valueChoices = {"1","2"})
    private String[] customState;

    @Event(format = "When $val", stateFieldName = "customState")
    private String[] customEvent;

    public TouchPortalPluginExample() {}

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            if (COMMAND_START.equals(args[0])) {
                TouchPortalPluginExample touchPortalPluginExample = new TouchPortalPluginExample();
                touchPortalPluginExample.connectThenPairAndListen(touchPortalPluginExample);
            }
        }
    }

    @Action(description = "Long Description of Dummy Action with Data", format = "Set text to {$text$}")
    private void dummyWithData(@Data String text) {
        System.out.println("Action dummyWithData received: " + text);
    }

    @Action(description = "Long Description of Dummy Action without Data")
    private void dummyWithoutData(JSONObject jsonAction) {
        System.out.println("Action dummyWithoutData received [" + jsonAction + "]");
    }

    @Override
    public void onDisconnect(Exception exception) {
        // Socket connection is lost or closed
    }

    @Override
    public void onReceive(JSONObject jsonMessage) {
        if (ReceivedMessageHelper.isAnAction(jsonMessage)) {
            String receivedActionId = ReceivedMessageHelper.getActionId(jsonMessage);
            if (ActionHelper.getActionId(TouchPortalPluginExample.class, "dummyWithData").equals(receivedActionId)) {
                this.dummyWithData(ReceivedMessageHelper.getActionDataValue(receivedActionId, jsonMessage, "text"));
            }
            else if (ActionHelper.getActionId(TouchPortalPluginExample.class, "dummyWithoutData").equals(receivedActionId)) {
                this.dummyWithoutData(jsonMessage);
            }
        }
    }
}