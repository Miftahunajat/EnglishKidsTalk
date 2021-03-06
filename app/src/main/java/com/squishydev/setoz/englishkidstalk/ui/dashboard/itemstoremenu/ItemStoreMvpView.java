package com.squishydev.setoz.englishkidstalk.ui.dashboard.itemstoremenu;

import com.squishydev.setoz.englishkidstalk.data.network.model.Item;
import com.squishydev.setoz.englishkidstalk.data.network.model.User;
import com.squishydev.setoz.englishkidstalk.ui.base.MvpView;

import java.util.List;

public interface ItemStoreMvpView extends MvpView {

    void setupItem(List<Item> items);

    void setUser(User user);
}
