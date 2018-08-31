package com.naver.mycnex.viewpageapplication.bus;

import com.squareup.otto.Bus;

public class BusProvider {
    private static BusProvider curr = null;
    private Bus bus;

    public static BusProvider getInstance() {
        if (curr == null) {
            curr = new BusProvider();
        }

        return curr;
    }

    private BusProvider() {
        bus = new Bus();
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
