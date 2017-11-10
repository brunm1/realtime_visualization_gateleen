package ch.bfh.ti.gapa.process.recording;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class ListenerHooks {

    public static void readJson(String listenerHooksJson) {
        JSONObject jsonObject = new JSONObject(listenerHooksJson);

        System.out.println();
    }

    private class ListenerHook {
        private Pattern urlPattern = null;
        private Pattern filter = null;
        private String destination = null;
        private List<String> methods = new ArrayList<>();
        private boolean fullUrl = false;

        public ListenerHook(JSONObject jsonObject) {
            if(jsonObject.has("requesturl")) {

            }

            if(jsonObject.has("hook")) {
                JSONObject hook = jsonObject.getJSONObject("hook");
                if(hook.has("destination")) {
                    destination = hook.getString("destination");
                } else {
                    //ToDo Throw exception? destination is required
                }
                if(hook.has("methods")) {
                    List<Object> tempList = hook.getJSONArray("methods").toList();
                    for (int i = 0; i < tempList.size(); i++) {
                        methods.add((String) tempList.get(i));
                    }
                }

                if(hook.has("filter")) {
                    filter = Pattern.compile(hook.getString("filter"));
                }

                if(hook.has("fullUrl")) {
                    fullUrl = hook.getBoolean("fullUrl");
                }
            }
        }
    }


}
