package dev.buhanzaz.validate;

import dev.buhanzaz.state.MenuState;

public final class MenuValidated {
    private MenuValidated() {
    }

    public static boolean isAddImageMenu(MenuState state) {
        return state == MenuState.ADD_IMAGE_MENU;
    }
}
